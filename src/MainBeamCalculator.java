import org.berthold.beamCalc.*;
import java.util.*;

/**
 * Library demo.
 * 
 * Shows how to use the library.
 * 
 * @author Berthold
 */
public class MainBeamCalculator {

	/**
	 * Main
	 * 
	 * @param args Not used.
	 */
	public static void main(String[] args) {

		// Create a new beam
		Beam myBeam = new Beam(4);
		myBeam.addBearing(new Support("A (Left)", 1.5, Support.ROLLER_SUPPORT));
		myBeam.addBearing(new Support("B (Right)", 4, Support.PIN_SUPPORT));
		
		// Add load
		// NAME/ Force/ Distance/ Angle /Length
		myBeam.addLoad(new Load("F1", -2.0, 2, 10, 0));
		myBeam.addLoad(new Load("q1", -5, 0, 0,4));

		// Show beam info
		System.out.println("Beam:");
		System.out.println("Length=" + myBeam.getLength());

		// Get supports sorted by distance from left end of beam, sort asc...
		List<Support> sortedBearings = new ArrayList<>();
		sortedBearings = myBeam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc();
		System.out.println("Supports sorted by distance from left end of beam:");
		for (Support b : sortedBearings)
			System.out.println("Support " + b.getNameOfSupport() + "    xn=" + b.getDistanceFromLeftEndOfBeam_m());

		// Get and show loads sorted by distance from left end of beam.
		List<Load> sortedLoads = new ArrayList<>();
		sortedLoads = myBeam.getLoadsSortedByDistanceFromLeftSupportDesc();
		System.out.println("");
		System.out.println("Number of Loads:" + myBeam.getNumberOfLoads());
		System.out.println("Loads sorted by distance from left end of beam:");
		for (Load l : sortedLoads)
			System.out.println("Name:" + l.getName() + " Magnitute at start:" + l.getForceStart_N() + " Point Load:"
					+ l.getForce_N() + " distance from left end:" + l.getDistanceFromLeftEndOfBeam_m() + " Lengtht:"
					+ l.getLengthOfLineLoad_m() + "  Force at end:" + l.getForceEnd_N() + " Resulting force:"
					+ l.getResultantForce_N() + " is acting " + l.getCenterOfGravity_m() + " m from left end of beam.");

		// Solve
		BeamResult result = BeamSolver.getResults(myBeam, "2f");

		// Show results or error's
		//
		// First order of business: One shlould check for errors first.
		if (result.getErrorCount() == 0) {
			System.out.println("");
			System.out.println("RESULT:");
			System.out.println("Left support:" + result.getResultingForceAtLeftBearing_N() + " N. Right bearing:"
					+ result.getResultingForceAtRightBearing_N() + " N.");
			System.out.println(
					"Horizontal force at right support:" + result.getResultingHorizontalForceAtRightSupport_N());
			System.out.println("Term:" + result.getSolutionTermForHorizontalForces());
			System.out.println("Term:" + result.getSolutionTermForRightBearing());
			System.out.println("Term:" + result.getSolutionTermForLeftBearing());

			//
			// Shearing forces Q
			//
			System.out.println("");
			System.out.println("=== Shearing forces => Q ===");
			StressResultantTable qTable = QSolver.solve(myBeam,"N");
			
			List <StressResultant> maxima=new ArrayList<>();
			maxima=qTable.getMaxima();
			
			System.out.println("Local maxi-/  minima");
			for (StressResultant r:maxima) 
					System.out.println("x=" + r.getX_m() + " m   Q=" + r.getShearingForce()+" "+r.getUnit());
			
			List <StressResultant> dis=new ArrayList<>();
			dis=qTable.getDiscontinuitys();
			System.out.println("Points of disconuity in Q");
			for (StressResultant r:dis) 
					System.out.println("x=" + r.getX_m() + " m   Q=" + r.getShearingForce()+" "+r.getUnit());
			
			StressResultantDraw d=new StressResultantDraw("Q",myBeam,qTable,600,1200,10,10,"%.2f");
			d.draw();

			//
			// Bending moment M
			//
			System.out.println("");
			System.out.println("=== Bending moment => M ===");
			StressResultantTable mTable = MSolver.solve(qTable, myBeam,"Nm");

			maxima=mTable.getMaxima();
			
			System.out.println("Local maxi-/  minima");
			for (StressResultant r:maxima) 
					System.out.println("x=" + r.getX_m() + " m   M=" + r.getShearingForce()+" "+r.getUnit());

			dis=mTable.getDiscontinuitys();
			System.out.println("Points of disconuity in M");
			for (StressResultant r:dis) 
					System.out.println("x=" + r.getX_m() + " m   M=" + r.getShearingForce()+" "+r.getUnit());
		
			StressResultantDraw m=new StressResultantDraw("M",myBeam,mTable,600,1200,10,10,"%.2f");
			m.draw();
	
			//
			// N
			//
			System.out.println("");
			System.out.println("=== Normal forces => N ===");
			StressResultantTable nTable = NSolver.solve(myBeam,"N");

			for (int n = 0; n <= nTable.getLength() - 1; n++) {
				StressResultant v = nTable.getShearingForceAtIndex(n);

				if (v.isDiscontiunuity())
					System.out.println("x=" + v.getX_m() + " m   N[N]=" + v.getShearingForce()+" "+v.getUnit());
			}
			
			StressResultantDraw n=new StressResultantDraw("N",myBeam,nTable,600,1200,10,10,"%.2f");
			n.draw();

		} else
			showErrors(result);
	}

	/*
	 * Show a description of all errors occured.....
	 */
	private static void showErrors(BeamResult result) {
		int errorCount = result.getErrorCount();
		System.out.println("Errors:" + errorCount);

		for (int i = 0; i <= errorCount - 1; i++) {
			BeamCalcError error = result.getError(i);
			if (error.getOriginOfError() == BeamCalcError.LOAD_ERROR)
				System.out.println(
						"Error for load #" + error.getIndexOfError() + " Reason:" + error.getErrorDescription());
			if (error.getOriginOfError() == BeamCalcError.SUPPORT_ERROR)
				System.out.println("Error for support:" + error.getErrorDescription());
		}
	}
}
