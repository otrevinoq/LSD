package net.net63.codearcade.LSD.screens.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.screens.AbstractScreen;
import net.net63.codearcade.LSD.utils.GUIBuilder;

/**
 * Created by Basim on 31/01/16.
 */
public class YesNoDialogScreen extends AbstractOverlay {

    private String title;
    private String message;

    public YesNoDialogScreen(LSD game, AbstractScreen previousScreen, String title, String message) {
        super(game, previousScreen);

        this.title = title;
        this.message = message;
    }

    @Override
    public void setupUI(Stage stage) {

        Table contentTable = new Table();
        Table wrapperTable = new Table();

        Label titleLabel = GUIBuilder.createLabel(title, Assets.FontSizes.THIRTY, Color.DARK_GRAY);
        Label textLabel = GUIBuilder.createLabel(message, Assets.FontSizes.TWENTY, Color.GRAY);
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.center);

        contentTable.add(titleLabel).padLeft(50).padRight(50).padTop(20).center();
        contentTable.row();
        contentTable.add(textLabel).fillX().center().pad(20);

        NinePatch bg = new NinePatch(Assets.getAsset(Assets.Images.SETTINGS_BACKGROUND, Texture.class), 9, 9, 9, 9);
        final Image background = new Image(bg);
        background.setSize(contentTable.getWidth(), contentTable.getHeight());
        wrapperTable.stack(background, contentTable);
        wrapperTable.setPosition( (800 - background.getWidth()) / 2, (600 - background.getHeight()) / 2);

        stage.addActor(wrapperTable);
    }

    @Override
    public void update() {
        if (Gdx.input.justTouched()) {
            previousScreen.resumeLogic();
            game.setScreen(previousScreen);
        }
    }
}
