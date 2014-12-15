import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Main {
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }
				
                GUI.startupRelease();
				//DebugGUI.startupDebug();
			}
             
			
		});
	}
}
