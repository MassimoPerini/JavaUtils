import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class Gui extends JFrame implements Player {
	private Color [] colors={Color.WHITE, Color.RED, Color.BLACK, Color.BLUE, Color.YELLOW};
	private GridBagConstraints c = new GridBagConstraints();
	private JLabel [][] celle;
	
	public int getCode ()
	{
		return 1;
	}
	
	public void update(List<List<Integer>> valori)
	{
		for (int i=0;i<valori.size();i++)
		{
			List<Integer> riga=valori.get(i);
			for (int j=0;j<riga.size();j++)
			{
				Integer valore=riga.get(j);
				JLabel cella=celle[i][j];
				cella.setBackground(this.colors[valore]);
				if (valore>0)
					cella.setEnabled(false);
		//			cella.removeMouseListener(cella.getMouseListeners()[0]);
				else
					cella.setEnabled(true);
			}
		}
		this.repaint();
	}
	
	public Gui (int rows)
	{
		super();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.setBounds(100, 100, 500, 500);
		celle=new JLabel [rows][rows];
		
		c.fill=GridBagConstraints.BOTH;
		c.gridwidth=1;
		c.gridheight=1;
		c.ipadx=0;
		c.ipady=0;
		c.insets=new Insets(1,1,1,1);
		c.anchor=GridBagConstraints.CENTER;
		
		for (int i=0;i<rows;i++)
		{
			for (int j=0;j<rows;j++)
			{
				c.gridx=j;
				c.gridy=i;
				final MyJLabel l=new MyJLabel(i, j);
				l.setPreferredSize(new Dimension(150,150));
				l.setOpaque(true);
				l.setBackground(Color.WHITE);
				
				l.addMouseListener(new MouseListener ()
						{

							@Override
							public void mouseClicked(MouseEvent arg0) {	
								if (!l.isEnabled())
									return;
								l.setBackground(Color.WHITE);
								Control.setMossaUtente(l.getPosition());
							}

							@Override
							public void mouseEntered(MouseEvent arg0) {}

							@Override
							public void mouseExited(MouseEvent arg0) {}

							@Override
							public void mousePressed(MouseEvent arg0) {}

							@Override
							public void mouseReleased(MouseEvent arg0) {}
							
						});
				
				celle[i][j]=l;
				this.add(l, c);
			}
		}
		
		this.setVisible(true);
		
	}
	
}

class MyJLabel extends JLabel
{
	private int [] position =new int [2];
	public MyJLabel (int x, int y)
	{
		super();
		position[0]=x;
		position[1]=y;
	}
	
	public int [] getPosition ()
	{
		return position;
	}
	
}

