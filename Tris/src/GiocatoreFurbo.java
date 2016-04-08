import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class GiocatoreFurbo implements Player{
	private int actCount;
	private int [][] statoAttuale=new int [3][3];
	
	public GiocatoreFurbo (int count)
	{
		super();
		actCount=count;
	}
	
	public int getCode ()
	{
		return actCount;
	}
	
	private void statoAttuale (List<List<Integer>> status)
	{
		for (int i=0;i<status.size();i++)
		{
			List<Integer> tmp=status.get(i);
			for (int j=0;j<status.size();j++)
			{
				this.statoAttuale[i][j]=tmp.get(j);
			}
		}
	}
	
	public int [] move (List<List<Integer>> status, LinkedList <int[]> where)
	{
		statoAttuale(status);
		double maxScore=0;
		int [] mossa=new int[2];
		
		for (int i=0;i<where.size();i++)
		{
			double score=getScore(where.get(i)[0], where.get(i)[1]);
			if (score>maxScore)
			{
				maxScore=score;
				mossa=where.get(i);
			}
		}
		return mossa;
		
	}
	
	private int contaAltri (int [] riga)
	{
		int conta=0;
		for (int i=0;i<riga.length;i++)
		{
			if (riga[i]!=0 && riga[i]!=actCount){
				conta++;
			}
		}
		return conta;
	}
	
	private int contaMe (int [] riga)
	{
		int conta=0;
		for (int i=0;i<riga.length;i++)
		{
			if (riga[i]==actCount){
				conta++;
			}
		}
		return conta;
	}
	
	private double getScore (int row, int col)
	{
		//Valuto in base alla posizione
		int score=0;
		if (row==col)
		{
			score+=800;
		}
		else if (row==0 || row==statoAttuale.length-1)
		{
			if (col == 0 || col==statoAttuale.length-1){
				score+=600;
			}
		}
			
		//Punteggi in base all'avversario.

			
		//Controllo riga
		int [] riga=new int[statoAttuale.length];
		for (int i=0;i<riga.length;i++)
		{
			riga[i]=statoAttuale[row][i];
		}
		
		int conta=contaAltri(riga);
		score+=(Math.pow(45, conta));
		
/*		if (conta==statoAttuale.length-1)
		{
			score+=4000;
		}*/
		conta=contaMe(riga);
		score+=(Math.pow(80, conta));
		/*
		if (conta==statoAttuale.length-1)
		{
			return Double.POSITIVE_INFINITY;
		}
		*/
		//Controllo colonna
		for (int i=0;i<riga.length;i++)
		{
			riga[i]=statoAttuale[i][col];
		}
		
		conta=contaAltri(riga);
		score+=(Math.pow(45, conta));

/*		
		if (conta==statoAttuale.length-1)
		{
			score+=4000;
		}*/
		conta=contaMe(riga);
		score+=(Math.pow(80, conta));
		
		//Mossa speciale
		
		double diagonale1 []=new double[3];	
		for (int i=0;i<riga.length;i++)
			diagonale1[i]=statoAttuale[i][i];
		
	//	if (diagonale1[0])
		

		//Controllo diagonale principale
		if (row==col){


			conta=contaAltri(riga);
			score+=(Math.pow(50, conta));
/*
			if (conta==statoAttuale.length-1)
			{
				conta+=5030;
			}*/
			conta=contaMe(riga);
			score+=(Math.pow(85, conta));
			
			//Mossa particolare
			
/*			if (conta==statoAttuale.length-1)
			{
				return Double.POSITIVE_INFINITY;
			}*/
		}
		//Controllo diagonale secondaria
		if (row==statoAttuale.length-1-col){
			for (int i=0;i<riga.length;i++)
			{
				riga[i]=statoAttuale[i][statoAttuale.length-1-i];
			}
			conta=contaAltri(riga);
			score+=(Math.pow(50, conta));
/*
			if (conta==statoAttuale.length-1)
			{
				conta+=5030;
			}*/

			conta=contaMe(riga);
			score+=(Math.pow(85, conta));
/*
			if (conta==statoAttuale.length-1)
			{
				return Double.POSITIVE_INFINITY;
			}*/
		}
		
		
		
		System.out.println(score);
		return score;


		
		
	}
}
