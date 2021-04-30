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
		myBeam.addBearing(new Support("Left bearing", 0, Support.ROLLER_SUPPORT));
		myBeam.addBearing(new Support("Right bearing", 4, Support.PIN_SUPPORT));

		// Add loads
		// NAME/ Force/ Distance/ Angle /Length
		//myBeam.addLoad(new Load("F1", -2.0, 2, 0, 0));
		//myBeam.addLoad(new Load("F2", -3, 4,0, 0));
		myBeam.addLoad(new Load("q1", -5, 0, 0, 4));
		//myBeam.addLoad(new Load("q2", -2, 1, 0, 3));
		//myBeam.addLoad(new Load("q3", -4, 2, 0, 2));

		// Show beam info
		System.out.println("Beam:");
		System.out.println("Length=" + myBeam.getLength());

		// Get supports sorted by distance from left end of beam, sort asc...
		List<Support> sortedBearings = new ArrayList<>();
		sortedBearings = myBeam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc();
		System.out.println("Bearings sorted by distance from left end of beam:");
		for (Support b : sortedBearings)
			System.out.println("Bearing " + b.getNameOfSupport() + "    xn=" + b.getDistanceFromLeftEndOfBeam_m());

		// Get and show loads sorted by distance from left end of beam.
		List<Load> sortedLoads = new ArrayList<>();
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

		// Show results or error's
		//
		// First order of business: One shlould check for errors first.
		if (result.getErrorCount() == 0) {
			System.out.println("");
			System.out.println("RESULT:");
			System.out.println("Left bearing:" + result.getResultingForceAtLeftBearing_N() + " N. Right bearing:"
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
			System.out.println("Shearing forces => Q");
			StressResultantTable qTable = QSolver.solve(myBeam);

			for (int n = 0; n <= qTable.getLength() - 1; n++) {
				StressResultant v = qTable.getShearingForceAtIndex(n);

				if (v.isDiscontiunuity())
					System.out.println("x=" + v.getX_m() + " m Delta=" + v.getShearingForceDeltaBy() + " N   Q[N]="+
							+ v.getShearingForce()+" N");
			}
			
			//for (StressResultant r:qTable.getShearingForceTable()) {
			//	System.out.println("x="+r.getX_m()+"    Q="+r.getShearingForce()+" DIS="+r.isDiscontiunuity()+"    Zero="+r.isZeroPoint());
			//}

			//
			// Bending moments M
			//
			System.out.println("");
			System.out.println("Shearing forces => M");
			StressResultantTable mTable = MSolver.solve(qTable, myBeam);

			for (int n = 0; n <= mTable.getLength() - 1; n++) {
				StressResultant v = mTable.getShearingForceAtIndex(n);

				if (v.isDiscontiunuity())
					System.out.println("x=" + v.getX_m() + " m   M=" + v.getShearingForce()+" Nm");
			}
			
			//for (StressResultant r:mTable.getShearingForceTable()) {
			//	System.out.println("x="+r.getX_m()+"    M="+r.getShearingForce());
			//}
	
			//
			// N
			//
			System.out.println("");
			System.out.println("Normal forces => N");
			StressResultantTable nTable = NSolver.solve(myBeam);

			for (int n = 0; n <= nTable.getLength() - 1; n++) {
				StressResultant v = nTable.getShearingForceAtIndex(n);

				if (v.isDiscontiunuity())
					System.out.println("x=" + v.getX_m() + " m   N[N]=" + v.getShearingForce()+" N");
			}

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
