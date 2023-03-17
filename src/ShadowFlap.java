import bagel.*;
// reused code from project 1 solution

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 *
 * Please filling your name below
 * @author: Prashansa Singh
 */
public class ShadowFlap extends AbstractGame {
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;

    private final String SCORE_MSG = "SCORE: ";
    private final double SCORE_MSG_X = 100.0;
    private final double SCORE_MSG_Y = 100.0;

    private final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String LEVEL_ONE_START_MSG  = "PRESS 'S' TO SHOOT";
    private final int LEVEL_ONE_MSG_OFFSET = 68;

    private static final String LEVEL_UP_MSG = "LEVEL-UP!";
    private final int LEVEL_UP_SCREEN_DURATION = 150; // updated from 20 -> 150


    private static final String WIN_MSG = "CONGRATULATIONS!";

    private static final String[] LEVEL_WIN_MSGS = {LEVEL_UP_MSG, WIN_MSG};

    private final int LEVEL_ZERO = 0;
    private final int LEVEL_ONE = 1;


    private int frameCount = 0;
    private int LevelUpFrameCount = 0;

    private int score;
    private int currLevel;

    private boolean gameOn;
    private boolean levelOneActive;
    private boolean gameOver;
    private boolean levelZeroWin;
    private boolean levelOneWin;

    private LevelZero levelZero;
    private LevelOne levelOne;
    private Level[] levels;

    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        score = 0;
        currLevel = 0;

        gameOn = false;
        levelOneActive = false;
        levelZeroWin = false;
        levelOneWin = false;
        gameOver = false;

        levelZero = new LevelZero();
        levelOne = new LevelOne();
        levels = new Level[]{levelZero, levelOne};
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        frameCount++;

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // while waiting for level 0 or 1 to start
        if (!gameOn) {
            renderStartScreen(currLevel);
            if (input.wasPressed(Keys.SPACE)) {
                gameOn = true;
            }
        }

        if (gameOn) {
            levels[currLevel].getBackground().draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

            levels[currLevel].updateTimescale(input);
            levels[currLevel].spawnPipesContinuously(frameCount);
            levels[currLevel].updatePipes(frameCount);
            levels[currLevel].updateBird(input, frameCount);


            // Generate and update weapons in level One
            if (currLevel == LEVEL_ONE) {
                levelOne.updateWeapons(input);
                levelOne.spawnWeaponsContinuously(frameCount);
            }

            // if all lives lost
            if (levels[currLevel].getBird().getLifeBar().isLifeBarDepleted()) {
                gameOver = true;
                gameOn = false;
                levelOneWin = false;
            }

            // keep track of score so far in level 0, level up if higher than threshold
            if (score == levels[currLevel].getLevelUpThreshold()) {
                gameOn = false;

                if (currLevel == LEVEL_ZERO) {
                    levelZeroWin = true;
                } else {
                    levelOneWin = true;
                }
            }

            score += levels[currLevel].updateScore();
            renderScore();

        }

        if (!gameOn && levelZeroWin && !levelOneActive && !gameOver) {
            // render level up screen
            if (LevelUpFrameCount >= LEVEL_UP_SCREEN_DURATION) {
                levelOneActive = true;
                currLevel = LEVEL_ONE;
                score = 0;
            }
            LevelUpFrameCount++;
            renderLevelWinScreen(LEVEL_ZERO);
        }

        if (levelOneWin) {
            renderLevelWinScreen(LEVEL_ONE);
        }

        if (gameOver) {
            // render loss screen
            renderGameOverScreen(currLevel);

        }
    }

    /**
     * Renders the start screen of the level specified
     * @param levelNumber is the number of the level
     */
    public void renderStartScreen(int levelNumber) {
        Image background;

        // first render the background of the appropriate level
        if (levelNumber == LEVEL_ZERO) {
            background = levelZero.getBackground();
        } else {
            background = levelOne.getBackground();
        }
        background.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        // render start message for both levels
        FONT.drawString(INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)),
                (Window.getHeight()/2.0 + (FONT_SIZE/2.0)));

        // for level 1, render secondary instruction message below
        if (levelNumber == LEVEL_ONE) {
            FONT.drawString(LEVEL_ONE_START_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_ONE_START_MSG)/2.0)),
                    (Window.getHeight()/2.0 + (FONT_SIZE/2.0)) + LEVEL_ONE_MSG_OFFSET);
        }
    }

    /**
     * Renders the win screen for level specified
     * @param level is the number of the level
     */
    public void renderLevelWinScreen(int level) {
        levels[level].getBackground().draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        FONT.drawString(LEVEL_WIN_MSGS[level], (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_WIN_MSGS[level])/2.0)),
                (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
    }

    /**
     * Renders the game over screen for level specified
     * @param level is the number of the level
     */
    public void renderGameOverScreen(int level) {
        levels[level].getBackground().draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)),
                (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)),
                (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * Renders the scores on the screen
     */
    public void renderScore() {
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, SCORE_MSG_X, SCORE_MSG_Y); // MAKE CONSTANT
    }

}
