import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;


public class Control {
	private static Gui g;
	private static Match m;
	private static GiocatoreFurbo a;
	
	public static void main(String[] args) {
		LinkedList <Player> players=new LinkedList <Player>();
		a=new GiocatoreFurbo(2);
		RandomPlayer b=new RandomPlayer();
		players.add(a);
		players.add(b);
		g=new Gui(3);
		m=new Match(3, g, players.get(1));
		
		
	}
	
	public static void gioca ()
	{
/*		while (!m.isFinished())
		{
			int i=0;
			int [] mossa;
			int who=1;
			LinkedList<int[]> celleRim=m.getEmptyCells();

			if (i%2==0){
				mossa=a.move(m.getStatus(), celleRim);
				who=1;
			}
			else{
				mossa=b.move(m.getStatus(), celleRim);
				who=2;
			}
			m.setMovement(mossa, who);
			i++;
			celleRim=m.getEmptyCells();
		}
		Player winner=m.whoWon();
		if (null!=winner)
			System.out.println("Il vincitore è: "+m.whoWon().getCode());
		else
			System.out.println("Non ha vinto nessuno");
		*/
	}
	
	public static void setMossaUtente (int [] posizione)
	{
		m.setMovement(posizione, g.getCode());
		LinkedList<int[]> celleRim=m.getEmptyCells();

		
		if (!m.isFinished())
		{
			int [] mossa=a.move(m.getStatus(), celleRim);
			m.setMovement(mossa, 2);
			checkWinner();
			changed(m.getStatus());
		}
		else
		{
			checkWinner();
			changed(m.getStatus());
		}

		
	}
	
	public static void checkWinner ()
	{
		if (m.isFinished())
		{
			Player winner=m.whoWon();
			
			//Debug
			int dim=m.getStatus().size();
			for (int i=0;i<dim;i++)
			{
				for (int j=0;j<dim;j++)
				{
					System.out.print(m.getStatus().get(i).get(j));
				}
				System.out.print("\n");
			}
			
			if (null!=winner){
				changed(m.getStatus());
				if (g.getCode()==m.whoWon().getCode())
					JOptionPane.showMessageDialog(null,"Congratulazioni, hai vinto");
				else
					JOptionPane.showMessageDialog(null,"Il vincitore è: "+m.whoWon().getCode()+" (computer)");


			}
			else
				JOptionPane.showMessageDialog(null,"Non ha vinto nessuno");
			m.clearMatch();
		}

	}
	
	public static void changed (List<List<Integer>> valori)
	{
		g.update(valori);
	}

}
