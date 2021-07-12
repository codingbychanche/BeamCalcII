package org.berthold.beamCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculates the bending moments along the length of a {@link Beam}- object.
 * Q(x) => M(x)
 * </p>
 * 
 * FOR THE TIME BEEING THIS WORKS NOT FOR UNEVENLY DISTRIBUTED LOADS
 * </p>
 * 
 * @author Berthold
 *
 */
public class MSolver {

	// Any step between two nighboring shearing forces counts as an
	// discontiunuity
	private static final double DISCONTIUNUITY_THRESHOLD = 0.001;

	/**
	 * Calculates the bending moments along the beam.
	 * 
	 * Algorithm:<br>
	 * q(n+1)-q(n) x X (distance from left end of beam)
	 * 
	 * @param qTable
	 * @param beam
	 * @param unit
	 *            The unit (N, kN.....).
	 * @return A table of {@link StressResultant}- objects containing the
	 *         bending moments along the beam => M(x).
	 */
	public static StressResultantTable solve(StressResultantTable qTable, Beam beam, String unit) {

		StressResultantTable mTable = new StressResultantTable(beam, qTable.getSectionLength_m(), unit);

		double m_Nm = 0;
		double m1_Nm = 0;
		double q_N, q1_N;
		double deltaM_Nm = 0;
		double sectionLength_m = mTable.getSectionLength_m();
		double x = 0;

		for (int n = 0; n <= qTable.getLength() - 2; n++) {

			q_N = qTable.getShearingForceAtIndex(n).getShearingForce();

			q1_N = qTable.getShearingForceAtIndex(n + 1).getShearingForce();
			double deltaQ_N = q1_N - q_N;

			// Checks if leading sign changes or if there is an discontiunuity
			// if so, disregard....
			// if (Math.signum(q_N) == Math.signum(q1_N) && Math.abs(deltaQ_N)
			// <= DISCONTIUNUITY_THRESHOLD) {
			m_Nm = q_N * x;
			m1_Nm = q1_N * (x + sectionLength_m);
			deltaM_Nm = m1_Nm - m_Nm;
			// } else {

			// }

			m_Nm = mTable.getShearingForceAtIndex(n).getShearingForce();
			mTable.getShearingForceAtIndex(n + 1).setShearingForce(m_Nm + deltaM_Nm);

			// Check for zero points in Q(x). Zero points are local
			// maxima in M(x).
			if (qTable.getShearingForceAtIndex(n).isZeroPoint())
				mTable.getShearingForceAtIndex(n).setMaxima(true);

			// Check for disconuinity in Q(x) because
			// M(x) must also be a diconuinity or a local maxima/ minima
			if (qTable.getShearingForceAtIndex(n).isDiscontiunuity()) {
				if (!mTable.getShearingForceAtIndex(n).isMaxima())
					mTable.getShearingForceAtIndex(n).setDiscontiunuity(true);
				mTable.getShearingForceAtIndex(n).setShearingForceDeltaBy(m_Nm + deltaM_Nm);
			}

			// Next
			x = x + sectionLength_m / (1 / sectionLength_m);
		}
		return mTable;
	}
}