package org.berthold.beamCalc;
/**
 * Solves a simply supported beam with single and/ or line loads.
 * 
 * Beam has a statically determined support configuration. Bearings may be placed arbitrary.
 * 
 * Solver has to check for valid initial conditions.
 * If any of the expected initial conditions are not met (e.g. bearing not inside beam length) result is set to 0
 * and an {@link Error} object is added to {@link Result} object.
 * 
 * Vertical Forces: Force, leading sign: (+) Force acts upwards (-) Force acts downwards.
 * Horizontal Forces: Force acts from right to left (+), force acts from left to right (-)
 * Torque, leading sign (+) Clockwise. (-) Anti- clockwise.
 * 
 * Single loads may act in an arbitrary angle. See {@link Solve}- class.
 * Line loads may only act downwards or upwards. The angle argument will not affect the result.
 * 
 * Solver calculates from left to right by solving the following equation:
 * 		TorqueSumAtRightBearing=0=F_Leftxl-F1x(l2-x1)-F2x(l2-x2)-....-Fn(l2-xn)
 * 
 * Resulting force at right bearing is calculated by solving this equation:
 * 		SummOfVerticalForces=0=F_Left-F1-F2-....-Fn+F_Right
 *
 * @author 	Berthold
 *
 */

import java.util.ArrayList;
import java.util.List;

public class BeamSolver {

	public static int NUMBER_OF_BEARINGS = 2;

	/**
	 * Solves a simply supported beam.
	 * 
	 * @param beam			A simply supported {@link Beam} with two {@link Bearing}'s and at least one {@link Load}
	 * @param floatFormat 	Float Format. Determines the decimal places used within the term containing the detailed solution.<br>
	 * 						Example: "2f" means two decimal places....
	 * @return {@link BeamResult}
	 */
	public static BeamResult getResults(Beam beam,String floatFormat) {

		/*
		 * Init
		 */
		BeamResult result = new BeamResult();
		double spaceBetweenBearings_m = 0;
		double torqueSum = 0;
		double loadSumVertical = 0;
		double loadSumHorizontal = 0;
		Load load;
		double verticalLoad, horizontalLoad; 	// if angle of load is>0, split
												// load in H and V.
		/*
		 * Get and check bearings
		 */

		// Sort bearings by distance from left end of beam....
		List<Bearing> bearingsSorted = new ArrayList<Bearing>();
		bearingsSorted = beam.getBearingsSortedByDistanceFromLeftEndOfBeamDesc();
		Bearing leftBearing = bearingsSorted.get(0);
		Bearing rightBearing = bearingsSorted.get(1);

		// Check if bearing is inside beam length
		if (beam.isInsideOfBeamLength(leftBearing.getDistanceFromLeftEndOfBeam_m())
				&& beam.isInsideOfBeamLength(rightBearing.getDistanceFromLeftEndOfBeam_m())) {
			spaceBetweenBearings_m = Math
					.abs(leftBearing.getDistanceFromLeftEndOfBeam_m() - rightBearing.getDistanceFromLeftEndOfBeam_m());
		} else {
			BeamCalcError error = new BeamCalcError(BeamCalcError.BEARING_ERROR, 0, "Bearing outside of beam");
			result.addError(error);
		}

		// Prepeare string which will contain the term for the solution in detail.
		StringBuilder termForSolutionAtLeftBearing = new StringBuilder();
		termForSolutionAtLeftBearing.append(bearingsSorted.get(0).getNameOfBearing() + "=(");

		StringBuilder termForSolutionAtRightBearing = new StringBuilder();
		termForSolutionAtRightBearing.append(bearingsSorted.get(1).getNameOfBearing() + "=");

		// Get and check loads, calculate resultant forces at left and right
		// bearing
		for (int i = 0; i <= beam.getNumberOfLoads() - 1; i++) {
			load = beam.getLoad(i);

			if (beam.isInsideOfBeamLength(load.getDistanceFromLeftEndOfBeam_m()) && beam
					.isInsideOfBeamLength(load.getDistanceFromLeftEndOfBeam_m() + load.getLengthOfLineLoad_m())) {

				if (load.getLengthOfLineLoad_m() == 0) {

					/*
					 * Load is a single load
					 */
					double angleOfLoadInRadians = load.getAngleOfLoad_degrees() * Math.PI / 180;

					// If load is not acting straight downwards or upwards,
					// split
					// load in vertical and horizontal part and increase
					// horizontal
					// load sum.
					if (load.getAngleOfLoad_degrees() != 0) {

						verticalLoad = load.getForce_N() * Math.cos(angleOfLoadInRadians);
						horizontalLoad = load.getForce_N() * Math.sin(angleOfLoadInRadians) * (-1);
						loadSumHorizontal = loadSumHorizontal + horizontalLoad;

					} else
						// Load acting vertical up/ down.
						verticalLoad = load.getForce_N();

					loadSumVertical = loadSumVertical + verticalLoad;
					torqueSum = torqueSum + verticalLoad
							* (rightBearing.getDistanceFromLeftEndOfBeam_m() - load.getDistanceFromLeftEndOfBeam_m());

					// Build solution term
					String p = partOfTermForSingleLoad(verticalLoad,
							rightBearing.getDistanceFromLeftEndOfBeam_m() - load.getDistanceFromLeftEndOfBeam_m(), beam,
							i);
					termForSolutionAtLeftBearing.append(p);

				} else {
					
					/*
					 * Load is line load
					 */
					double lineLoad_N = load.getForce_N();
					double resultandForce_N = lineLoad_N * load.getLengthOfLineLoad_m();
					
					double distanceOfResultandForceFromLeftEndOfbeam_m = (load.getDistanceFromLeftEndOfBeam_m())
							+ load.getLengthOfLineLoad_m() / 2;
					
					double lengthOfLeverToRightBearing_m=rightBearing.getDistanceFromLeftEndOfBeam_m()
							- distanceOfResultandForceFromLeftEndOfbeam_m;
					
					// Calc torque- sum
					loadSumVertical = loadSumVertical + resultandForce_N;
					torqueSum = torqueSum + resultandForce_N * lengthOfLeverToRightBearing_m;

					// Build soulution term.
					String p = partOfTermForLineLoad(lineLoad_N, load.getLengthOfLineLoad_m(),lengthOfLeverToRightBearing_m, beam, i);
					termForSolutionAtLeftBearing.append(p);
				}
			} else {
				BeamCalcError error = new BeamCalcError(BeamCalcError.LOAD_ERROR, i,
						"Load #" + i + 1 + " acts " + load.getDistanceFromLeftEndOfBeam_m()
								+ " m from left end of Beam. Beam length is " + beam.getLength() + " m");
				result.addError(error);
			}
		}
		if (result.getErrorCount() == 0) {
			result.setResultingForceAtLeftBearingBearing_N(-1 * torqueSum / spaceBetweenBearings_m);
			result.setResultingForceAtRightBearing_N(-1 * loadSumVertical - result.getResultingForceAtLeftBearing_N());
			result.setSumOfHorizontalForcesIn_N(loadSumHorizontal);

			
			/* 
			 * Finish strings with detail solution and add them to the result- instance
			 */

			// For the left bearing
			String resultingForceAtLeftBearingFormatet=String.format("%."+floatFormat, result.getResultingForceAtLeftBearing_N());
			termForSolutionAtLeftBearing.append(")/" + spaceBetweenBearings_m + "m = " + resultingForceAtLeftBearingFormatet + "N");
			result.addSolutionTermForLeftBearing(termForSolutionAtLeftBearing.toString());

			// For the right bearing
			String finalTerm = solutionTermForRightBearing(beam, result, termForSolutionAtRightBearing,floatFormat);
			
			// Finished....
			result.addSolutionTermForRightBearing(finalTerm);
		}
		return result;
	}

	/*
	 * Left Bearing, for single loads.
	 * 
	 * Helps building the term showing the solution for the left bearing. 
	 * 
	 * Returns "force x distance +" if more than two forces acting or
	 * "force x distance " if only one force is acting or the force is the last
	 * one acting.
	 */
	private static String partOfTermForSingleLoad(double force_N, double distance_m, Beam beam, int indexOfLoad) {
		String factor = -1 * force_N + "N x " + distance_m + "m";

		if (-1 * force_N < 0)
			factor = addParatheses(factor);
		if (indexOfLoad < beam.getNumberOfLoads()-1)
			return factor + "+";
		else
			return factor;
	}

	/*
	 * Left bearing, for line loads. 
	 * 
	 * Helps building the term showing the solution for the left bearig. 
	 */
	private static String partOfTermForLineLoad(double lineLoad_N, double lengthOfLineLoad_m,
			double resultantDistanceFromLeftEnd_m, Beam beam, int indexOfLoad) {
		String factor = -1 * lineLoad_N + " N/m x " + lengthOfLineLoad_m + "m x " + resultantDistanceFromLeftEnd_m
				+ "m";

		if (-1 * lineLoad_N < 0)
			factor = addParatheses(factor);
		if (indexOfLoad < beam.getNumberOfLoads()-1)
			return factor + "+";
		else
			return factor;
	}

	/*
	 * Right bearing.
	 * 
	 * This builds the string containing the complete solution for the right
	 * bearing which is obtained by solving following equation:
	 * 
	 * SummOfVerticalForces=0=F_Left-F1-F2-....-Fn+F_Right
	 */
	private static String solutionTermForRightBearing(Beam beam, BeamResult result,
			StringBuilder termForSolutionAtLeftBearing,String floatFormat) {

		String summand;
		double loadAtRightBearing = result.getResultingForceAtRightBearing_N();
		double loadAtLeftBearing = result.getResultingForceAtLeftBearing_N();
		
		// Get all actiong forces, change their leading sign and add them to the 
		// string containing the term with the solution.
		for (int i = 0; i <= beam.getNumberOfLoads() - 1; i++) {
			Load l = beam.getLoad(i);

			double force = l.getForce_N() * -1; // '*-1' => because all known
												// paramters are broghth to the
												// other side of the equation...

			if (l.getLengthOfLineLoad_m() > 0)
				summand = force + "N x " + l.getLengthOfLineLoad_m() + "m";
			else
				summand = force + "N";

			if (force < 0)
				summand = addParatheses(summand);

			termForSolutionAtLeftBearing.append(summand + " + ");
		}
	
		// Change leading sign of resulting force at left bearing 
		// and, if it changes to '-', add paratheses
		loadAtLeftBearing=loadAtLeftBearing*-1;
		String loadAtLeftBearingFormatted=String.format("%."+floatFormat, loadAtLeftBearing);
	
		if (loadAtLeftBearing<0)
			loadAtLeftBearingFormatted=addParatheses(loadAtLeftBearingFormatted+"N");
		else
			loadAtLeftBearingFormatted=loadAtLeftBearing+"N";
		
		// Format resulting force at right bearing
		String loadAtRightBearingFormatted=String.format("%."+floatFormat, loadAtRightBearing);
		
		// The completed term:
		termForSolutionAtLeftBearing.append(loadAtLeftBearingFormatted + " = " + loadAtRightBearingFormatted + "N");

		return termForSolutionAtLeftBearing.toString();
	}

	/*
	 * Encloses a summand of a term in paratheses.
	 * 
	 */
	private static String addParatheses(String summand) {
		return summand = "(" + summand + ")";
	}
}
