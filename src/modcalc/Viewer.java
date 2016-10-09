package modcalc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author Ceno
 */
public class Viewer extends JPanel implements ActionListener {
    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null)
            return new ImageIcon(imgURL);
        else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    private final ImageIcon standInSquare = createImageIcon("res/skillicon_standin1_up.png");
    private final ImageIcon standInRound = createImageIcon("res/skillicon_standin2_up.png");
    private final ImageIcon background = createImageIcon("res/skills_classwindowbackgroundimage.png");
    
    public Viewer() {
        super(new BorderLayout());
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
