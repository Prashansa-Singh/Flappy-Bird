import bagel.Image;
import bagel.Window;

import java.util.ArrayList;
import java.util.Random;

public class LevelZero extends Level {

    private final static int LEVEL_ZERO_NUMBER = 0;
    private final static int LEVEL_ZERO_THRESHOLD = 2;
    private final static int TOTAL_LIVES_LEVEL_ZERO = 3;
    private final static Image LEVEL_ZERO_BACKGROUND_IMAGE = new Image("res/level-0/background.png");

    private final static double[] PIPE_GAPS_TOP_Y = {100.0, 300.0, 500.0};

    /**
     * Generates level zero object
     */
    public LevelZero() {
        super(LEVEL_ZERO_NUMBER, LEVEL_ZERO_THRESHOLD, TOTAL_LIVES_LEVEL_ZERO, LEVEL_ZERO_BACKGROUND_IMAGE);
    }

    /**
     * Generates pipe for level 0
     */
    public void generatePipe() {
        double newPipeX = Window.getWidth();

        // pick randomly from PIPE_GAPS_TOP_Y to generate the y-coordinate of top of gap of the pipe
        Random rand = new Random();
        int randomNumber = rand.nextInt(PIPE_GAPS_TOP_Y.length);
        double newPipeY = PIPE_GAPS_TOP_Y[randomNumber];

        // create plastic pipes only in Level 0 and add to pipes array
        PlasticPipeSet newPipe = new PlasticPipeSet(newPipeX, newPipeY);
        pipes.add(newPipe);
    }

    /**
     * Updates score for level 0
     * @return int score increased by
     */
    public int updateScore() {
        return updateScoreFromPassingPipes();
    }
}
