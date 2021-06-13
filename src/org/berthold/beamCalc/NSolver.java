package org.berthold.beamCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculates the normal forces along the length of a {@link Beam}- object. N(x)
 * 
 * @author Berthold
 *
 */
public class NSolver {

	/**
	 * Calculates the normal forces along the length of the beam.
	 * 
	 * Basis is a a table containing {@link StressResultant}- Objects. <p>
	 * 
	 * 1: Get Nn <br>
	 * 2: Get Nn+1<br>
	 * 3: Store the sum of Nn+Nn+1 at n+1
	 * <p>
	 * 
	 * The resulting table contains resulting normal forces along the length of the
	 * beam.
	 * <p>
	 * 
	 * For the time beeing this works only for point loads!
	 * 
	 * @param beam A {@link Beam}- object.
	 * @param unit The unit (N, kN.....).
	 * @return {@link ShearingForceTable} containing the normal forces along the
	 *         beam. If the beam has no pin support, null is returned.
	 */
	public static StressResultantTable solve(Beam beam,String unit) {

		final double sectionLength_m = 0.001; // Small values lead to more accurate results.

		StressResultantTable nTable = new StressResultantTable(beam, sectionLength_m,unit);

		// Add all point loads
		List<Load> singleLoads = new ArrayList<Load>();
		singleLoads = beam.getLoadsSortedByDistanceFromLeftSupportDesc();

		for (Load load : singleLoads)
			if (load.getLengthOfLineLoad_m() == 0) {
				double angleOfLoadInRadians = load.getAngleOfLoad_degrees() * Math.PI / 180;
				double horizontalLoad = load.getForce_N() * Math.sin(angleOfLoadInRadians);
				Load h = new Load("Hn", horizontalLoad, load.getDistanceFromLeftEndOfBeam_m(), 0, 0);
				nTable.addForce(h);
			}

		// Get position of pin support (at this support normal forces are 0
		double xPosOfPinSupport_m = 0;

		if (beam.getBearing(0).getType() == Support.PIN_SUPPORT)
			xPosOfPinSupport_m = beam.getBearing(0).getDistanceFromLeftEndOfBeam_m();
		else {
			if (beam.getBearing(1).getType() == Support.PIN_SUPPORT)
				xPosOfPinSupport_m = beam.getBearing(1).getDistanceFromLeftEndOfBeam_m();
			}

			// Calculate normal forces from existing table and write results back
			// Calculate from left beam end in the direction of the pin support
			StressResultant nn_N, nn1_N;
			double x = 0;
			for (int n = 0; n <= nTable.getLength() - 2; n++) {
				nn_N = nTable.getShearingForceAtIndex(n);
				nn1_N = nTable.getShearingForceAtIndex(n + 1);

				x = x + sectionLength_m;

				if (x < xPosOfPinSupport_m) {
					nn1_N.addValue(nn_N.getShearingForce());
					nTable.setAtIndex(n + 1, nn1_N);
				}
			}
			
			// Calculate from the right end of the beam in the direction of the pin support.
			x=beam.getLength();
			for (int n=nTable.getLength()-1;n>1;n--) {
				nn_N = nTable.getShearingForceAtIndex(n);
				nn1_N = nTable.getShearingForceAtIndex(n - 1);

				x = x - sectionLength_m;

				if (x > xPosOfPinSupport_m) {
					nn1_N.addValue(nn_N.getShearingForce());
					nTable.setAtIndex(n - 1, nn1_N);
				}
			}
		return nTable;
	}
}
