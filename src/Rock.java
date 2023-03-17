import bagel.Image;

public class Rock extends Weapon {
    private final static int ROCK_SHOOTING_FRAME_RANGE = 25;
    private final static Image ROCK_IMAGE = new Image("res/level-1/rock.png");

    /**
     * Creates Rock weapon
     */
    public Rock() {
        super(ROCK_SHOOTING_FRAME_RANGE, ROCK_IMAGE);
    }

}
