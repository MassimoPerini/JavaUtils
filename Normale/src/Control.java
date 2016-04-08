import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.demo.BarChartDemo1;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/*
 * Utilizzando i metodi di integrazione numerica studiati, tabulare la funzione che descrive le aree sottese alla normale standardizzata da 0 a z,
 * con z<=4. Utilizzando poi il metodo Monte Carlo e considerando un particolare valore di z, determinare l'area sottesa alla funzione di densita' da 0 a z
 * e studiare la distribuzione dei valori ottenuti per tale area al variare del numero n di punti utilizzati per la simulazione. Nell'ipotesi di normalita' di
 * questa distribuzione, valutare le frequenze osservate e le frequenze teoriche per stabilire se si ritiene accettabile l'ipotesi di un adattamento della normale
 * alla distribuzione delle aree.
 */

class Gui
{
	JFrame f;
	public Gui(final double [] tabella, double incremento)
	{
		f=new JFrame();
		
		f.setLayout(new BorderLayout ());
		f.setSize(500, 1000);
		 f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String [][] pTabella=new String [tabella.length][2];
		final DecimalFormat twoDForm = new DecimalFormat("#.##");

		for (int i=0;i<tabella.length;i++){
			pTabella[i][0]=String.valueOf(twoDForm.format((incremento*i)));
			pTabella[i][1]=String.valueOf(tabella[i]);
		}
		String [] intestazione={"z", "P (z) "};
		
	    JTable table = new JTable(pTabella, intestazione);
	    JScrollPane scrollpane = new JScrollPane(table);
	    f.add(scrollpane, BorderLayout.CENTER);
	    JButton btnMonteCarlo = new JButton ("Monte carlo");
	    JPanel tools=new JPanel ();
	    tools.setLayout(new BorderLayout ());
	    tools.add(new JLabel ("z: "), BorderLayout.WEST);
	    final JTextField txtValZ=new JTextField (2);
	    tools.add(txtValZ, BorderLayout.CENTER);
	    btnMonteCarlo.addActionListener(new ActionListener ()
	    {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				double ascissaMonteCarlo=Double.parseDouble(txtValZ.getText());
				
				String [] intestazione={"Numero punti", "Area sottesa"};
				String[][] tabella2=new String [16][2];
				double aree[][] = new double [8][2000];
				
				for (int i=0;i<8;i++)
				{
					for (int j=0;j<2000;j++){				//prove per ciascun punto
						
						aree[i][j]=Control.probAreaSottesa (ascissaMonteCarlo, (int) Math.pow(4, i+2))*((1/Math.sqrt(2*Math.PI))*ascissaMonteCarlo);
					}
				}
				double nClassi=16;
				double ampiezzaClassi=0.01;
				double [] freq=new double [16];
				
				double classeInf=tabella[(int) (ascissaMonteCarlo*100)]-((nClassi/2)*ampiezzaClassi);
				
				JTabbedPane tabbedPane = new JTabbedPane();
				JTabbedPane tabbedPaneString = new JTabbedPane();

				
				for (int i=0;i<8;i++)			//8
				{
					for (int j=0;j<2000;j++)
					{									//Divido in classi
						System.out.println("classe: "+i+" area: "+aree[i][j]);
						aree[i][j]-=classeInf;
						if (aree[i][j]<0)
							aree[i][j]=0;
						else if (aree[i][j]>(nClassi-2)*ampiezzaClassi)
							aree[i][j]=nClassi*ampiezzaClassi;
						aree[i][j]*=((double)1/ampiezzaClassi);
						freq [(int) aree[i][j]]++;
					}
					for (int j=0;j<nClassi;j++)
					{
						if (j==0)
							tabella2[j][0]="<"+twoDForm.format(classeInf+ampiezzaClassi);
						else if (j==nClassi-1)
							tabella2[j][0]=">"+twoDForm.format(((nClassi-2)*ampiezzaClassi)+classeInf+ampiezzaClassi);
						else
							tabella2[j][0]=twoDForm.format((classeInf+((j-1)*ampiezzaClassi)+ampiezzaClassi))+" -| "+twoDForm.format(classeInf+(j*ampiezzaClassi)+ampiezzaClassi);
						tabella2[j][1]=String.valueOf(freq[j]);
					}
				
					ChartPanel chart = creaGrafico("Vertical Bar Chart Demo", tabella2);
					
					JTable table = new JTable(tabella2, intestazione);
				    JScrollPane scrollpane = new JScrollPane(table);
			//	    panel2.add(scrollpane, BorderLayout.CENTER);
					tabbedPaneString.addTab(String.valueOf(i), scrollpane);

					JComponent panel1 = chart;
					tabbedPane.addTab(String.valueOf(i), panel1);
					tabella2=new String [16][2];
					freq=new double [16];
			//		chart.setVisible(true);
				}
				aree=null;
				
				JFrame grafico=new JFrame ("Adattamento");
				tabbedPane.setVisible(true);
				grafico.add(tabbedPane);
				grafico.setSize(900, 900);
				grafico.setVisible(true);
				
				
				JFrame f2=new JFrame ();
				f2.setLayout(new BorderLayout ());
				f2.setSize(300, 600);
				f2.add(tabbedPaneString);
				f2.setVisible(true);
				

				f2.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				f2.setVisible(true);
			}
	    	
	    });
	    tools.add(btnMonteCarlo, BorderLayout.EAST);
	    f.add(tools, BorderLayout.SOUTH);
	    f.setVisible(true);
	    System.out.println("fine");
	}
	ChartPanel creaGrafico(String title, String [][] valori) {

	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
       for (int i=0;i<valori.length;i++){
       	dataset.setValue(Double.parseDouble(valori[i][1]), String.valueOf(i), valori[i][0]);
   //    	System.out.println("dataSet "+dataSet[i]);
       }
       System.out.println("\n\n");
       JFreeChart objChart = ChartFactory.createBarChart(
       	       "Demo ",     //Chart title
       	    "x",     //Domain axis label
       	    "y",         //Range axis label
       	    dataset,         //Chart Data 
       	    PlotOrientation.VERTICAL, // orientation
       	    false,             // include legend?
       	    true,             // include tooltips?
       	    false             // include URLs?
       	);

       
		final ChartPanel chartPanel = new ChartPanel(objChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		return chartPanel;
		}
}





public class Control {
	
	public static void main (String [] args)
	{
		//(h/6)*(f(a)+f(c)+4*f(1))
		System.out.println("Start");
		double limiteEstremo=4;
		double incremento=0.01;
		
		double [] prob=calcolaTavola (limiteEstremo, incremento);

		Gui g=new Gui (prob, incremento);
		
		//Monte carlo
		
		
	}
	
	static double probAreaSottesa (double z, int nPuntiSparati)
	{
		
		double valMaxAsc=(double)1/Math.sqrt(2*Math.PI);
		Random r=new Random();
		
		double result=0;
		
		for (int i=0;i<nPuntiSparati;i++)
		{
			double nCasAsc=r.nextDouble()*z;
			double nCasOrd=r.nextDouble()*valMaxAsc;
			
			if (calcolaNormale(nCasAsc)>nCasOrd)
				result++;
			
		}
		result/=nPuntiSparati;
		return result;
	}
	
	static double [] calcolaTavola (double limiteEstremo, double incremento)
	{
		double intervallo=incremento/2;
		double result=0;
		double [] prob=new double [(int) (limiteEstremo/incremento)+1];
		int i=0;
		for (double valoreAscissa=0;valoreAscissa<limiteEstremo;valoreAscissa+=incremento)
		{
			System.out.println("ValoreAscissa "+valoreAscissa);
			double margineMin=calcolaNormale (valoreAscissa);
			double margineMax=calcolaNormale (valoreAscissa+incremento);
			double margineMed=calcolaNormale (valoreAscissa+intervallo);
			
			
			result+=(((double)incremento/(double)6)*(margineMin+margineMax+(4*margineMed)));
			prob[i]=result;
			
			i++;
		}
		System.out.println(result);
		return prob;
	}
	
	static double calcolaNormale (double valore)
	{
//		double base=(double)(1/(double)Math.sqrt((double)2*Math.PI)*Math.E);
	//	double esponente=(double)-1/2*(double)Math.pow(valore, 2);
	//	System.out.println("BASE"+base+"ESPONENTE"+esponente+"  "+(double)Math.pow(base, esponente));
		return (Math.pow(Math.E, ((double)-1/2)*Math.pow(valore, 2)))/Math.sqrt((double)2*Math.PI);		//e^(-(x)^2/(2))/(sqrt(2 π)
	}
	
}
