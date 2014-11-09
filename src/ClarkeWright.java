import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Route implements Comparable<Route>
{
	private double _cost;
	private double _savings;
    private ArrayList<Customer> _customers;
    
	private void calculateSavings()
	{
		//cost of each 
		double originalCost = 0;
		double newCost = 0;
		double tempcost =0;
		Customer prev = null;
		for(Customer c:_customers)
		{ 
			tempcost = Math.sqrt((c.x*c.x)+(c.y*c.y));
			originalCost += (2.0*tempcost);
		
			if(prev != null)
			{
				//distance from previous customer to this customer
				double x = (prev.x - c.x);
				double y = (prev.y - c.y);
				newCost += Math.sqrt((x*x)+(y*y));
			}else{
				newCost += tempcost;
			}
			prev = c;
		}
		newCost += tempcost;
		
		for(Customer c:_customers)
		{
			//use squared distance for computational speed
			//originalCost += (c.x * c.x) + (c.y * c.y);
		}
		
		_cost = newCost;
		_savings = originalCost - newCost;
	}
	
	public Route(){
		_customers = new ArrayList<Customer>();
	}
    public void addCustomer(Customer c)
    {
    	_customers.add(c);
    	calculateSavings();
    }
    
    public double getSavings()
    {
    	return _savings;
    	
    };
    public double getCost()
    {
    	return _cost;
    }

	public int compareTo(Route r) {
		return (int) (r.getSavings() - this._savings);
	}

}

public class ClarkeWright {
	public static int truckCapacity = 100;
	
	
	
	public static ArrayList<List<Customer>> solve(ArrayList<Customer> customers)
	{
		ArrayList<List<Customer>> solution = new ArrayList<List<Customer>>();
		
		//calculate the savings of all the pairs
		ArrayList<Route> pairs = new ArrayList<Route>(); 
		
		for(int i=0; i<customers.size(); i++)
		{
			for(int j=i+1; j<customers.size(); j++)
			{
				Route r = new Route();
				r.addCustomer(customers.get(i));
				r.addCustomer(customers.get(j));
				pairs.add(r);
			}
		}
		//order pairs by savings
		Collections.sort(pairs);
		
		return solution;
	}
}
