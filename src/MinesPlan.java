
/**
 * Minesweeper game plan
 */
public class MinesPlan {

    private boolean[][] mines;
    private boolean[][] hidden;
    private boolean[][] marked;
    private int width;
    private int height;

    /**
     * Default constructor - clear 2x2 plan.
     */
    public MinesPlan() {
        this.width = 2;
        this.height = 2;
        clearAllMines();
        clearAllMarks();
        coverAll();
    }

    /**
     * Constructor for clear plan with given size.
     *
     * @param w width
     * @param h height
     * @throws BadNumberException if w or h is smaller than 2
     */
    public MinesPlan(int w, int h) {
        if (w < 2) {
            throw new BadNumberException("Width of the map must be at least 2.");
        }
        if (h < 2) {
            throw new BadNumberException("Height of the map must be at least 2.");
        }
        this.width = w;
        this.height = h;
        clearAllMines();
        clearAllMarks();
        coverAll();
    }

    /**
     * Removes all mines from the game plan.
     */
    public void clearAllMines() {
        mines = new boolean[width][height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                mines[x][y] = false;
            }
        }
    }

    /**
     * Removes all marks from the game plan.
     */
    public void clearAllMarks() {
        marked = new boolean[width][height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                marked[x][y] = false;
            }
        }
    }

    /**
     * Covers all fields on the game plan.
     */
    public void coverAll() {
        hidden = new boolean[width][height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                hidden[x][y] = true;
            }
        }
    }

    /**
     * Uncovers all fields on the game plan.
     */
    public void uncoverAll() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                hidden[x][y] = false;
            }
        }
    }

    /**
     * Checks if there is a mine in the specified field.
     *
     * @return boolean
     * @param x x-coord
     * @param y y-coord
     * @throws BadCoordsException if the coordinates are off the game plan
     */
    public boolean isMineAt(int x, int y) {
        if (x < 0 || x >= this.width) {
            throw new BadCoordsException("Incorrect x coordinate.");
        }
        if (y < 0 || y >= this.height) {
            throw new BadCoordsException("Incorrect y coordinate.");
        }
        return mines[x][y];
    }

    /**
     * Checks if the specified field is covered.
     *
     * @return boolean
     * @param x x-coord
     * @param y y-coord
     * @throws BadCoordsException if the coordinates are off the game plan
     */
    public boolean isCoveredAt(int x, int y) {
        if (x < 0 || x >= this.width) {
            throw new BadCoordsException("Incorrect x coordinate.");
        }
        if (y < 0 || y >= this.height) {
            throw new BadCoordsException("Incorrect y coordinate.");
        }
        return hidden[x][y];
    }

    /**
     * Checks if the specified field is marked.
     *
     * @return boolean
     * @param x x-coord
     * @param y y-coord
     * @throws BadCoordsException if the coordinates are off the game plan
     */
    public boolean isMarkedAt(int x, int y) {
        if (x < 0 || x >= this.width) {
            throw new BadCoordsException("Incorrect x coordinate.");
        }
        if (y < 0 || y >= this.height) {
            throw new BadCoordsException("Incorrect y coordinate.");
        }
        return marked[x][y];
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    /**
     * Returns number of all mines in the game plan.
     *
     * @return int
     */
    public int getNumberOfMines() {
        int mineCounter = 0;
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (mines[x][y]) {
                    mineCounter++;
                }
            }
        }
        return mineCounter;
    }

    /**
     * Returns the number of mines in the surrounding 8 fields.
     *
     * @param x x-coord
     * @param y y-coord
     * @throws BadCoordsException if the coordinates are off the game plan
     */
    public int getNumberOfMines(int x, int y) {
        if (x < 0 || x >= this.width) {
            throw new BadCoordsException("Incorrect x coordinate.");
        }
        if (y < 0 || y >= this.height) {
            throw new BadCoordsException("Incorrect y coordinate.");
        }
        int mineCounter = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (x + i < 0 || x + i >= this.width || y + j < 0 || y + j >= this.height) {
                    continue;
                }
                if (mines[x + i][y + j] && !(i == 0 && j == 0)) {
                    mineCounter++;
                }
            }
        }
        return mineCounter;
    }

    /**
     * Returns number of covered fields in the game plan.
     *
     * @return int
     */
    public int getNumberOfCovered() {
        int coveredCounter = 0;
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (hidden[x][y]) {
                    coveredCounter++;
                }
            }
        }
        return coveredCounter;
    }

    /**
     * Sets new mine at given coordinates.
     *
     * @param x x-coord
     * @param y y-coord
     * @throws BadCoordsException if the coordinates are off the game plan
     */
    public void setMineAt(int x, int y) {
        if (x < 0 || x >= this.width) {
            throw new BadCoordsException("Incorrect x coordinate.");
        }
        if (y < 0 || y >= this.height) {
            throw new BadCoordsException("Incorrect y coordinate.");
        }
        mines[x][y] = true;
    }

    public void clearMineAt(int x, int y) {
        if (x < 0 || x >= this.width) {
            throw new BadCoordsException("Incorrect x coordinate.");
        }
        if (y < 0 || y >= this.height) {
            throw new BadCoordsException("Incorrect y coordinate.");
        }
        mines[x][y] = false;
    }

    /**
     * Uncovers given field
     *
     * @param x x-coord
     * @param y y-coord
     * @throws BadCoordsException if the coordinates are off the game plan
     */
    public void uncover(int x, int y) {
        if (x < 0 || x >= this.width) {
            throw new BadCoordsException("Incorrect x coordinate.");
        }
        if (y < 0 || y >= this.height) {
            throw new BadCoordsException("Incorrect y coordinate.");
        }
        hidden[x][y] = false;
    }

    /**
     * Sets given field marked or unmarked
     *
     * @param x x-coord
     * @param y y-coord
     * @param marked marked state
     * @throws BadCoordsException if the coordinates are off the game plan
     */
    public void mark(int x, int y, boolean marked) {
        if (x < 0 || x >= this.width) {
            throw new BadCoordsException("Incorrect x coordinate.");
        }
        if (y < 0 || y >= this.height) {
            throw new BadCoordsException("Incorrect y coordinate.");
        }
        this.marked[x][y] = marked;
    }

}
