import javax.swing.JFrame;

//import limiteCentrale.Graph.X2;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.lsmp.djep.sjep.PNodeI;
import org.lsmp.djep.sjep.PolynomialCreator;
import org.lsmp.djep.xjep.XJep;
import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

class DrawFunction extends JFrame
{
	public DrawFunction (XYSeriesCollection values)
	{
//		super("");
/*		this.values=values;
		this.plot=creaNormale();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.createGraph();
		this.setBounds(0, 0, 1000, 1000);*/
	}
	
	public DrawFunction ()
	{
//		super("");
/*		this.plot=creaNormale();
		values=new XYSeriesCollection();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/

	}
	
	
	
	private XYPlot disegnaFunzione (double margineInf, double margineSup, String funzione)
	{
		X2.setFunzione(funzione);
        XYDataset dataset = DatasetUtilities.sampleFunction2D(new X2(), 
                margineInf, margineSup, 100, "");

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
        plot.getDomainAxis().setLowerMargin(-50.0);
        plot.getDomainAxis().setUpperMargin(50.0);
        
        return plot;
	}
	
	static class X2 implements Function2D {

	    /* (non-Javadoc)
	     * @see org.jfree.data.function.Function2D#getValue(double)
	     */
		
		static String funzione="";
		
	    public double getValue(double x) {
	    	//parserizzare la funzione
	    	return 0;
	    }

		public static String getFunzione() {
			return funzione;
		}

		public static void setFunzione(String funzione) {
//			this.funzione = funzione;
		}
	    
	    
	}
}


public class Control {

	private static JEP parser=new JEP();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//TODO: GUI
		parser = new JEP();
		parser.addStandardFunctions();
		parser.addStandardConstants();
		parser.setImplicitMul(true);
		parser.setAllowUndeclared(true);
	//	System.out.println(""+bisezione("(sin(x))+(cos(x))", -1, 1, 0.0001));
		System.out.println(+bisezione("(e^x)-2", -1, 1, 0.000001));
		System.out.println(secante("(e^x)-2", -1, 3, 0.000001));
	/*	try {
			System.out.println("RISULTATO: "+tangente("(3*(x^2))-2", 0.5, 3, 0.000001));	//e=exp(1)
		} catch (MatlabConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MatlabInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try
		{
			System.out.println(puntoUnito("((x^3)/2)", 0.5, 0.0000001));
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Il metodo diverge");
		}
	}
	
	
	public static double puntoUnito (String funzione, double inizio, double precisione) throws IllegalArgumentException
	{
		double errore = Double.POSITIVE_INFINITY;
		double errorePrec=Double.POSITIVE_INFINITY;
		double valore=inizio;
		double valPrec=0;
		while (errore>precisione)
		{
			if (errore>errorePrec)
				throw new IllegalArgumentException();
			parser.addVariable("x", valore);
			parser.parseExpression(funzione);
			valPrec=valore;
			valore=parser.getValue();
			errorePrec=errore;
			errore=Math.abs(valore-valPrec);
			if (errore==errorePrec)
				return valore;
		}
		return valore;
	}
	
	public static double tangente (String funzione, double inizio, double fine, double precisione) throws MatlabConnectionException, MatlabInvocationException
	{
		//Prendo la derivata della funzione
		MatlabProxyFactory factory = new MatlabProxyFactory();
		
		String funzioneMatlab=funzione;
		funzioneMatlab=funzioneMatlab.replace("^", ".^");
		MatlabProxy proxy = factory.getProxy();
//		proxy.eval("syms x f");	
//		proxy.eval("f="+funzione);			//inserire le *
//		proxy.eval("d=diff(f)");
//		Object derivata = proxy.getVariable("d");
		System.out.println("PRENDO VALORI");
		proxy.eval("[d,err]=derivest(@(x) "+ funzioneMatlab+", "+inizio+");");
		double dInizio = ((double[]) proxy.getVariable("d"))[0];

		proxy.eval("[d1,err1]=derivest(@(x) "+ funzioneMatlab+", "+fine+");");
		double dFine = ((double[]) proxy.getVariable("d1"))[0];

		
		parser.addVariable("x", inizio);
		parser.parseExpression(funzione);
		double yInizio=parser.getValue();
		
		parser.addVariable("x", fine);
		parser.parseExpression(funzione);
		double yFine=parser.getValue();
		
		double nuovoPunto=0;
		double errore;
		
		if (yInizio*dInizio>0)
		{
			nuovoPunto=inizio-(yInizio/dInizio);
			errore=Math.abs(inizio-nuovoPunto);
		}
		else
		{
			nuovoPunto=fine-(yFine/dFine);
			errore=Math.abs(fine-nuovoPunto);
		}
		double yNuovoPunto;
		double dNuovoPunto;
		
		while (errore>precisione)
		{
			proxy.eval("[d,err]=derivest(@(x) "+ funzioneMatlab+", "+nuovoPunto+")");
			dNuovoPunto = ((double[]) proxy.getVariable("d"))[0];
			
			parser.addVariable("x", nuovoPunto);
			parser.parseExpression(funzione);
			yNuovoPunto=parser.getValue();
			
			nuovoPunto=nuovoPunto-(yNuovoPunto/dNuovoPunto);
		}
		
		return nuovoPunto;
	}
	
	
	public static double bisezione (String funzione, double inizio, double fine, double precisione) throws IllegalArgumentException
	{
		parser.addVariable("x", inizio);
		parser.parseExpression(funzione);
		double yInizio = parser.getValue();
		if (yInizio==0)
			return inizio;
		parser.addVariable("x", fine);
		parser.parseExpression(funzione);
		double yFine=parser.getValue();
		if (yFine==0)
			return fine;
		if (yInizio*yFine>0)
			throw new IllegalArgumentException();
		
		//TODO derivata
		
		while ((fine-inizio)>precisione)
		{
		//	System.out.println(fine);
		//	System.out.println(inizio);
			parser.addVariable("x", (inizio+fine)/2);
			parser.parseExpression(funzione);
			
			double result = parser.getValue();
			
			if (yInizio*result>0)				//stesso segno
			{
				inizio=(inizio+fine)/2;
				
			}
			else
				fine=(inizio+fine)/2;
		}
		
		return (fine+inizio)/2;


	}
	
	public static double secante (String funzione, double inizio, double fine, double precisione)
	{
		parser.addVariable("x", inizio);
		parser.parseExpression(funzione);
		double yInizio = parser.getValue();
		if (yInizio==0)
			return inizio;
		parser.addVariable("x", fine);
		parser.parseExpression(funzione);
		double yFine=parser.getValue();
		if (yFine==0)
			return fine;
		if (yInizio*yFine>0)
			throw new IllegalArgumentException();
		
		
		String retta="(((x2-x1)*(-1*y1))/(y2-y1))+x1";
		while (fine-inizio>precisione)
		{

			parser.addVariable("x1", inizio);
			parser.addVariable("x2", fine);
			parser.addVariable("y1", yInizio);
			parser.addVariable("y2", yFine);
	        
			parser.parseExpression(retta);
			double ascissaX=parser.getValue();
			
			parser.parseExpression(funzione);
			parser.addVariable("x", ascissaX);
			double valoreFunzione=parser.getValue();
			
			//Calcolo la derivata
			parser.addVariable("x", ascissaX+0.000000001);
			parser.parseExpression(funzione);
			double r1=parser.getValue();
			
			parser.addVariable("x", ascissaX);
			double r2=parser.getValue();
			
			double derivata=r2-r1 / (2*0.000000001);
			
			if (valoreFunzione*derivata<0)
			{
				inizio=ascissaX;
				yInizio=r2;
			}
			else
			{
				fine=ascissaX;
				yFine=r2;
			}
		}
		return (fine+inizio)/2;
		
		
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
		XYStepRenderer steps=new XYStepRenderer ();
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
