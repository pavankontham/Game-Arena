import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class DinoGame extends JFrame implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;
    private static final int GROUND_LEVEL = 300;
    private static final int DINO_SIZE = 50;
    private static final int OBSTACLE_SIZE = 30;

    private int dinoX, dinoY;
    private int dinoSpeedY;
    private boolean isJumping;
    private int score;

    private List<Obstacle> obstacles;
    private Timer timer;

    public DinoGame() {
        setTitle("Dino Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        dinoX = 50;
        dinoY = GROUND_LEVEL - DINO_SIZE;
        dinoSpeedY = 0;
        isJumping = false;
        score = 0;

        obstacles = new ArrayList<>();
        Thread obstacleGeneratorThread = new Thread(new ObstacleGenerator());
        obstacleGeneratorThread.start();

        timer = new Timer(16, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw ground
        g.setColor(Color.GREEN);
        g.fillRect(0, GROUND_LEVEL, WIDTH, HEIGHT - GROUND_LEVEL);

        // Draw obstacles
        g.setColor(Color.BLUE);
        for (Obstacle obstacle : obstacles) {
            g.fillRect(obstacle.getX(), obstacle.getY(), OBSTACLE_SIZE, OBSTACLE_SIZE);
        }

        // Draw dinosaur (circle)
        g.setColor(Color.RED);
        g.fillOval(dinoX, dinoY, DINO_SIZE, DINO_SIZE);

        // Draw score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update game logic here
        if (isJumping) {
            dinoY += dinoSpeedY;
            dinoSpeedY += 1;

            if (dinoY >= GROUND_LEVEL - DINO_SIZE) {
                dinoY = GROUND_LEVEL - DINO_SIZE;
                dinoSpeedY = 0;
                isJumping = false;
            }
        }

        // Move obstacles
        synchronized (obstacles) {
            for (Obstacle obstacle : obstacles) {
                obstacle.move();
                if (obstacle.getX() + OBSTACLE_SIZE < 0) {
                    obstacles.remove(obstacle);
                    score++;
                    break;
                }
            }
        }

        // Check for collisions
        for (Obstacle obstacle : obstacles) {
            if (isCollision(obstacle)) {
                gameOver();
                return;
            }
        }

        repaint();
    }

    private boolean isCollision(Obstacle obstacle) {
        return dinoX < obstacle.getX() + OBSTACLE_SIZE &&
                dinoX + DINO_SIZE > obstacle.getX() &&
                dinoY < obstacle.getY() + OBSTACLE_SIZE &&
                dinoY + DINO_SIZE > obstacle.getY();
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !isJumping) {
            jump();
        }
    }

    private void jump() {
        isJumping = true;
        dinoSpeedY = -15; // Increase the jump strength
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used in this example
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used in this example
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DinoGame dinoGame = new DinoGame();
            dinoGame.setVisible(true);
        });
    }

    private class Obstacle {
        private int x, y;

        public Obstacle(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void move() {
            x -= 5;
        }
    }

    private class ObstacleGenerator implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (obstacles) {
                    obstacles.add(new Obstacle(WIDTH, GROUND_LEVEL - OBSTACLE_SIZE));
                }
                try {
                    Thread.sleep(2000); // Adjust timing for obstacle generation
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
