import bagel.Input;
import bagel.Keys;

public class Timescale {
    private final static int INITIAL_TIMESCALE = 1;
    private final static int MIN_TIMESCALE = 1;
    private final  static int MAX_TIMESCALE = 5;
    private final static double INITIAL_FACTOR = 1.0;
    private final static double CHANGE_FACTOR = 1.5;
    private boolean timescaleJustUpdated = false;

    private int timescaleValue;
    private double rateFactor;

    public Timescale() {
        timescaleValue = INITIAL_TIMESCALE;
        rateFactor = INITIAL_FACTOR;
    }

    /**
     * Takes in input and using that updates the timescale of the game
     * @param input Input from user
     */
    public void update(Input input) {
        timescaleJustUpdated = false;
        int newTimescaleValue;

        // when 'L' is pressed, increase timescale if possible
        if (input.wasPressed(Keys.L)) {
            newTimescaleValue = Math.min(timescaleValue + 1, MAX_TIMESCALE);

            if (newTimescaleValue != timescaleValue) {
                rateFactor *= CHANGE_FACTOR;
                timescaleJustUpdated = true;
            }
            timescaleValue = newTimescaleValue;
        }

        // when 'K' is pressed, decrease timescale if possible
        if (input.wasPressed(Keys.K)) {
            newTimescaleValue = Math.max(timescaleValue - 1, MIN_TIMESCALE);

            if (newTimescaleValue != timescaleValue) {
                rateFactor = rateFactor / CHANGE_FACTOR;
                timescaleJustUpdated = true;
            }
            timescaleValue = newTimescaleValue;

        }
    }

    /**
     * Returns a value which represents the stacked total change in rate based on all timescale changes in the level
     * @return double total change factor
     */
    public double getRateFactor() {
        return rateFactor;
    }

    /**
     * Indicates if timescale of the level was just changed or not
     * @return bool if changed
     */
    public boolean isTimescaleJustUpdated() {
        return timescaleJustUpdated;
    }
}
