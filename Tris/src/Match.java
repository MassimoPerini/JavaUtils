import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Match {

	private int status [][];
	private LinkedList<int[]> emptyCells;
	private ArrayList <Player> players=new ArrayList <Player> ();
	
	public Match (int size, Player firstPlayer, Player secondPlayer) throws IllegalArgumentException
	{
		super();
		if (size<1)
			throw new IllegalArgumentException();
		this.status=new int [size][size];
		this.players.add(firstPlayer);
		this.players.add(secondPlayer);
		this.clearMatch();
	}
	
	public void clearMatch ()
	{
		for (int i=0;i<status.length;i++)
		{
			for (int j=0;j<status[0].length;j++)
			{
				status[i][j]=0;
			}
		}
		this.emptyCells=this.contaCelleRimanenti();
	}
	
	public boolean isFinished ()
	{
		if (null!=this.whoWon() || this.emptyCells.size()==0)
		{
			return true;
		}
		return false;
	}
	
	public Player whoWon ()
	{
		//TODO interrompi appena vincitore
	    int test= controllaOrizzontali();
	    if (test<0)
	    	test=controllaVerticali();
	    if (test<0)
	    	test=controllaDiagonali();
	    if (test<0)
	    	return null;
	    return this.players.get(test);

	}
	
	public LinkedList<int[]> getEmptyCells ()
	{
		return this.emptyCells;
	}
	
	public void setMovement (int [] where, int who)
	{
		status[where[0]][where[1]]=who;
		this.emptyCells=this.contaCelleRimanenti();
	}
	
	private LinkedList<int[]> contaCelleRimanenti (){
	    LinkedList<int[]> indiciCella=new LinkedList<int[]> ();
	    
	    for (int i=0;i<this.status.length;i++){
	        for (int j=0;j<this.status[i].length;j++){
	        	boolean isEmpty=true;
	        	for (int k=0;k<this.players.size();k++)
	        	{
	        		if (this.status[i][j]==k+1)
	        			isEmpty=false;
	        	}
	        	if (isEmpty)
	        	{
	        		int [] indici={i,j};
	        		indiciCella.add(indici);
	        	}
	        }
	    }
	    return indiciCella;
	}
	
	public List<List<Integer>> getStatus ()
	{
		LinkedList <List<Integer>> outList=new LinkedList <List<Integer>> ();
		for (int i=0;i<this.status.length;i++)
		{
			LinkedList <Integer> tmpList=new LinkedList <Integer> ();
			for (int j=0;j<this.status[i].length;j++)
			{
				Integer val=status[i][j];
				tmpList.add(val);
			}
			Collections.unmodifiableList(tmpList);
			outList.add(tmpList);
		}
		return Collections.unmodifiableList(outList);
	}
	
	private int controllaOrizzontali ()
	{
		int test=-1;
	    for (int i=0;i<this.status.length;i++){
	        test=checkWinner(status[i]);
	        if (test>=0)
	            return test;
	    }
	    return -1;
	}
	
	private int controllaVerticali (){
	    for (int i=0;i<this.status.length;i++ ){
	        int [] arrTest=new int [status.length];
	        for (int j = 0;j<this.status.length;j++){
	            arrTest[j]=this.status[j][i];
	        }
	        int test=checkWinner(arrTest);
	        if (test>=0)
	            return test;
	    }
	    return -1;
	}

	private int controllaDiagonali (){
	    int arrTest[]=new int [this.status.length];
	    for (int i=0;i<this.status.length;i++){
	        arrTest[i]=this.status[i][i];
	    }
	    int test=checkWinner(arrTest);
	    if (test<0){
		    arrTest=new int [this.status.length];
	        for (int i=0;i<this.status.length;i++){
	            arrTest[i]=this.status[i][this.status.length-1-i];
	        }
	        return checkWinner(arrTest);
	    }
	    return test;
	}	
	
	private int checkWinner (int [] arr){
		
	    for (int i=0;i<players.size();i++)
	    {
	    	int count=0;
	    	for (int j=0;j<arr.length;j++)
	    	{
	    		if (arr[j]==i+1){
	    			count++;
	    		}
	    	}
	    	if (count==arr.length){
	    		return i;
	    	}
	    }
	    return -1;
	}
	
}
