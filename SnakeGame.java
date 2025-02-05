import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 25;
    private final int WIDTH = 800;
    private final int HEIGHT = 800;
    private final int ALL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);
    private final int DELAY = 140;

    private final int[] x = new int[ALL_TILES];
    private final int[] y = new int[ALL_TILES];

    private int bodyParts = 3;
    private int appleX;
    private int appleY;
    private int score = 0;

    private char direction = 'R';
    private boolean inGame = true;

    private Timer timer;

    public SnakeGame() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        loadGame();
    }

    private void loadGame() {
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 50 - i * TILE_SIZE;
            y[i] = 50;
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame) {
            drawObjects(g);
        } else {
            gameOver(g);
        }
    }

    private void drawObjects(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(appleX, appleY, TILE_SIZE, TILE_SIZE);

        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(new Color(45, 180, 0));
            }
            g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void locateApple() {
        Random rand = new Random();
        appleX = rand.nextInt((int) (WIDTH / TILE_SIZE)) * TILE_SIZE;
        appleY = rand.nextInt((int) (HEIGHT / TILE_SIZE)) * TILE_SIZE;
    }

    private void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            score++;
            locateApple();
        }
    }

    private void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[(i - 1)];
            y[i] = y[(i - 1)];
        }

        switch (direction) {
            case 'U':
                y[0] -= TILE_SIZE;
                break;
            case 'D':
                y[0] += TILE_SIZE;
                break;
            case 'L':
                x[0] -= TILE_SIZE;
                break;
            case 'R':
                x[0] += TILE_SIZE;
                break;
        }
    }

    private void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        if (y[0] >= HEIGHT || y[0] < 0 || x[0] >= WIDTH || x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        String scoreMsg = "Score: " + score;
        Font small = new Font("Helvetica", Font.BOLD, 36);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (WIDTH - metr.stringWidth(msg)) / 2, HEIGHT / 2 - 50);
        g.drawString(scoreMsg, (WIDTH - metr.stringWidth(scoreMsg)) / 2, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (direction != 'R')) {
                direction = 'L';
            }

            if ((key == KeyEvent.VK_RIGHT) && (direction != 'L')) {
                direction = 'R';
            }

            if ((key == KeyEvent.VK_UP) && (direction != 'D')) {
                direction = 'U';
            }

            if ((key == KeyEvent.VK_DOWN) && (direction != 'U')) {
                direction = 'D';
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            SnakeGame game = new SnakeGame();
            frame.add(game);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
