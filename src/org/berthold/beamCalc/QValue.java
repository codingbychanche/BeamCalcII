package org.berthold.beamCalc;

/**
 * Q- value consisting of a x- coordinate and a force acting.
 * 
 * Acts as a helper- class for the {@link QSolver}- class.
 * 
 * @author Berthold
 *
 */

public class QValue {
	private double x_m,q_N;
	
	/**
	 * Creates a new Q- Value.
	 * 
	 * @param x_m	Distance from left end of beam.
	 * @param f_N	Force acting.
	 */
	public QValue(double x_m, double f_N) {
		super();
		this.x_m = x_m;
		this.q_N = f_N;
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
	public double getQ_N() {
		return q_N;
	}

	/**
	 * Sets the magnitute of this shearing force.
	 * 
	 * @param f_N	Shearing force magnitute.
	 */
	public void setQ_N(double f_N) {
		this.q_N = f_N;
	}
	/**
	 * Adds an arbitrary value to this shearing force.
	 * 
	 * @param q_N Sheraing force to be added to this value.
	 */
	public void addQ_N(double q_N) {
		this.q_N=this.q_N+q_N;
	}
}
