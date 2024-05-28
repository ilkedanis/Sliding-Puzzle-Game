package sliding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class PuzzleMonaLisa extends JFrame implements ActionListener {
    private JButton[] buttons = new JButton[9];
    private JButton shuffleButton, optionsButton;
    private int emptyButtonIndex = 8; // Last button is the empty space
    private int counter = 0;
    private JLabel counterLabel;

    private Color tileColor;
    private Color backgroundColor;
    private JFrame optionsFrame;

    private ImageIcon[] imageIcons = new ImageIcon[8]; // Array to hold 8 image icons

    public PuzzleMonaLisa(Color tileColor, Color backgroundColor) {
        this.tileColor = tileColor;
        this.backgroundColor = backgroundColor;
        setSize(400, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setBounds(90 + (i % 3) * 70, 60 + (i / 3) * 55, 50, 40);
            buttons[i].addActionListener(this);
            add(buttons[i]);

            if (i < 8) {
                // Load images (replace with your image paths)
                imageIcons[i] = new ImageIcon("image_part_00" + (i + 1) + ".jpg");
                buttons[i].setIcon(imageIcons[i]);
            }
        }

        buttons[emptyButtonIndex].setBackground(backgroundColor); // Set empty button background
        buttons[emptyButtonIndex].setIcon(null); // Set empty button icon to null

        shuffleButton = new JButton("Shuffle!");
        shuffleButton.setBounds(135, 245, 100, 40);
        shuffleButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        shuffleButton.addActionListener(this);
        add(shuffleButton);

        optionsButton = new JButton("Options");
        optionsButton.setBounds(135, 300, 100, 40);
        optionsButton.setBackground(Color.LIGHT_GRAY);
        optionsButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        optionsButton.addActionListener(this);
        add(optionsButton);

        counterLabel = new JLabel("Clicks: 0");
        counterLabel.setBounds(145, 15, 180, 40);
        counterLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
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
        } else if (e.getSource() == optionsButton) {
            optionsFrame.setVisible(true);
        } else {
            for (int i = 0; i < 9; i++) {
                if (e.getSource() == buttons[i]) {
                    moveButton(i);
                    break;
                }
            }
        }
    }

    private void moveButton(int i) {
        int[] validMoves = {-1, 1, -3, 3};
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
        return newIndex >= 0 && newIndex < 9 &&
               (Math.abs(i % 3 - newIndex % 3) <= 1) &&
               (Math.abs(i / 3 - newIndex / 3) <= 1);
    }

    private void swapButtons(int i, int j) {
        Icon tempIcon = buttons[i].getIcon();
        buttons[i].setIcon(buttons[j].getIcon());
        buttons[j].setIcon(tempIcon);
    }

    private void shuffle() {
        ArrayList<Integer> buttonOrder = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            buttonOrder.add(i);
        }
        buttonOrder.add(8); // Adding the empty space

        Collections.shuffle(buttonOrder);

        for (int i = 0; i < 9; i++) {
            if (buttonOrder.get(i) == 8) {
                buttons[i].setIcon(null);
                emptyButtonIndex = i;
            } else {
                buttons[i].setIcon(imageIcons[buttonOrder.get(i)]);
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
        for (int i = 0; i < 9; i++) {
            if (i != emptyButtonIndex) {
                buttons[i].setBackground(tileColor);
            } else {
                buttons[i].setBackground(backgroundColor);
            }
        }
        getContentPane().setBackground(backgroundColor);
    }

    private void checkWin() {
        for (int i = 0; i < 8; i++) {
            if (!buttons[i].getIcon().equals(imageIcons[i])) {
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "YOU WON!\n" + "You clicked: " + counter + " times.");
        JOptionPane.showMessageDialog(this, "İlke, Sude and Ahmet thanks you for playing.");
    }
}
