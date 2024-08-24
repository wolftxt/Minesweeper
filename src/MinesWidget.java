
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Minesweeper widget
 */
public class MinesWidget extends JComponent {

    private Point selected;
    private BufferedImage imageFlag;
    private BufferedImage imageCovered;
    private BufferedImage imageMine;
    private BufferedImage imageExploded;
    private BufferedImage imageWrongFlag;
    private BufferedImage[] imageBombCount;
    private MinesGame game;

    private double xOffset = 0;

    /**
     * Default widget constructor.
     *
     * Creates a default game with 5x5 fields and 4 mines.
     */
    public MinesWidget() {
        this.game = new MinesGame(5, 5, 4);
    }

    /**
     * Constructor for the given (non-null) game.
     *
     * @param game
     * @throws NullPointerException if game is null.
     */
    public MinesWidget(MinesGame game) {
        this.game = game;
    }

    /**
     * Constructor for given plan size and mines number.
     *
     * It does not catch exceptions thrown by the game constructor.
     *
     * @param w width
     * @param h height
     * @param mines number of mines
     */
    public MinesWidget(int w, int h, int mines) {
        game = new MinesGame(w, h, mines);
    }

    /**
     * Set coordinates of selected cell.
     *
     * Null value disables selection. The field is not highlighted if the
     * coordinates are outside the game plan.
     *
     * @param cell
     */
    private int scaling() {
        int w = this.getWidth() / game.getPlan().getWidth();
        int h = this.getHeight() / game.getPlan().getHeight();
        int s = w < h ? w : h;
        if (h < w && this.getWidth() > game.getPlan().getWidth() * s / 2) {
            xOffset = (this.getWidth() - game.getPlan().getWidth() * s) / 2;
        } else {
            xOffset = 0;
        }
        return s;
    }

    public void setSelected(Point cell) {
        selected = cell;
    }

    /**
     * Returns the coordinates of the last selected field (or null).
     *
     * @return Point
     */
    public Point getSelected() {
        return selected;
    }

    public void newGame(int w, int h, int mines) {
        this.game = new MinesGame(w, h, mines);
    }

    /**
     * Sets new game.
     *
     * @param game
     * @throws NullPointerException if game is null.
     */
    public void setGame(MinesGame game) {
        if (game == null) {
            throw new NullPointerException("Setting a null game.");
        }
        this.game = game;
    }

    /**
     * Returns the current game.
     *
     * @return MinesGame
     */
    public MinesGame getGame() {
        return this.game;
    }

    /**
     * Draws the game plan.
     *
     * The plan has a fixed box size. Covered fields are dark grey, uncovered
     * fields show the number of mines in the vicinity (except for the field
     * with 0 mines in the vicinity) or mine. The selected field is framed in
     * dark orange.
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        loadImages();
        int s = scaling();
        for (int x = 0; x < game.getPlan().getWidth(); x++) {
            for (int y = 0; y < game.getPlan().getHeight(); y++) {
                if (game.getPlan().isCoveredAt(x, y)) {
                    g.drawImage(imageCovered, x * s + (int) xOffset, y * s, s, s, this);
                }
                if (game.getPlan().isMarkedAt(x, y)) {
                    g.drawImage(imageFlag, x * s + (int) xOffset, y * s, s, s, this);
                }
                if (game.getPlan().isMineAt(x, y) && game.getState() == game.STATE_EXPLODED && !game.getPlan().isMarkedAt(x, y)) {
                    g.drawImage(imageMine, x * s + (int) xOffset, y * s, s, s, this);
                }
                if (!game.getPlan().isMineAt(x, y) && !game.getPlan().isCoveredAt(x, y)) {
                    g.drawImage(imageBombCount[game.getPlan().getNumberOfMines(x, y)], x * s + (int) xOffset, y * s, s, s, this);
                }
                if (game.getState() == game.STATE_EXPLODED && game.getPlan().isMarkedAt(x, y) && !game.getPlan().isMineAt(x, y)) {
                    g.drawImage(imageWrongFlag, x * s + (int) xOffset, y * s, s, s, this);
                }
                if (game.getState() == game.STATE_EXPLODED && !game.getPlan().isCoveredAt(x, y) && game.getPlan().isMineAt(x, y)) {
                    g.drawImage(imageExploded, x * s + (int) xOffset, y * s, s, s, this);
                }
            }
        }
    }

    /**
     * Set selected field from real pixel coordinates on widget
     *
     * @param x_pix
     * @param y_pix
     */
    public void selectPosition(int x_pix, int y_pix) {
        int s = scaling();
        selected = new Point((x_pix - (int) xOffset) / s, y_pix / s);
    }

    /**
     * Uncover field on real pixel coordinates on widget TODO
     *
     * @param x_pix
     * @param y_pix
     */
    public void uncoverPosition(int x_pix, int y_pix) {
        int s = scaling();
        game.uncover((x_pix - (int) xOffset) / s, y_pix / s);
    }

    /**
     * Flip marking on a field after real pixel coordinates on widget TODO
     *
     * @param x_pix
     * @param y_pix
     */
    public void markingPosition(int x_pix, int y_pix) {
        int s = scaling();
        game.switchMarked((x_pix - (int) xOffset) / s, y_pix / s);
    }

    /**
     * Returns plan coordinates from pixel position on widget or null.
     *
     * @param x_pix pixel x position on widget
     * @param y_pix pixel y position on widget
     * @return Point - plan coordinates or null if position outside of plan
     */
    private Point getCoordsFromPosition(int x_pix, int y_pix) {
        int s = scaling();
        return new Point((x_pix - (int) xOffset) / s, y_pix / s);
    }

    /**
     * Loads all images into image attributes or throws RuntimeException.
     */
    private void loadImages() {
        imageBombCount = new BufferedImage[9];
        try {
            for (int i = 0; i < 9; i++) {
                imageBombCount[i] = ImageIO.read(MinesWidget.class.getResourceAsStream("/images/" + i + ".png"));
            }
            imageMine = ImageIO.read(MinesWidget.class.getResourceAsStream("/images/mine.png"));
            imageExploded = ImageIO.read(MinesWidget.class.getResourceAsStream("/images/exploded.png"));
            imageWrongFlag = ImageIO.read(MinesWidget.class.getResourceAsStream("/images/wrongFlag.png"));
            imageCovered = ImageIO.read(MinesWidget.class.getResourceAsStream("/images/covered.png"));
            imageFlag = ImageIO.read(MinesWidget.class.getResourceAsStream("/images/flag.png"));
        } catch (IOException ex) {
            throw new RuntimeException("Error occured while loading images.");
        }
    }
}
