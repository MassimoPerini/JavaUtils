import java.util.Arrays;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * 
 */

/**
 * @author Massimo
 *
 */
public class Control {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int ripetizioni=Integer.parseInt(JOptionPane.showInputDialog("Inserire il numero di ripetizioni"));
		
		double[] varianzeCampionarie=new double [ripetizioni];
		
		for (int j=0;j<ripetizioni;j++)
		{
		
			Random r=new Random ();
			int nDaEstrarre=200;
			
			double [] determinazioni=new double [nDaEstrarre];
			
			for (int i=0;i<nDaEstrarre;i++)
			{
				determinazioni[i]=generaNCas (r);
			}
			
			double mCampionaria=calcolaMediaCampionaria (determinazioni);
			double varianzaCampionaria=calcolaVarianzaCampionaria(mCampionaria, determinazioni);
			varianzeCampionarie[j]=varianzaCampionaria;
		}
		
		Arrays.sort(varianzeCampionarie);
		
		double scartoCampione=0.002;
		
		for (int i=0;i<varianzeCampionarie.length;i++)
		{
			
		}
		
	}
	
	public static double calcolaVarianzaCampionaria (double mediaCampionaria, double [] determinazioni)
	{
		double result=0;
		
		for (int i=0;i<determinazioni.length;i++)
		{
			result+=Math.pow((determinazioni[i]-mediaCampionaria),2);
		}
		result/=(double)determinazioni.length-1;
		return result;
	}
	
	public static double calcolaMediaCampionaria (double [] determinazioni)
	{
		double result=0;
		
		for (int i=0;i<determinazioni.length;i++)
		{
			result+=determinazioni[i];
		}
		result/=determinazioni.length;
		return result;
	}
	
	public static double generaNCas (Random r)
	{
		return r.nextDouble();
	}

}
