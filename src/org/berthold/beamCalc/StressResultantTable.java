package org.berthold.beamCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * A table containing {@link StressResultantValue}- objects
 * 
 * This is acts as an helper- class for the {@link QSolver} and the
 * {@link MSolver}- class.
 * 
 * After a calculation is done, this contains the resultant forces along the
 * {@link Beam}- object.
 * 
 * @author Berthold
 *
 */
public class StressResultantTable {

	private static final int DISCONTIUNUITY_THRESHOLD = 1;

	List<StressResultantValue> sfValues = new ArrayList<StressResultantValue>();
	double sectionLength_m;
	Beam beam;

	/**
	 * A new Stress resultant table.
	 * 
	 * Creates a new table for the passed Beam-object and adds the supporting
	 * forces.
	 * 
	 * @param beam            A {@kink Beam}- object from which the table is build.
	 * @param sectionLength_m Sections between the forces acting.
	 */
	public StressResultantTable(Beam beam, double sectionLength_m) {
		this.beam = beam;
		this.sectionLength_m = sectionLength_m;

		// Create empty list with acting forces and add supporting forces
		for (double x_m = 0; x_m <= (beam.getLength()); x_m = x_m + sectionLength_m) {
			StressResultantValue q = new StressResultantValue(x_m, 0);
			sfValues.add(q);
		}
	}

	/**
	 * Number of values inside the table of shearing forces.
	 * 
	 * @return Size of the table.
	 */
	public int getLength() {
		return sfValues.size();
	}

	/**
	 * Section length.
	 * 
	 * @return The length between each section the beam is divided into (the space
	 *         between the resultant forces).
	 */
	public double getSectionLength_m() {
		return sectionLength_m;
	}

	/**
	 * Resultant force at a specified index in the table.
	 * 
	 * This method is used by the {@link QSolver}- and {@link MSolver} the class.
	 * When adding a shearing force use the {@link addForce()} method which
	 * calculates the index in the table depending from the section length and the
	 * position of the force relative the the left end of the beam.
	 * 
	 * @param index
	 * @return Shearing force.
	 */
	public StressResultantValue getShearingForceAtIndex(int index) {
		return sfValues.get(index);
	}

	/**
	 * Resultant force at a specified index in the table.
	 * 
	 * This method is used by the {@link QSolver}- and the {@link MSolver} class.
	 * 
	 * @param index
	 * @param shearingForceValue The shearing force.
	 */
	public void setAtIndex(int index, StressResultantValue shearingForceValue) {
		sfValues.set(index, shearingForceValue);
	}

	/**
	 * Adds a point load to this table.
	 * 
	 * @param l The {@link Load}- object representing the load.
	 */
	public void addForce(Load l) {

		double x_m = l.getDistanceFromLeftEndOfBeam_m();
		double force_N = l.getForce_N();

		int n = 0;
		for (double i = 0; i < beam.getLength(); i = i + sectionLength_m) {

			if (x_m >= i && x_m < i + sectionLength_m) {
				StressResultantValue q = this.sfValues.get(n);

				q.setDiscontiunuity(true);
				q.setShearingForceDeltaBy(force_N);

				q.setShearingForce(force_N);
				q.setX_m(x_m);
			}
			n++;
		}
	}

	/**
	 * Superimposes a uniformly distributed load.
	 * 
	 * @param dl_Nm Load.
	 */
	public void addDistributedLoad(Load dl_Nm) {

		double xStart_m = dl_Nm.getDistanceFromLeftEndOfBeam_m();
		double xEnd_m = dl_Nm.getDistanceFromLeftEndOfBeam_m() + dl_Nm.getLengthOfLineLoad_m();
		double dq_perSectionLength = dl_Nm.getForce_N() * this.sectionLength_m;

		double fn, fn1;

		int n = 0;
		for (double x = 0; x <= dl_Nm.getLengthOfLineLoad_m(); x = x + this.sectionLength_m) {

			// Set disconiuity at start of load
			if (x == xStart_m) {
				this.sfValues.get(n).setDiscontiunuity(true);
				this.sfValues.get(n).setShearingForceDeltaBy(dq_perSectionLength);
			}

			if (x >= xStart_m && x <= xEnd_m) {
				this.sfValues.get(n).addValue(dq_perSectionLength);
			}

			// Set disconiuity at end of load
			if (x == xEnd_m) {
				this.sfValues.get(n).setDiscontiunuity(true);
				this.sfValues.get(n).setShearingForceDeltaBy(dq_perSectionLength);
			}
			n++;
		}

		// Debug....
		for (StressResultantValue v : this.sfValues)
			System.out.println("x=" + v.getX_m() + "       Q=" + v.getShearingForce());
	}

	/**
	 * List of shearing forces.
	 * 
	 * @return This list of {@link StressResultantValue}- objects.
	 */
	public List<StressResultantValue> getShearingForceTable() {
		return sfValues;
	}
}
