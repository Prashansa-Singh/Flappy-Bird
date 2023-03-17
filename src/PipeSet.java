import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
// reused code from project 1 solution

public abstract class PipeSet {
    private final static double HALF = 0.5;
    private final static int PIPE_GAP = 168;
    private final static int INITIAL_PIPE_SPEED = 5;
    private double pipeSpeed = INITIAL_PIPE_SPEED;
    protected final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI); // same here - do in ur code too

    private Image pipeImage;
    protected double pipeX;
    protected double pipeY;

    // centre y coordinates of top and bottom pipes
    protected double pipeTopCentreY;
    protected double pipeBottomCentreY;


    private boolean destroyed = false;
    private boolean collidedWithBird = false;
    private boolean removed = false;
    private boolean hasPassedBird = false;


    /**
     * Creates pipe object at the given location with the given image
     * @param pipeX is centre x-coordinate of pipe
     * @param pipeY is centre y-coordinate of pipe
     * @param pipeImage is image used for pipe
     */

    public PipeSet(double pipeX, double pipeY, Image pipeImage) {
        this.pipeX = pipeX;
        this.pipeY = pipeY;
        this.pipeImage = pipeImage;
        pipeTopCentreY = pipeY - pipeImage.getHeight() * HALF;
        pipeBottomCentreY = pipeY + PIPE_GAP + pipeImage.getHeight() * HALF;
    }

    /**
     * Returns the pipe x-coordinate
     * @return double for pipe x-coordinate
     */
    public double getPipeX() {
        return pipeX;
    }

    /**
     * Returns whether pipe has collided with bird
     * @return bool for collision
     */
    public boolean isCollidedWithBird() {
        return collidedWithBird;
    }

    /**
     * Returns whether bird has passed pipe
     * @return bool for has passed
     */
    public boolean isHasPassedBird() {
        return hasPassedBird;
    }

    /**
     * Used to set bool for if bird has passed pipe
     * @param hasPassedBird is the new bool value it is set to
     */
    public void setHasPassedBird(boolean hasPassedBird) {
        this.hasPassedBird = hasPassedBird;
    }

    /**
     * Returns whether the pipe has stopped rendering
     * @return bool for if removed
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     * Used to set bool for if pipe has stopped rendering
     * @param removed is new bool value for removed
     */
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    /**
     * Used to set bool for if the bird has collided with pipe
     * @param collidedWithBird is new bool value for collision
     */
    public void setCollidedWithBird(boolean collidedWithBird) {
        this.collidedWithBird = collidedWithBird;
    }

    /**
     * Updates motion of pipe
     */
    public void update() {
        if (!removed) {
            renderPipeSet();
            pipeX -= pipeSpeed;
        }
    }

    /**
     * Returns bounding box for top pipe
     * @return Rectangle bounding box
     */
    public Rectangle getTopBox() {
        return pipeImage.getBoundingBoxAt(new Point(pipeX, pipeTopCentreY));
    }

    /**
     * Returns bounding box for bottom pipe
     * @return Rectangle bounding box
     */
    public Rectangle getBottomBox() {
        return pipeImage.getBoundingBoxAt(new Point(pipeX, pipeBottomCentreY));
    }

    /**
     * Renders pipe set
     */
    public void renderPipeSet() {
        pipeImage.draw(pipeX, pipeTopCentreY);
        pipeImage.draw(pipeX, pipeBottomCentreY, ROTATOR);

    }

    /**
     * Returns if pipe has been destroyed
     * @return bool for destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Used to set the bool value for if pipe has been destroyed
     * @param destroyed is new bool value provided
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /**
     * Used to set pipeSpeed
     * @param pipeSpeed is new double pipeSpeed
     */
    public void setPipeSpeed(double pipeSpeed) {
        this.pipeSpeed = pipeSpeed;
    }
}
