package graphs;

	
	import java.awt.Color;

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

	/**
	 *
	 * @author abimbolaakinmeji
	 */
	
	@SuppressWarnings("serial")
	public class MethodStartEndTime extends javax.swing.JPanel {
	    ChartPanel chartPanel;

	    /**
	     * Creates new form NewJPanel
	     */
	    public MethodStartEndTime() {
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
	        final XYDataset dataset = createDataset();
	        final JFreeChart chart = ChartFactory.createXYLineChart(
	                "Line Chart Demo 6",      // chart title
	                "X",                      // x axis label
	                "Y",                      // y axis label
	                dataset,                  // data
	                PlotOrientation.VERTICAL,
	                false,                     // include legend
	                true,                     // tooltips
	                false                     // urls
	            );
	        chartPanel = new ChartPanel(chart);
	        chartPanel.setSize(400, 800);
	       // chartPanel.setLocation(40, 600);
	        this.add(chartPanel);
	    }
	    
	  private XYDataset createDataset() {
	        final XYSeriesCollection dataset = new XYSeriesCollection();
		  GenerateLoad gl = new GenerateLoad();
	    	
		  for (int i = 1; i <= 20; i++)
	    		gl.generateNextTimeStep((int)(300*Generate.Normal(10, 3, i)));
			gl.generateNextTimeStep(0);
		  
			for (int i = 0; i < gl.generated.size(); i++) {
				XYSeries series = new XYSeries("Method #" + (i+1));
				series.add(gl.generated.get(i).arivalTime, i+1);
				series.add(gl.generated.get(i).deadline, i+1);
				dataset.addSeries(series);
			}
			
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




