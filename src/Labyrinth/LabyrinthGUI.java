package Labyrinth;

//import MyCanvasPanel;
//import Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Labyrinthproblem
// Belegungen:
// 0 - leeres Feld (ohne Bedeutung)
// 1 - Wand
// 2 - Position
// 3 - markiert (Feld wurde schon besucht)
// 4 - markiert (Startfeld)
// 5 - markiert (Zielfeld)
public class LabyrinthGUI {
    private JPanel mainPanel;
    private JButton startButton;
    private JPanel graphPanel;
    private byte[][] myLabyrinth = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1},
            {1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1},
            {1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1},
            {1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    private Point startpunkt = new Point(18, 18);
    private Point zielpunkt = new Point(1, 1);
    private final byte leer = 0; // leeres Feld
    private final byte wand = 1; // Wand
    private final byte aktuell = 2; // aktuelle Position
    private final byte besucht = 3; // besuchtes Feld (aktueller Weg)
    private final byte besuchtF = 33; // besuchtes Feld (falscher Weg)
    private final byte start = 4; // Starposition
    private final byte ziel = 5; // Zielposition
    private MyCanvasPanel lPanel = new MyCanvasPanel(myLabyrinth);

    // Thread, in welchem rekursive Methode aufgerufen wird - ermöglicht
    // schrittweises Vorgehen
    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            if (sucheWeg(startpunkt)) {
                JOptionPane.showMessageDialog(null, "Ziel gefunden",
                        "Suche im Labyrinth", JOptionPane.OK_CANCEL_OPTION);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Ziel nicht gefunden", "Suche im Labyrinth",
                        JOptionPane.OK_CANCEL_OPTION);
            }
        }
    });

    public LabyrinthGUI() {
        // Zeichenfläche hinzufügen
        lPanel.setPreferredSize(new Dimension(400, 400));
        lPanel.setBackground(Color.WHITE);
        graphPanel.add(lPanel);
        // Starwerte setzen
        myLabyrinth[zielpunkt.getX()][zielpunkt.getY()] = ziel;
        myLabyrinth[startpunkt.getX()][startpunkt.getY()] = start;
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.start();
            }
        });
        lPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int ex = e.getX() / 20;
                int ey = e.getY() / 20;

                if (!e.isShiftDown()) {
                    // Maustaste -> Startpunkt
                    myLabyrinth[startpunkt.getY()][startpunkt.getX()] = leer;
                    myLabyrinth[ey][ex] = start;
                    startpunkt.setX(ex);
                    startpunkt.setY(ey);
                } else {
                    // Shift + Maustaste -> Zielpunkt
                    myLabyrinth[zielpunkt.getY()][zielpunkt.getX()] = leer;
                    myLabyrinth[ey][ex] = ziel;
                    zielpunkt.setX(ex);
                    zielpunkt.setY(ey);
                }
                lPanel.repaint();
            }
        });
    }

    private boolean sucheWeg(Point currPos) {
        if (myLabyrinth[currPos.getX()][currPos.getY()] == ziel) {
            return true; // Yay, found it!!
        } else if (myLabyrinth[currPos.getX()][currPos.getY()] == besucht || myLabyrinth[currPos.getX()][currPos.getY()] == besuchtF) {
            return false; // Already been here
        }

        // wait
        try {
            t.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // paint before move
        myLabyrinth[currPos.getX()][currPos.getY()] = aktuell;
        lPanel.repaint();
        myLabyrinth[currPos.getX()][currPos.getY()] = besucht;


        boolean found;

        // go up if possible
        if (myLabyrinth[currPos.getX() + 1][currPos.getY()] != wand) {
            currPos.setX(currPos.getX() + 1); // move up
            found = sucheWeg(currPos);
            if (!found) {
                currPos.setX(currPos.getX() - 1); // move down
            } else {
                return true;
            }
        }

        // go down
        if (myLabyrinth[currPos.getX() - 1][currPos.getY()] != wand) {
            currPos.setX(currPos.getX() - 1); // move down
            found = sucheWeg(currPos);
            if (!found) {
                currPos.setX(currPos.getX() + 1); // move up
            } else {
                return true;
            }
        }

        // go right
        if (myLabyrinth[currPos.getX()][currPos.getY() + 1] != wand) {
            currPos.setY(currPos.getY() + 1); // move up
            found = sucheWeg(currPos);
            if (!found) {
                currPos.setY(currPos.getY() - 1); // move down
            } else {
                return true;
            }
        }

        // go left
        if (myLabyrinth[currPos.getX()][currPos.getY() - 1] != wand) {
            currPos.setY(currPos.getY() - 1); // move down
            found = sucheWeg(currPos);
            if (!found) {
                currPos.setY(currPos.getY() + 1); // move up
            } else {
                return true;
            }
        }

        // not part of the final path
        myLabyrinth[currPos.getX()][currPos.getY()] = besuchtF;
        return false;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LabyrinthGUI");
        frame.setContentPane(new LabyrinthGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
