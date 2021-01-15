package org.berthold.beamCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * Solves a simply supported beam with single and/ or line loads.
 * 
 * Beam has a statically determined support configuration. supports may be
 * placed arbitrary.
 * 
 * Solver has to check for valid initial conditions. If any of the expected
 * initial conditions are not met (e.g. support not inside beam length) result
 * is set to 0 and an {@link Error} object is added to {@link Result} object.
 * 
 * Vertical Forces: Force, leading sign: (+) Force acts upwards (-) Force acts
 * downwards. Horizontal Forces: Force acts from right to left (+), force acts
 * from left to right (-) Torque, leading sign (+) Clockwise. (-) Anti-
 * clockwise.
 * <p>
 * 
 * Single loads may act in an arbitrary angle. Line loads may only act downwards
 * or upwards. Angle for those loads will be ignored.
 * <p>
 * 
 * Solver calculates from left to right by solving the following equation:
 * <p>
 * TorqueSumAtRightsupport=0=F_Leftxl-F1x(l2-x1)-F2x(l2-x2)-....-Fn(l2-xn)
 * <p>
 * 
 * Resulting force at right support is calculated by solving this equation:
 * <p>
 * SummOfVerticalForces=0=F_Left-F1-F2-....-Fn+F_Right
 *
 * @author Berthold
 * 
 */
public class BeamSolver {

	public static int NUMBER_OF_SUPPORTS = 2; // Not one more yet, sorry.

	/**
	 * Creates a new result.
	 *
	 *
	 * @param beam
	 *            A simply supported {@link Beam} with two {@link Support}'s and
	 *            at least one {@link Load}
	 * 
	 * @param floatFormat
	 *            Float Format. Determines the decimal places used within the
	 *            term containing the detailed solution.<br>
	 *            Example: "2f" means two decimal places....
	 * 
	 * @return {@link BeamResult}
	 */
	public static BeamResult getResults(Beam beam, String floatFormat) {

		BeamResult result = new BeamResult();
		double spaceBetweenSupports_m = 0;
		double torqueSum = 0;
		double loadSumVertical = 0;
		double loadSumHorizontal = 0;
		Load load;
		double verticalLoad, horizontalLoad;

		// Sort supports by distance from left end of beam....
		List<Support> supportsSorted = new ArrayList<Support>();
		supportsSorted = beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc();
		Support leftSupport = supportsSorted.get(0);
		Support rightSupport = supportsSorted.get(1);

		// Check if support is inside beam length
		if (beam.isInsideOfBeamLength(leftSupport.getDistanceFromLeftEndOfBeam_m())
				&& beam.isInsideOfBeamLength(rightSupport.getDistanceFromLeftEndOfBeam_m())) {
			spaceBetweenSupports_m = Math
					.abs(leftSupport.getDistanceFromLeftEndOfBeam_m() - rightSupport.getDistanceFromLeftEndOfBeam_m());
		} else {
			BeamCalcError error = new BeamCalcError(BeamCalcError.SUPPORT_ERROR, 0, "Bearing outside of beam");
			result.addError(error);
		}

		// Prepeare string which will contain the term for the solution in
		// detail.
		String partOfMathTerm; // String holding current mathematical term.

		StringBuilder termForSolutionAtLeftBearing = new StringBuilder();
		termForSolutionAtLeftBearing.append(supportsSorted.get(0).getNameOfSupport() + "=(");

		StringBuilder termForSolutionAtRightBearing = new StringBuilder();
		termForSolutionAtRightBearing.append(supportsSorted.get(1).getNameOfSupport() + "=");

		StringBuilder termForSolutionOfHorizForce = new StringBuilder();
		termForSolutionOfHorizForce.append("N=");

		// Get and check loads, calculate resultant forces at left and right
		// bearing
		for (int i = 0; i <= beam.getNumberOfLoads() - 1; i++) {
			load = beam.getLoad(i);

			if (beam.isInsideOfBeamLength(load.getDistanceFromLeftEndOfBeam_m()) && beam
					.isInsideOfBeamLength(load.getDistanceFromLeftEndOfBeam_m() + load.getLengthOfLineLoad_m())) {

				/*
				 * Single load?
				 */
				if (load.getLengthOfLineLoad_m() == 0) {

					double angleOfLoadInRadians = load.getAngleOfLoad_degrees() * Math.PI / 180;

					// If load is acting at an angle, split!
					// load in vertical and horizontal part and increase
					// horizontal load sum.
					if (load.getAngleOfLoad_degrees() != 0) {

						verticalLoad = load.getForce_N() * Math.cos(angleOfLoadInRadians);
						horizontalLoad = load.getForce_N() * Math.sin(angleOfLoadInRadians) * (-1);
						loadSumHorizontal = loadSumHorizontal + horizontalLoad;

						// Build mathematical term accordingly.
						// Vertical load
						partOfMathTerm = FormatSolutionString.partOfTermForSingleLoad(load.getForce_N(),
								load.getAngleOfLoad_degrees(),
								rightSupport.getDistanceFromLeftEndOfBeam_m() - load.getDistanceFromLeftEndOfBeam_m(),
								beam, i);
						termForSolutionAtLeftBearing.append(partOfMathTerm);

						// Horizontal force.
						partOfMathTerm = FormatSolutionString.solutionTermForHorizForces(load);
						termForSolutionOfHorizForce.append(partOfMathTerm);

					} else {
						// Load acting vertical up/ down.
						verticalLoad = load.getForce_N();

						// Build solution term
						partOfMathTerm = FormatSolutionString.partOfTermForSingleLoad(verticalLoad, 0,
								rightSupport.getDistanceFromLeftEndOfBeam_m() - load.getDistanceFromLeftEndOfBeam_m(),
								beam, i);
						termForSolutionAtLeftBearing.append(partOfMathTerm);

					}
					loadSumVertical = loadSumVertical + verticalLoad;
					torqueSum = torqueSum + verticalLoad
							* (rightSupport.getDistanceFromLeftEndOfBeam_m() - load.getDistanceFromLeftEndOfBeam_m());

				} else {

					/*
					 * Load is line load
					 */
					double resultandForce_N = load.getForce_N();
					double distanceOfResultandForceFromLeftEndOfbeam_m = load.getCenterOfGravity_m()
							+ load.getDistanceFromLeftEndOfBeam_m();

					double lengthOfLeverToRightBearing_m = rightSupport.getDistanceFromLeftEndOfBeam_m()
							- distanceOfResultandForceFromLeftEndOfbeam_m;

					// Calc torque- sum
					loadSumVertical = loadSumVertical + resultandForce_N;
					torqueSum = torqueSum + resultandForce_N * lengthOfLeverToRightBearing_m;

					String p = FormatSolutionString.partOfTermForLineLoad(lengthOfLeverToRightBearing_m, beam, i);
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
			result.setResultingForceAtLeftBearingBearing_N(-1 * torqueSum / spaceBetweenSupports_m);

			//
			// Calculate resulting force at right bearing.
			//
			result.setResultingForceAtRightBearing_N(-1 * loadSumVertical - result.getResultingForceAtLeftBearing_N());
			result.setSumOfHorizontalForcesIn_N(loadSumHorizontal);

			/*
			 * Finish strings with detail solution and add them to the result-
			 * instance
			 */

			// For the horizontal forces
			String sumOfHorizontalLoadsFormated = String.format("%." + floatFormat, loadSumHorizontal);

			if (loadSumHorizontal != 0)
				termForSolutionOfHorizForce.append(" = " + sumOfHorizontalLoadsFormated + "N");
			else
				termForSolutionOfHorizForce.append(sumOfHorizontalLoadsFormated + "N");

			result.addSolutionTermForHorizontalForce(termForSolutionOfHorizForce.toString());

			// For the left bearing
			String resultingForceAtLeftBearingFormatet = String.format("%." + floatFormat,
					result.getResultingForceAtLeftBearing_N());
			termForSolutionAtLeftBearing
					.append(")/" + spaceBetweenSupports_m + "m = " + resultingForceAtLeftBearingFormatet + "N");
			result.addSolutionTermForLeftBearing(termForSolutionAtLeftBearing.toString());

			// For the right bearing
			String finalTerm = FormatSolutionString.solutionTermForRightBearing(beam, result,
					termForSolutionAtRightBearing, floatFormat);

			// Finished....
			result.addSolutionTermForRightBearing(finalTerm);
		}
		return result;
	}
}
