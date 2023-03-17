import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;
// reused code from project 1 solution

public class Bird {
    private final static Image LEVEL_ZERO_WING_DOWN_IMAGE = new Image("res/level-0/birdWingDown.png");
    private final static Image LEVEL_ZERO_WING_UP_IMAGE = new Image("res/level-0/birdWingUp.png");

    private final static Image LEVEL_ONE_WING_DOWN_IMAGE = new Image("res/level-1/birdWingDown.png");
    private final static Image LEVEL_ONE_WING_UP_IMAGE = new Image("res/level-1/birdWingUp.png");

    private final static Image[] WING_UP_IMAGES = {LEVEL_ZERO_WING_UP_IMAGE, LEVEL_ONE_WING_UP_IMAGE};
    private final static Image[] WING_DOWN_IMAGES = {LEVEL_ZERO_WING_DOWN_IMAGE, LEVEL_ONE_WING_DOWN_IMAGE};

    private final double SWITCH_FRAME = 10;
    private final double X = 200;
    private final double INITIAL_Y = 350;
    private final double Y_TERMINAL_VELOCITY = 10;
    private final double FALL_SIZE = 0.4;
    private final double FLY_SIZE = 6;

    private LifeBar lifeBar;
    private boolean pickedUpWeapon = false;

    private double y;
    private double yVelocity;
    private Rectangle boundingBox;

    /**
     * Creates bird object with  initial motion and  life bar
     * @param level is Level that bird is created for
     */
    public Bird(Level level) {
        y = INITIAL_Y;
        yVelocity = 0;
        lifeBar = new LifeBar(level.getTotalLives());
        boundingBox = WING_DOWN_IMAGES[level.getLevelNumber()].getBoundingBoxAt(new Point(X, y));
    }

    /**
     * Returns x-coordinate of bird
     * @return double x-coordinate
     */
    public double getX() {
        return X;
    }

    /**
     * Returns y-coordinate of bird
     * @return double y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Used to respawn bird after going out of bounds
     */
    public void respawn() {
        y = INITIAL_Y;
        // once respawned, make the bird fly
        yVelocity = -FLY_SIZE;
    }

    /**
     * Used to update motion and display of bird
     * @param input Input from the user
     * @param level Level that bird is generated in
     * @param frameCount int of game so far
     * @return Rectangle bounding box of the bird
     */
    public Rectangle update(Input input, int level, int frameCount) {

        // make the bird fly if space pressed
        if (input.wasPressed(Keys.SPACE)) {
            yVelocity = -FLY_SIZE;
            WING_DOWN_IMAGES[level].draw(X, y);

        // make bird fall
        } else {
            yVelocity = Math.min(yVelocity + FALL_SIZE, Y_TERMINAL_VELOCITY);

            // show wing flapping behaviour
            if (frameCount % SWITCH_FRAME == 0) {
                WING_UP_IMAGES[level].draw(X, y);
                boundingBox = WING_UP_IMAGES[level].getBoundingBoxAt(new Point(X, y));
            }
            else {
                WING_DOWN_IMAGES[level].draw(X, y);
                boundingBox = WING_DOWN_IMAGES[level].getBoundingBoxAt(new Point(X, y));
            }
        }
        y += yVelocity;

        return boundingBox;
    }

    /**
     * Gets bounding box of bird
     * @return Rectangle bounding box
     */
    public Rectangle getBox() {
        return boundingBox;
    }

    /**
     * Gets life bar of bird
     * @return LifeBar object of bird
     */
    public LifeBar getLifeBar() {
        return lifeBar;
    }

    /**
     * Returns whether bird is out of bounds
     * @return bool indicating out of bounds
     */
    public boolean birdOutOfBound() {
        return (y > Window.getHeight()) || (y < 0);
    }

    /**
     * Returns whether bird picked up weapon
     * @return bool
     */
    public boolean isPickedUpWeapon() {
        return pickedUpWeapon;
    }

    /**
     * Sets if bird picked up weapon
     * @param pickedUpWeapon bool to set value to
     */
    public void setPickedUpWeapon(boolean pickedUpWeapon) {
        this.pickedUpWeapon = pickedUpWeapon;
    }
}
