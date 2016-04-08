import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class Control {

	/**
	 * @param args
	 */

		public static void main(String[] args) {
			// TODO Auto-generated method stub
			Lavoratore alv=new Lavoratore ("Alvise", "Architetto");
			Lavoratore marc=new Lavoratore ("Marco", "Operaio");
			Lavoratore tiz=new Lavoratore ("Tiziano", "Artigiano");
			Strumento s=new Strumento("Gru");
			Strumento imp=new Strumento ("Impalcatura");
			Strumento penn=new Strumento ("Pennello");
			Materiale m6=new Materiale ("Sensori",590);
			Elementare el1=new Elementare ("a0", "Posa allarme antincendio", 43);
			el1.addLavoratori(marc);
			el1.addMateriali(m6);
			Elementare el2 = new Elementare ("a1", "Costruzione muri", 220);
			el2.addLavoratori(marc);
			el2.addLavoratori(alv);
			el2.addMateriali(new Materiale ("Mattoni", 2700));
			el2.addMateriali(new Materiale ("Cemento", 550));
			Elementare el3=new Elementare ("a2", "Dipinta muri", 76);
			el3.addLavoratori(tiz);
			el3.addMateriali(new Materiale ("Pittura", 95));
			el3.addMateriali(new Materiale ("Legno", 23));
			el3.addStrumento(imp);
			el3.addStrumento (penn);
			Elementare el4=new Elementare ("a3", "Posa travi", 142);
			el4.addLavoratori(alv);
			el4.addLavoratori(marc);
			el4.addStrumento(s);
			el4.addMateriali(new Materiale("Travi di legno", 770));
			el4.addMateriali(new Materiale ("Cemento", 130));
			Elementare el5=new Elementare ("a4", "Copertura tetto", 90);
			el5.addLavoratori(marc);
			el5.addLavoratori (alv);
			el5.addStrumento(imp);
			el5.addMateriali(new Materiale ("Legno", 1100));
			Composte c1=new Composte ("c1", "Realizzazione pareti");
			c1.add(el2);
			c1.add(el3);
			Composte c2=new Composte ("c2", "Costruzione tetto");
			c2.add(el4);
			c2.add(el5);
			Composte c3=new Composte ("c3", "Costruzione tetto");
			c3.add(el1);
			c3.add(c1);
			c3.add(c2);
			
			System.out.println("Tempo seriale "+c3.calcolaTempo());
			int tPar=Math.max(Math.max(Math.max(Math.max(el1.calcolaTempo(), el2.calcolaTempo()),el3.calcolaTempo()), el4.calcolaTempo()), el5.calcolaTempo());
			System.out.println("Tempo parallelo "+ tPar);
			
			Attivita [] att={el1, el2, el3, el4, el5};
			LinkedList <Risorsa> r=new LinkedList <Risorsa> ();
			int siz=0;
			String nome="";
			
			for (int i=0;i<5;i++)
			{
				Elementare e=(Elementare) att[i];				//Ogni attivita' elementare
				r=e.getMateriali();					//Lista di materiali per ogni attivita'
				siz=r.size();
				for (int j=0;j<siz;j++)
				{
					nome=r.get(j).getNome();		//Nome del materiale
					if (nome.equals("Legno") || nome.equals("Travi di legno"))
					{
						Composte cT=new Composte ("cT","compostaT");			//Sostituire l'attivita'
						cT.add(e);												//Aggiungi l'attivita' elementare
						cT.add(new Elementare ("e", "Verniciatura ignifuga", 1));
						att[i]=cT;
						break;
					}
				}
			}
			
			for (int i=0;i<5;i++)
			{
				System.out.println(att[i].toString());
			}
			//Albero a=new Albero ();
	}

}

/*class Albero extends JFrame
{
	public Albero ()
	{
	    DefaultMutableTreeNode top = new DefaultMutableTreeNode("Attivita'");
	    top.add(new DefaultMutableTreeNode("Semplici"));
	    top.add(new DefaultMutableTreeNode ("Composte"));

	    final JTree albero = new JTree(top);
	    albero.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	  //  albero.addTreeSelectionListener((TreeSelectionListener) this);
	    albero.setDragEnabled(true);
	    
	    
	    this.setBounds(100, 100, 500, 500);
	    this.setLayout(new GridBagLayout ());
	    GridBagConstraints c=new GridBagConstraints ();

	    c.gridx=0;
	    c.gridy=0;
	    c.weightx=1;
	    c.weighty=1;
	    c.fill=GridBagConstraints.BOTH;
	    c.gridheight=6;
	    c.gridwidth=4;
	    JScrollPane pann = new JScrollPane(albero);

	    this.add(pann, c);
	    
	    c.gridx=1;
	    c.gridy=9;
	    c.gridheight=1;
	    c.gridwidth=1;
	    c.fill=GridBagConstraints.NONE;
	    final JLabel avviso=new JLabel ("");
	    avviso.setForeground(new Color (255, 0, 0));
	    this.add(avviso, c);
	    JButton agg=new JButton ("Aggiungi");
	    
	    ActionListener al=new ActionListener ()
	    {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame finAggiungi=new JFrame ("Aggiungi");
				finAggiungi.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				TreePath selezione=albero.getSelectionModel().getSelectionPath();
				if (selezione==null)
				{
					avviso.setText("Errore, selezionare il nodo padre");
					return;
				}
				avviso.setText("");
				
				
				
				finAggiungi.setVisible(true);
			}
	    	
	    };
	    agg.addActionListener(al);
	    c.gridy=7;
	    this.add(agg, c);
	    
	    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	    this.setVisible(true);

	}
	
}*/
