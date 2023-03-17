import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Random;

public abstract class Weapon {
    private static final double SPAWN_LOWER_Y = 100.0;
    private final double SPAWN_UPPER_Y = 500.0;
    private static final double SPAWN_X = Window.getWidth();

    private final double INITIAL_WEAPON_STEP_SIZE = 5.0;
    private final double WEAPON_SHOOT_SPEED = 5.0;

    private double weaponStepSize = INITIAL_WEAPON_STEP_SIZE;

    private boolean isPickedUp = false;
    private boolean isActive = false;
    private boolean outsideRange = false;
    private boolean weaponUsedUp = false;

    private int shootingFrameRange;
    private int shootingFrameCount = 0;
    private Image weaponImage;

    private double weaponX;
    private double weaponY;

    /**
     * Creates Weapon object
     * @param shootingFrameRange int of weapon type
     * @param weaponImage Image used to generate weapon
     */
    public Weapon(int shootingFrameRange, Image weaponImage) {
        this.shootingFrameRange = shootingFrameRange;
        this.weaponImage = weaponImage;

        weaponX = SPAWN_X;

        // generate random value in the given range for the y-coordinate that weapon is spawned at
        Random rand = new Random();
        weaponY = SPAWN_LOWER_Y + (SPAWN_UPPER_Y - SPAWN_LOWER_Y) * rand.nextDouble();

    }

    /**
     * Updates the motion of the weapon
     * @param bird Bird object
     */
    public void update(Bird bird) {

        // if weapon is not yet picked up by bird
        if (!isPickedUp) {
            updateBeforePickedUp();

        // if weapon has been picked up by bird but not yet fired
        } else if (isPickedUp && !isActive) {
            updateWhilePickedUp(bird);

        // while weapon is being fired
        } else if (isActive) {
            updateWhileActive();
        }
    }


    /**
     * Keeps track of how many frames the weapon has been fired for
     */
    public void updateShootingFrameCount() {
        shootingFrameCount++;
    }


    /**
     * Updates motion of bird while it has not been picked up by bird
     */
    public void updateBeforePickedUp() {
        weaponX -= weaponStepSize;
        render();
    }

    /**
     * Updates motion of bird while picked up by bird
     * @param bird
     */
    public void updateWhilePickedUp(Bird bird) {
        // while weapon is picked up but not yet fired, render at beak of bird
        weaponX = bird.getBox().right();
        weaponY = bird.getY();
        render();
    }


    /**
     * Updates motion of bird while it is active (being fired)
     */
    public void updateWhileActive() {
        // only render weapon if within it is shooting frame range and has not destroyed a pipe yet
        if (shootingFrameCount <= shootingFrameRange && !weaponUsedUp) {
            weaponX += WEAPON_SHOOT_SPEED;
            render();
        } else {
            outsideRange = true;
        }
    }

    /**
     * Renders the weapon image
     */
    public void render() {
        weaponImage.draw(weaponX, weaponY);
    }

    /**
     * Returns bounding box of weapon
     * @return Rectangle bounding box
     */
    public Rectangle getBox() {
        return weaponImage.getBoundingBoxAt(new Point(weaponX, weaponY));
    }


    /**
     * Indicates if weapon has been picked up
     * @return boolean
     */
    public boolean isPickedUp() {
        return isPickedUp;
    }

    /**
     * Used to set whether weapon picked up
     * @param pickedUp new bool value
     */
    public void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }

    /**
     * Indicates if weapon is active
     * @return bool for active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Used to set whether weapon active (fired)
     * @param active new bool value
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Used to set if weapon travelled out of range
     * @param outsideRange new bool value
     */
    public void setOutsideRange(boolean outsideRange) {
        this.outsideRange = outsideRange;
    }

    /**
     * To check if weapon is within shooting range
     * @return bool
     */
    public boolean isOutsideRange() {
        return outsideRange;
    }

    /**
     * Set if weapon used up (destroyed pipe)
     * @param weaponUsedUp bool
     */
    public void setWeaponUsedUp(boolean weaponUsedUp) {
        this.weaponUsedUp = weaponUsedUp;
    }

    /**
     * Return the X coord of weapon
     * @return double x-coord
     */
    public double getWeaponX() {
        return weaponX;
    }

    /**
     * Sets weapon speed
     * @param weaponStepSize double new speed
     */
    public void setWeaponStepSize(double weaponStepSize) {
        this.weaponStepSize = weaponStepSize;
    }

}


