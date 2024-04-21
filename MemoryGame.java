import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MemoryGameGUI gameGUI = new MemoryGameGUI();
            gameGUI.setVisible(true);
        });
    }
}

class Tile {
    private final int id;
    private boolean isFlipped;

    public Tile(int id) {
        this.id = id;
        this.isFlipped = false;
    }

    public int getId() {
        return id;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void flip() {
        isFlipped = !isFlipped;
    }

    // You can add more methods as needed
}

class MemoryGameGUI extends JFrame {
    private final ArrayList<Tile> tiles;
    private final ArrayList<JButton> tileButtons;
    private Tile firstFlippedTile;

    public MemoryGameGUI() {
        super("Memory Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        tiles = new ArrayList<>();
        tileButtons = new ArrayList<>();

        initializeGame();
        setupGUI();
    }

    private void initializeGame() {
        for (int i = 1; i <= 8; i++) {
            tiles.add(new Tile(i));
            tiles.add(new Tile(i));
        }
        Collections.shuffle(tiles);
    }

    private void setupGUI() {
        JPanel panel = new JPanel(new GridLayout(4, 4));

        for (Tile tile : tiles) {
            JButton button = new JButton();
            button.addActionListener(new TileButtonListener(tile));
            tileButtons.add(button);
            panel.add(button);
        }

        add(panel);
    }

    private class TileButtonListener implements ActionListener {
        private final Tile tile;

        public TileButtonListener(Tile tile) {
            this.tile = tile;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!tile.isFlipped()) {
                tile.flip();
                updateButton(tile);

                if (firstFlippedTile == null) {
                    firstFlippedTile = tile;
                } else {
                    if (firstFlippedTile.getId() == tile.getId()) {
                        // Match found
                        tileButtons.get(tiles.indexOf(firstFlippedTile)).setEnabled(false);
                        ((JButton) e.getSource()).setEnabled(false);
                    } else {
                        // No match, flip back
                        firstFlippedTile.flip();
                        tile.flip();
                        updateButton(firstFlippedTile);
                        updateButton(tile);
                    }

                    firstFlippedTile = null;
                }
            }
        }

        private void updateButton(Tile tile) {
            int index = tiles.indexOf(tile);
            JButton button = tileButtons.get(index);

            if (tile.isFlipped()) {
                button.setText(String.valueOf(tile.getId()));
            } else {
                button.setText("");
            }
        }
    }
}
