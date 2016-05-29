package net.net63.codearcade.LSD;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.managers.LevelManager;
import net.net63.codearcade.LSD.managers.ShaderManager;
import net.net63.codearcade.LSD.managers.SoundManager;
import net.net63.codearcade.LSD.screens.MenuScreen;
import net.net63.codearcade.LSD.utils.Settings;

/**
 * Top level game class and libGDX entry point for the game
 * 
 * @author Basim
 *
 */
public class LSD extends Game {

    private FPSLogger fpsLogger;

	public LSD() {
        super();

        fpsLogger = new FPSLogger();
	}

	@Override
	public void create() {
		Assets.loadAll();
        LevelManager.loadAll();
        SoundManager.loadAll();
        ShaderManager.loadAll();
        Settings.loadSettings();

        SoundManager.playMusic();

		this.setScreen(new MenuScreen(this));
	}

    @Override
    public void pause() {
        super.pause();

        Settings.saveSettings();
    }

    @Override
    public void dispose() {
        super.dispose();

        Assets.dispose();
        LevelManager.dispose();
        SoundManager.dispose();
        Settings.saveSettings();
    }

    @Override
    public void render() {
        super.render();

        if (Settings.isDebugEnabled()) fpsLogger.log();
    }
	
	
}
