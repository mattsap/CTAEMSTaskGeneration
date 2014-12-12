import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import project.Distribute;
import project.Generate;
import project.GenerateLoad;
import sexpr.Parser;
import sexpr.Sexpr;


public class GUI extends JApplet {
	//VisualsGUI visual = new VisualsGUI(this);
	private static JPanel fxContainer;
	
	private TestPanel testPanel;
	public GUI(TestPanel tp) {
		testPanel = tp;
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
		/*Parser p = new Parser();
		List<Sexpr> structure = null;
		try {
			//structure = p.parse("(spec_task (label root) (subtasks task1%50 task3%20 IdontExist useless...) ) (spec_task (label task1) (subtasks Method%75 task2) (deadline 100)) (spec_task (label task2) (subtasks Method%30) (world 2)) (spec_task (label task3) (subtasks Method%100)) (spec_task (label useless) (subtasks Method#1))");
			
			// figure out opening file
			// replace string with contents of opened file
			//structure = p.parse("(spec_task (label hello) (subtasks hello2 Method#1) (deadline 100) (earliest_start_time -1))(spec_task (label hello2) (subtasks Method#1) (deadline 100) (earliest_start_time -1))(spec_task (label root) (subtasks tasksof2...%40 tasksof3...%60))  (spec_task (label tasksof2) (subtasks hello Method#2)) (spec_task (label tasksof3) (subtasks Method#3))");
			structure = p.parse(new String(Files.readAllBytes(Paths.get("C:\\dev\\workspace\\CTAEMSTaskGeneration\\CTAEMSTaskGeneration\\Samples\\1.input")),Charset.defaultCharset()));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//End On File Open */
		
		/*
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
    	/*Distribute.ToSexprs(structure, gl);
    	
    	// get generated structure as a string
    	String finalStructure = "";
    	for (Sexpr exp : structure) {
    		finalStructure += (exp.toString());
    	}
    	//End On Save */
    	/*
    	System.out.println(finalStructure);
    	System.out.println(gl.generated.size());
    	System.out.println(gl.methodsUsed());
		//if (true)return;
    	*/

		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                }
				
                startupRelease();
			}
             
			
		});
	}
	
	@Override
    public void init() {
        fxContainer = new JPanel();
        //JPanel ts = new MethodStartEndTime();
        JPanel ts = testPanel;//new TestPanel();
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
	
	public static GenerateLoad generate() {
		GenerateLoad gl = new GenerateLoad();
    	
    	int[] loadDist = new int[100];
    	for (int i = 0; i < loadDist.length; i++)
    		loadDist[i] = (int)(10000*Generate.Normal(50, 20, i));
    		//loadDist[i] = (int)(13000*Generate.Poisson(40, i/2));
    	
    	//gl.generate(loadDist);
    	
    	int[] rewardDist = new int[5];
    	for (int i = 0; i < rewardDist.length; i++)
    		rewardDist[i] = 2;
    	
    	int[] opentimeDist = new int[30];
    	for (int i = 0; i < opentimeDist.length; i++)
    		opentimeDist[i] = (int)(10000*Generate.Normal(15, 5, i));
    	
    	int[] timepressureDist = new int[100];
    	//for (int i = 0; i < 10; i++)
    		//timepressureDist[i] = 2;
    	timepressureDist[10] = 1;
    	timepressureDist[20] = 1;
    	
		
    	gl.generate(loadDist, rewardDist, opentimeDist, timepressureDist);

    	Parser p = new Parser();
		List<Sexpr> structure = null;
		try {
			//structure = p.parse("(spec_task (label root) (subtasks task1%50 task3%20 IdontExist useless...) ) (spec_task (label task1) (subtasks Method%75 task2) (deadline 100)) (spec_task (label task2) (subtasks Method%30) (world 2)) (spec_task (label task3) (subtasks Method%100)) (spec_task (label useless) (subtasks Method#1))");
			
			// figure out opening file
			// replace string with contents of opened file
			structure = p.parse("(spec_task (label root) (subtasks Method%50 tasksof2...%40 tasksof3...%60))  (spec_task (label tasksof2) (subtasks Method#2)) (spec_task (label tasksof3) (subtasks Method#3))");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Distribute.ToSexprs(structure, gl);
    	
    	// get generated structure as a string
    	/*String finalStructure = "";
    	for (Sexpr exp : structure) {
    		finalStructure += (exp.toString());
    	}*/
    	//End On Save */
    	
    	//System.out.println(finalStructure);
    	System.out.println("Generated: " + gl.generated.size());
    	System.out.println("Used: " + gl.methodsUsed());
    
    	
    	/*
    	int value = 0;
    	for (int i = 1; i <= 20; i++)
    		gl.generateNextTimeStep((int)(300*Generate.Normal(10, 3, i)));
    		//gl.generateNextTimeStep((int)(300*Generate.Laplace(10, 3, i)));
    		//gl.generateNextTimeStep((int)(300*Generate.Exponential(1, 20, i)));
    		//gl.generateNextTimeStep((int)(700 * Generate.Poisson(4, i)));
    		//gl.generateNextTimeStep((value += (int)(700 * Generate.Poisson(10, i))));
    	gl.generateNextTimeStep(0);*/
    	
    	double[] loadDistClone = new double[100];
    	for (int i = 0; i < loadDist.length; i++)
    		loadDistClone[i] = loadDist[i];
    	double[] opentimeActual = new double[gl.generated.size()];
    	int sumOpenTime = 0;
		for (int i = 0; i < opentimeDist.length; i++)
			sumOpenTime += opentimeDist[i];
		int countO = 0;
		for (int i = 0; i < opentimeDist.length; i++)
			for (int j = 0; j < opentimeDist[i] * opentimeActual.length / sumOpenTime; j++)
				opentimeActual[countO++] = i;
		
		return gl;
	
	}
	
	public static void startupRelease() {
		JFrame frame = new JFrame("JavaFX 2 in Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JApplet applet = new GUI(new TestPanel());
        applet.init();
        
        frame.setContentPane(applet.getContentPane());
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        applet.start();
	}
	
	public static void startupDebug() {
		GenerateLoad gl = generate();
		for (int i = 0; i < 4; i++) {
        JFrame frame = new JFrame("JavaFX 2 in Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        TestPanel tp = null;
        switch(i) {
        case 0:
        	tp = new TestPanel("Input Load", gl.actualLoadHist(), "Structure Load", gl.structureloadhistogram(), gl.timeLength+1, 0, gl.timeLength);
        	break;
        case 1:
        	tp = new TestPanel("Input Open Time", gl.actualOpenTimeHist(), "Structure Open Time", gl.structureopentimehistogram(), gl.timeLength+1, 0, gl.timeLength);
        	break;
        case 2:
        	tp = new TestPanel("Input Reward", gl.actualRewardHist(), "Structure Reward", gl.structurerewardhistogram(), gl.maxReward+1, 0, gl.maxReward);
        	break;
        case 3:
        	tp = new TestPanel("Input Time Pressure", gl.actualTimePressureHist(), "Structure Time Pressure", gl.structuretimePressurehist(), 100+1, 0, 100);
        	break;
        }
        JApplet applet = new GUI(tp);
        applet.init();
        
        frame.setContentPane(applet.getContentPane());
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        applet.start();
		}
	}
}
