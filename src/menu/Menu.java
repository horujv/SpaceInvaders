package menu;
import audioPlayer.AudioPlayer;
import userControl.GameBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Menu implements ActionListener {
    private  JFrame frame;
    private JPanel panel;
    JButton button;
    JButton button2;
    JButton button1;
    private AudioPlayer  audioPlayer=new AudioPlayer();
    public Menu(){
        audioPlayer.playMusic("menu.wav");
        frame = new JFrame();
        panel = new JPanel();
        frame.add(panel, BorderLayout.CENTER);

        button = new JButton("Start the game") ;
        button.addActionListener(this);
        panel.add(button);


        button1 = new JButton("View the statistics");
        button1.addActionListener(this);
        panel.add(button1);

        button2 = new JButton("Exit");
        button2.addActionListener(this);
        panel.add(button2);

        panel.setBackground(Color.GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        panel.setLayout(new GridLayout(0, 1));

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Space Invaders Menu");
        frame.pack();
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new Menu();

    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button){
            audioPlayer.stopMusic();
            JFrame window=new JFrame("Game");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setContentPane(new GameBoard());
            window.setSize(700,700);
            window.setLocationRelativeTo(null);
            window.pack();
            window.setVisible(true);

        }
        if(e.getSource() == button1){
            frame=new JFrame();
            panel=new JPanel();
            panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
            panel.setBackground(Color.DARK_GRAY);

            panel.setLayout(new GridLayout(2,0));
            try {
                BufferedReader reader = new BufferedReader(new FileReader("Score"));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                String ls = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }
                // delete the last new line separator
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                reader.close();
                panel.add("Statistics",new TextArea("Scores from your last games are: " + stringBuilder));
                panel.add(button2);

            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            frame.add(panel,BorderLayout.CENTER) ;
            frame.pack();
            frame.setSize(500,500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
        if(e.getSource()==button2){
            frame.dispose();
        }
    }


}

