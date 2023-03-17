import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class FlameSet {
    private final Image FLAME_IMAGE = new Image("res/level-1/flame.png");
    private final int FLAME_SHOOT_RATE = 20;
    private final int FLAME_DURATION = 30;
    private final double FLAME_IMG_OFFSET = 10.0;

    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);

    private double flameX;
    private double flameTopY;
    private double flameBottomY;
    private boolean duringFlame;
    private int flameDurationCount;

    /**
     * Creates flames from steel pipe
     */
    public FlameSet() {
        duringFlame = false;
        flameDurationCount = 0;
    }

    /**
     * Updates the motion of the flame set and render the shooting of flames
     * @param pipeX double of pipe flame connected too
     * @param pipeTopBox Rectangle of top pipe
     * @param pipeBottomBox Rectangle of bottom pipe
     * @param frameCount of game so far
     */
    public void update(double pipeX, Rectangle pipeTopBox, Rectangle pipeBottomBox, int frameCount) {

        // mechanism to shoot out flames
        if ((frameCount % (FLAME_SHOOT_RATE + FLAME_DURATION) == 0) || duringFlame) {

            // if first frame when flame is shot
            if (!duringFlame) {
                duringFlame = true;
                flameDurationCount = 0;
            }
            flameDurationCount++;

            // stop shooting flames after frame duration reached
            if (flameDurationCount == FLAME_DURATION) {
                duringFlame = false;
            }

            flameX = pipeX;
            flameTopY = pipeTopBox.bottom() + FLAME_IMG_OFFSET;
            flameBottomY = pipeBottomBox.top() - FLAME_IMG_OFFSET;

            // render flame
            FLAME_IMAGE.draw(flameX, flameTopY);
            FLAME_IMAGE.draw(flameX, flameBottomY, ROTATOR);
        }

    }

    /**
     * Gets bounding box of top flame
     * @return Rectangle bounding box
     */
    public Rectangle getTopBox() {
        return FLAME_IMAGE.getBoundingBoxAt(new Point(flameX, flameTopY));
    }

    /**
     * Gets bounding box of bottom flame
     * @return Rectangle bounding box
     */
    public Rectangle getBottomBox() {
        return FLAME_IMAGE.getBoundingBoxAt(new Point(flameX, flameBottomY));
    }

    /**
     * Indicates whether a flame is being currently shot out of the steel pipes
     * @return bool if shot out
     */
    public boolean isDuringFlame() {
        return duringFlame;
    }
}
