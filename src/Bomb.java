import bagel.Image;

public class Bomb extends Weapon {

    private final static int BOMB_SHOOTING_FRAME_RANGE = 50;
    private final static Image BOMB_IMAGE = new Image("res/level-1/bomb.png");

    /**
     * Creates Bomb weapon
     */
    public Bomb() {
        super(BOMB_SHOOTING_FRAME_RANGE, BOMB_IMAGE);
    }


}
