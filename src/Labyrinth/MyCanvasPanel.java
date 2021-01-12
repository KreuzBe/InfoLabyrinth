package Labyrinth;

import javax.swing.*;
import java.awt.*;

public class MyCanvasPanel extends JPanel {
    private byte[][] feld;

    public MyCanvasPanel(byte[][] feld) { // Beim Erstellen das Feld im Konstruktor �bergeben
        this.feld = feld;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                switch (feld[y][x]) {
                case 1:
                    g.setColor(new Color(100, 100, 100)); // Wand -> dunkelgrau

                    break;

                case 2:
                    g.setColor(new Color(0, 0, 255)); // Position -> blau

                    break;

                case 3:
                    g.setColor(new Color(150, 255, 150)); // Weg -> grau

                    break;

                case 33:
                    g.setColor(new Color(200, 200, 200)); // WegF -> hellgrau

                    break;

                case 4:
                    g.setColor(new Color(0, 255, 0)); // Start -> gr�n

                    break;

                case 5:
                    g.setColor(new Color(255, 0, 0)); // Ziel -> rot

                    break;

                default:
                    g.setColor(new Color(255, 255, 255)); // leer -> wei�
                }

                g.fillRect(x * 20, y * 20, 19, 19);
            }
        }

    }
}
