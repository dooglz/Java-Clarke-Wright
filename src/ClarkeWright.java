import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Route implements Comparable<Route>
{
	private int _weight;
	private double _cost;
	private double _savings;
    public ArrayList<Customer> customers;
    
	private void calculateSavings()
	{
		double originalCost = 0;
		double newCost = 0;
		double tempcost =0;
		Customer prev = null;
		
		for(Customer c:customers)
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
		_cost = newCost;
		_savings = originalCost - newCost;
	}
	
	public Route(){
		customers = new ArrayList<Customer>();
		_weight =0;
		_cost= 0;
		_savings =0;
	}
	
    public void addCustomer(Customer c, boolean order){
    	if(order){
    		customers.add(0,c);
    	}else{
    		customers.add(c);
    	}
    	if(c.c > 100){
    		System.out.println("SINGLE CUSTOMER WITH TOO MUCH");
    	}
    	_weight += c.c;
    	
    	if(_weight > 100){
    		System.out.println("Route Overlaoded");
    	}
    	calculateSavings();
    }
    
    public double getSavings(){
    	return _savings;
    }
    public double getCost(){
    	return _cost;
    }
    public int getWeight(){
    	return _weight;
    }
	public int compareTo(Route r) {
		//System.out.println(r.getSavings()+" --- "+this._savings);
		return Double.compare(r.getSavings(), this._savings);
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
				r.addCustomer(customers.get(i),false);
				r.addCustomer(customers.get(j),false);
				pairs.add(r);
			}
		}
		//order pairs by savings
		Collections.sort(pairs);
		
		//start combining routes
		for(int i=0; i<pairs.size(); i++)
		{
			Route ro = pairs.get(i);
			
			for(int j=1; j<pairs.size(); j++)
			{
				Route r = pairs.get(j);
				Customer c1 = r.customers.get(0);
				Customer c2 = r.customers.get(r.customers.size()-1);
				Customer cr1 = ro.customers.get(0);
				Customer cr2 = ro.customers.get(ro.customers.size()-1);
				//do they have any common nodes?
				if(c1 == cr1){
					//could we combine these based on weight?
					if(c2.c + ro.getWeight() < 100){
						ro.addCustomer(c2, true);
						pairs.remove(j);
					}
				}else if (c1 == cr2){
					if(c2.c + ro.getWeight() < 100){
						ro.addCustomer(c2, false);
						pairs.remove(j);
					}
				}else if (c2 == cr1){
					if(c1.c + ro.getWeight() < 100){
						ro.addCustomer(c1, true);
						pairs.remove(j);
					}
				}else if (c2 ==cr2){
					if(c1.c + ro.getWeight() < 100){
						ro.addCustomer(c1, false);
						pairs.remove(j);
					}
				}
			}
			//Now remove any routes that have any of the already visited customers
			for(int j=1; j<pairs.size(); j++)
			{
				Route r = pairs.get(j);
				if (!Collections.disjoint(r.customers, ro.customers))
				{
					pairs.remove(r);
				}
			}
		}
		
		//output
		for(Route r:pairs)
		{
			ArrayList<Customer> l = new ArrayList<Customer>();
			l.addAll(r.customers);
			solution.add(l);
		}
		return solution;
	}
}


















