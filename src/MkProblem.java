/*
 * Used to generate random instances of the Vehicle Routing Problem
 * Duplicate customer addresses are prevented
 * This was used to generate the problem set
 * Students do not need to run this themselves - but they may wish to generate different sized problems
 */
import java.util.*;
import java.io.*;
public class MkProblem {

	public static void main(String[] args) throws Exception {
		int diameter_of_world = 500;
		//Coordinates of the depot
		int cx = 250;
		int cy = 250;
		//Capacity of the truck(s)
		int capacity = 50;
		//Maximum load required by a customer
		int max_requirement = 10;
		for (int ps:new int[] {
				10, 20, 30, 40, 50, 60, 70, 80, 90, // Small data sets - suitable for a quadratic algorithm 
				100,200,300,400,500,500, 600, 700, 800, 900        // Larger data sets - suitable for a linear algorithm
				}){
			String filename = String.format("rand%05dprob.csv", ps);
			PrintWriter pw = new PrintWriter(filename);
			pw.printf("%d,%d,%d\n",cx,cy,capacity);
			HashSet<String> unq = new HashSet<String>();
			for(int i=0;i<ps;i++){
				int x = (int)(Math.random()*diameter_of_world);
				int y = (int)(Math.random()*diameter_of_world);
				while (unq.contains(x+" "+y)){
					x = (int)(Math.random()*diameter_of_world);
					y = (int)(Math.random()*diameter_of_world);					
				}
				unq.add(x+" "+y);
				pw.printf("%d,%d,%d\n",
						(int)(x),
						(int)(y),
						(int)(Math.random()*max_requirement)+1
						);
			}
			pw.close();
		}
		
	}

}
