package sliding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class Puzzle extends JFrame implements ActionListener {
    private JButton[] buttons = new JButton[9]; 
    private JButton shuffleButton, optionsButton;
    private int emptyButtonIndex = 8;
    private int counter = 0;
    private JLabel counterLabel;

    private Color tileColor = Color.decode("#5adbb5");
    private Color backgroundColor = Color.decode("#f0f0f0");
    private JFrame optionsFrame;
    public Puzzle() {
        this(Color.decode("#5adbb5"), Color.decode("#f0f0f0")); 
    }

    public Puzzle(Color tileColor, Color backgroundColor) {
        this.tileColor = tileColor;
        this.backgroundColor = backgroundColor;
        setSize(400, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton(String.valueOf(i + 1));
            buttons[i].setBounds(90 + (i % 3) * 70, 60 + (i / 3) * 55, 50, 40);
            buttons[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }

        buttons[emptyButtonIndex].setText(" ");

        shuffleButton = new JButton("Shuffle!");
        shuffleButton.setBounds(135, 245, 100, 40);
        shuffleButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        shuffleButton.addActionListener(this);
        add(shuffleButton);

        optionsButton = new JButton("Options");
        optionsButton.setBounds(135, 300, 100, 40);
        optionsButton.setBackground(Color.LIGHT_GRAY);
        optionsButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        optionsButton.addActionListener(this);
        add(optionsButton);

        counterLabel = new JLabel("Clicks: 0");
        counterLabel.setBounds(145, 15, 180, 40);
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
        String tempText = buttons[i].getText();
        buttons[i].setText(buttons[j].getText());
        buttons[j].setText(tempText);
    }

    private void shuffle() {
        ArrayList<Integer> buttonOrder = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            buttonOrder.add(i);
        }
        Collections.shuffle(buttonOrder);
        for (int i = 0; i < 9; i++) {
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
        tileColorChooser.getSelectionModel().addChangeListener(e -> {
            tileColor = tileColorChooser.getColor(); 
            updateColors();
        });

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
        }
        buttons[emptyButtonIndex].setBackground(backgroundColor); 
        getContentPane().setBackground(backgroundColor);
    }

    private void checkWin() {
        for (int i = 0; i < 8; i++) {
            if (!buttons[i].getText().equals(String.valueOf(i + 1))) {
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "YOU WON!\n" + "You clicked: " + counter + " times.");
        JOptionPane.showMessageDialog(this, "İlke, Sude and Ahmet thanks you for playing.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Puzzle()); 
    }


}
