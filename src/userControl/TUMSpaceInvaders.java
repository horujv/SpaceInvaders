package userControl;

import javax.swing.JFrame;


public class TUMSpaceInvaders {

    public static void main(String[] args){

        JFrame window=new JFrame("Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new GameBoard());

        window.pack();
        window.setVisible(true);
    }
}
