import java.io.*;
import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;

class BgPanel extends JPanel {
    Image bg = new ImageIcon("img/Untitled-1.png").getImage();
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
}
