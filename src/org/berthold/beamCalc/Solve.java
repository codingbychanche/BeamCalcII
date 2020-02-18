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
 * @param 	beam A simply supported {@link Beam} with two {@link Bearing}'s and at least one {@link Load}
 * @return	{@link Result}
 * @see 	Beam, Result
 * @author 	Berthold
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solve {
	
	public static int NUMBER_OF_BEARINGS=2;

	public static Result getResults(Beam beam) {

		/*
		 * Init
		 */
		Result result = new Result();
		int errorCount = 0;
		double spaceBetweenBearings_m = 0;
		double torqueSum = 0;
		double loadSumVertical = 0;
		double loadSumHorizontal=0;
		Load load;
		double verticalLoad,horizontalLoad; // if angle of load is>0, split load in H and V.

		/*
		 * Get and check bearings
		 */

		// Sort bearings by distance from left end of beam....
		List <Bearing> bearingsSorted=new ArrayList();
		bearingsSorted=beam.getBearingsSortedByDistanceFromLeftEndOfBeamDesc();
		Bearing leftBearing = bearingsSorted.get(0);
		Bearing rightBearing = bearingsSorted.get(1);
		
		// Check if bearing is inside beam length
		if (beam.isInsideOfBeamLength(leftBearing.getDistanceFromLeftEndOfBeam_m())
				&& beam.isInsideOfBeamLength(rightBearing.getDistanceFromLeftEndOfBeam_m())) {
			spaceBetweenBearings_m = Math
					.abs(leftBearing.getDistanceFromLeftEndOfBeam_m() - rightBearing.getDistanceFromLeftEndOfBeam_m());
		} else {
			BeamCalcError error=new BeamCalcError(BeamCalcError.BEARING_ERROR,0,"Bearing outside of beam");
			result.addError(error);
		}
		
		 // Get and check loads, calculate resultant forces at left and right
		 // bearing
		for (int i = 0; i <= beam.getNumberOfLoads() - 1; i++) {
			load = beam.getLoad(i);

			if (beam.isInsideOfBeamLength(load.getDistanceFromLeftEndOfBeam_m()) && beam
					.isInsideOfBeamLength(load.getDistanceFromLeftEndOfBeam_m() + load.getLengthOfLineLoad_m())) {
				if (load.getLengthOfLineLoad_m() == 0) {

					// Load is a single load
					double angleOfLoadInRadians=load.getAngleOfLoad_degrees()*Math.PI/180;
					
					// If load is not acting straight downwards or upwards, split
					// load in vertical and horizontal part and increase horizontal
					// load sum.
					if (load.getAngleOfLoad_degrees()!=0){
						
						verticalLoad=load.getForce_N()*Math.cos(angleOfLoadInRadians);
						horizontalLoad=load.getForce_N()*Math.sin(angleOfLoadInRadians)*(-1);
						loadSumHorizontal = loadSumHorizontal +horizontalLoad;

					} else
						// Load acting vertical up/ down. 
						verticalLoad=load.getForce_N();
					
					loadSumVertical = loadSumVertical +verticalLoad;
					torqueSum = torqueSum + verticalLoad
							* (rightBearing.getDistanceFromLeftEndOfBeam_m() - load.getDistanceFromLeftEndOfBeam_m());
				} else {
					// Load is line load
					double resultandForce_N = load.getForce_N() * load.getLengthOfLineLoad_m();
					double distanceOfResultandForceFromLeftEndOfbeam_m = (load.getDistanceFromLeftEndOfBeam_m())
							+ load.getLengthOfLineLoad_m() / 2;
					loadSumVertical = loadSumVertical + resultandForce_N;
					torqueSum = torqueSum + resultandForce_N * (rightBearing.getDistanceFromLeftEndOfBeam_m()
							- distanceOfResultandForceFromLeftEndOfbeam_m);
				}
			} else {
				BeamCalcError error=new BeamCalcError(BeamCalcError.LOAD_ERROR,i,"Load #"+i+1+" acts "+load.getDistanceFromLeftEndOfBeam_m()+
						" m from left end of Beam. Beam length is "+beam.getLength()+" m");
				result.addError(error);
			}
		}
		if (result.getErrorCount() == 0) {
			result.setResultingForceAtLeftBearingBearing_N(-1 * torqueSum / spaceBetweenBearings_m);
			result.setResultingForceAtRightBearing_N(-1 * loadSumVertical - result.getResultingForceAtLeftBearing_N());
			result.setSumOfHorizontalForcesIn_N(loadSumHorizontal);
		}
		return result;
	}
}
