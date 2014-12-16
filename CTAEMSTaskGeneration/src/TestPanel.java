
	
	import generate.Distribution;
import generate.IDurationDistribution;
import generate.ILoadDistribution;
import generate.IRewardDistribution;
import generate.ITimePressureDistribution;
import generate.LoadFirstDurationGenerator;
import generate.MethodGenerator;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
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
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import project.Distribute;
import sexpr.Sexpr;
import sexpr.SexprParser;

	/**
	 *
	 * @author abimbolaakinmeji
	 */
	
	@SuppressWarnings("serial")
	public class TestPanel extends javax.swing.JPanel {
	    ChartPanel loadPanel;
	    ChartPanel slackPanel;
	    ChartPanel rewardPanel;
	    GridBagLayout Layout = new GridBagLayout();
	    GridBagConstraints c = new GridBagConstraints();
	    JPanel TpContainer = new JPanel();
	    JPanel SdContainer = new JPanel();
	    JSlider sliderx = new JSlider();
	    JSlider slidery = new JSlider(0,20);
	    JLabel 	labelx = new JLabel();
	    JLabel 	labely = new JLabel();
	    JLabel 	setdomain = new JLabel();
	    NumericTextField domaininput = new NumericTextField();
		JFreeChart loadChart;
		JFreeChart slackChart;
		JFreeChart rewardChart;
		double sliderxv;
		double slideryv;
		JButton Openfile = new JButton("Open File");
		JButton SaveFile = new JButton("Save File");
		JButton Toggle = new JButton("Toggle");
		JButton generate = new JButton("Generate");
		JButton Normal = new JButton("Normal");
		JButton Uniform = new JButton("Uniform");
		JButton Poisson = new JButton("Poisson");
		JButton slack = new JButton("Slack");
		JButton reward = new JButton("Reward");
		JButton load = new JButton("Load");
		JButton Submit = new JButton("Submit");
		JButton Random = new JButton("Random");
		JButton Clear = new JButton("Clear");
		FlowLayout Fl2 = new FlowLayout(FlowLayout.CENTER);
		XYSeries currentSeries;
		String currentSeriesName;
		XYSeries LdSeries = new XYSeries("Load");
		XYSeries slackSeries = new XYSeries("Slack");
		XYSeries Reward = new XYSeries("Reward");
    	XYSeriesCollection loadCollection = new XYSeriesCollection();
    	XYSeriesCollection slackCollection = new XYSeriesCollection();
    	XYSeriesCollection rewardCollection = new XYSeriesCollection();
		double NormalV;
		double PoissonV;
		IntervalXYDataset loadDataset;
		IntervalXYDataset slackDataset;
		IntervalXYDataset rewardDataset;
		String FileText;
		MethodGenerator methodGenerator;
		boolean generated = false;
		boolean opened = false;
		JPanel self = this;
		Frame frame;
		JPanel currentPanel;
		double domainV;
		double lastDomain;
		
		

	    /**
	     * Creates new form NewJPanel
	     * @param frame2 
	     */
	    public TestPanel(JFrame frame2) {
	    	initComponents();
	    	createLoadGraph();
	    	createSlackGraph();
	    	createRewardGraph();
		    this.add(slackPanel);
		    currentSeries = slackSeries;
		    currentPanel = slackPanel;
	    	this.frame = frame2;
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
	        SdContainer.add(Random);
	        SdContainer.add(load);
	        SdContainer.add(slack);
	        SdContainer.add(reward);
	        SdContainer.add(Clear);
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
	        this.TpContainer.add(setdomain);
	        this.TpContainer.add(domaininput);
	        this.TpContainer.add(Submit);
	        setdomain.setText("Enter Max Time for All Graphs");
	        domaininput.setColumns(5);
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
	        domainV = 100;
	        
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
	    	
	    
	                  

	  public final void createLoadGraph() {
	        loadDataset = createLoadDataset();
	        loadChart = createLoadChart(loadDataset);
	        loadPanel = new ChartPanel(loadChart);
	        loadPanel.setSize(400, 400);
	       

	    }
	  
	  public final void createSlackGraph() {
	        slackDataset = createSlackDataset();
	        slackChart = createSlackChart(slackDataset);
	        slackPanel = new ChartPanel(slackChart);
	        slackPanel.setSize(400, 400);
	        c.gridx = 1;
	        c.gridy = 1;
	        c.anchor = GridBagConstraints.CENTER;
	        Layout.addLayoutComponent(slackPanel, c);
	       
	        
	  }
	  
	  public final void createRewardGraph() {
	        rewardDataset = createRewardDataset();
	        rewardChart = createRewardChart(rewardDataset);
	        rewardPanel = new ChartPanel(rewardChart);
	        rewardPanel.setSize(400, 400);	        
	       
	  }
	  
	  public void ButtonListeners() {
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
			        self.updateUI();
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
						self.updateUI();
					}
					catch(Exception ex) {
						currentSeries.addOrUpdate(sliderxv,slideryv);
					}
					
					
					currentSeries.addOrUpdate(sliderxv,slideryv);
					}
					
				}
					
				
			});
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
				   


				      JPanel myPanel = new JPanel();
				      myPanel.add(new JLabel("Mean:"));
				      myPanel.add(Mean);
				      myPanel.add(Box.createHorizontalStrut(4)); // a spacer
				      myPanel.add(new JLabel("Standard Deviation:"));
				      myPanel.add(STD);
				      myPanel.add(Box.createHorizontalStrut(4)); // a spacer
				     

				      int result = JOptionPane.showConfirmDialog(null, myPanel, 
				               "Please Enter Mean, Standard Deviation", JOptionPane.OK_CANCEL_OPTION);
				      if (result == JOptionPane.OK_OPTION) {
				    	 for(int i = 0; i <= domainV;i++) {
				    		 double value = Distribution.Normal(Double.parseDouble(Mean.getText()), Double.parseDouble(STD.getText()), i);
					    	 try{
					    		 currentSeries.remove(i);
					    	 }
					    	 catch(Exception exp){
					    		 currentSeries.addOrUpdate(i,value);
	
					    	 }
					    	 finally{
								  	self.updateUI();
					    	 }
				    	 }
				      }
				    }
				 }});
	    	  Random.addActionListener(new ActionListener() {
	    		  
					@Override
					public void actionPerformed(ActionEvent e) {
						
						if(currentSeries == null){
							JOptionPane.showMessageDialog(null, "Please Select A Type Of Graph Before Trying To Plot A Distribution");
						}
						else {
						  NumericTextField min = new NumericTextField();
						  min.setColumns(5);
					      NumericTextField max = new NumericTextField();
					      max.setColumns(5);
					   


					      JPanel myPanel = new JPanel();
					      myPanel.add(new JLabel("Minimum Range:"));
					      myPanel.add(min);
					      myPanel.add(Box.createHorizontalStrut(4)); // a spacer
					      myPanel.add(new JLabel("Maximum Range:"));
					      myPanel.add(max);
					      myPanel.add(Box.createHorizontalStrut(4)); // a spacer
					     

					      int result = JOptionPane.showConfirmDialog(null, myPanel, 
					               "Please Enter Minimum and Maximum Time Values", JOptionPane.OK_CANCEL_OPTION);
					      if (result == JOptionPane.OK_OPTION) {
					    	  if(Double.parseDouble(min.getText()) > Double.parseDouble(max.getText())) {
									JOptionPane.showMessageDialog(null, "Minimum Range is Greater than Maximum Range, Please Re-input Values");
					    	  }
					    	  else {
						    	 for(int i = 0; i <= domainV;i++) {
						    		 double value = Distribution.UniformRandom(Double.parseDouble(min.getText()), Double.parseDouble(max.getText()));
							    	 try{
							    		 currentSeries.remove(i);
							    	 }
							    	 catch(Exception exp){
							    		 currentSeries.addOrUpdate(i,value);
			
							    	 }
							    	 finally{
										  	self.updateUI();
							    	 }
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
						

					    
					      JPanel myPanel = new JPanel();
					      myPanel.add(new JLabel("Lambda:"));
					      myPanel.add(Lambda);
				

					      int result = JOptionPane.showConfirmDialog(null, myPanel, 
					               "Please Enter Lambda", JOptionPane.OK_CANCEL_OPTION);
					      if (result == JOptionPane.OK_OPTION) {
					    	for(int i = 0; i <= domainV;i++) {
					    	  double value = Distribution.Poisson(Double.parseDouble(Lambda.getText()), i);
						    	  try{
						    		  currentSeries.remove(i);
						    	  	}
						    	  catch(Exception exp){
							    	  currentSeries.addOrUpdate(i,value);
						    	  	}
						    	  currentSeries.addOrUpdate(i,value);
						    	  self.updateUI();
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
					NumericTextField EndDomain = new NumericTextField();
					EndDomain.setColumns(5);
					JPanel myPanel = new JPanel();
				    myPanel.add(new JLabel("Value:"));
					myPanel.add(Value);
				    myPanel.add(new JLabel("Start Domain:"));
					myPanel.add(Domain);
					myPanel.add(new JLabel("Stop Domain:"));
					myPanel.add(EndDomain);
					int result = JOptionPane.showConfirmDialog(null, myPanel, 
				               "Please Enter Value", JOptionPane.OK_CANCEL_OPTION);
				      if (result == JOptionPane.OK_OPTION) {
				    	  int xEnd = Integer.parseInt(EndDomain.getText());
				    	  int xStart = Integer.parseInt(Domain.getText());
				    	  if(xStart > xEnd) {
								JOptionPane.showMessageDialog(null, "Minimum Value is Greater than Maximum Values, Please Re-input Values");
				    	  }
				    	  else {
				    		  
				    	  
					    	  for(int i = xStart; i <= xEnd; i++ ){
					    		  try{
					    			  currentSeries.remove(i);
					    			  self.updateUI();
					    		  }
					    		  catch(Exception ex){
							    	  currentSeries.addOrUpdate(i, Double.parseDouble(Value.getText()));
					    		  	}
					    		  currentSeries.addOrUpdate(i, Double.parseDouble(Value.getText()));
					    	  }
				    	  }
				      }
					}
				}	  
	    	  });
	    	  load.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					self.validate();
					if(currentSeries == null){
						currentSeries = LdSeries;
						currentSeriesName = "Load";
						currentPanel = loadPanel;
						
				        c.gridx = 1;
				        c.gridy = 1;
				        c.anchor = GridBagConstraints.CENTER;
					    self.add(loadPanel);
				        Layout.addLayoutComponent(loadPanel, c);
				    	self.updateUI();
					}
					else {
			        	self.remove(currentPanel);
						currentSeries = LdSeries;
						currentSeriesName = "Load";
						currentPanel = loadPanel;
						
				        c.gridx = 1;
				        c.gridy = 1;
				        c.anchor = GridBagConstraints.CENTER;
					    self.add(currentPanel);
				        Layout.addLayoutComponent(loadPanel, c);
				    	self.updateUI();;
					}	
					
				}
	    		  
	    	  });
	    	  
	    	  slack.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						self.validate();
						if(currentSeries == null){
							currentSeries = slackSeries;
							currentSeriesName = "Slack";
							currentPanel = slackPanel;
							c.gridx = 1;
						    c.gridy = 1;
						    c.anchor = GridBagConstraints.CENTER;
						    self.add(currentPanel);
						    Layout.addLayoutComponent(slackPanel, c);
						    self.updateUI();
						}
						else {
				        	self.remove(currentPanel);
							currentSeries = slackSeries;
							currentSeriesName = "Slack";
							currentPanel = slackPanel;
							c.gridx = 1;
						    c.gridy = 1;
						    c.anchor = GridBagConstraints.CENTER;
						    self.add(currentPanel);
						    Layout.addLayoutComponent(slackPanel, c);
						    self.updateUI();
						}
						    

						
						
					}
		    		  
		    	  });
	    	  
	    	  	
	    	  	reward.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						self.validate();
						if(currentSeries == null){
							currentSeries = Reward;
							currentSeriesName = "Reward";
							currentPanel = rewardPanel;
							c.gridx = 1;
						    c.gridy = 1;
						    c.anchor = GridBagConstraints.CENTER;
						    self.add(rewardPanel);
						    Layout.addLayoutComponent(rewardPanel, c);
						    self.updateUI();

							
						}
						else {
							self.remove(currentPanel);
							currentSeries = Reward;
							currentSeriesName = "Reward";
							currentPanel = rewardPanel;
							c.gridx = 1;
						    c.gridy = 1;
						    c.anchor = GridBagConstraints.CENTER;
						    self.add(rewardPanel);
						    Layout.addLayoutComponent(rewardPanel, c);
						    self.updateUI();

						}
						
						
					}
		    		  
		    	  });
	    	  	Clear.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						try{
							currentSeries.clear();
						}
						catch(Exception exp) {
							self.updateUI();
						}
						finally{
							self.updateUI();
						}
						
						
					}
	    	  		
	    	  	});
	    	  	
	    	  	generate.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						LoadFirstDurationGenerator lfg = new LoadFirstDurationGenerator();
						final int[] loadDist = convertSeries(LdSeries);
						//final int[] OpenDist = convertSeries(Open);
						final int[] TpDist = convertSeries(slackSeries);
						final int[] RewardDist = convertSeries(Reward);
						
						lfg.setLoadDist(new ILoadDistribution() {
							
							@Override
							public int getTimeScale() {
								return loadDist.length;
							}
							
							@Override
							public int getLoadAtTimeUnit(int timeUnit) {
								return loadDist[timeUnit];
							}
						});
						
						lfg.setDurationDist(new IDurationDistribution() {
							
							@Override
							public int getDurationAtArrivalTime(int arrivalTime) {
								// TODO Auto-generated method stub
								return 10;
							}
						});
						
						lfg.setRewardDist(new IRewardDistribution() {
							
							@Override
							public int getRewardWithArrivalTime(int arrivalTime) {
								return RewardDist[arrivalTime];
							}
						});
						
						lfg.setTimePressureDist(new ITimePressureDistribution() {
							
							@Override
							public double getTimePressureFor(int arrivalTime) {
								return TpDist[arrivalTime]  / 100.0;
							}
						});
						try{
							methodGenerator = lfg;
							methodGenerator.generate();
							generated = true;
						}
						catch(Exception exp){
							JOptionPane.showMessageDialog(null, "Error during Generation, Please check The Console");
							exp.printStackTrace();
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
						SexprParser p = new SexprParser();
						List<Sexpr> structure = null;
						try {
							
							structure = p.parse(FileText);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Distribute.ToSexprs(structure, methodGenerator.getGeneratedMethods());
				    	
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
							 if(currentSeriesName.equals("Slack")) {
								 double[] tpgen = methodGenerator.histStructureTimePressure();
								 XYSeries TPGenSeries = new XYSeries("Time Pressure Generated");
								 for(int i = 0; i <= tpgen.length;i++){
									 TPGenSeries.addOrUpdate(i, tpgen[i]);
								 }
								 slackCollection.addSeries(TPGenSeries);
								 self.updateUI();
								}
								
								else if(currentSeriesName.equals("Reward")){
									double[] RewardGen = methodGenerator.histStructureReward();
									 XYSeries RewardGenSeries = new XYSeries("Reward Generated");
									 for(int i = 0; i <= RewardGen.length;i++){
										 RewardGenSeries.addOrUpdate(i, RewardGen[i]);
									 }
									 rewardCollection.addSeries(RewardGenSeries);
									 self.updateUI();
								}
								else if(currentSeriesName.equals("Load")){
									double[] LoadGen = methodGenerator.histStructureLoad();
									 XYSeries LoadGenSeries = new XYSeries("Load Generated");
									 for(int i = 0; i <= LoadGen.length;i++){
										 LoadGenSeries.addOrUpdate(i, LoadGen[i]);
									 }
									 loadCollection.addSeries(LoadGenSeries);
									 self.updateUI();
								} 
						 }
					}
	    	  		
	    	  	 });
	    	  	 Submit.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						lastDomain = domainV;
						domainV = Double.parseDouble(domaininput.getText());
						clearExtra(lastDomain);
						slackChart.getXYPlot().getDomainAxis().setRange(0, domainV);
						loadChart.getXYPlot().getDomainAxis().setRange(0, domainV);
						rewardChart.getXYPlot().getDomainAxis().setRange(0, domainV);
						self.updateUI();
						
						
					}
	    	  		 
	    	  	 });
	    	  	 
	      	}
	  
	  public void clearExtra(double lastDomain2){
		  for(double i = domainV+1; i <= lastDomain2; i++ ){
			  try{
			  currentSeries.remove(i);
			  }
			  catch(Exception e){
				  
			  }
		  }
	  }
	  
		  
	  	
	    
	    private XYSeriesCollection createLoadDataset() {

	    	
	  
	    	try{
	    		loadCollection.addSeries(LdSeries);
	    
	    	}
	    	catch(Exception exp) {
	    		
	    	}
	   
	       
	       return loadCollection;
	        
	    }
	    
	    private XYSeriesCollection createSlackDataset() {
	    	
	    	try {
	    		slackCollection.addSeries(slackSeries);
	    	}
	    	catch(Exception exp) {
	    		
	    	}
	    	return slackCollection;
	    	
	    }
	    
	    private XYSeriesCollection createRewardDataset() {
	    	
	    	try {
	    		rewardCollection.addSeries(Reward);
	    	}
	    	catch(Exception exp) {
	    		
	    	}
	    	return rewardCollection;
	    	
	    }
	    
	    private JFreeChart createLoadChart(final IntervalXYDataset dataset) {
	    	
	        loadChart = ChartFactory.createHistogram(
	                "Load",      // chart title
	                "Time",                      // x axis label
	                "Load",                      // y axis label
	                dataset,                  // data
	                PlotOrientation.VERTICAL,
	                true,                     // include legend
	                true,                     // tooltips
	                false                     // urls
	        );

			// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
			loadChart.setBackgroundPaint(Color.white);

			loadChart.setBackgroundPaint(new Color(230,230,230));
	                 XYPlot xyplot = (XYPlot)loadChart.getPlot();
	                 xyplot.setForegroundAlpha(0.7F);
	                 xyplot.setBackgroundPaint(Color.WHITE);
	                 xyplot.setDomainGridlinePaint(new Color(150,150,150));
	                 xyplot.setRangeGridlinePaint(new Color(150,150,150));
	                 XYBarRenderer xybarrenderer = (XYBarRenderer)xyplot.getRenderer();
	                 NumberAxis domain = (NumberAxis) xyplot.getDomainAxis();
	                 domain.setRange(0.00, domainV);
	                 xybarrenderer.setShadowVisible(false);
	                 xybarrenderer.setBarPainter(new StandardXYBarPainter()); 
			return loadChart;
		}
	    
	    private JFreeChart createSlackChart(final IntervalXYDataset dataset) {
	    	
	        slackChart = ChartFactory.createHistogram(
	                "Slack",      // chart title
	                "Time",                      // x axis label
	                "Slack",                      // y axis label
	                dataset,                  // data
	                PlotOrientation.VERTICAL,
	                true,                     // include legend
	                true,                     // tooltips
	                false                     // urls
	        );

			// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
			slackChart.setBackgroundPaint(Color.white);

			slackChart.setBackgroundPaint(new Color(230,230,230));
	                 XYPlot xyplot = (XYPlot)slackChart.getPlot();
	                 xyplot.setForegroundAlpha(0.7F);
	                 xyplot.setBackgroundPaint(Color.WHITE);
	                 xyplot.setDomainGridlinePaint(new Color(150,150,150));
	                 xyplot.setRangeGridlinePaint(new Color(150,150,150));
	                 XYBarRenderer xybarrenderer = (XYBarRenderer)xyplot.getRenderer();
	                 NumberAxis domain = (NumberAxis) xyplot.getDomainAxis();
	                 domain.setRange(0.00, domainV);
	                 xybarrenderer.setShadowVisible(false);
	                 xybarrenderer.setBarPainter(new StandardXYBarPainter()); 
//	    xybarrenderer.setDrawBarOutline(false);
			return slackChart;
		}

	    private JFreeChart createRewardChart(final IntervalXYDataset dataset) {
	
	    rewardChart = ChartFactory.createHistogram(
	            "Reward",      // chart title
	            "Time",                      // x axis label
	            "Reward",                      // y axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	    );
	
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
				 rewardChart.setBackgroundPaint(Color.white);
				 rewardChart.setBackgroundPaint(new Color(230,230,230));
	             XYPlot xyplot = (XYPlot)rewardChart.getPlot();
	             xyplot.setForegroundAlpha(0.7F);
	             xyplot.setBackgroundPaint(Color.WHITE);
	             xyplot.setDomainGridlinePaint(new Color(150,150,150));
	             xyplot.setRangeGridlinePaint(new Color(150,150,150));
	             XYBarRenderer xybarrenderer = (XYBarRenderer)xyplot.getRenderer();
	             NumberAxis domain = (NumberAxis) xyplot.getDomainAxis();
                 domain.setRange(0.00, domainV);
	             xybarrenderer.setShadowVisible(false);
	             xybarrenderer.setBarPainter(new StandardXYBarPainter()); 
		return rewardChart;
	}



	}





