package pkg2dstealth;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameLauncher extends Applet{
    
    private static Game game = new Game();
    public static final boolean DEBUG=true;
    
    @Override
    public void init() {
        setLayout(new BorderLayout());
        add(game,BorderLayout.CENTER);
        setMaximumSize(game.DIMENSIONS);
        setMinimumSize(game.DIMENSIONS);
        setPreferredSize(game.DIMENSIONS);
        game.debug=DEBUG;
    }
    
    @Override
    public void start() {
        game.start();
    }
    
    @Override
    public void stop() {
        game.stop();
    }
    public static void main(String[] args){
        
        game.setMinimumSize(game.DIMENSIONS);
        game.setMaximumSize(game.DIMENSIONS);
        game.setPreferredSize(game.DIMENSIONS);
        
        game.frame = new JFrame(game.NAME);
        
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLayout(new BorderLayout());
        
        game.frame.add(game, BorderLayout.CENTER);
        game.frame.pack();
        
        game.frame.setResizable(false);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);
        game.start();
        game.debug=DEBUG;
    }
}
