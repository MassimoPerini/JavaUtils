import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class RandomPlayer implements Player{
	
	private Random random=new Random();
	private static int count=2;
	private int actCount;
	
	public RandomPlayer ()
	{
		super();
		actCount=count;
		count++;
	}
	
	public int getCode ()
	{
		return actCount;
	}
	
	public int [] move (List<List<Integer>> status, LinkedList <int[]> where)
	{
		int position=random.nextInt(where.size());
		return where.get(position);
	}
	

	
}
