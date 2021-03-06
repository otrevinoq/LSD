package net.net63.codearcade.LSD.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.net63.codearcade.LSD.LSD;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.managers.LevelManager;
import net.net63.codearcade.LSD.managers.ShaderManager;
import net.net63.codearcade.LSD.managers.SoundManager;
import net.net63.codearcade.LSD.utils.BackgroundRenderer;
import net.net63.codearcade.LSD.utils.CentreGUI;
import net.net63.codearcade.LSD.utils.GUIBuilder;
import net.net63.codearcade.LSD.utils.Settings;

import java.util.ArrayList;

/**
 * A screen allowing the player to select which
 * level they want to play of a specific pack
 *
 * Created by Basim on 13/11/15.
 */
public class LevelSelectScreen extends AbstractScreen {

    // ------------------- Settings / Constants --------------------

    private static final int NUM_COLS = 4;
    private static final int MAX_ROWS = 4;

    private static final int PADDING_TOP = 40;
    private static final int PADDING_SIDE = 40;

    private static final float BUTTON_WIDTH = 1000 * 0.13f;
    private static final float BUTTON_HEIGHT = 690 * 0.13f;

    private static final Color TEXT_COLOR = new Color(100/255f, 100/255f, 100/255f, 1f);
    private static final Color DISABLED_TEXT_COLOR = new Color(100/255f, 100/255f, 100/255f, 0.3f);

    // ----------------- Instance Variables ---------------------

    //Temporary vector for storage
    private static final Vector3 tmp = new Vector3();

    private LSD game;
    private int levelPack;

    private CentreGUI centreGUI;
    private BackgroundRenderer backgroundRenderer;

    private ImageButton backButton;
    private ArrayList<ImageButton> buttons;

    private boolean changing = false;
    private boolean backClicked = false;
    private int levelTo = 0;

    public LevelSelectScreen(LSD game, int mapPack) {
        super(game);

        this.game = game;

        //Get the level pack
        this.levelPack = mapPack;
        buttons = new ArrayList<ImageButton>();

        //Create renderers
        centreGUI = new CentreGUI();
        backgroundRenderer = new BackgroundRenderer(ShaderManager.Shaders.MENU, BackgroundRenderer.DEFAULT);

        setupUI();
    }

    /**
     * Utility method to setup all the GUI
     */
    private void setupUI() {

        //Keep a pointer to the stage
        Stage stage = centreGUI.getStage();

        //Create and position the title at the top centre
        Label title = GUIBuilder.createLabel("Level Select", Assets.FontSizes.FIFTY, Color.YELLOW);
        title.setX((800 -  title.getWidth()) / 2);
        title.setY(580 - title.getHeight());

        //Back button (positioned in the top left corner)
        backButton = GUIBuilder.createButton(Assets.Buttons.BACK);
        float ratio = 2f/3;
        backButton.setSize(BUTTON_WIDTH * ratio, BUTTON_HEIGHT * ratio);
        backButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                backClicked = true;
                SoundManager.playSound(SoundManager.getClick());
                event.handle();
            }


        });

        //Add to the stage
        stage.addActor(title);
        stage.addActor(backButton);

        //The main table for storing the buttons
        Table buttonTable = new Table();
        buttonTable.setPosition(PADDING_SIDE, PADDING_TOP);
        buttonTable.setSize(800 - 2*PADDING_SIDE, title.getY() - 2*PADDING_TOP);

        int cols = 0;

        //Loop over every level and add a button for it to the table
        for (int i = 0; i < LevelManager.getPack(levelPack).numLevels; i++) {

            //Create and add the button to the table
            buttonTable.add(createButton(i))
                    .width(BUTTON_WIDTH)
                    .height(BUTTON_HEIGHT)
                    .uniform()
                    .fill().expand();

            //Add a new row at each appropriate position
            cols++;
            if (cols >= NUM_COLS) {
                buttonTable.row();
                cols = 0;
            }
        }

        //Add the table to the GUI stage
        stage.addActor(buttonTable);
    }

    /**
     * Create a new button with the specified index
     *
     * @param num The number to show (zero based but the text will be 1 based)
     * @return The image button with the image and text
     */
    private Actor createButton(int num) {

        //Create the label
        Label text = GUIBuilder.createLabel(Integer.toString(num + 1), Assets.FontSizes.FORTY, TEXT_COLOR);
        text.setAlignment(Align.center);

        //Check whether or not the current level is enabled or not
        if (Settings.getLevelsUnlocked(LevelManager.getPack(levelPack).name) < num) {

            //Get the image for this disabled button
            Image disabled = new Image(Assets.getAsset(Assets.Images.LEVEL_SELECT_DISABLED, Texture.class));

            //Set the correct text colour
            text.setColor(DISABLED_TEXT_COLOR);

            //Create a new table and stack the button with the text
            Table container = new Table();
            container.stack(disabled, text).expand().fill();

            //Return this table
            return container;
        }

        //Create the button
        ImageButton button = GUIBuilder.createButton(Assets.Buttons.PLAIN);

        //The contents table
        Table contents = new Table();
        contents.add(text).center().colspan(3);

        contents.row();
        int starsCollected = Settings.getStarsCollected(LevelManager.getPack(levelPack).name, num);
        Texture star = Assets.getAsset(Assets.Images.STAR, Texture.class);
        Texture empty = Assets.getAsset(Assets.Images.EMPTY_STAR, Texture.class);

        for (int i = 0; i < 3; i++) {
            contents.add(new Image(i < starsCollected ? star : empty)).size(20).space(2);
        }

        //Align the inside on top on the centre
        button.clearChildren();
        button.stack(button.getImage(), contents).expand().fill();

        //Add a click listener
        button.addListener(new ButtonClickListener(num));
        buttons.add(button);

        return button;
    }

    /**
     * Listener class that modifies the state variables on a user click
     */
    private class ButtonClickListener extends ClickListener {

        private int level;

        public ButtonClickListener(int level) {
            this.level = level;
        }

        @Override
        public void clicked (InputEvent event, float x, float y) {
            changing = true;
            levelTo = level;

            SoundManager.playSound(SoundManager.getClick());
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        //Resize the background and the GUI
        backgroundRenderer.resize(width, height);
        centreGUI.resize(width, height);

        //Position the back button in the top left corner
        Viewport viewport = centreGUI.getStage().getViewport();
        tmp.set(0, 0, 0);
        viewport.getCamera().unproject(tmp);
        backButton.setPosition(tmp.x + 10, tmp.y - 10 - backButton.getHeight());
    }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        //Set the correct hover of each button
        for (ImageButton button : buttons) button.setChecked(button.isOver());
        backButton.setChecked(backButton.isOver());

        //Render the background and the GUI
        backgroundRenderer.render(deltaTime);
        centreGUI.render(deltaTime);

        //Check if changing screen and change to the appropriate one
        if (changing) {
            dispose();
            game.setScreen(new GameScreen(game, levelPack, levelTo));
        }

        //If the back button was clicked go back to the menu
        if (backClicked) {
            dispose();
            game.setScreen(new PackSelectScreen(game));
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        backgroundRenderer.dispose();
        centreGUI.dispose();
    }


}
