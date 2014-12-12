import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;


public class DebugPanel extends javax.swing.JPanel {

    double[] hist1;double[] hist2; String n1; String n2;
    int bins, min, max;
    
    ChartPanel chartPanel;
    

    public DebugPanel(String n1, double[] hist1, String n2,double[] hist2, int bins, int min, int max) {
    	this.hist1 = hist1;
        this.hist2 = hist2;
        this.n1 = n1;
        this.n2 = n2;
        this.bins = bins;
        this.min = min;
        this.max = max;
        simpleGraph();
    }
    

	public final void simpleGraph() {
        IntervalXYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setSize(400, 400);	        
        this.add(chartPanel);
	}

    private HistogramDataset createDataset() {
       HistogramDataset dataset = new HistogramDataset();
       dataset.addSeries(n1, hist1, bins, min, max);
       dataset.addSeries(n2, hist2, bins, min, max);
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
//    xybarrenderer.setDrawBarOutline(false);
		return chart;
	}
}
