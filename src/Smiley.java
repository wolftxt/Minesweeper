
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class Smiley extends JComponent {

    private BufferedImage smiley;
    private BufferedImage winSmiley;
    private BufferedImage loseSmiley;

    private int s;
    private double xOffset;

    private int smileyState = 1;

    public Smiley() {
        loadImages();
    }

    public int getSmileyState() {
        return smileyState;
    }

    public void setSmileyState(int smileyState) {
        this.smileyState = smileyState;
    }

    public int getS() {
        return s;
    }

    public double getxOffset() {
        return xOffset;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        s = this.getWidth() < this.getHeight() ? this.getWidth() : this.getHeight();
        xOffset = (1 - (double) this.getHeight() / this.getWidth()) * this.getWidth() / 2;
        if (smileyState == 1) {
            g.drawImage(smiley, (int) xOffset, 0, s, s, this);
        }
        if (smileyState == 2) {
            g.drawImage(loseSmiley, (int) xOffset, 0, s, s, this);
        }
        if (smileyState == 3) {
            g.drawImage(winSmiley, (int) xOffset, 0, s, s, this);
        }

    }

    private void loadImages() {
        try {
            smiley = ImageIO.read(MinesWidget.class.getResourceAsStream("/images/smiley.png"));
            winSmiley = ImageIO.read(MinesWidget.class.getResourceAsStream("/images/winSmiley.png"));
            loseSmiley = ImageIO.read(MinesWidget.class.getResourceAsStream("/images/loseSmiley.png"));
        } catch (IOException ex) {
            throw new RuntimeException("Error occured while loading images.");
        }
    }
}
