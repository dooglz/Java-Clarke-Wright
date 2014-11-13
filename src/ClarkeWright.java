import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

class Route implements Comparable<Route>
{
	private int _capacity;
	private int _weight;
	private double _cost;
	private double _savings;
	public ArrayList<Customer> customers;

	private void calculateSavings(){
		double originalCost = 0;
		double newCost = 0;
		double tempcost =0;
		Customer prev = null;

		//Foreach customer in the route:
		for(Customer c:customers){ 
			// Distance from Depot
			tempcost = Math.sqrt((c.x*c.x)+(c.y*c.y));
			originalCost += (2.0*tempcost);

			if(prev != null){
				// Distance from previous customer to this customer
				double x = (prev.x - c.x);
				double y = (prev.y - c.y);
				newCost += Math.sqrt((x*x)+(y*y));
			}else{
				//If this is the first customer in the route, no change
				newCost += tempcost;
			}
			prev = c;
		}
		newCost += tempcost;
		_cost = newCost;
		_savings = originalCost - newCost;
	}

	public Route(int capacity){
		_capacity = capacity;
		customers = new ArrayList<Customer>();
		_weight =0;
		_cost= 0;
		_savings =0;
	}

	public void addCustomer(Customer c, boolean order){
		//Add customer to the start or end of the route?
		if(order){
			customers.add(0,c);
		}else{
			customers.add(c);
		}

		if(c.c > _capacity){
			System.out.println("Customer order too large");
		}

		_weight += c.c;

		if(_weight > _capacity){
			System.out.println("Route Overloaded");
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
		return Double.compare(r.getSavings(), this._savings);
	}

}

//
//##Sequential solver##
//

public class ClarkeWright 
{
	public static int truckCapacity = 0;

	public static ArrayList<List<Customer>> solve(ArrayList<Customer> customers){
		ArrayList<List<Customer>> solution = new ArrayList<List<Customer>>();
		HashSet<Customer> abandoned = new HashSet<Customer>();

		//calculate the savings of all the pairs
		ArrayList<Route> pairs = new ArrayList<Route>(); 

		for(int i=0; i<customers.size(); i++){
			for(int j=i+1; j<customers.size(); j++){
				Route r = new Route(truckCapacity);
				r.addCustomer(customers.get(i),false);
				r.addCustomer(customers.get(j),false);
				pairs.add(r);
			}
		}
		//order pairs by savings
		Collections.sort(pairs);		

		HashSet<Route> routes = new HashSet<Route>();
		routes.add(pairs.get(0));
		pairs.remove(0);
		
		//start combining pairs into routes
		for(Route ro :routes)
		{
			Customer cr1 = ro.customers.get(0);
			Customer cr2 = ro.customers.get(ro.customers.size()-1);
			
			for(int i=0; i<pairs.size(); i++){
				Route r = pairs.get(i);
				Customer c1 = r.customers.get(0);
				Customer c2 = r.customers.get(r.customers.size()-1);
				
				//do they have any common nodes?
				if(c1 == cr1 || c1 == cr2){
					//could we combine these based on weight?
					if(c2.c + ro.getWeight() <= truckCapacity){
						//Does the route already contain BOTH these nodes?
						if(!ro.customers.contains(c2)){
							//no, but is it in another route already?
							boolean istaken = false;
							for(Route rr :routes)
							{
								if(rr.customers.contains(c2)){
									istaken = true;
									break;
								}
							}
							if(!istaken){
								//No other route have this, add to route
								if(c1 == cr1){
									ro.addCustomer(c2, true);
								}else{
									ro.addCustomer(c2, false);
								}
							}
						}
						abandoned.remove(c2);
						pairs.remove(r);
						i--;
						continue;
					}
				}
				if (c2 == cr1 || c2 ==cr2){
					//could we combine these based on weight?
					if(c1.c + ro.getWeight() <= truckCapacity){
						//Does the route already contain BOTH these nodes?
						if(!ro.customers.contains(c1)){
							//no, but is it in another route already?
							boolean istaken = false;
							for(Route rr :routes)
							{
								if(rr.customers.contains(c1)){
									istaken = true;
									break;
								}
							}
							if(!istaken){
								if(c2 == cr1){
									ro.addCustomer(c1, true);
								}else{
									ro.addCustomer(c1, false);
								}
							}
						}
						abandoned.remove(c1);
						pairs.remove(r);
						i--;
						continue;
					}
				}
				
				//If we reach here, the pair hasn't been added to any routes			
				boolean a = false;
				boolean b = false;
				for(Route rr :routes)
				{
					for(Customer cc:r.customers){
						if(rr.customers.contains(c1)){
							a = true;
						}
						if(rr.customers.contains(c2)){
							b = true;
						}
					}
				}
				if(!(a||b)){
					//no routes have any of these customers, make new route
					abandoned.remove(c1);
					abandoned.remove(c2);
					routes.add(r);
				}else{
					//Some routes have some of these customers already
					if(!a){
						abandoned.add(c1);
					}
					if(!b){
						abandoned.add(c2);
					}
				}
				pairs.remove(r);
				i--;
				
			}
			
		}

		//Edge case: A single Customer can be left out of all routes due to capacity constraints
		outerloop:for(Customer C:abandoned){
			//we could tack this onto the end of a route if it would fit
			for(Route r:routes){
				if(r.getWeight() + C.c < truckCapacity)
				{
					//would this be more efficient than sending a new truck?
					Customer cc = r.customers.get(r.customers.size()-1);
					{
						double X = C.x - cc.x;
						double Y = C.y - cc.y;	
						if(Math.sqrt((X*X)+(Y*Y)) < Math.sqrt((C.x*C.x)+(C.y*C.y))){
							r.addCustomer(C, false);
							break outerloop;
						}
					}
					cc  = r.customers.get(0);
					{
						double X = C.x - cc.x;
						double Y = C.y - cc.y;	
						if(Math.sqrt((X*X)+(Y*Y)) < Math.sqrt((C.x*C.x)+(C.y*C.y))){
							r.addCustomer(C, true);
							break outerloop;
						}
					}
				}
			}
			
			//Send a new truck, just for this Customer
			ArrayList<Customer> l = new ArrayList<Customer>();
			l.add(C);
			solution.add(l);
		}

		//output
		for(Route r:routes){
			ArrayList<Customer> l = new ArrayList<Customer>();
			l.addAll(r.customers);
			solution.add(l);
		}
		return solution;
	}
	
	//
	//##Parallel solver##
	//

	public static ArrayList<List<Customer>> solveP(ArrayList<Customer> customers){
		ArrayList<List<Customer>> solution = new ArrayList<List<Customer>>();
		HashSet<Customer> abandoned = new HashSet<Customer>();

		//calculate the savings of all the pairs
		ArrayList<Route> pairs = new ArrayList<Route>(); 

		for(int i=0; i<customers.size(); i++){
			for(int j=i+1; j<customers.size(); j++){
				Route r = new Route(truckCapacity);
				r.addCustomer(customers.get(i),false);
				r.addCustomer(customers.get(j),false);
				pairs.add(r);
			}
		}
		//order pairs by savings
		Collections.sort(pairs);		

		HashSet<Route> routes = new HashSet<Route>();
		routes.add(pairs.get(0));
		pairs.remove(0);

		//start combining pairs into routes
		outerloop: for(int j=0; j<pairs.size(); j++){
			Route r = pairs.get(j);
			Customer c1 = r.customers.get(0);
			Customer c2 = r.customers.get(r.customers.size()-1);

			for(Route ro :routes)
			{
				Customer cr1 = ro.customers.get(0);
				Customer cr2 = ro.customers.get(ro.customers.size()-1);

				//do they have any common nodes?
				if(c1 == cr1 || c1 == cr2){
					//could we combine these based on weight?
					if(c2.c + ro.getWeight() <= truckCapacity){
						//Does the route already contain BOTH these nodes?
						if(!ro.customers.contains(c2)){
							//no, but is it in another route already?
							boolean istaken = false;
							for(Route rr :routes)
							{
								if(rr.customers.contains(c2)){
									istaken = true;
									break;
								}
							}
							if(!istaken){
								//No other route have this, add to route.
								if(c1 == cr1){
									ro.addCustomer(c2, true);
								}else{
									ro.addCustomer(c2, false);
								}
							}
						}
						abandoned.remove(c2);
						pairs.remove(r);
						j--;
						continue outerloop;
					}
				}
				if (c2 == cr1 || c2 ==cr2){
					//could we combine these based on weight?
					if(c1.c + ro.getWeight() <= truckCapacity){
						//Does the route already contain BOTH these nodes?
						if(!ro.customers.contains(c1)){
							//no, but is it in another route already?
							boolean istaken = false;
							for(Route rr :routes)
							{
								if(rr.customers.contains(c1)){
									istaken = true;
									break;
								}
							}
							if(!istaken){
								if(c2 == cr1){
									ro.addCustomer(c1, true);
								}else{
									ro.addCustomer(c1, false);
								}
							}
						}
						abandoned.remove(c1);
						pairs.remove(r);
						j--;
						continue outerloop;
					}
				}
			}

			//If we reach here, the pair hasn't been added to any routes			
			boolean a = false;
			boolean b = false;
			for(Route ro :routes)
			{
				for(Customer cc:r.customers){
					if(ro.customers.contains(c1)){
						a = true;
					}
					if(ro.customers.contains(c2)){
						b = true;
					}
				}
			}
			if(!(a||b)){
				//no routes have any of these customers, make new route
				abandoned.remove(c1);
				abandoned.remove(c2);
				routes.add(r);
			}else{
				//Some routes have some of these customers already
				if(!a){
					abandoned.add(c1);
				}
				if(!b){
					abandoned.add(c2);
				}
			}
			pairs.remove(r);
			j--;

		}

		//Edge case: A single Customer can be left out of all routes due to capacity constraints
		outerloop:for(Customer C:abandoned){
			//we could tack this onto the end of a route if it would fit
			for(Route r:routes){
				if(r.getWeight() + C.c < truckCapacity)
				{
					//would this be more efficient than sending a new truck?
					Customer cc = r.customers.get(r.customers.size()-1);
					{
						double X = C.x - cc.x;
						double Y = C.y - cc.y;	
						if(Math.sqrt((X*X)+(Y*Y)) < Math.sqrt((C.x*C.x)+(C.y*C.y))){
							r.addCustomer(C, false);
							break outerloop;
						}
					}
					cc  = r.customers.get(0);
					{
						double X = C.x - cc.x;
						double Y = C.y - cc.y;	
						if(Math.sqrt((X*X)+(Y*Y)) < Math.sqrt((C.x*C.x)+(C.y*C.y))){
							r.addCustomer(C, true);
							break outerloop;
						}
					}
				}
			}
			
			//Send a new truck, just for this Customer
			ArrayList<Customer> l = new ArrayList<Customer>();
			l.add(C);
			solution.add(l);
		}

		//output
		for(Route r:routes){
			ArrayList<Customer> l = new ArrayList<Customer>();
			l.addAll(r.customers);
			solution.add(l);
		}
		return solution;
	}
}


















