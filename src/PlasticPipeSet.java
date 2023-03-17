import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class PlasticPipeSet extends PipeSet {
    private static final Image PLASTIC_PIPE_IMAGE = new Image("res/level/plasticPipe.png");

    /**
     * Creates plastic pipe set object
     * @param pipeX double to generate pipe x-coordinate
     * @param pipeY double to generate pipe y-coordinate
     */
    public PlasticPipeSet(double pipeX, double pipeY) {
        super(pipeX, pipeY, PLASTIC_PIPE_IMAGE);
    }

}
