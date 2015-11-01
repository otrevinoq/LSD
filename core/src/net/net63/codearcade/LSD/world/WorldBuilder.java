package net.net63.codearcade.LSD.world;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.components.*;
import net.net63.codearcade.LSD.utils.Assets;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * Created by Basim on 10/07/15.
 */
public class WorldBuilder {

    private static Engine engine;
    private static World world;
    private static LevelDescriptor levelDescriptor;
    private static Rectangle bounds;

    public static void setup(Engine engine, World world, LevelDescriptor levelDescriptor) {
        WorldBuilder.engine = engine;
        WorldBuilder.world = world;
        WorldBuilder.levelDescriptor = levelDescriptor;

        bounds = new Rectangle();
    }

    public static void loadFromMap(TiledMap map) {
        createWorld();

        loadMeta(map.getLayers().get("meta"));
        loadSensors(map.getLayers().get("sensors"));
        loadWalls(map.getLayers().get("walls"));

        bounds.setPosition(bounds.x - Constants.BOUNDS_BUFFER_X, bounds.y - Constants.BOUNDS_BUFFER_Y);
        bounds.setSize(bounds.width + Constants.BOUNDS_BUFFER_X, bounds.height + Constants.BOUNDS_BUFFER_Y);
        levelDescriptor.setWorldBounds(bounds);
    }

    private static void loadMeta(MapLayer metaLayer) {
        MapObject playerPosition = metaLayer.getObjects().get("player-position");
        float[] dimensions = getDimensions(playerPosition);
        float posX = dimensions[0] + dimensions[2] / 2.0f;
        float posY = dimensions[1] + dimensions[3] / 2.0f;
        createPlayer(posX, posY);
    }

    private static void loadSensors(MapLayer sensorLayer) {

        int sensorCount = 0;

        for (MapObject sensor: sensorLayer.getObjects()) {
            float[] dimensions = getDimensions(sensor);

            if (dimensions != null) {
                createSensor(dimensions[0], dimensions[1], dimensions[2], dimensions[3]);
                sensorCount++;
            }
        }

        levelDescriptor.setSensorCount(sensorCount);
    }

    private static void loadWalls(MapLayer wallLayer) {

        for (MapObject wallObject: wallLayer.getObjects()) {
            float[] dimensions = getDimensions(wallObject);

            if (dimensions != null) {
                createWall(dimensions[0], dimensions[1], dimensions[2], dimensions[3]);
            }
        }

    }

    private static void addBoundedBody(float x, float y, float width, float height) {
        bounds.merge(x, y);
        bounds.merge(x + width, y + height);
    }

    private static float[] getDimensions(MapObject mapObject) {
        MapProperties properties = mapObject.getProperties();

        String[] keys = { "x", "y", "width", "height" };
        float[] values = new float[keys.length];

        for (int i = 0; i < keys.length; i++) {
            if (! properties.containsKey(keys[i])) return null;
            values[i] = Constants.PIXEL_TO_METRE * properties.get(keys[i], Float.class).floatValue();
        }

        return values;
    }

    public static Entity createWorld() {
        WorldComponent worldComponent = new WorldComponent();
        worldComponent.world = world;

        return createEntityFrom(worldComponent);
    }

    public static Entity createSensor(float x, float y, float width, float height) {

        addBoundedBody(x, y, width, height);

        SensorComponent sensorComponent = new SensorComponent();
        RenderComponent renderComponent = new RenderComponent();
        BodyComponent bodyComponent = new BodyComponent();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set((width / 2) + x, (height / 2) + y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0.0f;
        fixtureDef.shape = new PolygonShape();
        fixtureDef.filter.categoryBits = Constants.CategoryBits.SENSOR;

        ((PolygonShape) fixtureDef.shape).setAsBox(width / 2, height / 2);

        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.createFixture(fixtureDef);

        renderComponent.texture = new TextureRegion(Assets.getAsset(Assets.Images.SENSOR_TILE, Texture.class));
        renderComponent.tileWidth = renderComponent.texture.getRegionWidth();
        renderComponent.tileHeight = renderComponent.texture.getRegionHeight();
        renderComponent.tileToSize = true;

        return createEntityFrom(sensorComponent, bodyComponent, renderComponent);
    }

    public static Entity createWall(float x, float y, float width, float height) {

        addBoundedBody(x, y, width, height);

        WallComponent wallComponent = new WallComponent();
        RenderComponent renderComponent = new RenderComponent();
        BodyComponent bodyComponent = new BodyComponent();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set((width / 2) + x, (height / 2) + y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0.0f;
        fixtureDef.shape = new PolygonShape();
        fixtureDef.filter.categoryBits = Constants.CategoryBits.WALL;

        ((PolygonShape) fixtureDef.shape).setAsBox(width / 2, height / 2);

        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.createFixture(fixtureDef);

        renderComponent.texture = new TextureRegion(Assets.getAsset(Assets.Images.WALL_TILE, Texture.class));
        renderComponent.tileWidth = renderComponent.texture.getRegionWidth();
        renderComponent.tileHeight = renderComponent.texture.getRegionHeight();
        renderComponent.tileToSize = true;

        return createEntityFrom(wallComponent, bodyComponent, renderComponent);

    }

    public static Entity createPlayer(float x, float y) {

        PlayerComponent playerComponent = new PlayerComponent();
        StateComponent stateComponent = new StateComponent();
        AnimationComponent animationComponent = new AnimationComponent();
        RenderComponent renderComponent = new RenderComponent();
        BodyComponent bodyComponent = new BodyComponent();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0.0f;
        fixtureDef.shape = new CircleShape();
        fixtureDef.shape.setRadius(Constants.PLAYER_WIDTH / 2.0f);
        fixtureDef.filter.maskBits = Constants.MaskBits.PLAYER;
        fixtureDef.filter.categoryBits = Constants.CategoryBits.PLAYER;

        Animation still = Assets.getAnimation(Assets.Animations.PLAYER_STILL);
        Animation jumping = Assets.getAnimation(Assets.Animations.PLAYER_JUMPING);
        Animation falling = Assets.getAnimation(Assets.Animations.PLAYER_FALLING);

        still.setPlayMode(Animation.PlayMode.LOOP);
        jumping.setPlayMode(Animation.PlayMode.LOOP);
        falling.setPlayMode(Animation.PlayMode.LOOP);

        still.setFrameDuration(0.1f);
        jumping.setFrameDuration(0.1f);
        falling.setFrameDuration(0.1f);

        animationComponent.animations.put(PlayerComponent.STATE_STILL, still);
        animationComponent.animations.put(PlayerComponent.STATE_AIMING, still);
        animationComponent.animations.put(PlayerComponent.STATE_FIRING, still);
        animationComponent.animations.put(PlayerComponent.STATE_HITTING, still);
        animationComponent.animations.put(PlayerComponent.STATE_JUMPING, jumping);
        animationComponent.animations.put(PlayerComponent.STATE_FALLING, falling);

        stateComponent.set(PlayerComponent.STATE_STILL);
        renderComponent.texture = animationComponent.animations.get(stateComponent.get()).getKeyFrame(0);

        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.createFixture(fixtureDef);

        return createEntityFrom(playerComponent, bodyComponent, stateComponent, animationComponent, renderComponent);
    }

    private static Entity createEntityFrom(Component... components) {
        Entity entity = new Entity();

        for (Component component: components) {
            entity.add(component);

            if (component instanceof BodyComponent) {
                ((BodyComponent) component).body.setUserData(entity);
            }
        }

        engine.addEntity(entity);

        return entity;
    }
}
