import graphs.MethodStartEndTime;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import project.GenerateLoad;


public class GUI extends JApplet {
	//VisualsGUI visual = new VisualsGUI(this);
	private static JPanel fxContainer;
	
	public GUI() {
		
	}

	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }
                
                JFrame frame = new JFrame("JavaFX 2 in Swing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                JApplet applet = new GUI();
                applet.init();
                
                frame.setContentPane(applet.getContentPane());
                
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
                applet.start();
            			}
			
		});
	}
	
	@Override
    public void init() {
        fxContainer = new JPanel();
        //JPanel ts = new MethodStartEndTime();
        JPanel ts = new TestPanel();
        fxContainer.setPreferredSize(new Dimension(ts.getWidth(), ts.getHeight()));
		this.getContentPane().setBackground(Color.gray);

        add(ts, BorderLayout.AFTER_LAST_LINE);
        // create JavaFX scene
      /*  Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                createScene();
            }
        });*/
    }


	protected void createScene() {
		// TODO Auto-generated method stub
		
	}
}
