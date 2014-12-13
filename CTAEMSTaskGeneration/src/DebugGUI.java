import generate.Distribution;
import generate.ILoadDistribution;
import generate.IMakeSpanDistribution;
import generate.IRewardDistribution;
import generate.ITimePressureDistribution;
import generate.LoadFirstGenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

import project.Distribute;
import project.SexprGraph;
import sexpr.SexprParser;
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
		LoadFirstGenerator mg = generate();
		for (int i = 0; i < 3; i++) {
	        JFrame frame = new JFrame("JavaFX 2 in Swing");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        DebugPanel tp = null;
	        switch(i) {
	        case 0:
	        	tp = new DebugPanel("Input Load", mg.histInputLoad(), "Structure Load", mg.histStructureLoad(), mg.getTimeScale()+1, 0, mg.getTimeScale());
	        	break;
	        case 1:
	        	tp = new DebugPanel("Input Time Pressure", mg.histInputTimePressure(), "Structure Time Pressure", mg.histStructureTimePressure(), mg.getTimeScale()+1, 0, mg.getTimeScale());
	        	break;
	        case 2:
	        	tp = new DebugPanel("Input Reward", mg.histInputReward(), "Structure Reward", mg.histStructureReward(), mg.getTimeScale()+1, 0, mg.getTimeScale());
	        	break;
	        case 3:
	        	//tp = new DebugPanel("Input Open Time", mg.actualOpenTimeHist(), "Structure Open Time", gl.structureopentimehistogram(), gl.timeLength+1, 0, gl.timeLength);
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
	
	public static LoadFirstGenerator generate() {
		LoadFirstGenerator mg = new LoadFirstGenerator();
    	
		mg.setLoadDist(new ILoadDistribution() {
			
			@Override
			public int getTimeScale() {
				return 100;
			}
			
			@Override
			public int getLoadAtTimeUnit(int timeUnit) {
				return (int)(10000*Distribution.Normal(50, 20, timeUnit));
			}
		});
		
		mg.setMakeSpanDist(new IMakeSpanDistribution() {
			
			@Override
			public int getSampleMakeSpan() {
				// TODO Auto-generated method stub
				return 10;
			}
		});
		
		mg.setRewardDist(new IRewardDistribution() {
			
			@Override
			public int getRewardWithArrivalTime(int arrivalTime) {
				// TODO Auto-generated method stub
				return arrivalTime;
			}
		});
		
		mg.setTimePressureDist(new ITimePressureDistribution() {
			@Override
			public int getDurationForMakeSpanAndArrivalTime(int makeSpan,
					int arrivalTime) {
				// TODO Auto-generated method stub
				return Math.max(1, ((arrivalTime%10) * makeSpan) / 10);
			}
		});
		
    	int[] loadDist = new int[100];
    	for (int i = 0; i < loadDist.length; i++)
    		loadDist[i] = (int)(10000*Distribution.Normal(50, 20, i));
    		//loadDist[i] = (int)(13000*Generate.Poisson(40, i/2));
    	
    	//gl.generate(loadDist);
    	
    	int[] rewardDist = new int[5];
    	for (int i = 0; i < rewardDist.length; i++)
    		rewardDist[i] = 2;
    	
    	int[] opentimeDist = new int[30];
    	for (int i = 0; i < opentimeDist.length; i++)
    		opentimeDist[i] = (int)(10000*Distribution.Normal(15, 5, i));
    	
    	int[] timepressureDist = new int[100];
    	//for (int i = 0; i < 10; i++)
    		//timepressureDist[i] = 2;
    	timepressureDist[10] = 1;
    	timepressureDist[20] = 1;
    	
		
    	mg.generate();

		List<Sexpr> structure = SexprParser.parse("(spec_task (label root) (subtasks Method%50 tasksof2...%40 tasksof3...%60))  (spec_task (label tasksof2) (subtasks Method#2)) (spec_task (label tasksof3) (subtasks Method#3))");
		
		SexprGraph sg = Distribute.ToSexprs(structure, mg.getGeneratedMethods());
    	
    	System.out.println(sg.EmitGraph());
    	System.out.println("Generated: " + mg.getGeneratedMethods().size());
    	System.out.println("Used: " + mg.getMethodsUsed());
    	
		return mg;
	
	}
}
