import bagel.Image;
import bagel.Input;
import bagel.util.Rectangle;

import java.util.ArrayList;

public abstract class Level {

    private int levelNumber;
    private int totalLives;
    private int levelUpThreshold;
    private Image background;
    private Bird bird;

    public Bird getBird() {
        return bird;
    }
    protected Timescale timescale;
    protected ArrayList<PipeSet> pipes;


    protected final static long INITIAL_PIPE_SPAWN_RATE = 100;
    // spawnRate for both pipes and weapons
    protected long spawnRate;

    protected final static int INITIAL_PIPE_SPEED = 5;
    protected double pipeSpeed = INITIAL_PIPE_SPEED;


    /**
     * Generates level
     * @param levelNumber int of level
     * @param levelUpThreshold int score to level up by
     * @param totalLives int of bird in level
     * @param background Image of level
     */
    public Level(int levelNumber, int levelUpThreshold, int totalLives, Image background) {
        this.levelNumber = levelNumber;
        this.levelUpThreshold = levelUpThreshold;
        this.totalLives = totalLives;
        this.background = background;

        pipes = new ArrayList<>();
        timescale = new Timescale();
        bird = new Bird(this);
        spawnRate = INITIAL_PIPE_SPAWN_RATE;
    }

    /**
     * Spawns pipes continuously
     * @param frameCount int of game so far
     */
    public void spawnPipesContinuously(int frameCount) {
        if (frameCount % spawnRate == 0) {
            generatePipe();
        }
    }

    /**
     * Generates a pipe object
     */
    public abstract void generatePipe();


    /**
     * Updates timescale of level
     * @param input Input from user
     */
    public void updateTimescale(Input input) {
        timescale.update(input);

        if (timescale.isTimescaleJustUpdated()) {
            pipeSpeed = INITIAL_PIPE_SPEED * timescale.getRateFactor();
            spawnRate = Math.round((INITIAL_PIPE_SPAWN_RATE / timescale.getRateFactor()));
        }

    }

    /**
     * Updates the motion and display of pipes
     * @param frameCount int of game so far
     */
    public void updatePipes(int frameCount) {
        for (PipeSet pipe: pipes) {
            pipe.update();
            // set pipe speed based on current timescale of level
            pipe.setPipeSpeed(pipeSpeed);

            // if pipe travels out of the screen or collides with bird or is destroyed, pipe should disappear
            if (pipe.getPipeX() < 0 || pipe.isCollidedWithBird() || pipe.isDestroyed()) {
                pipe.setRemoved(true);
            }

            // keep track of frameCount to render flames which shoot out of steel pipes
            if (pipe instanceof SteelPipeSet) {
                SteelPipeSet steelPipe = (SteelPipeSet) pipe;
                steelPipe.updateFlames(frameCount);
            }
        }

        // make pipe disappear
        for (PipeSet pipe: pipes) {
            if (pipe.isRemoved()) {
                pipes.remove(pipe);
                break;
            }
        }
    }

    /**
     * Updates motion and keeps track of lives of bird
     * @param input Input from user
     * @param frameCount int of game
     */
    public void updateBird(Input input, int frameCount) {
        boolean lifeLost = false;
        bird.update(input, levelNumber, frameCount);

        // if bird out of bounds, life is lost
        if (bird.birdOutOfBound()) {
            bird.respawn();
            lifeLost = true;
        }
        // if bird collides with pipe (without destroying it), life lost
        if (detectBirdPipeCollision()) {
            lifeLost = true;
        }

        // remove life from life bar and render it
        if (lifeLost) {
            bird.getLifeBar().lifeLost();
        }
        int numLivesLeft = bird.getLifeBar().getNumLivesLeft();
        bird.getLifeBar().renderLifeBar(numLivesLeft);
    }

    /**
     * Gets background of level
     * @return Image
     */
    public Image getBackground() {
        return background;
    }

    /**
     * Gets level number
     * @return int
     */
    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Gets total lives of bird
     * @return int
     */
    public int getTotalLives() {
        return totalLives;
    }

    /**
     * Updates score
     * @return int of how much score increases by
     */
    public abstract int updateScore();


    /**
     * Increases score if bird successfully passes through a pipe
     * @return int of how much score increases by
     */
    public int updateScoreFromPassingPipes() {
        int scoreIncreasesBy = 0;
        double birdX = bird.getX();

        for (PipeSet pipe: pipes) {
            // if bird has recently passed through this pipe
            if (birdX > pipe.getTopBox().right() && !pipe.isHasPassedBird()) {
                scoreIncreasesBy++;
                pipe.setHasPassedBird(true);
            }
        }
        return scoreIncreasesBy;
    }

    /**
     * Detects collision between bird and pipe and flame set
     * @return bool collision
     */
    public boolean detectBirdPipeCollision() {
        Rectangle birdBox = bird.getBox();

        for (PipeSet pipe: pipes) {
            // detect if collision between bird and pipeset
            Rectangle topPipeBox = pipe.getTopBox();
            Rectangle bottomPipeBox = pipe.getBottomBox();
            boolean birdPipeCollision = birdBox.intersects(topPipeBox) || birdBox.intersects(bottomPipeBox);

            // detect if collision between bird and flameset when flame is being shot out of steel pipe
            boolean birdFlameCollision = false;
            if (pipe instanceof SteelPipeSet) {
                SteelPipeSet steelPipe = (SteelPipeSet) pipe;
                if (steelPipe.getFlames().isDuringFlame()) {
                    Rectangle topFlameBox = steelPipe.getFlames().getTopBox();
                    Rectangle bottomFlameBox = steelPipe.getFlames().getBottomBox();
                    birdFlameCollision = birdBox.intersects(topFlameBox) || birdBox.intersects(bottomFlameBox);
                }
            }

            // if collision occurred, pipe disappears and bird life is lost
            if (birdPipeCollision || birdFlameCollision) {
                pipe.setCollidedWithBird(true);
                pipe.setRemoved(true);
                pipes.remove(pipe);
                return true;
            }
        }
        return false;
    }


    /**
     * Gets threshold value for level up
     * @return int
     */
    public int getLevelUpThreshold() {
        return levelUpThreshold;
    }



}
