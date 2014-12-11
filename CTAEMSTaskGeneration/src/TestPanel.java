
	
	import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import project.Generate;
import project.GenerateLoad;
import project.Method;

	/**
	 *
	 * @author abimbolaakinmeji
	 */
	
	@SuppressWarnings("serial")
	public class TestPanel extends javax.swing.JPanel {
	    ChartPanel chartPanel;

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

	        setLayout(new java.awt.GridBagLayout());
	    }                     


	                  

	  public final void createGraph() {
	        final IntervalXYDataset dataset = createDataset();
	        final JFreeChart chart = createChart(dataset);
	        chartPanel = new ChartPanel(chart);
	        chartPanel.setSize(400, 400);
	       // chartPanel.setLocation(40, 600);
	        this.add(chartPanel);
	    }
	    
	    private HistogramDataset createDataset() {
	    	GenerateLoad gl = new GenerateLoad();
	    	
	    	int[] loadDist = new int[100];
	    	for (int i = 0; i < loadDist.length; i++)
	    		loadDist[i] = (int)(300000*Generate.Normal(50, 30, i));
	    		//loadDist[i] = (int)(13000*Generate.Poisson(14, i));
	    	

	    	ArrayList<Double> stuff = new ArrayList<Double>();
			
			for(int i = 0; i < loadDist.length; i++) {
				for (int j = 0; j < loadDist[i]; j++)
					stuff.add((double) i);
			}
			
			double[] ret = new double[stuff.size()];
			for (int i = 0; i < ret.length; i++)
				ret[i] = stuff.get(i);
	    	
	    	gl.generate(loadDist);
	    	
	    	
	    	/*
	    	int value = 0;
	    	for (int i = 1; i <= 20; i++)
	    		gl.generateNextTimeStep((int)(300*Generate.Normal(10, 3, i)));
	    		//gl.generateNextTimeStep((int)(300*Generate.Laplace(10, 3, i)));
	    		//gl.generateNextTimeStep((int)(300*Generate.Exponential(1, 20, i)));
	    		//gl.generateNextTimeStep((int)(700 * Generate.Poisson(4, i)));
	    		//gl.generateNextTimeStep((value += (int)(700 * Generate.Poisson(10, i))));
	    	gl.generateNextTimeStep(0);*/
	    	
	       HistogramDataset dataset = new HistogramDataset();
	       dataset.addSeries("Load", ret,loadDist.length+1,0,loadDist.length);
	       dataset.addSeries("Actual Load", gl.loadhistogram(),loadDist.length+1,0,loadDist.length);
	       //dataset.addSeries("Open Time", gl.opentimehistogram(), 10,1,10);
	       //dataset.addSeries("Time Pressure", gl.timePressurehist(), 10,0,10);
	       //dataset.addSeries("Time", gl.timeHist, 10,1,10);
	       return dataset;
	        
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





