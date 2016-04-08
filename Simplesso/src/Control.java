import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.lsmp.djep.sjep.PNodeI;
import org.lsmp.djep.sjep.PolynomialCreator;
import org.lsmp.djep.xjep.XJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

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
		
		
		Gui g=new Gui ();
		//First check
		
	/*	boolean ok=true;
		
		do
		{
			int posUguale=funzioneObiettivo.indexOf("=");
			int posZ=funzioneObiettivo.indexOf("z");
			if (posUguale==-1 || posZ==-1)
			{
				funzioneObiettivo=JOptionPane.showInputDialog("Inserire funzione obiettivo con z=...");
				ok=false;
			}
			
			else
				ok=true;
		}
		while (!ok);
		
		String vincolo="v";
		
		int i=1;
		
		LinkedList <String> vincoli=new LinkedList <String> ();
		LinkedList <String> variables=new LinkedList <String> ();
		
		int indiceS=1;
		int indiceW=1;
		int indiceU=1;
		
		String strS="";
		String strW="";
		String strU="";
		
		while (!vincolo.equals(""))
		{

			
			vincolo=JOptionPane.showInputDialog("Inserisci "+i+" vincolo o niente per procedere alla prossima fase");
			if (vincolo=="" || vincolo==null)
				break;
			else
			{
				if (vincolo.indexOf(">=")!=-1)
				{
					String toReplace="-S*"+indiceS+"+W*"+indiceW+"=";
					strS+="+0*S"+indiceS;
					strW+="-M*W"+indiceW;
					vincolo=vincolo.replace(">=", toReplace);
					indiceS++;
					indiceW++;
				}
				else if (vincolo.indexOf("<=")!=-1)
				{
					strU+="+0*U"+indiceU;
					String toReplace="+U"+indiceU+"=";
					vincolo=vincolo.replace("<=", toReplace);
					indiceU++;
				}
				else if (vincolo.indexOf("=")!=-1)
				{
					strW+="-M*W"+indiceW;
					String toReplace="+W"+indiceW+"=";
					vincolo=vincolo.replace("=", toReplace);
					indiceW++;
				}
				else
					continue;
			}
			i++;
			vincoli.add(vincolo);
				
		}
		
		LinkedList <String> funzObiett=parse (funzioneObiettivo, strU+strS+strW, false);
		
		System.out.println(funzObiett.toString()+vincoli.size());
		
		for (int j=0;j<vincoli.size();j++)
		{
			System.out.println(parse(vincoli.get(j), "", true).toString());
		}
	}
	
	
	
	public static LinkedList <String> parse (String function, String toAdd, boolean number)
	{
		boolean invert=false;
		
		String functionNoCoeff=function.replaceAll("[0-9]", "");
		functionNoCoeff=functionNoCoeff.replace("*", "");
		functionNoCoeff=functionNoCoeff.replace("/", "");
		
		if (!number)
		{
			int posZ=functionNoCoeff.indexOf("z");
			if (posZ>0)
				if (functionNoCoeff.charAt(posZ-1)=='-')
					invert=true;
				
		}
		
		if (invert)
		{
			String functionBck=function.replace("+", "|");
			functionBck=functionBck.replace("-", "+");
			functionBck=functionBck.replace("|", "-");
			function=functionBck;
		}

		if (!number)
		{
			int posZ=function.indexOf("z=");
			if (posZ!=-1)
			{
				String prev="";
				if (posZ>1)
				{
					prev=function.substring(0, posZ-1);
					prev="+"+prev;
					prev=prev.replace("+", "|");
					prev=prev.replace("-", "+");
					prev=prev.replace("|", "-");
					prev+="+";
					System.out.println(prev);
				}
				function=function.replace(function.substring(0, posZ+2), prev);
				System.out.println(function);
			}
			else
			{
				posZ=function.indexOf("=z");
				if (posZ!=-1)
				{
					String prev="";
					if (posZ<function.length()-2)
					{
						prev=function.substring(posZ+2, function.length());
						prev=prev.replace("+", "|");
						prev=prev.replace("-", "+");
						prev=prev.replace("|", "-");
					}
						function=function.replace(function.substring(posZ, function.length()), prev);
						System.out.println(function);
				}
			}

		}
		
        XJep parser = new XJep();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.addComplex();
        parser.setAllowUndeclared(true);
        parser.setImplicitMul(true);
        parser.setAllowAssignment(true);
        
        String result="";
        
        System.out.println("entra "+function);
        
	try
	{
        Node node = parser.parse(function);
	    Node processed = parser.preprocess(node);
	    Node simp = parser.simplify(processed);
	    parser.println(simp);
	    
	    PolynomialCreator pc = new PolynomialCreator(parser);
	    Node simplified = pc.simplify(node);
	    Node expand = pc.expand(node);
	    PNodeI poly = pc.createPoly(node);
	    parser.println(simplified);
	    result=parser.toString(simplified);
	//    System.out.println(poly.toString());
	}
	catch (ParseException e) 
	{
		System.out.println("Parse exception");
	}
	catch (Exception e) 
	{
	}
	
	System.out.println(result);
	
	LinkedList <String> listaFine=new LinkedList <String> ();
	StringTokenizer st=new StringTokenizer (result, "+");
	
	while (st.hasMoreTokens())
	{
		String token=st.nextToken();
		int posX=token.indexOf("*");
		if (posX==-1)
			listaFine.add("1");
		else
		{
			String coeff=token.substring(0, posX);
			listaFine.add(coeff);
		}
	}

	
	return listaFine; */
		
	}
	
	public static void getInfo ( double [] coeffFunObiett, LinkedList <double []> coeffVincoli, String [] segni, double [] results)
	{
		int nColonne=coeffVincoli.get(0).length;
		int nRighe=coeffVincoli.size();
		double [][] vincoli=new double [nRighe][nColonne];
		
		for (int i=0;i<nRighe;i++)
		{
			double [] riga=coeffVincoli.get(i);
			
			for (int k=0;k<nColonne;k++)
			{
				vincoli[i][k]=riga[k];
			}
		}
		Model simplesso=new Model (coeffFunObiett, vincoli, segni, results);
		simplesso.start();
	}
	
	
	
}
