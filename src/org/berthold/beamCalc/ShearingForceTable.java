package org.berthold.beamCalc;

import java.util.ArrayList;
import java.util.List;

/**
 * A table consisting of {@link ShearingForceValue}- objects which are
 * representing the loads acting and their position.
 * 
 * This is acts as an helper- class for the {@link QSolver}- class and holds the
 * final result of the calculation whih can be optained by invoking the getter
 * method for the qTable.
 * 
 * @author Berthold
 *
 */
public class ShearingForceTable {

	List<ShearingForceValue> qValues = new ArrayList<ShearingForceValue>();
	double sectionLength_m;
	Beam beam;

	/**
	 * A new QTable.
	 * 
	 * Creates a new table for the passed Beam-object and adds the supporting
	 * forces.
	 * 
	 * @param beam            A {@kink Beam}- object from which the table is build.
	 * @param sectionLength_m Sections between the forces acting.
	 */
	public ShearingForceTable(Beam beam, double sectionLength_m) {
		this.beam = beam;
		this.sectionLength_m = sectionLength_m;

		// Create empty list with acting forces and add supporting forces
		for (double x_m = 0; x_m <= (beam.getLength() + sectionLength_m); x_m = x_m + sectionLength_m) {
			ShearingForceValue q = new ShearingForceValue(x_m, 0);
			qValues.add(q);
		}
	}

	/**
	 * Number of values inside the table of shearing forces.
	 * 
	 * @return Size of the table.
	 */
	public int getLength() {
		return qValues.size();
	}

	/**
	 * Section length.
	 * 
	 * @return The length between each section the beam is divided into (the space
	 *         between the shearing forces).
	 */
	public double getSectionLength_m() {
		return sectionLength_m;
	}

	/**
	 * Shearing force at a specified index in the table.
	 * 
	 * This method is used by the {@link QSolver}- and {@link MSolver} the class.
	 * When adding a shearing force use the {@link addForce()} method which
	 * calculates the index in the table depending from the section length and the
	 * position of the force relative the the left end of the beam.
	 * 
	 * @param index
	 * @return Shearing force.
	 */
	public ShearingForceValue getShearingForceAtIndex(int index) {
		return qValues.get(index);
	}

	/**
	 * Shearing force at a specified index in the table.
	 * 
	 * This method is used by the {@link QSolver}- and the {@link MSolver} class.
	 * 
	 * @param index
	 * @param shearingForceValue The shearing force.
	 */
	public void setAtIndex(int index, ShearingForceValue shearingForceValue) {
		qValues.set(index, shearingForceValue);
	}

	/**
	 * Adds a force to this table.
	 * 
	 * @param l The {@link Load}- object representing the force.
	 */
	public void addForce(Load l) {

		double x_m = l.getDistanceFromLeftEndOfBeam_m();
		double force_N = l.getForce_N();

		int n = 0;
		for (double i = 0; i < beam.getLength(); i = i + sectionLength_m) {

			if (x_m >= i && x_m < i + sectionLength_m) {
				ShearingForceValue q = qValues.get(n);
				q.setShearingForce(force_N);
				q.setX_m(x_m);
			}
			n++;
		}
	}

	/**
	 * List of shearing forces.
	 * 
	 * @return List of {@link ShearingForceValue}- objects.
	 */
	public List<ShearingForceValue> getShearingForceTable() {
		return qValues;
	}
}
