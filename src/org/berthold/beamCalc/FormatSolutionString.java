package org.berthold.beamCalc;

/**
 * Static helper methods for building a mathematical term showing the solution
 * of a resultig force in detail.
 * 
 * @author Berthold
 *
 */
public class FormatSolutionString {

	/**
	 * Helps building the term showing the solution for the left bearing.
	 * 
	 * 
	 * @param force_N
	 * @param angleOfLoadIn_deg
	 * @param distance_m
	 * @param beam
	 * @param indexOfLoad
	 * @return "force x distance +" if more than two forces acting or "force x
	 *         distance " if only one force is acting or the force is the last one
	 *         acting.
	 */
	public static String partOfTermForSingleLoad(double force_N, double angleOfLoadIn_deg, double distance_m, Beam beam,
			int indexOfLoad) {

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

	/**
	 * Helps building the term showing the solution for the left bearig.
	 * 
	 * @param resultantDistanceFromLeftEnd_m
	 * @param beam
	 * @param indexOfLoad
	 * @return
	 */
	public static String partOfTermForLineLoad(double resultantDistanceFromLeftEnd_m, Beam beam, int indexOfLoad) {
		String factor;
		double forceStart_N = beam.getLoad(indexOfLoad).getForceStart_N();
		double forceEnd_N = beam.getLoad(indexOfLoad).getForceEnd_N();
		double resultandForce_N = beam.getLoad(indexOfLoad).getResultantForce_N();
		double lengthOfLineLoad_m = beam.getLoad(indexOfLoad).getLengthOfLineLoad_m();

		// If load is uniformly distributed, term for resultant force is
		// q/m x length
		//
		// if not, add only the resultant force to the term which is obtained
		// from the {@link Load} object.
		if (forceStart_N == forceEnd_N) {

			// Term for uniformly distributed line load
			factor = -1 * forceStart_N + " N/m x " + lengthOfLineLoad_m + "m x " + resultantDistanceFromLeftEnd_m + "m";
		} else {
			// Term for uniformingly changing line load
			factor = -1 * resultandForce_N + " N x " + resultantDistanceFromLeftEnd_m + "m";
		}

		if (-1 * resultandForce_N < 0)
			factor = addParatheses(factor);
		if (indexOfLoad < beam.getNumberOfLoads() - 1)
			return factor + "+";
		else
			return factor;
	}

	/**
	 * Helps building the solution term showing the sum of the horizontal loads.
	 * 
	 * @param load
	 * @return
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
	 * bearing.
	 * 
	 * @param beam
	 * @param result
	 * @param termForSolutionAtRightBearing
	 * @param floatFormat
	 * @return Solution term for the resulting force at the right bearing.
	 */
	public static String solutionTermForRightBearing(Beam beam, BeamResult result,
			StringBuilder termForSolutionAtRightBearing, String floatFormat) {

		String summand;
		double loadAtRightBearing = result.getResultingForceAtRightBearing_N();
		double loadAtLeftBearing = result.getResultingForceAtLeftBearing_N();

		// Get all acting forces, change their leading sign and add them to the
		// string containing the term with the solution.
		for (int i = 0; i <= beam.getNumberOfLoads() - 1; i++) {
			Load l = beam.getLoad(i);

			// '*-1' => because all known
			// parameters are brought to the
			// other side of the equation...
			double resultantForce_N = l.getResultantForce_N() * -1;
			double force_N=l.getForce_N()*-1;
			double forceStart_N = l.getForceStart_N() * -1;
			double forceEnd_N = l.getForceEnd_N() * -1;

			// Line load or single load?
			if (l.getLengthOfLineLoad_m() > 0) {
				// Line load:
				// Is line load is uniformly distributed or not?
				if (forceStart_N == forceEnd_N)
					summand = force_N+ "N x " + l.getLengthOfLineLoad_m() + "m";
				else
					summand = resultantForce_N + "N ";
			} else {
				// Single load
				if (l.getAngleOfLoad_degrees() != 0)
					summand =force_N + "N x cos(" + l.getAngleOfLoad_degrees() + ")";
				else
					summand = force_N + "N";
			}
			if (force_N < 0 || resultantForce_N<0)
				summand = addParatheses(summand);

			termForSolutionAtRightBearing.append(summand + " + ");
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
		termForSolutionAtRightBearing.append(loadAtLeftBearingFormatted + " = " + loadAtRightBearingFormatted + "N");

		return termForSolutionAtRightBearing.toString();
	}

	/*
	 * Encloses a summand of a term in paratheses.
	 * 
	 */
	private static String addParatheses(String summand) {
		return summand = "(" + summand + ")";
	}
}
