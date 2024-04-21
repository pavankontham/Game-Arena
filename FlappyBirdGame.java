import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Bird {
    private int x, y, velocity;

    public Bird() {
        x = 100;
        y = 250;
        velocity = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void jump() {
        velocity = -10;
    }

    public void move() {
        velocity += 1;
        y += velocity;

        if (y > 480) {
            y = 480;
            velocity = 0;
        }
    }
}

class Pipe {
    private int x, height;

    public Pipe(int startX, int pipeHeight) {
        x = startX;
        height = pipeHeight;
    }

    public int getX() {
        return x;
    }

    public int getHeight() {
        return height;
    }

    public void move() {
        x -= 5;
    }
}

class ObstacleGenerator implements Runnable {
    private List<Pipe> pipes;
    private Random random;

    public ObstacleGenerator(List<Pipe> pipes) {
        this.pipes = pipes;
        random = new Random();
    }

    @Override
    public void run() {
        while (true) {
            int pipeHeight = random.nextInt(200) + 50;
            synchronized (pipes) {
                pipes.add(new Pipe(600, pipeHeight));
                pipes.add(new Pipe(750, random.nextInt(200) + 50)); // Adjusted distance to 150 units
            }
            try {
                Thread.sleep(2000); // Adjust timing as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class FlappyBirdGame extends JFrame {
    private Bird bird;
    private List<Pipe> pipes;
    private Timer timer;
    private int score, gameTime;

    public FlappyBirdGame() {
        bird = new Bird();
        pipes = new ArrayList<>();
        score = 0;
        gameTime = 0;

        Thread obstacleGeneratorThread = new Thread(new ObstacleGenerator(pipes));
        obstacleGeneratorThread.start();

        timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameTime++;
                bird.move();

                for (Pipe pipe : pipes) {
                    pipe.move();
                    if (pipe.getX() + 50 == bird.getX()) {
                        score++;
                    }
                    if (bird.getX() + 30 > pipe.getX() && bird.getX() < pipe.getX() + 50 && (bird.getY() < pipe.getHeight() || bird.getY() + 30 > pipe.getHeight() + 150)) {
                        gameOver();
                    }
                }

                pipes.removeIf(pipe -> pipe.getX() < -50);

                repaint();
            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    bird.jump();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        setPreferredSize(new Dimension(600, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setTitle("Flappy Bird Game");
        setResizable(false);
        setVisible(true);

        timer.start();
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.BLUE);
        g.fillRect(bird.getX(), bird.getY(), 30, 30);

        g.setColor(Color.GREEN);
        for (Pipe pipe : pipes) {
            g.fillRect(pipe.getX(), 0, 50, pipe.getHeight());
            g.fillRect(pipe.getX(), pipe.getHeight() + 150, 50, 500 - pipe.getHeight() - 150);
        }

        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlappyBirdGame();
            }
        });
    }
}
