package net.net63.codearcade.LSD.screens.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.screens.GameScreen;
import net.net63.codearcade.LSD.utils.GUIBuilder;

/**
 * Created by Basim on 15/12/15.
 */
public class PauseScreen extends AbstractOverlay {

    public PauseScreen(LSD game, GameScreen previousGame) {
        super(game, previousGame);

        disposeGame = false;
    }

    @Override
    public void setupUI(Stage stage) {

        Label pauseLabel = GUIBuilder.createLabel("Game Paused", Assets.FontSizes.FIFTY, Color.ORANGE);
        pauseLabel.setPosition((800 - pauseLabel.getWidth()) / 2, 300);

        stage.addActor(pauseLabel);
    }

    @Override
    public void checkChange() {
        if (Gdx.input.justTouched()) {
            previousGame.resumeLogic();
            game.setScreen(previousGame);
        }
    }
}
