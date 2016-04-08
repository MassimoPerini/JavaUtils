import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;


public class Gui extends JFrame {

	LinkedList <JTextField> funzObiett=new LinkedList <JTextField> ();
	
	public Gui ()
	{
		this.setSize(1300, 500);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		GridBagLayout gb=new GridBagLayout ();
		
		this.setLayout(gb);
		
		GridBagConstraints c = new GridBagConstraints();
		

		final JLabel titolo=new JLabel ("Funzione obiettivo");
		c.anchor=GridBagConstraints.CENTER;
		c.gridx=0;
		c.gridy=0;
		gb.setConstraints(titolo, c);
		this.add(titolo);
		c.gridx=1;
		c.gridwidth=9;
		c.gridy=1;
		final Pannello fObiett=new Pannello ();
		gb.setConstraints(fObiett, c);
		this.add(fObiett);
		c.gridx=0;
		JLabel z=new JLabel ("Z=");
		gb.setConstraints(z, c);
		this.add(z);
		
		JLabel vin=new JLabel ("Vincoli:");
		c.gridy=2;
		c.gridx=0;
		gb.setConstraints(vin, c);
		this.add(vin);
		
		final Vincolo v=new Vincolo ();
		c.gridy=3;
		c.gridwidth=10;
		c.gridx=0;
		gb.setConstraints(v, c);
		this.add(v);
		
		JButton ok=new JButton ("OK!!!");
		
		ok.addActionListener(new ActionListener ()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				LinkedList <JComboBox> segniFObiett=fObiett.getCaselleSegni();
				LinkedList <JTextField> coeffObiett = fObiett.getCaselleCoeff();
				
				int l=coeffObiett.size();
				double [] rCoeffObiett=new double [l];
				
				for (int i=0;i<l;i++)
				{
					if (segniFObiett.get(i).getSelectedItem().equals("-"))
						rCoeffObiett[i]=Double.parseDouble(coeffObiett.get(i).getText())*-1;
					else
						rCoeffObiett[i]=Double.parseDouble(coeffObiett.get(i).getText());
				}
				
				LinkedList <Pannello> pannelliVincoli=v.getPannelliVincoli();
				int dimVincoli=pannelliVincoli.size();
				
				LinkedList <double []> coeffFinaliVincoli=new LinkedList <double []> ();
				
				String [] segniVincoli=new String [dimVincoli];
				double [] risultatiVincoli=new double [dimVincoli];
				
				for (int i=0;i<dimVincoli;i++)
				{
					Pannello coeffVincoli=pannelliVincoli.get(i);
					LinkedList <JTextField> coeffVincoloT=coeffVincoli.getCaselleCoeff();
					LinkedList <JComboBox> segniVincolo=coeffVincoli.getCaselleSegni();
					int n=coeffVincoloT.size();
					double [] coeffVincolo=new double [n];
					
					for (int k=0;k<n;k++)
					{
						if (segniVincolo.get(k).getSelectedItem().equals("-"))
							coeffVincolo[k]=Double.parseDouble(coeffVincoloT.get(k).getText())*-1;
						else
							coeffVincolo[k]=Double.parseDouble(coeffVincoloT.get(k).getText());
					}
					
					coeffFinaliVincoli.add(coeffVincolo);
					
				}
				
				LinkedList <JComboBox> segni=v.getSegni();
				LinkedList <JTextField> risultati=v.getRisultati();
				
				for (int i=0;i<dimVincoli;i++)
				{
					segniVincoli[i]=(String) segni.get(i).getSelectedItem();
					risultatiVincoli[i]=Double.parseDouble(risultati.get(i).getText());
				}
				
				if (coeffFinaliVincoli.size()<1)
				{
					//Non inseriti vincoli
				}
				
				System.out.println(rCoeffObiett[0]);
				
				Control.getInfo(rCoeffObiett, coeffFinaliVincoli, segniVincoli, risultatiVincoli);
				
			}
			
		});
		
		c.gridx=10;
		c.gridy=4;
		
		gb.setConstraints(ok, c);
		this.add(ok);
		
		this.setVisible(true);
		
		
	}
	
}

class Vincolo extends JPanel
{
	GridBagLayout layout = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	
	LinkedList <Pannello> pannelliVincoli=new LinkedList <Pannello> ();
	LinkedList <JComboBox> segni=new LinkedList <JComboBox> ();
	LinkedList <JTextField> risultati=new LinkedList <JTextField> ();
	LinkedList <JLabel> etichette=new LinkedList <JLabel> ();
	
	public Vincolo ()
	{
		this.setLayout(layout);
		this.setSize(900, 1000);
		c.anchor=GridBagConstraints.CENTER;
		c.gridx=0;
		c.gridy=0;
		c.weightx=1;
		c.weighty=1;
		
		JButton btnP=new JButton ("+ vincolo");
		JButton btnM=new JButton ("- vincolo");
		final Vincolo v=this;
		btnP.addActionListener(new ActionListener ()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Pannello p=new Pannello ();
				pannelliVincoli.add(p);
				int indice=pannelliVincoli.size();
				JLabel et=new JLabel ("V"+String.valueOf(indice));
				etichette.add(et);
				c.gridy++;
				c.gridx=0;
				layout.setConstraints(et, c);
				v.add(et);
				c.gridx=1;
				layout.setConstraints(p, c);
				v.add(p);
				
				String [] elements={">", "<", "="};
				JComboBox cEle=new JComboBox (elements);
				segni.add(cEle);
				c.gridx=2;
				layout.setConstraints(cEle, c);
				v.add(cEle);
				
				JTextField par=new JTextField (3);
				risultati.add(par);
				c.gridx=3;
				layout.setConstraints(par, c);
				v.add(par);
				
				v.repaint();
				v.revalidate();
				//Draw e redraw
			}
			
		});
		
		btnM.addActionListener(new ActionListener ()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				if (pannelliVincoli.size()>0)
				{
					v.remove(etichette.get(etichette.size()-1));
					etichette.remove(etichette.size()-1);
					
					v.remove(pannelliVincoli.get(pannelliVincoli.size()-1));
					pannelliVincoli.remove(pannelliVincoli.size()-1);
					
					v.remove(segni.get(segni.size()-1));
					segni.remove(segni.size()-1);
					
					v.remove(risultati.get(risultati.size()-1));
					risultati.remove(risultati.size()-1);
					
			//		c.gridx=0;
			//		c.gridy--;
					
					v.repaint();
					v.revalidate();
					
				}
			}
			
		});
		
		layout.setConstraints(btnP, c);
		this.add(btnP);
		c.gridx=3;
		layout.setConstraints(btnM, c);
		this.add(btnM);
		c.gridx=0;
		c.gridy=1;
		
		this.setVisible(true);
		
	}

	public LinkedList<Pannello> getPannelliVincoli() {
		return pannelliVincoli;
	}

	public void setPannelliVincoli(LinkedList<Pannello> pannelliVincoli) {
		this.pannelliVincoli = pannelliVincoli;
	}

	public LinkedList<JComboBox> getSegni() {
		return segni;
	}

	public void setSegni(LinkedList<JComboBox> segni) {
		this.segni = segni;
	}

	public LinkedList<JTextField> getRisultati() {
		return risultati;
	}

	public void setRisultati(LinkedList<JTextField> risultati) {
		this.risultati = risultati;
	}
	
	
	
}

class Pannello extends JPanel
{
	
	LinkedList <JTextField> caselleCoeff=new LinkedList <JTextField> ();
	LinkedList <JComboBox> caselleSegni=new LinkedList <JComboBox> ();
	int actualIndex=1;
	LinkedList <JLabel> caselleEtichette=new LinkedList <JLabel> ();
	GridBagLayout layout = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

	
	public Pannello ()
	{
		this.setLayout(layout);
		this.setSize(900, 300);
		c.anchor=GridBagConstraints.CENTER;
	//	c.gridheight=3;
	//	c.gridwidth=5;

		c.weightx=1;
		c.weighty=1;
		
		
		c.gridx=13;
		c.gridy=1;
		JButton addFObiett=new JButton ("+ variabile");
		final Pannello p=this;
		addFObiett.addActionListener(new ActionListener ()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				p.aggiungiCasella();
			}
			
		});
		layout.setConstraints(addFObiett, c);
		this.add(addFObiett);
		
		JButton remFObiett=new JButton ("- variabile");
		c.gridy=2;
		layout.setConstraints(remFObiett, c);
		remFObiett.addActionListener(new ActionListener ()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				p.rimuoviCasella();
			}
			
		});
		this.add(remFObiett);
		c.gridx=0;
		c.gridy=0;
	}
	
	public void aggiungiCasella ()
	{
		String [] segni={"+", "-"};
		JComboBox segno=new JComboBox (segni);
		this.add(segno, c);
		this.avanza ();
		caselleSegni.add(segno);
		JTextField coeff=new JTextField (3);
		caselleCoeff.add(coeff);
		this.add(coeff, c);
		this.avanza();
		JLabel etichetta=new JLabel ("x"+actualIndex);
		caselleEtichette.add(etichetta);
		this.add(etichetta, c);
		this.avanza();
		actualIndex++;
		this.repaint();
		this.revalidate();
		
	}
	
	public void rimuoviCasella ()
	{
		if (caselleCoeff.size()>0)
		{
			this.remove(caselleCoeff.get(caselleCoeff.size()-1));
			caselleCoeff.remove(caselleCoeff.size()-1);
			retrocedi();
			this.remove(caselleSegni.get(caselleSegni.size()-1));
			caselleSegni.remove(caselleSegni.size()-1);
			retrocedi();
			this.remove(caselleEtichette.get(caselleEtichette.size()-1));
			caselleEtichette.remove(caselleEtichette.size()-1);
			retrocedi();
			actualIndex--;
			this.repaint();
			this.revalidate();
		}
	}
	
	private void avanza ()
	{
		c.gridx++;
		if (c.gridx>12)
		{
			c.gridx=0;
			c.gridy++;
		}

	}
	private void retrocedi ()
	{
		c.gridx--;
		if (c.gridx<0)
		{
			c.gridx=12;
			c.gridy--;
		}

	}

	public LinkedList<JTextField> getCaselleCoeff() {
		return caselleCoeff;
	}

	public void setCaselleCoeff(LinkedList<JTextField> caselleCoeff) {
		this.caselleCoeff = caselleCoeff;
	}

	public LinkedList<JComboBox> getCaselleSegni() {
		return caselleSegni;
	}

	public void setCaselleSegni(LinkedList<JComboBox> caselleSegni) {
		this.caselleSegni = caselleSegni;
	}
	
	
}

class MostraRisultato
{
	private JFrame frame;
	private JPanel panel;
	public MostraRisultato ()
	{
		//JScrollPane pictureScrollPane = new JScrollPane(picture)
		this.frame=new JFrame ();
		panel=new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 
	//	panel.setLayout(new FlowLayout ());
		frame.getContentPane().setLayout(new BorderLayout ());

	}
	
	public void aggiungiTabella (double [][] tabella, int indiceU, int indiceS, int indiceW, LinkedList <String> nomiBase, ArrayList <String> delta)
	{
			
		
		int nX=tabella[0].length-(indiceU+indiceS+indiceW);
		String [] intestazioneTabella=new String [tabella[0].length+1];
		
		intestazioneTabella[0]="In base: ";
		
		for (int i=1;i<nX+1;i++)
		{
			String x="x"+String.valueOf(i);
			intestazioneTabella[i]=x;
		}
		
		for (int i=nX;i<nX+indiceU;i++)
			intestazioneTabella[i]="u"+String.valueOf((i-nX+1));
		
		for (int i=nX+indiceU;i<nX+indiceU+indiceS;i++)
			intestazioneTabella[i]="s"+String.valueOf((i-(nX+indiceU)+1));
		
		for (int i=nX+indiceU+indiceS;i<nX+indiceU+indiceS+indiceW;i++)
			intestazioneTabella[i]="w"+String.valueOf((i-(nX+indiceU+indiceS)+1));
		
		intestazioneTabella[nX+indiceU+indiceS+indiceW]="Valore noto";
		
		System.out.println("RIGHE: "+tabella.length);
		
		String [][] values = new String[tabella.length+1][tabella[0].length+1];
		
		for (int i=0;i<tabella.length;i++)
		{
			values[i][0]=nomiBase.get(i);
			for (int j=1;j<tabella[0].length+1;j++)
			{
				values[i][j]=String.valueOf(tabella[i][j-1]);
			}
		}
		for (int j=1;j<tabella[0].length;j++)
		{
			values[tabella.length][j]=delta.get(j-1).replace("x", "M");
		}
		
		values[tabella.length][0]="------";
		values[tabella.length][tabella[0].length]="------";
		
		JTable table=new JTable (values, intestazioneTabella);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		panel.add(scrollPane);
	//	scroll.add(scrollPane);
	}

	public void finito ()
	{
		JScrollPane scroll = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scroll.setPreferredSize(new Dimension(600, 600));
	//	scroll.setLayout(new ScrollPaneLayout());
	    scroll.setViewportView(panel);
        scroll.getVerticalScrollBar().setUnitIncrement(50);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	
	
}
