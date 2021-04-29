package org.berthold.beamCalc;

/**
 * Stress resltant value consisting of a x- coordinate and a force acting.
 * 
 * Acts as a helper- class for the {@link QSolver}- class.
 * 
 * @author Berthold
 *
 */

public class StressResultantValue {
	private double x_m;
	private double shearingForceValue;

	// Discontinuiuty=> A single load acting or start/ end of
	// a distributed load.
	//
	// This value is needed in order to determine the local maxima
	// in Q(x) and M(x).
	private boolean isDiscontiunuity;
	private double shearingForceDeltaBy; // Force acting here!
	
	private boolean isZeroPoint; // Zero points in Q(x) are maxima in M(x).
	private boolean isMaxima;	

	/**
	 * Creates a new Stress resltant value.
	 * 
	 * @param x_m                Distance from left end of beam.
	 * @param shearingForceValue Force acting.
	 */
	public StressResultantValue(double x_m, double shearingForceValue) {
		super();
		this.x_m = x_m;
		this.shearingForceValue = shearingForceValue;
	}

	/**
	 * Position of this shearing force.
	 * 
	 * @return Position relative to the left end of the beam.
	 */
	public double getX_m() {
		return x_m;
	}

	/**
	 * Sets the position of this stress resultant value.
	 * 
	 * @param x_m Position relative to the left end of the beam.
	 */
	public void setX_m(double x_m) {
		this.x_m = x_m;
	}

	/**
	 * Get the magnitute of this stress resltant value.
	 * 
	 * @return
	 */
	public double getShearingForce() {
		return shearingForceValue;
	}

	/**
	 * Sets the magnitute of this stress resltant value.
	 * 
	 * @param f_N Stress resltant value magnitute.
	 */
	public void setShearingForce(double f_N) {
		this.shearingForceValue = f_N;
	}

	/**
	 * Adds an arbitrary value to this stress resltant value.
	 * 
	 * @param value Sheraing force to be added to this value.
	 */
	public void addValue(double value) {
		this.shearingForceValue = this.shearingForceValue + value;
	}

	/**
	 * Multiplys this value by an arbitrary value
	 * 
	 * @param value
	 */
	public void multiplyValue(double value) {
		this.shearingForceValue = this.shearingForceValue * value;
	}

	public double getShearingForceValue() {
		return shearingForceValue;
	}

	public void setShearingForceValue(double shearingForceValue) {
		this.shearingForceValue = shearingForceValue;
	}

	public boolean isDiscontiunuity() {
		return isDiscontiunuity;
	}

	public void setDiscontiunuity(boolean isDiscontiunuity) {
		this.isDiscontiunuity = isDiscontiunuity;
	}

	public double getShearingForceDeltaBy() {
		return shearingForceDeltaBy;
	}

	public void setShearingForceDeltaBy(double shearingForceDeltaBy) {
		this.shearingForceDeltaBy = shearingForceDeltaBy;
	}

	public boolean isZeroPoint() {
		return isZeroPoint;
	}

	public void setZeroPoint(boolean isZeroPoint) {
		this.isZeroPoint = isZeroPoint;
	}

	public boolean isMaxima() {
		return isMaxima;
	}

	public void setMaxima(boolean isMaxima) {
		this.isMaxima = isMaxima;
	}
	
	
}
