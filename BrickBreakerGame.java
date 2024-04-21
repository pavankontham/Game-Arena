import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class BrickBreakerGame extends JFrame implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 20;
    private static final int BALL_SIZE = 20;
    private static final int BRICK_WIDTH = 70;
    private static final int BRICK_HEIGHT = 20;
    private static final int NUM_BRICKS_PER_ROW = 10;
    private static final int NUM_ROWS = 5;

    private Timer timer;
    private int paddleX, paddleY, ballX, ballY, ballXSpeed, ballYSpeed, score;
    private boolean isPlaying;
    private List<Rectangle> bricks;

    BrickBreakerGame() {
        setTitle("Brick Breaker Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(this);
        setFocusable(true);

        timer = new Timer(10, this);
        initializeGame();  // Start the game directly

        timer.start();  // Start the timer
    }

    private void initializeGame() {
        paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
        paddleY = HEIGHT - PADDLE_HEIGHT - 30;
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballXSpeed = 2;
        ballYSpeed = 2;
        score = 0;

        createBricks();
        isPlaying = true;  // Set isPlaying to true initially
    }

    private void createBricks() {
        bricks = new ArrayList<>();
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_BRICKS_PER_ROW; col++) {
                int brickX = col * (BRICK_WIDTH + 5) + 30;
                int brickY = row * (BRICK_HEIGHT + 5) + 50;
                Rectangle brick = new Rectangle(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
                bricks.add(brick);
            }
        }
    }

    private void movePaddle() {
        if (paddleX < 0) {
            paddleX = 0;
        }
        if (paddleX > WIDTH - PADDLE_WIDTH) {
            paddleX = WIDTH - PADDLE_WIDTH;
        }
    }

    private void moveBall() {
        if (ballX < 0 || ballX > WIDTH - BALL_SIZE) {
            ballXSpeed = -ballXSpeed;
        }
        if (ballY < 0 || (ballY > paddleY - BALL_SIZE && ballX > paddleX && ballX < paddleX + PADDLE_WIDTH)) {
            ballYSpeed = -ballYSpeed;
        }
        if (ballY > HEIGHT) {
            isPlaying = false;
        }

        ballX += ballXSpeed;
        ballY += ballYSpeed;
    }

    private void checkCollision() {
        Rectangle ballRect = new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE);
        for (int i = 0; i < bricks.size(); i++) {
            Rectangle brickRect = bricks.get(i);
            if (ballRect.intersects(brickRect)) {
                bricks.remove(i);
                ballYSpeed = -ballYSpeed;
                score += 10;

                if (bricks.isEmpty()) {
                    // All bricks are destroyed - You may implement logic for the next level
                    initializeGame();
                }

                break;
            }
        }
    }

    private void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

        g.setColor(Color.BLUE);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        g.setColor(Color.RED);
        for (Rectangle brick : bricks) {
            g.fillRect(brick.x, brick.y, BRICK_WIDTH, BRICK_HEIGHT);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 10, 20);
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Game Over", WIDTH / 2 - 100, HEIGHT / 2 - 15);
        g.drawString("Press Enter to Start", WIDTH / 2 - 150, HEIGHT / 2 + 30);
        g.drawString("Score: " + score, WIDTH / 2 - 70, HEIGHT / 2 + 75);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BrickBreakerGame game = new BrickBreakerGame();
            game.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPlaying) {
            moveBall();
            checkCollision();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddleX -= 40;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddleX += 40;
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER && !isPlaying) {
            initializeGame();  // Start a new game on Enter key press
            isPlaying = true;
        }

        movePaddle();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Unused
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (isPlaying) {
            draw(g);
        } else {
            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }
}
