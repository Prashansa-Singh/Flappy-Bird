import bagel.Image;

public class LifeBar {
    private static final Image FULL_HEART_IMAGE = new Image("res/level/fullLife.png");
    private static final Image EMPTY_HEART_IMAGE = new Image("res/level/noLife.png");
    private static final double FIRST_HEART_X = 100.0;
    private static final double HEART_Y = 15.0;
    private static final double HEART_GAP = 50.0;

    private int numLivesStart;
    private int numLivesLeft;
    private boolean lifeBarDepleted = false;


    /**
     * Creates life bar of bird
     * @param numLivesStart int of initial number of lives bird has
     */
    public LifeBar(int numLivesStart) {
        this.numLivesStart = numLivesStart;
        numLivesLeft = numLivesStart;
        lifeBarDepleted = false;
        renderLifeBar(numLivesLeft);
    }

    /**
     * Renders life bar
     * @param numLivesLeft int showing how many full lives left
     */
    public void renderLifeBar(int numLivesLeft) {
        double currHeartX = FIRST_HEART_X;
        Image heartImage = FULL_HEART_IMAGE;

        // render hearts one by one
        for (int i = 0; i < numLivesStart; i++) {
            if (i == numLivesLeft) {
                heartImage = EMPTY_HEART_IMAGE;
            }
            heartImage.drawFromTopLeft(currHeartX, HEART_Y);
            currHeartX += HEART_GAP;
        }

    }



    /**
     * Update lives if bird loses life
     */
    public void lifeLost() {
        numLivesLeft--;
        if (numLivesLeft == 0) {
            lifeBarDepleted = true;
        }
    }

    /**
     * Checks if life bar depleted
     * @return bool if depleted
     */
    public boolean isLifeBarDepleted() {
        return lifeBarDepleted;
    }

    /**
     * Checks number of lives bird has left
     * @return int lives
     */
    public int getNumLivesLeft() {
        return numLivesLeft;
    }

}
