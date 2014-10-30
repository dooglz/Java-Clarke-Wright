import java.util.ArrayList;
import java.io.*;

public class VRTests {

	public static void main(String[] args)throws Exception {
		String problemdir = "tests/";
		String outdir = "output/";
		String [] shouldPass = {
				"rand00010",
				"rand00020",
				"rand00030",
				"rand00040",
				"rand00050",
				"rand00060",
				"rand00070",
				"rand00080",
				"rand00090",
				"rand00100",
				"rand00200",
				"rand00300",
				"rand00400",
				"rand00500",
				"rand00600",
				"rand00700",
				"rand00800",
				"rand00900",
				"rand01000"
				};
		String [] shouldFail = {
				"fail00002",
				"fail00004"
				};
		System.out.println("\nShould Pass");
		System.out.println("Problem     \tSoln\tSize\tCost\tValid");
		for (String base:shouldPass){
			VRProblem vrp = new VRProblem(problemdir+base+"prob.csv");
			VRSolution vrs = new VRSolution(vrp);

			//Create a new solution using our poor algorithm
			vrs.oneRoutePerCustomerSolution();

			System.out.printf("%s\t%s\t%d\t%.0f\t%s\n",base,"dumb",vrp.size(),vrs.solnCost(),vrs.verify());
			vrs.writeSVG(outdir+base+"prob.svg",outdir+base+"dmsn.svg");
			if (new File(problemdir+base+"cwsn.csv").exists()){
				vrs.readIn(problemdir+base+"cwsn.csv");

				//Print out results of costing and verifying the solution
				System.out.printf("%s\t%s\t%d\t%.0f\t%s\n",base,"CW",vrp.size(),vrs.solnCost(),vrs.verify());
				
				//Write the SVG file
				vrs.writeSVG(outdir+base+"prob.svg",outdir+base+"cwsn.svg");
			}
		}
		System.out.println("\nShould Fail");
		System.out.println("Problem\tSolution\tSize\tCost\tValid");
		for (String b:shouldFail){
			VRProblem vrp = new VRProblem(problemdir+b+"prob.csv");
			VRSolution vrs = new VRSolution(vrp);
			if (new File(problemdir+b+"soln.csv").exists()){

				//Read an existing solution file
				vrs.readIn(problemdir+b+"soln.csv");

				//Print out results of costing and verifying the solution
				System.out.printf("%s\t%s\t%d\t%.0f\t%s\n",b,b,vrp.size(),vrs.solnCost(),vrs.verify());
			
				//Write the SVG file
				vrs.writeSVG(outdir+b+"prob.svg", outdir+b+"soln.svg");
			}
		}
	}
}
