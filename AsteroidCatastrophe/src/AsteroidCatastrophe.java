import javafx.scene.shape.Circle;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class AsteroidCatastrophe extends JFrame {
    private JPanel gameOver = new JPanel();
    private Label finalScore = new Label("Score: 0");
    private Timer timer;
    private int score = 0;
    private Map map = new Map();
    private InfoPanel infoPanel = new InfoPanel();

    public AsteroidCatastrophe() {
        setTitle("Asteroid Catastrophe");
        setVisible(true);
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension fullScreen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(fullScreen.width, fullScreen.height);

        gameOver.setLayout(new GridLayout(2, 1));
        Font overFont = new Font(Font.SERIF, Font.BOLD, 128);
        Label overText = new Label("Game Over");
        overText.setForeground(Color.WHITE);
        overText.setAlignment(Label.CENTER);
        gameOver.add(overText);
        finalScore.setForeground(Color.WHITE);
        finalScore.setAlignment(Label.CENTER);
        gameOver.add(finalScore);
        gameOver.setBackground(Color.BLACK);
        gameOver.setSize(1600, 800);
        gameOver.setVisible(false);
        gameOver.setFont(overFont);
        add(gameOver);

        map.setLayout(null);
        map.setFocusable(true);
        getContentPane().add(map, BorderLayout.CENTER);

        Dimension x = new Dimension(200, 100);
        infoPanel.setPreferredSize(x);
        getContentPane().add(infoPanel, BorderLayout.EAST);

        timer = new Timer(100, new AsteroidCatastrophe.ScoreTimer());
        timer.start();

    }

    public class InfoPanel extends JPanel {
        private JLabel text;

        public InfoPanel() {
            setBackground(Color.BLACK);
            Font myFont = new Font(Font.SERIF, Font.BOLD, 40);
            text = new JLabel("Score: " + score);
            text.setForeground(Color.WHITE);
            text.setFont(myFont);
            add(text);
        }

    }

    private class ScoreTimer implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (map.stop == true) {
                timer.stop();
                map.setVisible(false);
                gameOver.setVisible(true);
            } else {
                map.setVisible(true);
                gameOver.setVisible(false);
                score++;
                if (score < 10) {
                    infoPanel.text.setText("Score: " + score);
                    finalScore.setText("Score: " + score);
                } else {
                    infoPanel.text.setText("Score: " + score);
                    finalScore.setText("Score: " + score);
                }
            }
        }
    }

    public class Map extends JPanel {
        private boolean stop = false;
        private ImageIcon background = new ImageIcon("background.png");
        private ImageIcon spaceShip;
        private JPanel gameOver = new JPanel();
        private int locationX = 200;
        private int locationY = 500;
        private int time = 0;
        private ArrayList<Asteroid> asteroidArray = new ArrayList<>();
        private ArrayList<Laser> laserArray = new ArrayList();
        private Timer timer;
        private Timer timer2;
        private Timer timer3;
        private Timer timer4;
        private Timer timer5;

        Map() {
            timer = new Timer(50, new Map.TimeListener());
            timer.start();

            timer2 = new Timer(2500, new Map.AsteroidTimer());
            timer2.start();

            timer3 = new Timer(50, new Map.MoveTimer());
            timer3.start();

            timer4 = new Timer(1, new Map.DamageTimer());
            timer4.start();

            timer5 = new Timer(25, new Map.LaserTimer());
            timer5.start();

            spaceShip = new ImageIcon("spaceship1.png");
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            if (locationX >= 0) {
                                locationX -= 10;
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (locationX <= 1150) {
                                locationX += 10;
                            }
                            break;
                        case KeyEvent.VK_ENTER:
                            laserArray.add(new Laser());
                    }
                    repaint();
                }
            });
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            background.paintIcon(this, g, 0, 0);
            spaceShip.paintIcon(this, g, locationX, locationY);
            Iterator it = asteroidArray.iterator();
            while (it.hasNext()) {
                Asteroid temp = (Asteroid) it.next();
                temp.asteroid.paintIcon(this, g, temp.point.x, temp.point.y);
            }
        }

        private class TimeListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (time % 2 == 0) {
                    spaceShip = new ImageIcon("spaceship1.png");
                    time++;
                    repaint();
                } else {
                    spaceShip = new ImageIcon("spaceship2.png");
                    time++;
                    repaint();
                }
            }
        }

        private class AsteroidTimer implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                Asteroid temp = new Asteroid();
                asteroidArray.add(temp);
                repaint();
            }
        }

        private class MoveTimer implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator it = asteroidArray.iterator();
                ArrayList<Asteroid> tempAsteroid = new ArrayList<>();
                while (it.hasNext()) {
                    Asteroid temp = (Asteroid) it.next();
                    int moveY = temp.point.y += 5;
                    temp.point = new Point(temp.point.x, moveY);
                    tempAsteroid.add(temp);
                }
                asteroidArray.clear();
                asteroidArray.addAll(tempAsteroid);
                repaint();
            }
        }

        private class DamageTimer implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator it = asteroidArray.iterator();
                while (it.hasNext()) {
                    Asteroid temp = (Asteroid) it.next();
                    if ((temp.point.y >= (locationY + 10) && temp.point.y < (locationY + 90) && temp.point.x >= (locationX + 10) && temp.point.x < (locationX + 90))
                            || (temp.point.y + 50 >= (locationY + 10) && temp.point.y + 50 < (locationY + 90) && temp.point.x + 50 >= (locationX + 10) && temp.point.x + 50 < (locationX + 90))) {
                        timer.stop();
                        timer2.stop();
                        timer3.stop();
                        timer4.stop();
                        stop = true;
                    }
                }
                Iterator ti = laserArray.iterator();
                while (ti.hasNext()) {
                    Laser temp = (Laser) ti.next();
                    //if
                }
            }
        }

        private class LaserTimer implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator it = laserArray.iterator();
                ArrayList<Laser> tempLaser = new ArrayList<>();
                while (it.hasNext()) {
                    Laser temp = (Laser) it.next();
                    int moveY = temp.point.y -= 5;
                    temp.point = new Point(temp.point.x, moveY);
                    tempLaser.add(temp);
                }
                laserArray.clear();
                laserArray.addAll(tempLaser);
                repaint();
            }
        }

        class Asteroid {
            private ImageIcon asteroid = new ImageIcon("Asteroid.png");
            private Point point;

            Asteroid() {
                int gizmo = (int) (Math.random() * 1000);
                point = new Point(gizmo, 0);
            }
        }

        class Laser {
            private Circle laser = new Circle(10, javafx.scene.paint.Color.RED);
            private Point point;

            Laser() {

                point = new Point(locationX + 50, locationY);
            }
        }
    }


    public static void main(String[] args) {
        AsteroidCatastrophe total = new AsteroidCatastrophe();
    }
}
