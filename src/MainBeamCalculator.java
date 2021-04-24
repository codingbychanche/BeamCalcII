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
		myBeam.addBearing(new Support("Left bearing", 0,Support.ROLLER_SUPPORT));
		myBeam.addBearing(new Support("Right bearing", 4,Support.PIN_SUPPORT));

		// Show beam info
		System.out.println("Beam:");
		System.out.println("Length=" + myBeam.getLength());

		// Get supports sorted by distance from left end of beam, sort asc...
		List<Support> sortedBearings = new ArrayList();
		sortedBearings = myBeam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc();
		System.out.println("Bearings sorted by distance from left end of beam:");
		for (Support b : sortedBearings)
			System.out.println("Bearing " + b.getNameOfSupport() + "    xn=" + b.getDistanceFromLeftEndOfBeam_m());

		// Add loads

		// Name Force Dist Ang. Length
		// myBeam.addLoad(new Load("F0", -3, 0.0, 0, 0));
		// myBeam.addLoad(new Load("F0", -9, 0.0, 0, 0));

		// Name Force Dist Ang. Length
		//myBeam.addLoad(new Load("H1", -10.0, 2, 0, 0));
		//myBeam.addLoad(new Load("H2", -3, 1, 90, 0));
		//myBeam.addLoad(new Load("F3", 5, 3.5, 90, 0));
		//myBeam.addLoad(new Load("F3", -2, 2,0, 0));

		// Name Start_N // End_N // Dist. // Ang. // Length
		myBeam.addLoad(new Load("q1", -4.0, -4.0, 0.0, 0.0, 4));
		myBeam.addLoad(new Load("q2", -2.0, -4.0, 2, 0.0, 2));

		//myBeam.addLoad(new Load("F1", -2.5, 2, 0, 0));
		// myBeam.addLoad(new Load("F2", -3, 3, 0, 0));
		// myBeam.addLoad(new Load("F3",5, 3.5, 0, 0));

		// Get and show loads sorted by distance from left end of beam.
		List<Load> sortedLoads = new ArrayList();
		sortedLoads = myBeam.getLoadsSortedByDistanceFromLeftSupportDesc();
		System.out.println("");
		System.out.println("Number of Loads:" + myBeam.getNumberOfLoads());
		System.out.println("Loads sorted by distance from left end of beam:");
		for (Load l : sortedLoads)
			System.out.println("Name:" + l.getName() + " Magnitute at start:" + l.getForceStart_N() + " Single Load:"
					+ l.getForce_N() + " distance from left end:" + l.getDistanceFromLeftEndOfBeam_m() + " Lengtht:"
					+ l.getLengthOfLineLoad_m() + "  Force at end:" + l.getForceEnd_N() + " Resulting force:"
					+ l.getResultantForce_N() + " is acting " + l.getCenterOfGravity_m() + " m from left end of beam.");

		// Solve
		BeamResult result = BeamSolver.getResults(myBeam, "2f");

		// Show result or error's
		if (result.getErrorCount() == 0) {
			System.out.println("");
			System.out.println("RESULT:");
			System.out.println("Left bearing:" + result.getResultingForceAtLeftBearing_N() + " N. Right bearing:"
					+ result.getResultingForceAtRightBearing_N() + " N.");
			System.out.println("Horizontal force at right support:" + result.getResultingHorizontalForceAtRightSupport_N());
			System.out.println("Term:" + result.getSolutionTermForHorizontalForces());
			System.out.println("Term:" + result.getSolutionTermForRightBearing());
			System.out.println("Term:" + result.getSolutionTermForLeftBearing());
			
			System.out.println("");
			System.out.println("Shearing forces => Q");
			StressResultantTable qTable = QSolver.solve(myBeam);
			System.out.println("Maxima: x=" + qTable.getAbsoluteMaxima().getX_m() + "    Qmax="
					+ qTable.getAbsoluteMaxima().getShearingForce());
			System.out.println("Minimum: x=" + qTable.getAbsoluteMinimum().getX_m() + "    Qmin="
					+ qTable.getAbsoluteMinimum().getShearingForce());
			
			for (int n=0;n<=qTable.getLength()-1;n++) {
				System.out.println("x="+qTable.getShearingForceAtIndex(n).getX_m()+"   Q="+qTable.getShearingForceAtIndex(n).getShearingForce());
				
			}
			
			System.out.println("");
			System.out.println("Shearing forces => M");
			StressResultantTable mTable = MSolver.solve(qTable, myBeam);
			
			System.out.println("Maxima: x=" + mTable.getAbsoluteMaxima().getX_m() + "    Mmax="
					+ mTable.getAbsoluteMaxima().getShearingForce());
			System.out.println("Minimum: x=" + mTable.getAbsoluteMinimum().getX_m() + "    Mmin="
					+ mTable.getAbsoluteMinimum().getShearingForce());
				
			System.out.println("");
			System.out.println("Normal forces => N");
			StressResultantTable nTable=NSolver.solve(myBeam);
			System.out.println("x="+nTable.getAbsoluteMaxima().getX_m()+"     Nmax="+nTable.getAbsoluteMaxima().getShearingForce());
			System.out.println("x="+nTable.getAbsoluteMinimum().getX_m()+"     Nmin="+nTable.getAbsoluteMinimum().getShearingForce());
			
			/*
			for (StressResultantValue n : nTable.getShearingForceTable()) {
				System.out.println("x=" + n.getX_m() + "     N=" + n.getShearingForce());
			}		
			*/	
			
		} else
			showErrors(result);
	
		// Get shearing forces (Q)
		/*
		System.out.println("");
		System.out.println("Shearing forces => Q");
		StressResultantTable qTable = QSolver.solve(myBeam);

		for (StressResultantValue q : qTable.getShearingForceTable()) {
			System.out.println("x=" + q.getX_m() + "     Q=" + q.getShearingForce());
		}

		System.out.println("Maxima:" + qTable.getAbsoluteMaxima().getX_m() + "    Qmax="
				+ qTable.getAbsoluteMaxima().getShearingForce());
		System.out.println("Minimum:" + qTable.getAbsoluteMinimum().getX_m() + "    Qmin="
				+ qTable.getAbsoluteMinimum().getShearingForce());

		// Get bending moments (M)
		System.out.println("");
		System.out.println("Shearing forces => M");
		StressResultantTable mTable = MSolver.solve(qTable, myBeam);

		for (StressResultantValue m: mTable.getShearingForceTable()) {
		 System.out.println("x="+m.getX_m()+" M="+m.getShearingForce());
		}

		System.out.println("Maxima:" + mTable.getAbsoluteMaxima().getX_m() + "    Mmax="
				+ mTable.getAbsoluteMaxima().getShearingForce());
		System.out.println("Minimum:" + mTable.getAbsoluteMinimum().getX_m() + "    Mmin="
				+ mTable.getAbsoluteMinimum().getShearingForce());
				*/
				
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
