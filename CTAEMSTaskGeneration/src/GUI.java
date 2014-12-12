import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import project.Distribute;
import project.GenerateLoad;
import sexpr.Parser;
import sexpr.Sexpr;


public class GUI extends JApplet {
	//VisualsGUI visual = new VisualsGUI(this);
	private static JPanel fxContainer;
	
	public GUI() {
		
	}

	/**
	 * Write Up:
	 * 
	 * 
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		// On File Open
		Parser p = new Parser();
		List<Sexpr> structure = null;
		try {
			//structure = p.parse("(spec_task (label root) (subtasks task1%50 task3%20 IdontExist useless...) ) (spec_task (label task1) (subtasks Method%75 task2) (deadline 100)) (spec_task (label task2) (subtasks Method%30) (world 2)) (spec_task (label task3) (subtasks Method%100)) (spec_task (label useless) (subtasks Method#1))");
			
			// figure out opening file
			// replace string with contents of opened file
			structure = p.parse("(spec_task (label hello) (subtasks ) (deadline 100) (earliest_start_time -1))(spec_task (label root) (subtasks hello Method%50 tasksof2...%40 tasksof3...%60))  (spec_task (label tasksof2) (subtasks Method#2)) (spec_task (label tasksof3) (subtasks Method#3))");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//End On File Open */
		
		// On Generate
		GenerateLoad gl = new GenerateLoad();
    	
		// get all gl.generate arguements from the graphs
		 
		// IGNORE!!
    	int[] loadDist = new int[15];
    	for (int i = 0; i < loadDist.length; i++)
    		loadDist[i] = 2;
    	
    	int[] rewardDist = new int[5];
    	for (int i = 0; i < rewardDist.length; i++)
    		rewardDist[i] = 2;
    	
    	int[] opentimeDist = new int[5];
    	for (int i = 0; i < opentimeDist.length; i++)
    		opentimeDist[i] = 2;
    	
    	int[] timepressureDist = new int[100];
    	for (int i = 0; i < timepressureDist.length; i++)
    		timepressureDist[i] = 2;
    	
		
    	//  call to generate
    	gl.generate(loadDist, rewardDist, opentimeDist, timepressureDist);
    	
    	//End On Generate */
    	
    	
		// On Save
    	// inserting generated methods into loaded file
    	Distribute.ToSexprs(structure, gl);
    	
    	// get generated structure as a string
    	String finalStructure = "";
    	for (Sexpr exp : structure) {
    		finalStructure += (exp.toString());
    	}
    	//End On Save */
    	
    	System.out.println(finalStructure);
		if (true)
			return;
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
