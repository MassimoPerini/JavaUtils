package limiteCentrale;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;

public class Control {
	
	/*
	 * Si generano n numeri random r 0 1 e si esegue la loro somma. Studiare l'andamento della funzione di ripartizione della somma
		e verificare il teorema del limite centrale, ossia la convergenza in distribuzione ad una normale n/2, n/12
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int n=Integer.parseInt(JOptionPane.showInputDialog("Inserire quanti numeri estrarre"));
		int rip=Integer.parseInt(JOptionPane.showInputDialog("Inserire il numero di ripetizioni"));
		ArrayList <Double> ripetizioni=new ArrayList<Double> ();
		ArrayList <Double> frequenzeCumulate=new ArrayList<Double> ();
		Random r=new Random();
		double somma=0;
		double media=(double)n/2;
		double sqm=(double)Math.sqrt(((double)((double)1/(double)12))*(double)n);
		
		for (int i=0;i<rip;i++)
		{
			for (int j=0;j<n;j++)
				somma+=generaNumero(r);
			ripetizioni.add(somma);
			somma=0;
		}
		
		//standardizzazione
		
		for (int i=0;i<rip;i++){
			ripetizioni.set(i, (ripetizioni.get(i)-media)/sqm);
		/*	if (ripetizioni[i]>-0.0000000000000000001 && ripetizioni[i]<0.0000000000000000001)
			{
				ripetizioni[i]=0;
			}*/
		}
		//ordino e la p è nElemento/elementiTotali
		Collections.sort(ripetizioni);
		double prob=0;
		
//		if (ripetizioni.get(0)!=ripetizioni.get(1))
//			ripetizioni.remove(0);
		
		for (int i=0;i<ripetizioni.size();i++)
		{
				frequenzeCumulate.add(prob/(double)ripetizioni.size());
				prob++;
		}

		XYSeries series = new XYSeries("Valori estratti");
		
		for (int i=0;i<ripetizioni.size();i++){
			System.out.println(ripetizioni.get(i)+" "+frequenzeCumulate.get(i));
			series.add(ripetizioni.get(i),frequenzeCumulate.get(i));
		}
		
		
		Graph graph=new Graph(new XYSeriesCollection(series));
        RefineryUtilities.centerFrameOnScreen(graph);
        graph.setVisible(true);
		
	}
	
	/*
	 *     private ChartPanel createDemoPanel() {
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
            title, "X", "Y", createSampleData(),
            PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setVerticalTickLabels(true);
        return new ChartPanel(jfreechart);
    }

    private XYDataset createSampleData() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries("Random");
        for (int i = 0; i < N * N; i++) {
            double x = rand.nextGaussian();
            double y = rand.nextGaussian();
            series.add(x, y);
        }
        xySeriesCollection.addSeries(series);
        xySeriesCollection.addSeries(added);
        return xySeriesCollection;
    }

	 */
	
	public static double generaNumero (Random r)
	{
		return r.nextDouble();
	}

}

class Graph extends JFrame
{
	XYSeriesCollection values;
	XYPlot plot;

	public Graph (XYSeriesCollection values)
	{
//		super("");
		this.values=values;
		this.plot=creaNormale();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.createGraph();
		this.setBounds(0, 0, 1000, 1000);
	}
	
	public Graph ()
	{
//		super("");
		this.plot=creaNormale();
		values=new XYSeriesCollection();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	


	
	private XYPlot creaNormale ()
	{
		X2.setPreviousValue(-4.0);
        XYDataset dataset = DatasetUtilities.sampleFunction2D(new X2(), 
                -4.0, 4.0, 100, "fun. ripart. normale standardizzata");

        JFreeChart normale = ChartFactory.createXYLineChart(
                "Function2DDemo1 ",       // chart title
                "X",                      // x axis label
                "Y",                      // y axis label
                dataset,                  // data
                PlotOrientation.VERTICAL,  
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
         );
                
        XYPlot plot = (XYPlot) normale.getPlot();
        plot.getDomainAxis().setLowerMargin(0.0);
        plot.getDomainAxis().setUpperMargin(0.0);
        
        return plot;
	}
	
	private void createGraph()
	{
		MyXYStepRenderer steps=new MyXYStepRenderer ();
		
		steps.setDrawSeriesLineAsPath(false);
		steps.setUseFillPaint(false);
		
		plot.setDataset(1, values);
		plot.setRenderer(1, steps);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.setDomainAxis(new NumberAxis());
        JFreeChart chart=new JFreeChart (plot);
        ChartPanel chartPanel = new ChartPanel(chart);
    
        
	/*	JFreeChart chart = ChartFactory.createXYStepChart("Values","X", "Y", values, PlotOrientation.VERTICAL,true,true,false );
  //      ChartPanel chartPanel = new ChartPanel(chart);
        chart.getXYPlot().setDomainAxis(new NumberAxis());
        
        //Normale
        
        chart.getXYPlot().setRenderer(1,creaNormale());
      */  
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        this.add(chartPanel);

	}
	
	public void setPointValues (XYSeriesCollection values)
	{
		this.values=values;
	}
	
    static class X2 implements Function2D {

        /* (non-Javadoc)
         * @see org.jfree.data.function.Function2D#getValue(double)
         */
    	static double previousValue=-4;
    	static double actualValue=0;
    	static double result=0;
    	
        public double getValue(double x) {
        	//previousValue+=(1/Math.sqrt(2*Math.PI))*Math.pow(Math.E, -(Math.pow(x, 2))*(0.5));
        	double differenza=x-previousValue;
        	previousValue=x;
        	
        	result+=((1/Math.sqrt(2*Math.PI))*Math.pow(Math.E, -(Math.pow(x, 2))*(0.5))*0.0807);
        	System.out.println(result);
            return result;
        }

		public static double getPreviousValue() {
			return previousValue;
		}

		public static void setPreviousValue(double previousValue) {
			X2.previousValue = previousValue;
		}

		public static double getActualValue() {
			return actualValue;
		}

		public static void setActualValue(double actualValue) {
			X2.actualValue = actualValue;
		}
        
        
        
    }
    
}

class MyXYStepRenderer extends XYStepRenderer
{
    public MyXYStepRenderer() {
        super(null, null);
    }

    
    public MyXYStepRenderer(XYToolTipGenerator toolTipGenerator, XYURLGenerator urlGenerator) {
        super();
        setBaseToolTipGenerator(toolTipGenerator);
        setURLGenerator(urlGenerator);
        setBaseShapesVisible(false);
    }
    
    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state,
            Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
            ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
            int series, int item, CrosshairState crosshairState, int pass) {

        // do nothing if item is not visible
        if (!getItemVisible(series, item)) {
            return;
        }

        PlotOrientation orientation = plot.getOrientation();

        Paint seriesPaint = getItemPaint(series, item);
        Stroke seriesStroke = getItemStroke(series, item);
        g2.setPaint(seriesPaint);
        g2.setStroke(seriesStroke);

        // get the data point...
        double x1 = dataset.getXValue(series, item);
        double y1 = dataset.getYValue(series, item);

        RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
        double transX1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
        double transY1 = (Double.isNaN(y1) ? Double.NaN
                : rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation));

        if (pass == 0 && item > 0) {
            // get the previous data point...
            double x0 = dataset.getXValue(series, item - 1);
            double y0 = dataset.getYValue(series, item - 1);
            double transX0 = domainAxis.valueToJava2D(x0, dataArea,
                    xAxisLocation);
            double transY0 = (Double.isNaN(y0) ? Double.NaN
                    : rangeAxis.valueToJava2D(y0, dataArea, yAxisLocation));

            if (orientation == PlotOrientation.HORIZONTAL) {
                if (transY0 == transY1) {
                    // this represents the situation
                    // for drawing a horizontal bar.
                    drawLine(g2, state.workingLine, transY0, transX0, transY1,
                            transX1);
                }
                else {  //this handles the need to perform a 'step'.

                    // calculate the step point
                    double transXs = transX0 + (getStepPoint()
                            * (transX1 - transX0));
                    drawLine(g2, state.workingLine, transY0, transX0, transY0,
                            transXs);
            /*        drawLine(g2, state.workingLine, transY0, transXs, transY1,
                            transXs);*/
                    drawLine(g2, state.workingLine, transY1, transXs, transY1,
                            transX1);
                }
            }
            else if (orientation == PlotOrientation.VERTICAL) {
                if (transY0 == transY1) { // this represents the situation
                                          // for drawing a horizontal bar.
                    drawLine(g2, state.workingLine, transX0, transY0, transX1,
                            transY1);
                }
                else {  //this handles the need to perform a 'step'.
                    // calculate the step point
                    double transXs = transX0 + (getStepPoint()
                            * (transX1 - transX0));
                    drawLine(g2, state.workingLine, transX0, transY0, transXs,
                            transY0);
                /*    drawLine(g2, state.workingLine, transXs, transY0, transXs,
                            transY1);*/
                    
                /*    Ellipse2D.Double circle = new Ellipse2D.Double(transXs, transY1+0.5, 2, 2);
                    g2.fill(circle);
                    g2.draw(circle);*/
                    
                    drawLine(g2, state.workingLine, transXs, transY1, transX1,
                            transY1);
                }
            }

            // submit this data item as a candidate for the crosshair point
            int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
            int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
            updateCrosshairValues(crosshairState, x1, y1, domainAxisIndex,
                    rangeAxisIndex, transX1, transY1, orientation);

            // collect entity and tool tip information...
            EntityCollection entities = state.getEntityCollection();
            if (entities != null) {
                addEntity(entities, null, dataset, series, item, transX1,
                        transY1);
            }

        }

        if (pass == 1) {
            // draw the item label if there is one...
            if (isItemLabelVisible(series, item)) {
                double xx = transX1;
                double yy = transY1;
                if (orientation == PlotOrientation.HORIZONTAL) {
                    xx = transY1;
                    yy = transX1;
                }
                drawItemLabel(g2, orientation, dataset, series, item, xx, yy,
                        (y1 < 0.0));
            }
        }
    }
    
    private void drawLine(Graphics2D g2, Line2D line, double x0, double y0,
            double x1, double y1) {
        if (Double.isNaN(x0) || Double.isNaN(x1) || Double.isNaN(y0)
                || Double.isNaN(y1)) {
            return;
        }
        line.setLine(x0, y0, x1, y1);
        g2.draw(line);
    }
    
}