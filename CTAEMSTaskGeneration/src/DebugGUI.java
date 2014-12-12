import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

import project.Distribute;
import project.Generate;
import project.GenerateLoad;
import sexpr.Parser;
import sexpr.Sexpr;


public class DebugGUI extends JApplet{

	DebugPanel debugPanel;
	public DebugGUI(DebugPanel dp) {
		debugPanel = dp;
	}
	
	@Override
    public void init() {
		JPanel fxContainer = new JPanel();
        //JPanel ts = new MethodStartEndTime();
        JPanel ts = debugPanel;
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
	
	public static void startupDebug() {
		GenerateLoad gl = generate();
		for (int i = 0; i < 4; i++) {
        JFrame frame = new JFrame("JavaFX 2 in Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        DebugPanel tp = null;
        switch(i) {
        case 0:
        	tp = new DebugPanel("Input Load", gl.actualLoadHist(), "Structure Load", gl.structureloadhistogram(), gl.timeLength+1, 0, gl.timeLength);
        	break;
        case 1:
        	tp = new DebugPanel("Input Open Time", gl.actualOpenTimeHist(), "Structure Open Time", gl.structureopentimehistogram(), gl.timeLength+1, 0, gl.timeLength);
        	break;
        case 2:
        	tp = new DebugPanel("Input Reward", gl.actualRewardHist(), "Structure Reward", gl.structurerewardhistogram(), gl.maxReward+1, 0, gl.maxReward);
        	break;
        case 3:
        	tp = new DebugPanel("Input Time Pressure", gl.actualTimePressureHist(), "Structure Time Pressure", gl.structuretimePressurehist(), 100+1, 0, 100);
        	break;
        }
        JApplet applet = new DebugGUI(tp);
        applet.init();
        
        frame.setContentPane(applet.getContentPane());
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        applet.start();
		}
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
}
