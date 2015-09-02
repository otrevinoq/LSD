package net.net63.codearcade.LSD.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Class to load and handle all the assets 
 * 
 * @author Basim
 *
 */
public class Assets {
	
	public static class Images {
		public static final String BACKGROUND = "images/bg.jpg";
	}
    private static final String[] _Images = { Images.BACKGROUND };
	
	public static class Fonts {
		public static final String DEFAULT = "fonts/DisplayOTF";
	}
    private static final String[] _Fonts = { Fonts.DEFAULT };
	
	public static class FontSizes {
        public static final int TEN = 10;
        public static final int TWENTY = 20;
        public static final int FIFTY = 50;
        public static final int HUNDRED = 100;
    }
    private static final int[] _FontSizes = { FontSizes.TEN, FontSizes.TWENTY, FontSizes.FIFTY, FontSizes.HUNDRED };

    public static class Animations {

        public static final String PLAYER_STILL = "images/ball_anim.png";
        public static final String PLAYER_JUMPING = "images/ball_anim_jump.png";

    }
    public static final ArrayMap<String, Animation> animationList = new ArrayMap<String, Animation>();
    private static final String[] _Animations = { Animations.PLAYER_STILL, Animations.PLAYER_JUMPING };

	private static AssetManager assetManager = new AssetManager();
	
	//Private constructor to prevent instantiation
	private Assets () { }
	
	/**
	 *  Static method that loads all the assets in the asset classes
	 */
	public static void loadAll() {
		
		for (String image: _Images) assetManager.load(image, Texture.class);
        for (String animation: _Animations) assetManager.load(animation, Texture.class);

		for (String font: _Fonts) {
            for (int size: _FontSizes) {
                assetManager.load(font + size + ".fnt", BitmapFont.class);
            }
		}

		assetManager.finishLoading();

        //Setup the animations
        for (String animation: _Animations) {
            TextureRegion texture = new TextureRegion(assetManager.get(animation, Texture.class));
            TextureRegion[] regions = texture.split(24, 24)[0];

            animationList.put(animation, new Animation(0.1f, regions));
        }
	}
	
	/**
	 * Get a loaded asset of type T from the loaded cache
	 * 
	 * @param fileName The identifier for the asset 
	 * @param type The type to return
	 * @return	The asset with the type as specified
	 */
	public static <T> T getAsset(String fileName, Class<T> type) {
		return assetManager.get(fileName, type);
	}
	
	/**
	 * Returns a BitmapFont of the specified font and size
	 * 
	 * @param fontName	The font name
	 * @param size	The size of the font 
	 * @return The associated BitmapFont
	 */
	public static BitmapFont getFont(String fontName, int size) {
		return assetManager.get(fontName + size + ".fnt", BitmapFont.class);
	}


    public static Animation getAnimation(String animation) {
        return animationList.get(animation);
    }

	/**
	 * Dispose all the assets but the Asset Manager still remains (re-usable)
	 */
	public static void clear() {
		assetManager.clear();
	}
	
	/**
	 * Dispose all the assets as well as the Asset Manager (not resettable)
	 */
	public static void dispose() {
		assetManager.dispose();
	}
	
}
