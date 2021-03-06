package net.net63.codearcade.LSD.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;

/**
 * Created by Basim on 23/06/15.
 */
public class PlayerComponent implements Component{

    public static final int STATE_INIT      = 0;
    public static final int STATE_STILL     = 1;
    public static final int STATE_AIMING    = 2;
    public static final int STATE_JUMPING   = 3;
    public static final int STATE_FALLING   = 4;
    public static final int STATE_DEAD      = 5;

    public Vector2 launchStart = new Vector2();
    public Vector2 aimPosition = new Vector2();
    public Vector2 launchImpulse = new Vector2();
    public Vector2[] trajectoryPoints;
    public boolean invalidateAim;
    public boolean validLaunch;

    public Entity currentSensor = null;
    public Joint sensorJoint = null;
    public Vector2 collisionPoint;

    public Entity starCollected;

    public boolean isFlying = true;
    public boolean isDead = false;
}
