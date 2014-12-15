import generate.Distribution;
import generate.IDurationDistribution;
import generate.ILoadDistribution;
import generate.IMakeSpanDistribution;
import generate.IRewardDistribution;
import generate.ITimePressureDistribution;
import generate.LoadFirstDurationGenerator;
import generate.LoadFirstGenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

import project.Distribute;
import project.SexprGraph;
import sexpr.Sexpr;
import sexpr.SexprParser;


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
		LoadFirstDurationGenerator mg = generateLFDG();
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
	
	public static LoadFirstDurationGenerator generateLFDG() {
		LoadFirstDurationGenerator mg = new LoadFirstDurationGenerator();
    	
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
		
		mg.setDurationDist(new IDurationDistribution() {

			@Override
			public int getDurationAtArrivalTime(int arrivalTime) {
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
			public double getTimePressureFor(int arrivalTime) {
				return Math.max(0.1, (arrivalTime%10) / 10.0);
			}
		});
		
    	
    	mg.generate();

		List<Sexpr> structure = null;
		try {
			structure = SexprParser.parse(new String(Files.readAllBytes(Paths.get("C:\\dev\\workspace\\CTAEMSTaskGeneration\\CTAEMSTaskGeneration\\Samples\\2.input")),Charset.defaultCharset()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SexprGraph sg = Distribute.ToSexprs(structure, mg.getGeneratedMethods());
    	
    	System.out.println(sg.EmitGraph());
    	System.out.println("Generated: " + mg.getGeneratedMethods().size());
    	System.out.println("Used: " + mg.getMethodsUsed());
    	
		return mg;
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
			public double getTimePressureFor(int arrivalTime) {
				// TODO Auto-generated method stub
				return Math.max(1, (arrivalTime%10) / 10.0);
			}
		});
		
    	
    	mg.generate();

		List<Sexpr> structure = null;
		try {
			structure = SexprParser.parse(new String(Files.readAllBytes(Paths.get("C:\\dev\\workspace\\CTAEMSTaskGeneration\\CTAEMSTaskGeneration\\Samples\\2.input")),Charset.defaultCharset()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SexprGraph sg = Distribute.ToSexprs(structure, mg.getGeneratedMethods());
    	
    	System.out.println(sg.EmitGraph());
    	System.out.println("Generated: " + mg.getGeneratedMethods().size());
    	System.out.println("Used: " + mg.getMethodsUsed());
    	
		return mg;
	
	}
}
