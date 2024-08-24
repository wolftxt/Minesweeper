
import java.util.Random;
import java.util.LinkedList;

/**
 * Minesweeper game class
 */
public class MinesGame {

    public static int STATE_PLAYING = 1;
    public static int STATE_EXPLODED = 2;
    public static int STATE_DONE = 3;

    private MinesPlan plan;

    /**
     * Game constructor for new game
     *
     * @param w plan width (min. 2)
     * @param h plan height (min. 2)
     * @param mines number of mines (min. 1, max. w*h-1)
     * @throws BadNumberException if w, h or mines has a bad value
     */
    public MinesGame(int w, int h, int mines) {
        if (w < 2) {
            throw new BadNumberException("Width of the plan must be at least 2.");
        }
        if (h < 2) {
            throw new BadNumberException("Height of the plan must be at least 2.");
        }
        if (mines < 1 || mines > w * h - 1) {
            throw new BadNumberException("Incorrect mine number.");
        }
        this.plan = new MinesPlan(w, h);
        placeMines(mines);
    }

    /**
     * Game constructor for given plan
     *
     * @param plan ready to use game plan
     * @throws BadNumberException if the plan is smaller than 2x2 or if there is
     * no mine in the plan
     * @throws NullPointerException if plan is null
     */
    public MinesGame(MinesPlan plan) {
        if (plan == null) {
            throw new NullPointerException("Plan is null");
        }
        if (plan.getWidth() < 2) {
            throw new BadNumberException("Width of the map must be at least 2.");
        }
        if (plan.getHeight() < 2) {
            throw new BadNumberException("Height of the map must be at least 2.");
        }
        if (plan.getNumberOfMines() == 0) {
            throw new BadNumberException("There is no mine.");
        }
        this.plan = plan;
    }

    public MinesPlan getPlan() {
        return this.plan;
    }

    /**
     * Plan setter
     *
     * @param plan ready to use game plan
     * @throws BadNumberException if the plan is smaller than 2x2 or if there is
     * no mine in the plan
     * @throws NullPointerException if plan is null
     */
    public void setPlan(MinesPlan plan) {
        if (plan == null) {
            throw new NullPointerException("Plan is null");
        }
        if (plan.getWidth() < 2) {
            throw new BadNumberException("Width of the map must be at least 2.");
        }
        if (plan.getHeight() < 2) {
            throw new BadNumberException("Height of the map must be at least 2.");
        }
        if (plan.getNumberOfMines() == 0) {
            throw new BadNumberException("There is no mine.");
        }
        this.plan = plan;
    }

    /**
     * Returns plan state
     *
     * @retval STATE_PLAYING Game is playable
     * @retval STATE_EXPLODED A player has uncovered a minefield
     * @retval STATE_DONE Player has uncovered all squares without mines
     */
    public int getState() {
        int uncoveredMinesCounter = 0;
        for (int i = 0; i < plan.getWidth(); i++) {
            for (int j = 0; j < plan.getHeight(); j++) {
                if (plan.isMineAt(i, j) && !plan.isCoveredAt(i, j)) {
                    uncoveredMinesCounter++;
                }
            }
        }
        // Check for a loss
        if (uncoveredMinesCounter > 0) {
            return STATE_EXPLODED;
        }
        // Check for a win
        if (plan.getNumberOfCovered() == plan.getNumberOfMines()) {
            return STATE_DONE;
        }
        return STATE_PLAYING;
    }

    /**
     * Change marking state of an uncovered field during game.
     *
     * Mark unmarked, unmark marked. It will not make the change if the game is
     * not playable.
     *
     * @param x x-coord
     * @param y y-coord
     * @throws WrongActionException if field is uncovered
     * @throws BadCoordsException if coordinates are out of plan
     */
    public void switchMarked(int x, int y) {
        if (getState() != 1) {
            return;
        }
        if (x < 0 || x >= plan.getWidth()) {
            throw new BadCoordsException("Marking or unmarking a wrong coordinate.");
        }
        if (y < 0 || y >= plan.getHeight()) {
            throw new BadCoordsException("Marking or unmarking a wrong coordinate.");
        }
        if (!plan.isCoveredAt(x, y)) {
            throw new WrongActionException("Trying to mark an uncovered coordinate.");
        }
        if (plan.isMarkedAt(x, y)) {
            plan.mark(x, y, false);
        } else {
            plan.mark(x, y, true);
        }
    }

    /**
     * Uncover field during game.
     *
     * Sets the field as uncovered if the game is playable and the field is not
     * marked. Otherwise, no change is made. At 0 mines in the vicinity, it
     * triggers a recursive uncovering.
     *
     * @param x x-coord
     * @param y y-coord
     * @throws BadCoordsException if the coordinates are outside the game plan.
     */
    public void uncover(int x, int y) {
        // Exceptions
        if (getState() != MinesGame.STATE_PLAYING || plan.isMarkedAt(x, y)) {
            return;
        }
        if (x < 0 || x >= plan.getWidth() || y < 0 || y >= plan.getHeight()) {
            throw new BadCoordsException("Uncovering a wrong coordinate.");
        }
        // Rearrange mines for first click
        if (plan.getNumberOfCovered() == plan.getWidth() * plan.getHeight()) {
            if (plan.getNumberOfMines() < plan.getWidth() * plan.getHeight() - 9) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (x + i < 0 || x + i >= plan.getWidth() || y + j < 0 || y + j >= plan.getHeight()) {
                            continue;
                        }
                        if (plan.isMineAt(x + i, y + j)) {
                            placeMines(1);
                            plan.clearMineAt(x + i, y + j);
                        }
                        plan.uncover(x + i, y + j);
                    }
                }
                plan.coverAll();
            } else {
                if (plan.isMineAt(x, y)) {
                    placeMines(1);
                    plan.clearMineAt(x, y);
                }
            }
        }
        // Uncover
        plan.uncover(x, y);
        uncoverZero(x, y);
    }

    /**
     * Recursive method for uncovering fields in the vicinity of the free field.
     *
     * It does nothing if the field is uncovered or marked or off the game plan.
     * It uncovers the field and recursively starts uncovering in the
     * surrounding eight fields if there are no mines in the vicinity. It only
     * uncovers the field and does no recursion if there are any mines in the
     * vicinity.
     *
     * @param x x-coord
     * @param y y-coord
     */
    /*private void uncoverZero(int x, int y) {
        if (getState() != 1 || plan.isMarkedAt(x, y) || x < 0 || x >= plan.getWidth() || y < 0 || y >= plan.getHeight() || plan.getNumberOfMines(x, y) != 0) {
            return;
        }
        LinkedList<int[]> queue = new LinkedList<int[]>();
        queue.add(new int[]{x, y});
        while (!queue.isEmpty()) {
            int[] currentCell = queue.poll();
            int newX = currentCell[0];
            int newY = currentCell[1];
            plan.uncover(newX, newY);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    // Exceptions
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    if (newX + i < 0 || newX + i >= plan.getWidth() || newY + j < 0 || newY + j >= plan.getHeight()) {
                        continue;
                    }
                    if (!plan.isCoveredAt(newX + i, newY + j)) {
                        continue;
                    }
                    // End of exceptions
                    if (plan.getNumberOfMines(newX + i, newY + j) == 0) {
                        queue.add(new int[]{newX + i, newY + j});
                    } else {
                        plan.uncover(newX + i, newY + j);
                    }

                }
            }
        }
    } */
    private void uncoverZero(int x, int y) {
        if (getState() != 1 || plan.isMarkedAt(x, y) || x < 0 || x >= plan.getWidth() || y < 0 || y >= plan.getHeight() || plan.getNumberOfMines(x, y) != 0) {
            return;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Exceptions
                if (i == 0 && j == 0) {
                    continue;
                }
                if (x + i < 0 || x + i >= plan.getWidth() || y + j < 0 || y + j >= plan.getHeight()) {
                    continue;
                }
                if (!plan.isCoveredAt(x + i, y + j)) {
                    continue;
                }
                // End of exceptions
                plan.uncover(x + i, y + j);
                if (plan.getNumberOfMines(x + i, y + j) == 0) {
                    uncoverZero(x + i, y + j);
                }
            }
        }
    }

    /**
     * Randomly place a given number of mines in the covered fields in the plan.
     *
     * @param count Mines count, must be at least 1
     * @throws BadNumberException if the number of mines is less than 1 or
     * greater than the number of covered fields
     */
    private void placeMines(int count) {
        if (count < 1 || count > plan.getWidth() * plan.getHeight() - 1) {
            throw new BadNumberException("Placing an incorrect amount of mines.");
        }
        Random rand = new Random();
        while (count > 0) {
            int randomX = rand.nextInt(plan.getWidth());
            int randomY = rand.nextInt(plan.getHeight());
            if (plan.isMineAt(randomX, randomY)) {
                continue;
            }
            if (!plan.isCoveredAt(randomX, randomY)) {
                continue;
            }
            plan.setMineAt(randomX, randomY);
            count--;
        }
    }
}
