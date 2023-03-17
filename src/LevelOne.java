import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class LevelOne extends Level {

    private final static int LEVEL_ONE_NUMBER = 1;
    private final static int LEVEL_ONE_THRESHOLD = 12;
    private final static int TOTAL_LIVES_LEVEL_ONE = 12;
    private final static Image LEVEL_ONE_BACKGROUND_IMAGE = new Image("res/level-1/background.png");

    private final static double PIPE_GAP_LOWER_Y = 100.0;
    private final static double PIPE_GAP_UPPER_Y = 500.0;

    private final static int PLASTIC_PIPE = 1;
    private final static int STEEL_PIPE = 2;
    private final static int[] POSSIBLE_PIPE_TYPES = {PLASTIC_PIPE, STEEL_PIPE};

    private ArrayList<Weapon> weapons;
    private final static int ROCK = 1;
    private final static int BOMB = 2;
    private final static int[] POSSIBLE_WEAPON_TYPES = {ROCK, BOMB};

    private final static double INITIAL_WEAPON_STEP_SIZE = 5.0;
    private double weaponStepSize = INITIAL_WEAPON_STEP_SIZE;
    private final static double HALF = 0.5;
    private boolean pipeJustDestroyed = false;

    /**
     * Creates level one object
     */
    public LevelOne() {
        super(LEVEL_ONE_NUMBER, LEVEL_ONE_THRESHOLD, TOTAL_LIVES_LEVEL_ONE, LEVEL_ONE_BACKGROUND_IMAGE);
        weapons = new ArrayList<>();

    }


    /**
     * Generate a new pipe for Level 1
     */
    public void generatePipe() {
        double newPipeX = Window.getWidth();

        // generate random value in the given range for the y-coordinate of top of gap of the pipe
        Random rand = new Random();
        double randomValue = PIPE_GAP_LOWER_Y + (PIPE_GAP_UPPER_Y - PIPE_GAP_LOWER_Y) * rand.nextDouble();
        double newPipeY = randomValue;

        // choose randomly which type of pipe set to generate
        Random r = new Random();
        int randomNumber = r.nextInt(POSSIBLE_PIPE_TYPES.length);
        int pipeType = POSSIBLE_PIPE_TYPES[randomNumber];
        if (pipeType == PLASTIC_PIPE) {
            PlasticPipeSet newPipe = new PlasticPipeSet(newPipeX, newPipeY);
            pipes.add(newPipe);
        } else {
            SteelPipeSet newPipe = new SteelPipeSet(newPipeX, newPipeY);
            pipes.add(newPipe);
        }
    }


    /**
     * Updates the timescale of (motion/spawning of) pipes and weapons in level 1
     * @param input Input from user
     */
    @Override
    public void updateTimescale(Input input) {
        timescale.update(input);

        // update the speed of pipes and weapons and their spawn rates
        if (timescale.isTimescaleJustUpdated()) {
            pipeSpeed = INITIAL_PIPE_SPEED * timescale.getRateFactor();
            weaponStepSize = INITIAL_WEAPON_STEP_SIZE * timescale.getRateFactor();
            spawnRate = Math.round((INITIAL_PIPE_SPAWN_RATE / timescale.getRateFactor()));
        }
    }



    /**
     * Updates score
     * @return int of score increased by
     */
    public int updateScore() {
        // score increased due to bird passing between pipe or destroying pipe
        int scoreIncreasesby = updateScoreFromPassingPipes();

        if (pipeJustDestroyed) {
            scoreIncreasesby += 1;
            pipeJustDestroyed = false;
        }
        return scoreIncreasesby;
    }


    /**
     * Spawns weapons continuously
     * @param frameCount int in game
     */
    public void spawnWeaponsContinuously(int frameCount) {
        frameCount -= (int) Math.round(spawnRate * HALF);

        if (frameCount % spawnRate == 0) {
            generateWeapon();
        }
    }

    /**
     * Generates weapon
     */
    public void generateWeapon() {

        // randomly choose which type of weapon (Rock or Bomb) to generate
        Random r = new Random();
        int randomNumber = r.nextInt(POSSIBLE_WEAPON_TYPES.length);
        int weaponType = POSSIBLE_WEAPON_TYPES[randomNumber];

        if (weaponType == ROCK) {
            Rock newWeapon = new Rock();
            newWeapon.setWeaponStepSize(weaponStepSize);

            // check weapon does not overlap with pipe
            if (!detectWeaponPipeOverlap(newWeapon, false)) {
                weapons.add(newWeapon);

            }
        } else {
            Bomb newWeapon = new Bomb();
            newWeapon.setWeaponStepSize(weaponStepSize);

            if (!detectWeaponPipeOverlap(newWeapon, false)) {
                weapons.add(newWeapon);
            }
        }
    }


    /**
     * Determines whether a new weapon has been generated on top of a pipe
     * @param weapon Weapon
     * @param addedToArrayList boolean if weapon added to weapons list
     * @return boolean overlap
     */
    public boolean detectWeaponPipeOverlap(Weapon weapon, boolean addedToArrayList) {
        boolean overlap = false;

        for (PipeSet pipe: pipes) {
            if (detectWeaponPipeCollision(pipe, weapon)) {
                overlap = true;
                // if overlapping, do not render weapon
                if (addedToArrayList) {
                    weapons.remove(weapon);
                }
                break;
            }
        }
        return overlap;
    }



    /**
     * Updates the motion and state of the weapon
     * @param input Input from user
     */
    public void updateWeapons(Input input) {
        for (Weapon weapon : weapons) {

            // while weapon is not yet picked up by bird, ensure its speed reflects timescale
            if (!weapon.isPickedUp()) {
                weapon.setWeaponStepSize(weaponStepSize);
            }

            // if weapon goes outside screen, stop rendering it
            if (weapon.getWeaponX() < 0) {
                weapon.setOutsideRange(true);
            }

            // if weapon has just been fired by bird
            if (weapon.isPickedUp() && input.wasPressed(Keys.S)) {
                this.getBird().setPickedUpWeapon(false);
                weapon.setActive(true);
            }

            // while weapon is being shot, check if it collides with pipe
            if (weapon.isActive()) {
                weapon.updateShootingFrameCount();
                detectWeaponDestroyedPipe(weapon);
            }

            // update motion of weapon
            weapon.update(this.getBird());

            // if weapon has just been picked up by the bird
            if (detectBirdWeaponCollision(weapon) && !this.getBird().isPickedUpWeapon() && !weapon.isActive()) {
                weapon.setPickedUp(true);
                this.getBird().setPickedUpWeapon(true);
            }
        }

        // if weapon is outside screen or beyond its firing range, stop rendering
        for (Weapon weapon : weapons) {
            if (weapon.isOutsideRange()) {
                weapons.remove(weapon);
                break;
            }
        }
    }


    /**
     * Returns whether collision occurred or not between bird and weapon
     * @param weapon Weapon
     * @return boolean collision
     */
    public boolean detectBirdWeaponCollision(Weapon weapon) {
        boolean collision = false;
        Rectangle birdBox = this.getBird().getBox();
        Rectangle weaponBox = weapon.getBox();

        if (birdBox.intersects((weaponBox))) {
            collision = true;
        }
        return collision;
    }

    /**
     * Checks if a weapon has collided with any pipes
     * @param pipe PipeSet
     * @param weapon Weapon
     * @return boolean collision
     */
    public boolean detectWeaponPipeCollision(PipeSet pipe, Weapon weapon) {
        boolean collision = false;
        Rectangle weaponBox = weapon.getBox();

        // weapon collides if bounding boxes of the images intersect
        if (weaponBox.intersects(pipe.getTopBox()) || weaponBox.intersects(pipe.getBottomBox())) {
            collision = true;
        }

        return collision;
    }


    /**
     * Checks if a weapon has destroyed any pipes
     * @param weapon
     * @return boolean destroyed
     */
    public boolean detectWeaponDestroyedPipe(Weapon weapon) {
        boolean destroyed = false;

        for (PipeSet pipe: pipes) {
            // if collision occurred between pipe and weapon
            if (detectWeaponPipeCollision(pipe, weapon)) {

                // check if pipe type can be destroyed by the type of the weapon
                if ((weapon instanceof Rock) && (pipe instanceof SteelPipeSet)) {
                    destroyed = false;
                } else {
                    destroyed = true;
                }

                // if pipe destroyed, stop rendering pipe and weapon
                if (destroyed == true) {
                    pipe.setDestroyed(true);
                    pipeJustDestroyed = true;
                    weapon.setWeaponUsedUp(true);
                }

                break;
            }
        }
        return destroyed;
    }
}
