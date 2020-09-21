/**
 * A simple driver
 * 
 * Create a beam, add loads, solve.
 * 
 */
import org.berthold.beamCalc.*;
import java.util.*;

public class MainBeamCalculator {

	public static void main(String[] args) {

		Beam myBeam = new Beam(3);

		myBeam.addBearing(new Bearing("Left bearing",0));
		myBeam.addBearing(new Bearing("Right bearing",3));
		
		//						Name	Force		Dist		Ang.	Length
		//myBeam.addLoad(new Load("F0",		-3,		0.0,		0,		0));
		//myBeam.addLoad(new Load("F0",		-9,		0.0,		0,		0));
		myBeam.addLoad(new Load("q1",		-3,		0.0,		0,		2.0));
		myBeam.addLoad(new Load("F1",		-1.5,	1.5,		-45,		0));
		myBeam.addLoad(new Load("F2",		-2.5,	1.5,		179,		0));
		
		BeamResult result = BeamSolver.getResults(myBeam,"2f");

		if (result.getErrorCount() == 0) {
			System.out.println("Beam. Loads:" + myBeam.getNumberOfLoads());
			System.out.println("Left bearing:" + result.getResultingForceAtLeftBearing_N() + " N. Right bearing:"
					+ result.getResultingForceAtRightBearing_N() + " N.");
			System.out.println("Horizontal:" + result.getSumOfHorizontalForcesIn_N());
			
			System.out.println("Term:"+result.getSolutionTermForHorizontalForces());
			System.out.println("Term:"+result.getSolutionTermForRightBearing());
			System.out.println("Term:"+result.getSolutionTermForLeftBearing());
		} else
			showErrors(result);

		// Get loads sorted by distance from left end of beam, sort asc...
		List<Load> sortedLoads = new ArrayList();
		sortedLoads = myBeam.getLoadsSortedByDistanceFromLeftSupportDesc();

		System.out.println("Loads sorted by distance from left end of beam:");
		for (Load l: sortedLoads) 
			System.out.println("Name:"+l.getName()+" Magnitute:"+l.getForce_N()+" Lengtht:"+l.getLengthOfLineLoad_m());
		
		// Get bearings sorted by distance from left end of beam, sort asc...
		List<Bearing> sortedBearings = new ArrayList();
		sortedBearings = myBeam.getBearingsSortedByDistanceFromLeftEndOfBeamDesc();

		System.out.println("Bearings sorted by distance from left end of beam:");
		for (Bearing b: sortedBearings) 
			System.out.println("Bearing "+ b.getDistanceFromLeftEndOfBeam_m());
		
		// Get biggest load
		Double maxLoad = myBeam.getMaxLoadIn_N();
		System.out.println("Max load:" + maxLoad);
		
		// Shearing forces
		Beam QBeam=new Beam (myBeam.getLength());
		QBeam=ShearingForcesSolver.solve(myBeam, result);
		
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
			if (error.getOriginOfError() == BeamCalcError.BEARING_ERROR)
				System.out.println("Error for bearing:" + error.getErrorDescription());
		}
	}

}
