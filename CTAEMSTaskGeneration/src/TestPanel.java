
	
	import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.SeriesDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import project.Distribute;
import project.Generate;
import project.GenerateLoad;
import project.Method;
import sexpr.Parser;
import sexpr.Sexpr;

	/**
	 *
	 * @author abimbolaakinmeji
	 */
	
	@SuppressWarnings("serial")
	public class TestPanel extends javax.swing.JPanel {
	    ChartPanel chartPanel;
	    GridBagLayout Layout = new GridBagLayout();
	    GridBagConstraints c = new GridBagConstraints();
	    JPanel TpContainer = new JPanel();
	    JPanel SdContainer = new JPanel();
	    JSlider sliderx = new JSlider();
	    JSlider slidery = new JSlider(0,20);
	    JLabel 	labelx = new JLabel();
	    JLabel 	labely = new JLabel();
		JFreeChart chart;
		double sliderxv;
		double slideryv;
		JButton Openfile = new JButton("Open File");
		JButton SaveFile = new JButton("Save File");
		JButton Toggle = new JButton("Toggle");
		JButton generate = new JButton("Generate");
		JButton Normal = new JButton("Normal");
		JButton Uniform = new JButton("Uniform");
		JButton Poisson = new JButton("Poisson");
		JButton TimePressure = new JButton("Time Pressure");
		JButton reward = new JButton("Reward");
		JButton open = new JButton("Open");
		JButton load = new JButton("Load");
		FlowLayout Fl2 = new FlowLayout(FlowLayout.CENTER);
		XYSeries currentSeries;
		String currentSeriesName;
		XYSeries LdSeries = new XYSeries("Load");
		XYSeries TPSeries = new XYSeries("TimePressure");
		XYSeries Open = new XYSeries("Open");
		XYSeries Reward = new XYSeries("Reward");
    	XYSeriesCollection test = new XYSeriesCollection();
		double NormalV;
		double PoissonV;
		IntervalXYDataset dataset;
		String FileText;
		GenerateLoad g1;
		boolean generated = false;
		boolean opened = false;

		
		

	    /**
	     * Creates new form NewJPanel
	     */
	    public TestPanel() {
	        initComponents();
	        createGraph();
	    }
	    
	  

	    /**
	     * This method is called from within the constructor to initialize the form.
	     * WARNING: Do NOT modify this code. The content of this method is always
	     * regenerated by the Form Editor.
	     */
	    @SuppressWarnings("unchecked")
	    private void initComponents() {
	        setLayout(Layout);
	        this.TpContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
	        Fl2.setAlignOnBaseline(true);
	        this.SdContainer.setLayout(Fl2);
	        this.add(TpContainer);
	        this.add(SdContainer);
	        this.add(sliderx);
	        this.add(slidery);
	        this.add(labelx);
	        this.add(labely);
	        SdContainer.add(Normal);
	        SdContainer.add(Poisson);
	        SdContainer.add(Uniform);
	        SdContainer.add(load);
	        SdContainer.add(TimePressure);
	        SdContainer.add(open);
	        SdContainer.add(reward);
	        labely.setText("Y-VALUE : ");
	        c.gridx = 1;
	        c.gridy = 3;
	        c.anchor = GridBagConstraints.LAST_LINE_END;
	        Layout.addLayoutComponent(labely, c);
	        labelx.setText("X-VALUE : ");
	        c.gridx = 1;
	        c.gridy = 3;
	        c.anchor= GridBagConstraints.PAGE_END;
	        Layout.addLayoutComponent(labelx, c);
	        c.gridx = 1;
	        c.gridy = 3;
	        c.anchor = GridBagConstraints.LAST_LINE_START;
	        Layout.addLayoutComponent(sliderx, c);
	        c.gridx = 0;
	        c.gridy = 1;
	        c.anchor = GridBagConstraints.LINE_END;
	        slidery.setOrientation(JSlider.VERTICAL);
	        Layout.addLayoutComponent(slidery, c);
	        this.TpContainer.add(Openfile);
	        this.TpContainer.add(SaveFile);
	        this.TpContainer.add(Toggle);
	        this.TpContainer.add(generate);
	        c.gridx = 1;
	        c.gridy = 0;
	        c.anchor = GridBagConstraints.CENTER;
	        Layout.addLayoutComponent(this.TpContainer, c);
	        c.gridx = 1;
	        c.gridy = 2;
	        //c.gridwidth = GridBagConstraints.REMAINDER;	     
	        c.anchor = GridBagConstraints.CENTER;
	        Layout.addLayoutComponent(SdContainer, c);
	        ButtonListeners();
	        
	    }    
	    
	    public int[] convertSeries(XYSeries series){
	    	
	    	int size = series.getItems().size();
	    	int[] converted = new int [size];
	    	for(int i = 0; i <= size-1; i++) {
	    		converted[i] = series.getY(i).intValue();
	    	}
	    	
			return converted;
	    	
	    }
	    
    	static String readFile(String path, Charset encoding)  throws IOException 
    			{
    			  byte[] encoded = Files.readAllBytes(Paths.get(path));
    			  return new String(encoded, encoding);
    			}
	    	
	    


	                  

	  public final void createGraph() {
	        dataset = createDataset();
	        chart = createChart(dataset);
	        chartPanel = new ChartPanel(chart);
	        chartPanel.setSize(400, 400);	        
	        this.add(chartPanel);
	        c.gridx = 1;
	        c.gridy = 1;
	        c.anchor = GridBagConstraints.CENTER;
	        Layout.addLayoutComponent(chartPanel, c);
	        sliderx.setValue(0);
			slidery.setValue(0);
		
	        sliderx.addChangeListener(new ChangeListener(){

				@Override
				public void stateChanged(ChangeEvent e) {
					if(currentSeries == null){
						JOptionPane.showMessageDialog(null, "Please Select A Type Of Graph Before Trying To Plot A Distribution");
					}
					else {
					sliderxv = ((JSlider) e.getSource()).getValue();
			        labelx.setText("X-VALUE : " + String.valueOf(sliderx.getValue()));
			        try{
			        slidery.setValue((int) currentSeries.getY((int) sliderxv));
			        }
			        catch(Exception er){
			        	
			        }
					System.out.println(sliderxv);
					}
					
				}
				
			});
	        
	        slidery.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					if(currentSeries == null){
						JOptionPane.showMessageDialog(null, "Please Select A Type Of Graph Before Trying To Plot A Distribution");
					}
					else {
					slideryv = ((JSlider) e.getSource()).getValue();
			        labely.setText("Y-VALUE : " + String.valueOf(slidery.getValue()));

					try {
						currentSeries.remove(sliderxv);
						//currentSeries.
						System.out.println(sliderxv + " removed");
					}
					catch(Exception ex) {
						currentSeries.addOrUpdate(sliderxv,slideryv);
						System.out.println(slideryv);
					}
					
					
					currentSeries.addOrUpdate(sliderxv,slideryv);
					System.out.println(slideryv);
					}
					
				}
					
				
			});

	    }
	  
	  public void ButtonListeners() {
	    	  Normal.addActionListener(new ActionListener() {
	    		  
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if(currentSeries == null){
						JOptionPane.showMessageDialog(null, "Please Select A Type Of Graph Before Trying To Plot A Distribution");
					}
					else {
					  NumericTextField Mean = new NumericTextField();
					  Mean.setColumns(5);
				      NumericTextField STD = new NumericTextField();
				      STD.setColumns(5);
				      NumericTextField Domain = new NumericTextField();
				      Domain.setColumns(5);


				      JPanel myPanel = new JPanel();
				      myPanel.add(new JLabel("Mean:"));
				      myPanel.add(Mean);
				      myPanel.add(Box.createHorizontalStrut(4)); // a spacer
				      myPanel.add(new JLabel("Standard Deviation:"));
				      myPanel.add(STD);
				      myPanel.add(Box.createHorizontalStrut(4)); // a spacer
				      myPanel.add(new JLabel("Domain:"));
				      myPanel.add(Domain);

				      int result = JOptionPane.showConfirmDialog(null, myPanel, 
				               "Please Enter Mean, Standard Deviation, and Domain Values", JOptionPane.OK_CANCEL_OPTION);
				      if (result == JOptionPane.OK_OPTION) {
				    	 double xvalues = Double.parseDouble(Domain.getText());
				    	 for(int i = 0; i <= xvalues-1;i++) {
				    	 double value = Generate.Normal(Double.parseDouble(Mean.getText()), Double.parseDouble(STD.getText()), i);
				    	 try{
				    		 currentSeries.remove(i);
				    	 }
				    	 catch(Exception exp){
				    		 currentSeries.addOrUpdate(i,value);

				    	 }
				    	 finally{
								chartPanel.validate();
				    	 }
				    	 		}
				      }
				    	 	}
						}});
	    	  
	    	  
	    	  Poisson.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(currentSeries == null){
							JOptionPane.showMessageDialog(null, "Please Select A Type Of Graph Before Trying To Plot A Distribution");
						}
						else {
						  NumericTextField Lambda = new NumericTextField();
						  Lambda.setColumns(5);
						  NumericTextField Domain = new NumericTextField();
						  Domain.setColumns(5);

					    
					      JPanel myPanel = new JPanel();
					      myPanel.add(new JLabel("Lambda:"));
					      myPanel.add(Lambda);
					      myPanel.add(Box.createHorizontalStrut(5)); // a spacer
					      myPanel.add(new JLabel("Domain:"));
					      myPanel.add(Domain);
					      

					      int result = JOptionPane.showConfirmDialog(null, myPanel, 
					               "Please Enter Lambda and Domain", JOptionPane.OK_CANCEL_OPTION);
					      if (result == JOptionPane.OK_OPTION) {
					    	 int xvalues = Integer.parseInt(Domain.getText());
					    	for(int i = 0; i <= xvalues-1;i++) {
					    	  double value = Generate.Poisson(Double.parseDouble(Lambda.getText()), i);
						    	  try{
						    		  currentSeries.remove(i);
						    	  	}
						    	  catch(Exception exp){
							    	  currentSeries.addOrUpdate(i,value);
						    	  	}
						    	  currentSeries.addOrUpdate(i,value);
					    			}
					      		}
						}
							}});
	    	  
	    	  Uniform.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					
				if(currentSeries == null){
					JOptionPane.showMessageDialog(null, "Please Select A Type Of Graph Before Trying To Plot A Distribution");
				}
				else {
				NumericTextField Value = new NumericTextField();
				Value.setColumns(5);
				NumericTextField Domain = new NumericTextField();
				Domain.setColumns(5);
				JPanel myPanel = new JPanel();
			    myPanel.add(new JLabel("Value:"));
				myPanel.add(Value);
			    myPanel.add(new JLabel("Domain:"));
				myPanel.add(Domain);
				int result = JOptionPane.showConfirmDialog(null, myPanel, 
			               "Please Enter Value", JOptionPane.OK_CANCEL_OPTION);
			      if (result == JOptionPane.OK_OPTION) {
			    	  int xValues = Integer.parseInt(Domain.getText());
			    	  for(double i = 0; i <= xValues-1; i++ ){
			    		  try{
			    			  currentSeries.remove(i);
			    		  }
			    		  catch(Exception ex){
					    	  currentSeries.addOrUpdate(i, Double.parseDouble(Value.getText()));

			    		  }
			    	  currentSeries.addOrUpdate(i, Double.parseDouble(Value.getText()));
			    	  }
			      }

					
				}
				}	  
	    	  });
	    	  load.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					if(currentSeries == null) {
						currentSeries = LdSeries;
						currentSeriesName = "Load";
				    	test.removeSeries(TPSeries);
				    	test.removeSeries(Open);
				    	test.removeSeries(Reward);
					}
					else{
						if(currentSeriesName.equals("Time Pressure")) {
							TPSeries = currentSeries;
						}
						else if(currentSeriesName.equals("Open")){
							Open = currentSeries;
						}
						else if(currentSeriesName.equals("Reward")){
							Reward = currentSeries;
						}
						else if(currentSeriesName.equals("Load")){
							LdSeries = currentSeries;
						} 
					}
					currentSeries = LdSeries;
					currentSeriesName = "Load";
					chartPanel.validate();

					
				}
	    		  
	    	  });
	    	  
	    	  TimePressure.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(currentSeries == null) {
							currentSeries = TPSeries;
							currentSeriesName = "Time Pressure";
							test.removeSeries(LdSeries);
					    	test.removeSeries(Open);
					    	test.removeSeries(Reward);
						}
						else{
							if(currentSeriesName.equals("Time Pressure")) {
								test.removeSeries(TPSeries);
								TPSeries = currentSeries;
							}
							else if(currentSeriesName.equals("Open")){
						    	test.removeSeries(Open);
								Open = currentSeries;
							}
							else if(currentSeriesName.equals("Reward")){
						    	test.removeSeries(Reward);
								Reward = currentSeries;
							}
							else if(currentSeriesName.equals("Load")){
								test.removeSeries(LdSeries);
								LdSeries = currentSeries;
							} 
						}
				    	test.addSeries(TPSeries);
				  
						currentSeries = TPSeries;
						currentSeriesName = "Time Pressure";
					}
		    		  
		    	  });
	    	  
	    	  	open.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(currentSeries == null) {
							currentSeries = Open;
							currentSeriesName = "Open";
							test.removeSeries(LdSeries);
					    	test.removeSeries(TPSeries);
					    	test.removeSeries(Reward);						}
						else{
							if(currentSeriesName.equals("Time Pressure")) {
								TPSeries = currentSeries;
							}
							else if(currentSeriesName.equals("Open")){
								Open = currentSeries;
							}
							else if(currentSeriesName.equals("Reward")){
								Reward = currentSeries;
							}
							else if(currentSeriesName.equals("Load")){
								LdSeries = currentSeries;
							}
							currentSeries = Open;
							currentSeriesName = "Open";
							
						}
						
					}
		    		  
		    	  });
	    	  	reward.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(currentSeries == null) {
							currentSeries = Reward;
							currentSeriesName = "Reward";
							test.removeSeries(LdSeries);
					    	test.removeSeries(TPSeries);
					    	test.removeSeries(Open);
							createChart(dataset);
						}
						else{
							if(currentSeriesName.equals("Time Pressure")) {
								TPSeries = currentSeries;
							}
							else if(currentSeriesName.equals("Open")){
								Open = currentSeries;
							}
							else if(currentSeriesName.equals("Reward")){
								Reward = currentSeries;
						    	test.removeSeries(Reward);

							}
							else if(currentSeriesName.equals("Load")){
								LdSeries = currentSeries;
							} 
						
						currentSeries = Reward;
						currentSeriesName = "Reward";
						}
						
					}
		    		  
		    	  });
	    	  	
	    	  	generate.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						g1 = new GenerateLoad();
						int[] loadDist = convertSeries(LdSeries);
						int[] OpenDist = convertSeries(Open);
						int[] TpDist = convertSeries(TPSeries);
						int[] RewardDist = convertSeries(Reward);
						try{
						g1.generate(loadDist, RewardDist, OpenDist, TpDist);
						generated = true;
						}
						catch(Exception exp){
							JOptionPane.showMessageDialog(null, "Error during Generation, Please check The Console");
							generated = false;
						}
					}
	    	  		
	    	  	});
	    	  	
	    	  	 Openfile.addActionListener(new ActionListener() {
	    	  	      public void actionPerformed(ActionEvent ae) {
	    	  	        JFileChooser chooser = new JFileChooser();
	    	  	        chooser.setMultiSelectionEnabled(false);
	    	  	        int option = chooser.showOpenDialog(TestPanel.this);
	    	  	        if (option == JFileChooser.APPROVE_OPTION) {
	    	  	          File sf = chooser.getSelectedFile();
	    	  	          
	    	  	          try {
							FileText = readFile(sf.getPath(), Charset.defaultCharset());
							JOptionPane.showMessageDialog(null, sf.getName() + " Has been Opened");
							opened = true;
						} catch (IOException e) {
							opened = false;
							e.printStackTrace();
						}
	    	  	        
	    	  	        }
	    	  	        
	    	  	      }
	    	  	    });
	    	  	 SaveFile.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						if(opened == false || generated == false) {
							JOptionPane.showMessageDialog(null, "Please Make Sure you have Opened A file and Generated Distributions First");
						}
						else {
						Parser p = new Parser();
						List<Sexpr> structure = null;
						try {
							//structure = p.parse("(spec_task (label root) (subtasks task1%50 task3%20 IdontExist useless...) ) (spec_task (label task1) (subtasks Method%75 task2) (deadline 100)) (spec_task (label task2) (subtasks Method%30) (world 2)) (spec_task (label task3) (subtasks Method%100)) (spec_task (label useless) (subtasks Method#1))");
							
							// figure out opening file
							// replace string with contents of opened file
							structure = p.parse(FileText);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Distribute.ToSexprs(structure, g1);
				    	
				    	// get generated structure as a string
				    	String finalStructure = "";
				    	for (Sexpr exp : structure) {
				    		finalStructure += (exp.toString());
				    	}
				    	//End On Save 
				    	JFileChooser fileChooser = new JFileChooser();
	    	  	        int option = fileChooser.showSaveDialog(TestPanel.this);
	    	  	        if (option == JFileChooser.APPROVE_OPTION) {

				    	  File file = fileChooser.getSelectedFile();
				    	  // save to file
				    	  try(FileWriter fw = new FileWriter(file+".txt")) {
				    		    fw.write(finalStructure);
				    		    fw.close();
				    		}
				    	  catch(Exception ex){
				    		  
				    	  }
				    	}
				    	    
						}
					} 
	    	  	 });
	    	  	 Toggle.addActionListener(new ActionListener(){
	    	  		
					@Override
					public void actionPerformed(ActionEvent e) {
	    	  			
						 if(generated == false) {
								JOptionPane.showMessageDialog(null, "Please Generate a Distribution first");
		    	  		 }
						 else {
							 if(currentSeriesName.equals("Time Pressure")) {
								 double[] tpgen = g1.actualTimePressureHist();
								 XYSeries TPGenSeries = new XYSeries("Time Pressure Generated");
								 for(int i = 0; i <= tpgen.length;i++){
									 TPGenSeries.addOrUpdate(i, tpgen[i]);
								 }
								 test.addSeries(TPGenSeries);
								}
								else if(currentSeriesName.equals("Open")){
									double[] OpenGen = g1.actualOpenTimeHist();
									 XYSeries OpenGenSeries = new XYSeries("Open Time Generated");
									 for(int i = 0; i <= OpenGen.length;i++){
										 OpenGenSeries.addOrUpdate(i, OpenGen[i]);
									 }
									 test.addSeries(OpenGenSeries);								
									 }
								else if(currentSeriesName.equals("Reward")){
									double[] RewardGen = g1.actualRewardHist();
									 XYSeries RewardGenSeries = new XYSeries("Reward Generated");
									 for(int i = 0; i <= RewardGen.length;i++){
										 RewardGenSeries.addOrUpdate(i, RewardGen[i]);
									 }
									 test.addSeries(RewardGenSeries);	
								}
								else if(currentSeriesName.equals("Load")){
									double[] LoadGen = g1.actualOpenTimeHist();
									 XYSeries LoadGenSeries = new XYSeries("Load Generated");
									 for(int i = 0; i <= LoadGen.length;i++){
										 LoadGenSeries.addOrUpdate(i, LoadGen[i]);
									 }
									 test.addSeries(LoadGenSeries);									} 
						 }
					}
	    	  		
	    	  	 } );
	    	  	 
	      	}
	  
		  
	  	
	    
	    private XYSeriesCollection createDataset() {
	    	XYSeriesCollection test = new XYSeriesCollection();

	    	
	    	/*
	    	GenerateLoad gl = new GenerateLoad();
	    	
	    	int[] loadDist = new int[100];
	    	for (int i = 0; i < loadDist.length; i++)
	    	loadDist[i] = (int)(30000*Generate.Normal(50, 30, i));
	    	//	loadDist[i] = (int)(13000*Generate.Poisson(14, i));
	    	
	    	//gl.generate(loadDist);
	    	
	    	int[] rewardDist = new int[5];
	    	for (int i = 0; i < rewardDist.length; i++)
	    		rewardDist[i] = 2;
	    	
	    	int[] opentimeDist = new int[15];
	    	for (int i = 10; i < opentimeDist.length; i++)
	    		opentimeDist[i] = 2;
	    	
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
	    	String finalStructure = "";
	    	for (Sexpr exp : structure) {
	    		finalStructure += (exp.toString());
	    	}
	    	//End On Save 
	    	
	    	System.out.println(finalStructure);
	    
	    	/*
	    	
	    	int value = 0;
	    	for (int i = 1; i <= 20; i++)
	    		gl.generateNextTimeStep((int)(300*Generate.Normal(10, 3, i)));
	    		//gl.generateNextTimeStep((int)(300*Generate.Laplace(10, 3, i)));
	    		//gl.generateNextTimeStep((int)(300*Generate.Exponential(1, 20, i)));
	    		//gl.generateNextTimeStep((int)(700 * Generate.Poisson(4, i)));
	    		//gl.generateNextTimeStep((value += (int)(700 * Generate.Poisson(10, i))));
	    	gl.generateNextTimeStep(0);
	    	
	    	double[] opentimeActual = new double[gl.generated.size()];
	    	int sumOpenTime = 0;
			for (int i = 0; i < opentimeDist.length; i++)
				sumOpenTime += opentimeDist[i];
			int countO = 0;
			for (int i = 0; i < opentimeDist.length; i++)
				for (int j = 0; j < opentimeDist[i] * opentimeActual.length / sumOpenTime; j++)
					opentimeActual[countO++] = i;
	    	   */
	       //HistogramDataset dataset = new HistogramDataset();
	       
	       //dataset.addSeries("Load", loadDist,loadDist.length+1,0,loadDist.length);
	       //dataset.addSeries("Actual Load", gl.loadhistogram(),loadDist.length+1,0,loadDist.length);
	       //dataset.addSeries("Open Time", gl.opentimehistogram(), opentimeDist.length+1,0,opentimeDist.length);
	       //dataset.addSeries("Actual Open Time", opentimeActual,opentimeDist.length+1,0,opentimeDist.length);
	       //dataset.addSeries("Time Pressure", gl.timePressurehist(), timepressureDist.length+1,0,timepressureDist.length);
	       //dataset.addSeries("Actual Time Pressure", gl.actualTimePressureHist(),timepressureDist.length+1,0,timepressureDist.length);
	       //dataset.addSeries("Reward", gl.rewardhistogram(), rewardDist.length+1,0,rewardDist.length);
	       //dataset.addSeries("Actual Reward", gl.actualRewardHist(),rewardDist.length+1,0,rewardDist.length);
	       //dataset.addSeries("seies", Series, 2);
	    	try{
	    	test.addSeries(LdSeries);
	    	test.addSeries(TPSeries);
	    	test.addSeries(Open);
	    	test.addSeries(Reward);
	    	}
	    	catch(Exception exp) {
	    		
	    	}
	   
	       
	       return test;
	        
	    }
	    
	    private JFreeChart createChart(final IntervalXYDataset dataset) {
	    	
			final JFreeChart chart;
	        chart = ChartFactory.createHistogram(
	                "Histogram Demo",      // chart title
	                "Session",                      // x axis label
	                "Value",                      // y axis label
	                dataset,                  // data
	                PlotOrientation.VERTICAL,
	                true,                     // include legend
	                true,                     // tooltips
	                false                     // urls
	        );

			// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
			chart.setBackgroundPaint(Color.white);

			chart.setBackgroundPaint(new Color(230,230,230));
	                 XYPlot xyplot = (XYPlot)chart.getPlot();
	                 xyplot.setForegroundAlpha(0.7F);
	                 xyplot.setBackgroundPaint(Color.WHITE);
	                 xyplot.setDomainGridlinePaint(new Color(150,150,150));
	                 xyplot.setRangeGridlinePaint(new Color(150,150,150));
	                 XYBarRenderer xybarrenderer = (XYBarRenderer)xyplot.getRenderer();
	     
	                 xybarrenderer.setShadowVisible(false);
	                 xybarrenderer.setBarPainter(new StandardXYBarPainter()); 
//	    xybarrenderer.setDrawBarOutline(false);
			return chart;
		}



	}





