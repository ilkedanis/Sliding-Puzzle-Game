package sliding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class Puzzle4X4 extends JFrame implements ActionListener {
    private JButton[] buttons = new JButton[16];
    private JButton shuffleButton, optionsButton;
    private int emptyButtonIndex = 15;
    private int counter = 0;
    private JLabel counterLabel;
    private int secondsElapsed = 0;

    private Color tileColor = Color.decode("#5adbb5");
    private Color backgroundColor = Color.decode("#f0f0f0");
    private JFrame optionsFrame;

    public Puzzle4X4() {
        this(Color.decode("#5adbb5"), Color.decode("#f0f0f0"));
    }

    public Puzzle4X4(Color tileColor, Color backgroundColor) {
        this.tileColor = tileColor;
        this.backgroundColor = backgroundColor;
        setSize(480, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton(String.valueOf(i + 1));
            buttons[i].setBounds(80 + (i % 4) * 70, 60 + (i / 4) * 60, 60, 50);
            buttons[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }
        buttons[emptyButtonIndex].setText(" ");

        shuffleButton = new JButton("Shuffle!");
        shuffleButton.setBounds(155, 435, 120, 45);
        shuffleButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        shuffleButton.addActionListener(this);
        add(shuffleButton);

        optionsButton = new JButton("Options");
        optionsButton.setBounds(155, 490, 120, 45);
        optionsButton.setBackground(Color.LIGHT_GRAY);
        optionsButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        optionsButton.addActionListener(this);
        add(optionsButton);

        counterLabel = new JLabel("Clicks: 0");
        counterLabel.setBounds(165, 15, 180, 40);
        counterLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        add(counterLabel);

        updateColors();
        createOptionsFrame();
        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == shuffleButton) {
            shuffle();
            counter = 0;
            counterLabel.setText("Clicks: 0");
            secondsElapsed = 0;
        } else if (e.getSource() == optionsButton) {
            optionsFrame.setVisible(true);
        } else {
            for (int i = 0; i < 16; i++) {
                if (e.getSource() == buttons[i]) {
                    moveButton(i);
                    break;
                }
            }
        }
    }

    private void moveButton(int i) {
        int[] validMoves = {-1, 1, -4, 4};
        for (int move : validMoves) {
            int newIndex = i + move;
            if (newIndex == emptyButtonIndex && isValidMove(i, newIndex)) {
                swapButtons(i, emptyButtonIndex);
                emptyButtonIndex = i;
                counter++;
                counterLabel.setText("Clicks: " + counter);
                checkWin();
                return;
            }
        }
    }

    private boolean isValidMove(int i, int newIndex) {
        return newIndex >= 0 && newIndex < 16 &&
               (Math.abs(i % 4 - newIndex % 4) <= 1) &&
               (Math.abs(i / 4 - newIndex / 4) <= 1);
    }

    private void swapButtons(int i, int j) {
        String tempText = buttons[i].getText();
        buttons[i].setText(buttons[j].getText());
        buttons[j].setText(tempText);
    }

    private void shuffle() {
        ArrayList<Integer> buttonOrder = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            buttonOrder.add(i);
        }
        Collections.shuffle(buttonOrder);
        for (int i = 0; i < 16; i++) {
            buttons[i].setText(buttonOrder.get(i) == 0 ? " " : buttonOrder.get(i).toString());
            if (buttonOrder.get(i) == 0) {
                emptyButtonIndex = i;
            }
        }
    }

    public JFrame createOptionsFrame() {
        optionsFrame = new JFrame("Options");
        optionsFrame.setSize(300, 250);
        optionsFrame.setLayout(new GridLayout(4, 1, 5, 5));
        optionsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JLabel tileColorLabel = new JLabel("Tile Color:");
        tileColorLabel.setHorizontalAlignment(JLabel.CENTER);
        optionsFrame.add(tileColorLabel);

        JColorChooser tileColorChooser = new JColorChooser(tileColor);
        tileColorChooser.getSelectionModel().addChangeListener(e -> {
            tileColor = tileColorChooser.getColor();
            updateColors();
        });
        optionsFrame.add(tileColorChooser);

        JLabel backgroundColorLabel = new JLabel("Background Color:");
        backgroundColorLabel.setHorizontalAlignment(JLabel.CENTER);
        optionsFrame.add(backgroundColorLabel);

        JColorChooser backgroundColorChooser = new JColorChooser(backgroundColor);
        backgroundColorChooser.getSelectionModel().addChangeListener(e -> {
            backgroundColor = backgroundColorChooser.getColor();
            updateColors();
        });
        optionsFrame.add(backgroundColorChooser);
        return optionsFrame;
    }

    private void updateColors() {
        for (JButton button : buttons) {
            button.setBackground(tileColor);
            button.setForeground(getContrastingColor(tileColor));
        }
        buttons[emptyButtonIndex].setBackground(backgroundColor);
        getContentPane().setBackground(backgroundColor);
    }

    private Color getContrastingColor(Color bg) {
        double yiq = ((bg.getRed() * 299) + (bg.getGreen() * 587) + (bg.getBlue() * 114)) / 1000;
        return (yiq >= 128) ? Color.BLACK : Color.WHITE;
    }

    private void checkWin() {
        for (int i = 0; i < 15; i++) {
            if (!buttons[i].getText().equals(String.valueOf(i + 1))) {
                return;
            }
        }
        String winMessage = "YOU WON!\nClicks: ";
        JOptionPane.showMessageDialog(this, winMessage);
        JOptionPane.showMessageDialog(this, "İlke, Sude and Ahmet thanks you for playing.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Puzzle4X4());
    }
}