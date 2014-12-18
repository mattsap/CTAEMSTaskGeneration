package gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class GUI {

	public GUI() {

	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager
							.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (Exception e) {
				}
				JFrame frame = new JFrame("Method Generator");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				frame.setContentPane(new GeneratorPanel(frame));

				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}

		});

	}

}
