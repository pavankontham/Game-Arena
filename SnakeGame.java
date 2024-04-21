import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private final int gridSize = 20;
    private final int tileSize = 30;
    private LinkedList<Point> snake;
    private Point food;
    private int direction;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(gridSize * tileSize, gridSize * tileSize);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        snake = new LinkedList<>();
        initGame();

        addKeyListener(this);
        setFocusable(true);

        Timer timer = new Timer(100, this);
        timer.start();
    }

    private void initGame() {
        // Initialize the game by placing the snake in the center and spawning the initial food
        int centerX = gridSize / 2;
        int centerY = gridSize / 2;

        snake.clear();
        snake.add(new Point(centerX, centerY));
        direction = KeyEvent.VK_RIGHT;

        spawnFood();
    }

    private void spawnFood() {
        Random rand = new Random();
        int x, y;

        do {
            x = rand.nextInt(gridSize);
            y = rand.nextInt(gridSize);
        } while (snake.contains(new Point(x, y)) || (food != null && food.equals(new Point(x, y))));

        food = new Point(x, y);
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead;

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead = new Point(head.x, (head.y - 1 + gridSize) % gridSize);
                break;
            case KeyEvent.VK_DOWN:
                newHead = new Point(head.x, (head.y + 1) % gridSize);
                break;
            case KeyEvent.VK_LEFT:
                newHead = new Point((head.x - 1 + gridSize) % gridSize, head.y);
                break;
            case KeyEvent.VK_RIGHT:
                newHead = new Point((head.x + 1) % gridSize, head.y);
                break;
            default:
                return;
        }

        if (snake.contains(newHead) || newHead.equals(food)) {
            if (newHead.equals(food)) {
                snake.addFirst(newHead);
                spawnFood();
            } else {
                gameOver();
            }
        } else {
            snake.addFirst(newHead);
            snake.removeLast();
        }
    }

    private void gameOver() {
        int score = snake.size() - 1; 
    
        int option = JOptionPane.showConfirmDialog(
                this,
                "Game Over!\nYour Score: " + score + "\nDo you want to restart?",
                "Game Over",
                JOptionPane.YES_NO_OPTION);
    
        if (option == JOptionPane.YES_OPTION) {
            initGame();
        } else {
            System.exit(0); 
        }
    }
    

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw the snake
        for (Point point : snake) {
            g.setColor(Color.GREEN);
            g.fillRect(point.x * tileSize, point.y * tileSize, tileSize, tileSize);
        }

        // Draw the food
        g.setColor(Color.RED);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) ||
                (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) ||
                (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) ||
                (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP)) {
            direction = key;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed for this example
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed for this example
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame snakeGame = new SnakeGame();
            snakeGame.setVisible(true);
        });
    }
}
