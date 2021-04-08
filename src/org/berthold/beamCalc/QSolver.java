package org.berthold.beamCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a table of shearing forces along the length of a {@link Beam}.
 * 
 * @author Berthold
 *
 */
public class QSolver {

	/**
	 * Calculates the shearing forces along the length of the beam.
	 * 
	 * Shearing forces are calculated by following this algorythm: 
	 * Basis is a a table containing {@link ShearingForceValue}- Objects. 
	 * The starting ontition for this table is, that is must contain all supporting 
	 * forces and all acting forces.
	 * 
	 * This table is changed by the following algorythm:
	 * 
	 * 1: Get Qn 
	 * 2: Get Qn+1 
	 * 3: Store the sum of Qn+Qn+1 at n+1
	 * 
	 * The resulting table contains resulting shearing forces along the length of the
	 * beam.
	 * 
	 * For the time beeing this works only for point loads!
	 * 
	 * @param beam An {@link beam}- object from which the shearing forces are
	 *             calculated.
	 * @return A {@link ShearingForceTable}- object containing the shearing forces over the
	 *         length of the beam.
	 */

	public static ShearingForceTable solve(Beam beam) {
		final double sectionLength_m = 0.1;

		BeamResult result = BeamSolver.getResults(beam, "2f");

		ShearingForceTable qTable = new ShearingForceTable(beam, sectionLength_m);

		// Add supporting forces
		Load l = new Load("A", result.getResultingForceAtLeftBearing_N(),
				beam.getBearing(0).getDistanceFromLeftEndOfBeam_m(), 0, 0);
		qTable.addForce(l);
		l = new Load("B", result.getResultingForceAtRightBearing_N(),
				beam.getBearing(1).getDistanceFromLeftEndOfBeam_m(), 0, 0);
		qTable.addForce(l);		
		
		// Add all point loads
		List<Load> singleLoads = new ArrayList<Load>();
		singleLoads = beam.getLoadsSortedByDistanceFromLeftSupportDesc();
		for (Load load : singleLoads)
			if (load.getLengthOfLineLoad_m() == 0)
				qTable.addForce(load);
		

		// Calculate shear forces from existing tablend write results back
		ShearingForceValue qn_N, qn1_N;
	
		for (int n = 0; n <= qTable.getLength() - 2; n++) {
			qn_N = qTable.getShearingForceAtIndex(n);
			qn1_N = qTable.getShearingForceAtIndex(n + 1);
			qn1_N.addValue(qn_N.getShearingForce());
			qTable.setAtIndex(n + 1, qn1_N);
		}
		
		// Now get all line loads and superimpose them over the
		// existing table containing the shearing forces resulting
		// from the point loads.
	
		return qTable;
	}
}
