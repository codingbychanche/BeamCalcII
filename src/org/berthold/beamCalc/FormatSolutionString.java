package org.berthold.beamCalc;
/**
 * Static helper methods for building a mathematical term showing
 * the solution of a resultig force in detail.
 * 
 * @author Berthold
 *
 */
public class FormatSolutionString {
	
	/**
	 * Left Bearing, for single loads.
	 * 
	 * Helps building the term showing the solution for the left bearing.
	 * 
	 * Returns "force x distance +" if more than two forces acting or
	 * "force x distance " if only one force is acting or the force is the last
	 * one acting.
	 */
	public static String partOfTermForSingleLoad(double force_N, double angleOfLoadIn_deg, double distance_m,
			Beam beam, int indexOfLoad) {

		String factor;
		force_N = force_N * -1;

		if (angleOfLoadIn_deg == 0)
			factor = force_N + "N x " + distance_m + "m";
		else
			factor = force_N + "N x cos(" + angleOfLoadIn_deg + ") x " + distance_m + "m";

		if (force_N < 0)
			factor = addParatheses(factor);
		if (indexOfLoad < beam.getNumberOfLoads() - 1)
			return factor + "+";
		else
			return factor;
	}

	/***
	 * Left bearing, for line loads.
	 * 
	 * Helps building the term showing the solution for the left bearig.
	 */
	public static String partOfTermForLineLoad(double lineLoad_N, double lengthOfLineLoad_m,
			double resultantDistanceFromLeftEnd_m, Beam beam, int indexOfLoad) {
		String factor = -1 * lineLoad_N + " N/m x " + lengthOfLineLoad_m + "m x " + resultantDistanceFromLeftEnd_m
				+ "m";

		if (-1 * lineLoad_N < 0)
			factor = addParatheses(factor);
		if (indexOfLoad < beam.getNumberOfLoads() - 1)
			return factor + "+";
		else
			return factor;
	}

	/**
	 * Horizontal loads.
	 * 
	 * Helps building the solution term showing the sum of the horizontal loads.
	 */
	public static String solutionTermForHorizForces(Load load) {
		double force = load.getForce_N();
		double angle = load.getAngleOfLoad_degrees();
		String p;

		if (angle == 0)
			p = force + " N";
		else
			p = force + " N x sin(" + angle + ")";

		return p;

	}

	/**
	 * Right bearing.
	 * 
	 * This builds the string containing the complete solution for the right
	 * bearing which is obtained by solving following equation:
	 * 
	 * SummOfVerticalForces=0=F_Left-F1-F2-....-Fn+F_Right
	 */
	public static String solutionTermForRightBearing(Beam beam, BeamResult result,
			StringBuilder termForSolutionAtLeftBearing, String floatFormat) {

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
			else {
				if (l.getAngleOfLoad_degrees() != 0)
					summand = force + "N x cos(" + l.getAngleOfLoad_degrees() + ")";
				else
					summand = force + "N";
			}

			if (force < 0)
				summand = addParatheses(summand);

			termForSolutionAtLeftBearing.append(summand + " + ");
		}

		// Change leading sign of resulting force at left bearing
		// and, if it changes to '-', add paratheses
		loadAtLeftBearing = loadAtLeftBearing * -1;
		String loadAtLeftBearingFormatted = String.format("%." + floatFormat, loadAtLeftBearing);

		if (loadAtLeftBearing < 0)
			loadAtLeftBearingFormatted = addParatheses(loadAtLeftBearingFormatted + "N");
		else
			loadAtLeftBearingFormatted = loadAtLeftBearing + "N";

		// Format resulting force at right bearing
		String loadAtRightBearingFormatted = String.format("%." + floatFormat, loadAtRightBearing);

		// The completed term:
		termForSolutionAtLeftBearing.append(loadAtLeftBearingFormatted + " = " + loadAtRightBearingFormatted + "N");

		return termForSolutionAtLeftBearing.toString();
	}

	/**
	 * Encloses a summand of a term in paratheses.
	 * 
	 */
	public static String addParatheses(String summand) {
		return summand = "(" + summand + ")";
	}
}
