package org.berthold.beamCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * A table containing {@link StressResultant}- objects
 * 
 * This is acts as an helper- class for the {@link QSolver} and the
 * {@link MSolver}- class.
 * 
 * After a calculation is done, this contains the stress resultants along a
 * {@link Beam}- object.
 * 
 * @author Berthold
 *
 */
public class StressResultantTable {

	private static final double DISCONTIUNUITY_THRESHOLD = .01;

	public List<StressResultant> sfValues = new ArrayList<StressResultant>(); 
	private double sectionLength_m;
	private Beam beam;
	private String unit;

	/**
	 * A new Stress resultant table.
	 * 
	 * Creates a new table for the passed {@link Beam}-object, adds the supporting
	 * forces and all forces acting.
	 * 
	 * @param beam            A {@link Beam}- object from which the table is build.
	 * @param sectionLength_m Sections between the forces acting.
	 */
	public StressResultantTable(Beam beam, double sectionLength_m, String unit) {
		this.beam = beam;
		this.sectionLength_m = sectionLength_m;
		this.unit = unit;

		// Create empty list with acting forces and add supporting forces
		for (double x_m = 0; x_m <= (beam.getLength()); x_m = x_m + sectionLength_m) {
			StressResultant q = new StressResultant(x_m, 0, unit);
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
	 * Stress resultant at a specified index in the table.
	 * 
	 * This method is used by the {@link QSolver}- and {@link MSolver} the class.
	 * When adding a shearing force use the {@link addForce()} method which
	 * calculates the index in the table depending from the section length and the
	 * position of the force relative the the left end of the beam.
	 * 
	 * @param index
	 * @return Shearing force.
	 */
	public StressResultant getShearingForceAtIndex(int index) {
		return sfValues.get(index);
	}

	/**
	 * Stress resultant force at a specified index in the table.
	 * 
	 * This method is used by the {@link QSolver}- and the {@link MSolver} class.
	 * 
	 * @param index
	 * @param stressResultantValue The shearing force.
	 */
	public void setAtIndex(int index, StressResultant stressResultantValue) {
		sfValues.set(index, stressResultantValue);
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
				StressResultant q = this.sfValues.get(n);

				q.setName(l.getName());
				q.setDiscontiunuity(true);
				q.addValue(force_N);
				//q.setShearingForce(force_N);
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

		double dq_perSectionLength = dl_Nm.getForce_N() / (1 / this.sectionLength_m);
		double xStartOfLoad = dl_Nm.getDistanceFromLeftEndOfBeam_m();
		double xEndOfLoad = dl_Nm.getDistanceFromLeftEndOfBeam_m() + dl_Nm.getLengthOfLineLoad_m();

		for (StressResultant r : this.sfValues) {

			// Set disconiuity at start of load
			if (xStartOfLoad >= r.getX_m() && xStartOfLoad <= r.getX_m() + this.sectionLength_m) {
				r.setDiscontiunuity(true);
				r.addValue(dq_perSectionLength);
			}

			// Add load
			if (r.getX_m() >= xStartOfLoad && r.getX_m() <= xEndOfLoad)
				r.addValue(dq_perSectionLength);

			// Set disconiuity at end of load
			if (xEndOfLoad >= r.getX_m() && xEndOfLoad <= r.getX_m() + this.sectionLength_m) {
				r.setDiscontiunuity(true);
				r.setShearingForceDeltaBy(dq_perSectionLength);
			}
		}
	}

	/**
	 * List of stress resultant objects.
	 * 
	 * @return This list of {@link StressResultant}- objects.
	 */
	public List<StressResultant> getShearingForceTable() {
		return sfValues;
	}

	/**
	 * A list of all maxima of this stress resultant table.
	 * 
	 * @return List of {@link StressResultant}- objects containing all maxima found.
	 */
	public List<StressResultant> getMaxima() {
		List<StressResultant> maxima = new ArrayList<>();

		for (StressResultant r : this.sfValues)
			if (r.isMaxima())
				maxima.add(r);

		return maxima;
	}

	/**
	 * A list of all points of discontinuity of this stress resultant table.
	 * 
	 * @return List of {@link StressResultant}- objects containing all points of
	 *         discontinuity found.
	 */
	public List<StressResultant> getDiscontinuitys() {
		List<StressResultant> discontinuity = new ArrayList<>();

		for (StressResultant r : this.sfValues)
			if (r.isDiscontiunuity())
				discontinuity.add(r);

		return discontinuity;
	}

	/**
	 * A list of zero points of this table
	 * 
	 * @return List of {@link StressResultant}- objects containing all zero points
	 *         found.
	 */
	public List<StressResultant> getZeroPoints() {
		List<StressResultant> zeroPoints = new ArrayList<>();

		for (StressResultant r : this.sfValues)
			if (r.isZeroPoint())
				zeroPoints.add(r);

		return zeroPoints;
	}

	/**
	 * Determines the biggest value in this table.
	 * 
	 * @return Biggest stress resultant of ths table.
	 */

	public double getAbsMax() {
		double max = 0;
		double currentMax;

		for (int i = 0; i <= sfValues.size() - 1; i++) {
			currentMax = sfValues.get(i).getShearingForce();
			if (currentMax > max)
				max = currentMax;
		}
		return max;
	}

	/**
	 * Determines the smallest value in this table.
	 * 
	 * @return The smallest stress resultant of this table.
	 */
	public double getAbsMin() {
		double min = 0;
		double currentMin;

		for (int i = 0; i <= sfValues.size() - 1; i++) {
			currentMin = sfValues.get(i).getShearingForce();
			if (currentMin < min)
				min = currentMin;
		}
		return min;
	}

	/**
	 * Determines the biggest x- value, which is in fact the length of the asociated
	 * beam.
	 * 
	 * @return Beggest x- value.
	 */
	public double getAbsMaxX() {
		int last = sfValues.size() - 1;
		return sfValues.get(last).getX_m();
	}
}
