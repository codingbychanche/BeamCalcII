package org.berthold.beamCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a table of shearing forces along the length of a {@link Beam}.<br>
 * q(x)=> Q(x)
 * 
 * @author Berthold
 *
 */
public class QSolver {

	/**
	 * Calculates the shearing forces along the length of the beam.
	 * 
	 * Basis is a a table containing {@link StressResultant}- Objects. The starting
	 * contition for this table is, that is must contain all supporting forces and
	 * all acting forces.
	 * <p>
	 * Shearing forces are calculated by following this algorithm:
	 * <p>
	 * 1: Get Qn <br>
	 * 2: Get Qn+1<br>
	 * 3: Store the sum of Qn+Qn+1 at n+1
	 * <p>
	 * The resulting table contains the shearing forces along the length of the
	 * beam.
	 * <p>
	 * 
	 * For the time beeing this works only for point loads!
	 * 
	 * @param beam An {@link beam}- object from which the shearing forces are
	 *             calculated.	  
	 * @param unit The unit (N, kN.....).
	 * 
	 * @return A {@link StressResultantTable}- object containing the shearing forces
	 *         over the length of the beam => Q(x)
	 * 
	 */
	public static StressResultantTable solve(Beam beam, String unit) {
		final double sectionLength_m = .0001; // Small values lead to more accurate results.

		BeamResult result = BeamSolver.getResults(beam, "2f");

		StressResultantTable qTable = new StressResultantTable(beam, sectionLength_m,unit);

		// Add supporting forces
		Load l = new Load("A", result.getResultingForceAtLeftBearing_N(),
				beam.getBearing(0).getDistanceFromLeftEndOfBeam_m(), 0, 0);
		qTable.addForce(l);
		l = new Load("B", result.getResultingForceAtRightBearing_N(),
				beam.getBearing(1).getDistanceFromLeftEndOfBeam_m(), 0, 0);
		qTable.addForce(l);

		// Add all point loads
		List<Load> pointLoads = new ArrayList<Load>();
		pointLoads = beam.getLoadsSortedByDistanceFromLeftSupportDesc();
		double angleOfLoadInRadians;
		double verticalLoad;

		for (Load load : pointLoads)
			if (load.getLengthOfLineLoad_m() == 0) {
				angleOfLoadInRadians = load.getAngleOfLoad_degrees() * Math.PI / 180;
				verticalLoad = load.getForce_N() * Math.cos(angleOfLoadInRadians);
				Load v = new Load("Hn", verticalLoad, load.getDistanceFromLeftEndOfBeam_m(), 0, 0);
				qTable.addForce(v);
			}

		// Superimpose uniformingly distributed loads
		List<Load> loads = beam.getLoads();
		for (Load q : loads) {
			if (q.getLengthOfLineLoad_m() > 0) {
				qTable.addDistributedLoad(q);
			}
		}

		// Calculate shearing forces from existing table and write results back
		StressResultant qn_N, qn1_N;

		for (int n = 0; n <= qTable.getLength() - 2; n++) {
			qn_N = qTable.getShearingForceAtIndex(n);
			qn1_N = qTable.getShearingForceAtIndex(n + 1);
			qn1_N.addValue(qn_N.getShearingForce());
			qTable.setAtIndex(n + 1, qn1_N);

			if (Math.signum(qn_N.getShearingForce()) != Math.signum(qn1_N.getShearingForce()))
				qTable.getShearingForceAtIndex(n).setZeroPoint(true);
		}
		return qTable;
	}
}
