import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



class FMain extends JFrame implements ActionListener {
    private JButton BrickBreakerButton;
    private JButton SnakeGameButton;
    private JButton MemoryGameButton;
    private JButton FlappyBirdGameButton;
    private JButton TicTacToeGameButton;
    private JButton DinoGameButton;

    public FMain() {
        setTitle("Main Menu");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        

        BrickBreakerButton = new JButton("BrickBreaker");
        BrickBreakerButton.addActionListener(this);
        add(BrickBreakerButton);

        SnakeGameButton = new JButton("SnakeGame");
        SnakeGameButton.addActionListener(this);
        add(SnakeGameButton);

        MemoryGameButton = new JButton("MemoryGame");
        MemoryGameButton.addActionListener(this);
        add(MemoryGameButton);

        FlappyBirdGameButton = new JButton("FlappyBird");
        FlappyBirdGameButton.addActionListener(this);
        add(FlappyBirdGameButton);

        TicTacToeGameButton = new JButton("TicTacToe");
        TicTacToeGameButton.addActionListener(this);
        add(TicTacToeGameButton);

        DinoGameButton = new JButton("DinoGame");
        DinoGameButton.addActionListener(this);
        add(DinoGameButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == BrickBreakerButton) {
            BrickBreakerGame BrickBreaker = new BrickBreakerGame();
            BrickBreaker.setVisible(true);
        } else if (e.getSource() == SnakeGameButton) {
            SnakeGame SnakeGame = new SnakeGame();
            SnakeGame.setVisible(true);
        } else if (e.getSource() == MemoryGameButton) {
            MemoryGame.main(null);
        } else if (e.getSource() == FlappyBirdGameButton) {
            FlappyBirdGame FlappyBird = new FlappyBirdGame();
            FlappyBird.setVisible(true);
        } else if (e.getSource() == TicTacToeGameButton) {
            new TicTacToeFrame().setVisible(true);
        } else if (e.getSource() == DinoGameButton) {
            DinoGame dinoGame = new DinoGame();
            dinoGame.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FMain().setVisible(true);
            }
        });
    }
}
