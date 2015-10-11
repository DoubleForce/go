package Window;

import Go.Go;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class Window extends JFrame {
    final static int WINDOW_WHIDTH = 508;
    final static int WINDOW_HEIGHT = 523;

    public static Image visibleImage;
    JLabel l = new JLabel();

    public Window() throws IOException {
        JFrame f = new JFrame();

        visibleImage =(Image) ImageIO.read(new File("src/Window/deleteme.jpg"));//Go.PATH+"\\src\\Window\\deleteme.jpg"));

        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //закрывать после закрытия
        f.setResizable(false);  //Держать открытым
        f.setSize(WINDOW_WHIDTH, WINDOW_HEIGHT);
        f.setLocationRelativeTo(null);  //Установить по центру
        f.add(l);
        ImageIcon icon = new ImageIcon(visibleImage);
        l.setIcon(icon);
        f.setVisible(true);
    }

    /*
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        ImageIcon icon = new ImageIcon(visibleImage);
        l.setIcon(icon);
    }*/

}
