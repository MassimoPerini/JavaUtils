import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.lsmp.djep.sjep.PNodeI;
import org.lsmp.djep.sjep.PolynomialCreator;
import org.lsmp.djep.xjep.PrintVisitor;
import org.lsmp.djep.xjep.XJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;


public class Model {
	
	private ArrayList <String> coeffFunObiett=new ArrayList <String> ();
	private ArrayList <LinkedList <Double>> coeffVincoli;
	private String [] segni;
	private LinkedList <String> inBase=new LinkedList <String> ();
	private double [] results;
	private double [][] vincoliETermini;
	private double [][] vincoliETerminiOld;
	private ArrayList <String> delta = new ArrayList <String> ();
	
	private LinkedList <String> nomiBase=new LinkedList <String> ();
	
	private int indiceS=0;
	private int indiceW=0;
	private int indiceU=0;
	
	public Model (double [] coeffFunObiett, double [][] coeffVincoli, String [] segni, double [] results)
	{
		
		System.out.println(coeffFunObiett[0]);
		
		for (int i=0;i<coeffFunObiett.length;i++){
			this.coeffFunObiett.add(Double.toString(coeffFunObiett[i]));
		}
			
		int nRighe=coeffVincoli.length;
		int nColonne=coeffVincoli[0].length;
		
		
		ArrayList <LinkedList <Double>> r=new ArrayList <LinkedList <Double>> ();
		
		for (int i=0;i<nRighe;i++)
		{
			LinkedList <Double> l =new LinkedList <Double> ();
			
			for (int k=0;k<nColonne;k++)
				l.add(coeffVincoli[i][k]);
			
			r.add(l);
			
		}
		
		this.coeffVincoli=r;
		
		this.segni=segni;
		this.results=results;
		
		this.costruisciMatrice();				//Mette le u, w e s. Aggiungere il resto
		
		
		//Debug
		for (int i=0;i<inBase.size();i++)
		{
			System.out.println("INBASE "+inBase.get(i));
		}
		
		for (int i=0;i<this.coeffVincoli.size();i++)
		{
			LinkedList<Double> riga=this.coeffVincoli.get(i);
			System.out.println();
			for (int k=0;k<riga.size();k++)
			{
				System.out.print(riga.get(k)+" ");
			}
		}
		
		for (int i=0;i<this.coeffFunObiett.size();i++)
		{
			System.out.println(this.coeffFunObiett.get(i)+" ");
		}
		
		//Fine debug
		
		
	}
	
	private void costruisciMatrice ()
	{
		int l=segni.length;
		
		for (int i=0;i<l;i++)
		{
			
			if (segni[i].equals(">"))
			{
				indiceS++;
				indiceW++;
			}
			else if (segni[i].equals("="))
				indiceW++;
			else if (segni[i].equals("<"))
				indiceU++;
		}
			
		int currS=0;
		int currW=0;
		int currU=0;
		
		
		for (int i=0;i<l;i++)
		{
			LinkedList <Double> riga=coeffVincoli.get(i);
			
			if (segni[i].equals(">"))				//Prima tutte le U, poi le S, poi le W
			{
				inBase.add("-x");
				nomiBase.add("w"+String.valueOf(i+1));
				for (int k=0;k<this.indiceU;k++)
					riga.add((double)0);
				
				for (int k=0;k<currS;k++)
					riga.add((double) 0);
				riga.add((double) -1);
				currS++;
				for (int k=currS;k<this.indiceS;k++)
					riga.add((double) 0);
				
				for (int k=0;k<currW;k++)
					riga.add((double) 0);
				riga.add((double) 1);
				currW++;
				for (int k=currW;k<this.indiceW;k++)
					riga.add((double) 0);
			}
			
			if (segni[i].equals("="))				//Prima tutte le U, poi le S, poi le W
			{
				inBase.add("-x");
				nomiBase.add("w"+String.valueOf(i+1));
				for (int k=0;k<this.indiceU;k++)
					riga.add((double)0);
				
				for (int k=0;k<this.indiceS;k++)
					riga.add((double)0);
				
				for (int k=0;k<currW;k++)
					riga.add((double) 0);
				riga.add((double) 1);
				currW++;
				for (int k=currW;k<this.indiceW;k++)
					riga.add((double) 0);
			}
			
			if (segni[i].equals("<"))				//Prima tutte le U, poi le S, poi le W
			{
				inBase.add("0");
				nomiBase.add("u"+String.valueOf(i+1));
				for (int k=0;k<currU;k++)
					riga.add((double)0);
				currU++;
				riga.add((double) 1);
				for (int k=currU;k<this.indiceU;k++)
					riga.add((double) 0);
				
				for (int k=0;k<this.indiceS;k++)
					riga.add((double)0);
				
				for (int k=0;k<this.indiceW;k++)
					riga.add((double) 0);
			}
			
		}
		
		for (int i=0;i<this.indiceU;i++)
			this.coeffFunObiett.add("0");
		for (int i=0;i<this.indiceS;i++)
			this.coeffFunObiett.add("0");
		for (int i=0;i<this.indiceW;i++)
			this.coeffFunObiett.add("-x");
		
		//Delta
		this.generaDelta();
	}
	
	public void generaDelta ()
	{
		delta=new ArrayList <String> ();
        XJep parser = new XJep();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.addComplex();
        parser.setAllowUndeclared(true);
        parser.setImplicitMul(false);
        parser.setAllowAssignment(true);
        parser.getPrintVisitor().setMode(PrintVisitor.FULL_BRACKET, true);
        
		PNodeI [] delta=new PNodeI [coeffFunObiett.size()];
        
		for (int i=0;i<coeffFunObiett.size();i++)
		{
			String formula="";
			for (int k=0;k<inBase.size();k++)
			{
				LinkedList <Double> riga=coeffVincoli.get(k);
				if (i<riga.size())
				if (k==0)
					formula+=String.valueOf("("+coeffFunObiett.get(i))+"-("+riga.get(i)+"*"+inBase.get(k)+"))";
				else
					formula+="-("+riga.get(i)+"*"+inBase.get(k)+")";
			}
			formula=formula.substring(0, formula.length());
			System.out.println("formula: "+formula);
				try
				{
					Node node = parser.parse(formula);
					Node processed = parser.preprocess(node);
					Node simp = parser.simplify(processed);
		        	parser.println(simp);
		        	System.out.println("Semplifico: "+simp.toString());
			    
		        	PolynomialCreator pc = new PolynomialCreator(parser);
		        	Node simplified = pc.simplify(node);
		        	Node expand = pc.expand(simplified);
		        	PNodeI poly = pc.createPoly(expand);
		        	String risultato=poly.toString();
		        	risultato=risultato.replace("--", "+");
		        	System.out.println("risultato: "+risultato);
		        	this.delta.add(risultato);
		        }
		        catch (ParseException e) 
				{
					System.out.println("Parse exception");
				}
		        catch (Exception e) 
				{
				}
		}
		
		for (int i=0;i<delta.length;i++)
		{
			try
			{
				Node node = parser.parse(this.delta.get(i));
				Node processed = parser.preprocess(node);
				Node simp = parser.simplify(processed);
	        	parser.println(simp);
		    
	        	PolynomialCreator pc = new PolynomialCreator(parser);
	        	Node simplified = pc.simplify(node);
	        	Node expand = pc.expand(simplified);
	        	PNodeI poly = pc.createPoly(expand);
	        	String risultato=poly.toString();
	        	risultato=risultato.replace("--", "+");
	        	risultato=risultato.replace("-x", "-1.0*x");		//non funziona
	        	risultato=risultato.replace("+x", "+1.0*x");		//non funziona
	        	System.out.println("risultato2: "+risultato);
	        	this.delta.set(i, risultato);
			}
	        catch (ParseException e) 
			{
				System.out.println("Parse exception");
			}
	        catch (Exception e) 
			{
			}
			
		}
		//Delta � semplificato
		
		//Crea matrice coeff vincoli + termini noti
		
		vincoliETermini=new double[coeffVincoli.size()][coeffFunObiett.size()+1];
		
		for (int i=0;i<coeffVincoli.size();i++)
		{
			LinkedList <Double> riga=coeffVincoli.get(i);
			
			for (int k=0;k<riga.size();k++)
			{
				vincoliETermini[i][k]=riga.get(k);
			}
			vincoliETermini[i][coeffFunObiett.size()]=results[i];
		}
		
	}
	
	public void generaDelta (int indiceOrizzontale, int indiceVerticale)		//Genera delta con il metodo dei quadrati
	{
		ArrayList <String> deltaNuovo=new ArrayList <String> ();
        XJep parser = new XJep();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.setAllowUndeclared(true);
        parser.setImplicitMul(false);
        parser.setAllowAssignment(false);
   //     parser.getPrintVisitor().setMode(PrintVisitor.FULL_BRACKET, true);
        
		PNodeI [] delta=new PNodeI [coeffFunObiett.size()];
        
		double divisore=vincoliETerminiOld[indiceOrizzontale][indiceVerticale];
		
		for (int k=0;k<coeffFunObiett.size();k++)
		{
			String testoEspressione="("+this.delta.get(k)+")-(("+(float)vincoliETerminiOld[indiceOrizzontale][k]+"*("+this.delta.get(indiceVerticale)+"))/"+divisore+")";
			System.out.println("testoExpr"+testoEspressione);
			deltaNuovo.add(testoEspressione);
		}
		
		deltaNuovo.set(indiceVerticale, "0");
		
		this.delta=new ArrayList <String> ();
		
		for (int i=0;i<coeffFunObiett.size();i++)
		{
			
				try
				{
					Node node = parser.parse(deltaNuovo.get(i));
					Node processed = parser.preprocess(node);
					Node simp = parser.simplify(processed);
		        	parser.println(simp);
		        	System.out.println("Semplifico: "+simp.toString());
			    
		        	PolynomialCreator pc = new PolynomialCreator(parser);
		        	Node simplified = pc.simplify(node);
		        	Node expand = pc.expand(node);
		        	PNodeI poly = pc.createPoly(node);
		        	String risultato=poly.toString();
		        	risultato=risultato.replace("--", "+");
		        	System.out.println("risultato: "+risultato);
		        	
		        	
		        	this.delta.add(risultato);
		        }
		        catch (ParseException e) 
				{
					System.out.println("Parse exception");
				}
		        catch (Exception e) 
				{
				}
		}
		
		for (int i=0;i<this.coeffFunObiett.size();i++)
		{
			for (int k=0;k<2;k++)
			{
			try
			{
				Node node = parser.parse(this.delta.get(i));
				Node processed = parser.preprocess(node);
				Node simp = parser.simplify(processed);
	        	parser.println(simp);
		    
	        	PolynomialCreator pc = new PolynomialCreator(parser);
	        	Node simplified = pc.simplify(node);
	        	Node expand = pc.expand(simplified);
	        	PNodeI poly = pc.createPoly(expand);
	        	System.out.println("SEMPLIFICATO TEST "+poly.toString());
	        	String risultato=poly.toString();
	        	risultato=risultato.replace("--", "+");
	        	risultato=risultato.replace("-x", "-1.0*x");		//non funziona
	        	risultato=risultato.replace("+x", "+1.0*x");		//non funziona
	        	
        	/*	StringTokenizer st=new StringTokenizer (risultato, "*");
        		
        		while (st.hasMoreTokens())
        		{
        			String token=st.nextToken();
        			
        			if (token.indexOf("+")==-1 || token.indexOf("-")==-1)
        				break;
        			
	        		int indicePartenza=token.lastIndexOf("+");
	        		if (indicePartenza<token.lastIndexOf("-"))
	        			indicePartenza=token.lastIndexOf("-");
	        		String daSostituire=token.substring(indicePartenza+1, token.length());
	        		float val=Float.parseFloat(token.charAt(indicePartenza)+daSostituire);
	        		risultato=risultato.replace(daSostituire, String.valueOf(val));
        		}*/
	        	
	        	
	        	if (risultato.indexOf("E")!=-1)							//fix imprecisione
	        	{
	        		String exp=risultato.substring(0, risultato.lastIndexOf("E"));
	        		String dopo=risultato.substring(risultato.lastIndexOf("E"), risultato.length());
	        		int indicePartenza=exp.lastIndexOf("+");
	        		if (indicePartenza<exp.lastIndexOf("-"))
	        			indicePartenza=exp.lastIndexOf("-");
	        		if (indicePartenza==-1)
	        			indicePartenza=0;
	        		String daSostituire="";
	        		if (dopo.length()<7)
	        			daSostituire=risultato.substring(indicePartenza, risultato.length());
	        		else
	        		{
	        			String tagliaEsponente=dopo.substring(2, dopo.length());
	        			
	        			int indicePartenzaSucc=tagliaEsponente.indexOf("+");
	        			if (indicePartenzaSucc<tagliaEsponente.indexOf("-"))
	        				indicePartenzaSucc=tagliaEsponente.indexOf("-");
	        			daSostituire=risultato.substring(indicePartenza, indicePartenzaSucc+2+exp.length());
	        		}
	        		risultato=risultato.replace(daSostituire, "");
	        	}
	        	
	        	System.out.println("risultato2DELTA: "+risultato);
	        	this.delta.set(i, risultato);
			}
	        catch (ParseException e) 
			{
				System.out.println("Parse exception");
			}
	        catch (Exception e) 
			{
			}
			}
		}
			
	}
	
	public boolean checkDeltaPositivo (int indiceColonna)
	{
		StringTokenizer st=new StringTokenizer (delta.get(indiceColonna), "x");
		while (st.hasMoreTokens())
		{
			String token=st.nextToken();
			int indice=token.indexOf("+");
			if (indice==-1)
				indice=token.indexOf("-");
			if (indice!=-1)
			{
				int indice2=token.indexOf("*");
				if (indice2!=-1)
				{
					token=token.substring(indice, token.indexOf("*"));
					System.out.println("TOKEN CONTROLLO"+token);
					if (token.charAt(0)=='+')
						return true;
					else if (token.charAt(0)=='-')
						return false;
				}
			}
		}
		
		//Se arriva non ha m
		
		if (Double.parseDouble(delta.get(indiceColonna))>0)
			return true;
		else
			return false;
		
	}
	
	public void start ()
	{
		MostraRisultato export=new MostraRisultato();
		export.aggiungiTabella(vincoliETermini, indiceU, indiceS, indiceW, nomiBase, delta);
		
		int indice=this.check();
		System.out.println("INDICE > DELTA "+indice);
		while (this.checkDeltaPositivo(indice))
		{
			int indiceRiga=this.determinaRiga(indice);
			this.itera (indice, indiceRiga);
			this.generaDelta(indiceRiga, indice);
			indice=this.check();
			export.aggiungiTabella(vincoliETermini, indiceU, indiceS, indiceW, nomiBase, delta);
		}
		String res="";
		for (int i=0;i<coeffVincoli.size();i++)
			res+="  "+String.valueOf(this.vincoliETermini[i][coeffFunObiett.size()]);
		export.finito();
		JOptionPane.showMessageDialog(null, res);
		
	}
	
	public int check ()
	{
		LinkedList <Float> coeffM=new LinkedList <Float> ();
		LinkedList <Float> valoriInt=new LinkedList <Float> ();
		
		for (int i=0;i<delta.size();i++)
		{
			
			StringTokenizer st=new StringTokenizer (delta.get(i), "x");

			int somma=0;
			int debug=st.countTokens();
			if (delta.get(i).indexOf("x")==-1)
			{
				coeffM.add((float) 0);
				valoriInt.add(Float.parseFloat(delta.get(i)));
			}
			else
			while (st.hasMoreTokens())
			{
				String token=st.nextToken();
				System.out.println("TOKEN PURO"+token);
				if (token.equals("+x") && delta.get(i).length()>6)
					somma=1;
				else if (token.equals("-x") && delta.get(i).length()>6)
					somma=-1;
				else
				{
					int indice=token.lastIndexOf("+");
					int indice3=token.lastIndexOf("-");
					if (indice<indice3)
						indice=indice3;
					if (indice >-1)
					{
						int indice2=token.indexOf("*");
						if (indice2>-1)
						{
							token=token.substring(indice, token.lastIndexOf("*"));
							System.out.println("coeff. Token verticali"+token);
							if (token.equals("+1.0") && st.hasMoreTokens())
								somma=1;
							else if (token.equals("-1.0") && st.hasMoreTokens())
								somma=-1;
							else
							{
								int indiceFineValore=0;
								String strValore="";
								if (delta.get(i).charAt(0)=='-'|| delta.get(i).charAt(0)=='+'){
								strValore=delta.get(i).substring(1, delta.get(i).length());
								indiceFineValore=1;
								}
								else
									strValore=delta.get(i).substring(0, delta.get(i).length());
								
								int indiceFineValore2=strValore.indexOf("+");
								if ((strValore.indexOf("-")<strValore.indexOf("+") && strValore.indexOf("-")>-1) || indiceFineValore2<0)
									indiceFineValore2=strValore.indexOf("-");
								if (indiceFineValore2>-1)
									valoriInt.add(Float.parseFloat(delta.get(i).substring(0, indiceFineValore+indiceFineValore2)));
								else
									valoriInt.add((float) 0);
							coeffM.add(Float.parseFloat(token)+somma);
							somma=0;
							}
						}
					}
				}
		//		System.out.println("Token"+st.nextToken());
			}
		}
		
		float coeffMPrec=0;
		int indiceInBase=0;
		

		if (coeffM.size()!=0){
		LinkedList <Integer> indiciElementiUguali=new LinkedList <Integer> ();
		coeffMPrec=coeffM.get(0);
		for (int i=1;i<coeffM.size();i++)
		{
			if (coeffM.get(i)>coeffMPrec)
			{
				coeffMPrec=coeffM.get(i);
				indiceInBase=i;
				indiciElementiUguali=new LinkedList <Integer> ();
			}
			
			else if (coeffM.get(i)==coeffMPrec)		
			{
				indiciElementiUguali.add(i);
			}
		}
		
		if (coeffM.size()==0 || coeffMPrec<=0)
		{
			float numMaggiore=valoriInt.get(0);
			for (int i=1;i<coeffFunObiett.size();i++)
			{
				if (valoriInt.get(i)>numMaggiore){
					numMaggiore=valoriInt.get(i);
					indiceInBase=i;
				}
			}
			return indiceInBase;
		}
		
		System.out.println("coeffFunObiett 0: "+coeffFunObiett.get(0)+"coeff magg"+coeffMPrec);
		
		if (indiciElementiUguali.size()!=0)
		{
			float valoreInt=valoriInt.get(indiceInBase);
			
			for (int i=0;i<indiciElementiUguali.size();i++)
			{
				if (valoriInt.get(indiciElementiUguali.get(i))>valoreInt)
				{
					indiceInBase=indiciElementiUguali.get(i);
					valoreInt=valoriInt.get(indiciElementiUguali.get(i));
				}
			}
		}
		}
		System.out.println("INDICE IN BASE:"+indiceInBase);
		
		return indiceInBase;
	}
	
	public int determinaRiga (int indiceInBase)
	{						//Vincoli per riga (0->riga 1, 1->riga 2... L'indice indica la colonna)
		ArrayList <Double> vincoliColonna=new ArrayList <Double> ();
		double rapporto=0;
		LinkedList <Double> rapporti=new LinkedList <Double> ();
		for (int i=0;i<coeffVincoli.size();i++)
		{
			double valore=coeffVincoli.get(i).get(indiceInBase);
			if (valore<=0)
			{
				rapporti.add(Double.POSITIVE_INFINITY);		//Evito che in seguito individui questi valori
				continue;
			}
			rapporti.add(results[i]/valore);
		}
		
		double rappPrec=rapporti.get(0);
		int indiceOrizzontale=0;
		for (int i=1;i<rapporti.size();i++)
		{
			if (rapporti.get(i)<rappPrec)
			{
				System.out.println("rapporto"+rapporti.get(i));
				rappPrec=rapporti.get(i);
				indiceOrizzontale=i;
			}
		}
		return indiceOrizzontale;
	}
	
	public void itera (int indiceVerticale, int indiceOrizzontale)
	{
		inBase.set(indiceOrizzontale, coeffFunObiett.get(indiceVerticale));
		String nomeVInBase="";
		if (indiceVerticale<coeffFunObiett.size()+indiceU)
			nomeVInBase="x"+String.valueOf((indiceVerticale+1));
		else if (indiceVerticale<coeffFunObiett.size()+indiceS+indiceU)
			nomeVInBase="u"+String.valueOf((1+indiceVerticale)-indiceU);
		else if (indiceVerticale<coeffFunObiett.size()+indiceW+indiceS+indiceU)
			nomeVInBase="s"+String.valueOf(indiceVerticale-(indiceU+indiceS-1));
		else
			nomeVInBase="w"+String.valueOf(indiceVerticale-(indiceU+indiceS+indiceW-1));
		nomiBase.set(indiceOrizzontale, nomeVInBase);
		
		System.out.println("IN BASE AL TERMINE: in pos: "+indiceOrizzontale+" il valore: "+coeffFunObiett.get(indiceVerticale));
		double divisore=vincoliETermini[indiceOrizzontale][indiceVerticale];
		double [][] vincoliETerminiNuovo=new double [coeffVincoli.size()][coeffFunObiett.size()+1];
		for (int i=0;i<coeffVincoli.size();i++)
		{
			if (i==indiceOrizzontale)
			{
				for (int k=0;k<coeffFunObiett.size()+1;k++)
					vincoliETerminiNuovo[i][k]=vincoliETermini[i][k]/divisore;
			}
			else
			{
				for (int k=0;k<coeffFunObiett.size()+1;k++)
				{
					if (k==indiceVerticale)
						vincoliETerminiNuovo[i][k]=0;
					else						
						vincoliETerminiNuovo[i][k]=vincoliETermini[i][k]-((vincoliETermini[indiceOrizzontale][k]*vincoliETermini[i][indiceVerticale])/divisore);
					System.out.println("ITERAZIONE COLONNA "+k);
				}
			}
		}
		this.vincoliETerminiOld=vincoliETermini;
		this.vincoliETermini=vincoliETerminiNuovo;
		
	}

}
