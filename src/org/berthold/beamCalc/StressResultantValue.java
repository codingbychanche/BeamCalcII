package org.berthold.beamCalc;

/**
 * Q- value consisting of a x- coordinate and a force acting.
 * 
 * Acts as a helper- class for the {@link QSolver}- class.
 * 
 * @author Berthold
 *
 */

public class StressResultantValue {
	private double x_m,shearingForceValue;
	
	/**
	 * Creates a new Q- Value.
	 * 
	 * @param x_m	Distance from left end of beam.
	 * @param shearingForceValue	Force acting.
	 */
	public StressResultantValue(double x_m, double shearingForceValue) {
		super();
		this.x_m = x_m;
		this.shearingForceValue = shearingForceValue;
	}

	/**
	 * Position of this sharing force.
	 * 
	 * @return Position relative to the left end of the beam.
	 */
	public double getX_m() {
		return x_m;
	}

	/**
	 * Sets the position of this shearing force.
	 * 
	 * @param x_m Position relative to the left end of the beam.
	 */
	public void setX_m(double x_m) {
		this.x_m = x_m;
	}

	/**
	 * Get the magnitute of this shearing force.
	 * 
	 * @return
	 */
	public double getShearingForce() {
		return shearingForceValue;
	}

	/**
	 * Sets the magnitute of this shearing force.
	 * 
	 * @param f_N	Shearing force magnitute.
	 */
	public void setShearingForce(double f_N) {
		this.shearingForceValue = f_N;
	}
	/**
	 * Adds an arbitrary value to this shearing force.
	 * 
	 * @param value Sheraing force to be added to this value.
	 */
	public void addValue(double value) {
		this.shearingForceValue=this.shearingForceValue+value;
	}

	/**
	 * Multiplys this value by an arbitrary value
	 * 
	 * @param value
	 */
	public void multiplyValue(double value) {
		this.shearingForceValue=this.shearingForceValue*value;
	}
}
