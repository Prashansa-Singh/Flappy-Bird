import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class SteelPipeSet extends PipeSet {
    private static final Image STEEL_PIPE_IMAGE = new Image("res/level-1/steelPipe.png");
    private FlameSet flames;

    /**
     * Creates Steel Pipe Set object
     * @param pipeX double to generate pipe x-coordinate
     * @param pipeY double to generate pipe y-coordinate
     */
    public SteelPipeSet(double pipeX, double pipeY) {
        super(pipeX, pipeY, STEEL_PIPE_IMAGE);
        flames = new FlameSet();
    }

    /**
     * Updates the motion and shooting of the flames
     * @param frameCount int of game so far
     */
    public void updateFlames(int frameCount) {
        flames.update(pipeX, getTopBox(), getBottomBox(), frameCount);
    }

    /**
     * Returns the flames for the steel pipe
     * @return FlameSet flames
     */
    public FlameSet getFlames() {
        return flames;
    }

}
