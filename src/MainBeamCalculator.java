import org.berthold.beamCalc.*;
import java.util.*;

/**
 * Library demo.
 * 
 * 
 * @author Berthold
 *
 */
public class MainBeamCalculator {

	public static void main(String[] args) {

		// Create a new beam
		Beam myBeam = new Beam(7.5);
		myBeam.addBearing(new Support("Left bearing", 0));
		myBeam.addBearing(new Support("Right bearing", 7.5));

		// Add loads
		// Name Force Dist Ang. Length
		// myBeam.addLoad(new Load("F0", -3, 0.0, 0, 0));
		// myBeam.addLoad(new Load("F0", -9, 0.0, 0, 0));
		//myBeam.addLoad(new Load("F1", -3.0, 1.5, 0, 0));
		// myBeam.addLoad(new Load("F2", -2.5, 1.5, 179, 0));

		// Name Start_N End_N Dist. Ang. Length
		myBeam.addLoad(new Load("q1", 0, -4.0, 0.0, 0.0, 7.5));

		// Solve
		BeamResult result = BeamSolver.getResults(myBeam, "2f");

		// Show result or error's
		if (result.getErrorCount() == 0) {
			System.out.println("Beam. Loads:" + myBeam.getNumberOfLoads());
			System.out.println("Left bearing:" + result.getResultingForceAtLeftBearing_N() + " N. Right bearing:"
					+ result.getResultingForceAtRightBearing_N() + " N.");
			System.out.println("Horizontal:" + result.getSumOfHorizontalForcesIn_N());
			System.out.println("Term:" + result.getSolutionTermForHorizontalForces());
			System.out.println("Term:" + result.getSolutionTermForRightBearing());
			System.out.println("Term:" + result.getSolutionTermForLeftBearing());
		} else
			showErrors(result);

		// Get loads sorted by distance from left end of beam, sort asc...
		List<Load> sortedLoads = new ArrayList();
		sortedLoads = myBeam.getLoadsSortedByDistanceFromLeftSupportDesc();

		System.out.println("Loads sorted by distance from left end of beam:");
		for (Load l : sortedLoads)
			System.out.println(
					"Name:" + l.getName() + " Magnitute:" + l.getForce_N() +" xn="+l.getDistanceFromLeftEndOfBeam_m() +" Lengtht:" + l.getLengthOfLineLoad_m());

		// Get bearings sorted by distance from left end of beam, sort asc...
		List<Support> sortedBearings = new ArrayList();
		sortedBearings = myBeam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc();

		System.out.println("Bearings sorted by distance from left end of beam:");
		for (Support b : sortedBearings)
			System.out.println("Bearing "+b.getNameOfSupport()+ "    xn=" + b.getDistanceFromLeftEndOfBeam_m());
	}

	/*
	 * Show a description of all errors occurred.....
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
				System.out.println("Error for bearing:" + error.getErrorDescription());
		}
	}

}
