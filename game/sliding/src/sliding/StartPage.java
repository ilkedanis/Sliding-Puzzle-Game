package sliding;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;


public class StartPage extends JFrame implements ActionListener {
	 private JButton optionsButton, monaLisaButton, puzzle3x3Button, puzzle4x4Button;
    private static Color tileColor = Color.decode("#5adbb5");
    private static Color backgroundColor = Color.decode("#f0f0f0");
    private static JFrame optionsFrame;
    private static Clip clip; 
    private static JCheckBox musicCheckbox;

    public StartPage() {

        setTitle("Sliding Puzzle");
        setSize(400, 450);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        optionsButton = new JButton("Options");
        optionsButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
        optionsButton.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0; 
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20);

        gbc.gridy = 1; 
        add(optionsButton, gbc);
        
        monaLisaButton = new JButton("Mona Lisa");
        monaLisaButton.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
        monaLisaButton.addActionListener(this);
        
        puzzle3x3Button = new JButton("3x3 Puzzle");
        puzzle3x3Button.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
        puzzle3x3Button.addActionListener(this);
        
        puzzle4x4Button = new JButton("4x4 Puzzle");
        puzzle4x4Button.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
        puzzle4x4Button.addActionListener(this);
        
        gbc.gridy = 2;
        add(monaLisaButton, gbc);
        
        gbc.gridy = 3;
        add(puzzle3x3Button, gbc);
        
        gbc.gridy = 4;
        add(puzzle4x4Button, gbc);
        
        
        try {
            
        	File musicFile = new File("/Users/sude/Downloads/a686-601f-45a3-bb98-473905370235.wav"); 
        	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile); 
        	
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == optionsButton) {
         
            if (optionsFrame == null) {
                optionsFrame = createOptionsFrame();
            }
            optionsFrame.setVisible(true);
        } else if (e.getSource() == monaLisaButton) {
            PuzzleMonaLisa monaLisaGame = new PuzzleMonaLisa(tileColor, backgroundColor); 
            monaLisaGame.setVisible(true);
            dispose(); 
        } else if (e.getSource() == puzzle3x3Button) {
            Puzzle3X3 puzzle3x3Game = new Puzzle3X3(tileColor, backgroundColor); 
            puzzle3x3Game.setVisible(true);
            dispose(); 
        } else if (e.getSource() == puzzle4x4Button) {
            Puzzle4X4 puzzle4x4Game = new Puzzle4X4(tileColor, backgroundColor); 
            puzzle4x4Game.setVisible(true);
            dispose(); 
        }
    }
    
     public static void main(String[] args) {
        SwingUtilities.invokeLater(StartPage::new); 
    }

   
    private static JFrame createOptionsFrame() {
        JFrame optionsFrame = new JFrame("Options");
        optionsFrame.setSize(300, 250);
        optionsFrame.setLayout(new GridLayout(4, 1, 5, 5));
        optionsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); 

        JLabel tileColorLabel = new JLabel("Tile Color:");
        tileColorLabel.setHorizontalAlignment(JLabel.CENTER);
        optionsFrame.add(tileColorLabel);

        JColorChooser tileColorChooser = new JColorChooser(tileColor);
        tileColorChooser.getSelectionModel().addChangeListener(e -> {
            tileColor = tileColorChooser.getColor(); 
        });
        optionsFrame.add(tileColorChooser);

        JLabel backgroundColorLabel = new JLabel("Background Color:");
        backgroundColorLabel.setHorizontalAlignment(JLabel.CENTER);
        optionsFrame.add(backgroundColorLabel);

        JColorChooser backgroundColorChooser = new JColorChooser(backgroundColor);
        backgroundColorChooser.getSelectionModel().addChangeListener(e -> {
            backgroundColor = backgroundColorChooser.getColor(); 
        });
        optionsFrame.add(backgroundColorChooser);

       
        
        musicCheckbox = new JCheckBox("Play Music", true); 
        musicCheckbox.addActionListener(e -> {
            if (musicCheckbox.isSelected()) {
                if (!clip.isRunning()) { 
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } else {
                clip.stop(); 
            }
            
        }
        
        		
        		);
        
        optionsFrame.add(musicCheckbox); 
        return optionsFrame;
        
        
    }
    
}