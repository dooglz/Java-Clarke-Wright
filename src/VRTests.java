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
				"rand00150",
				"rand00200",
				"rand00250",
				"rand00300",
				"rand00350",
				"rand00400",
				"rand00450",
				"rand00500",
				"rand00550",
				"rand00600",
				"rand00650",
				"rand00700",
				"rand00750",
				"rand00800",
				"rand00850",
				"rand00900",
				"rand00950",
				"rand01000",
				"fail00002",
				"fail00004"
				};
		//System.out.println("Problem     \tSoln\tCusts\tTrips\tCost\tValid");
		System.out.println("Custs\tCW\tCWP\tOC\tCwC\tCwpC");
		for (String base:shouldPass){
		
		//	System.out.println(base+"------");
			VRProblem vrp = new VRProblem(problemdir+base+"prob.csv");
			VRSolution vrs = new VRSolution(vrp);
			VRSolution vrps = new VRSolution(vrp);
			VRSolution vrds = new VRSolution(vrp);

			//Print out results of costing and verifying the solution
			vrds.oneRoutePerCustomerSolution();
			//System.out.printf("%s\t%s\t%d\t%d\t%.0f\t%s\n",base,"Dumb",vrp.size(),vrds.soln.size(),vrds.solnCost(),vrds.verify());
			
			vrs.clarkeWrightSolution(false);
			//System.out.printf("%s\t%s\t%d\t%d\t%.0f\t%s\n",base,"CW",vrp.size(),vrs.soln.size(),vrs.solnCost(),vrs.verify());
			
			vrps.clarkeWrightSolution(true);
			//System.out.printf("%s\t%s\t%d\t%d\t%.0f\t%s\n",base,"CWP",vrp.size(),vrps.soln.size(),vrps.solnCost(),vrps.verify());

			System.out.printf("%d\t%d\t%d\t%.0f\t%.0f\t%.0f\t\n",vrp.size(),vrs.soln.size(),vrps.soln.size(),vrds.solnCost(),vrs.solnCost(),vrps.solnCost());
			
			//Write the SVG file
			vrds.writeSVG(outdir+base+"prob.svg",outdir+base+"dumbsn.svg");
			vrs.writeSVG(outdir+base+"prob.svg",outdir+base+"cwsn.svg");
			vrps.writeSVG(outdir+base+"prob.svg",outdir+base+"cwpsn.svg");
		}
		
	}
}
