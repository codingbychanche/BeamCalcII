package org.berthold.beamCalc;

/**
 * Stress resltant value consisting of a x- coordinate and a force acting.
 * 
 * @author Berthold
 *
 */

public class StressResultant {
	private double x_m;
	private double stressResultantValue;
	private String unit;
	private String name;

	// Discontinuiuty=> A single load acting or start/ end of
	// a distributed load.
	//
	// This value is needed in order to determine the local maxima
	// in Q(x) and M(x).
	private boolean isDiscontiunuity;
	private double shearingForceDeltaBy; // Force acting here!

	private boolean isZeroPoint; 
	private boolean isMaxima;

	/**
	 * Creates a new Stress resltant value.
	 * 
	 * @param x_m                Distance from left end of beam.
	 * @param shearingForceValue Force acting.
	 * @param unit	The unit (N, Nm etc...).
	 */
	public StressResultant(double x_m, double shearingForceValue,String unit) {
		super();
		this.x_m = x_m;
		this.stressResultantValue = shearingForceValue;
		this.unit=unit;
	}

	/**
	 * Position of this stress resultant.
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
		return stressResultantValue;
	}

	/**
	 * Sets the magnitute of this stress resltant value.
	 * 
	 * @param f_N Stress resltant value magnitute.
	 */
	public void setShearingForce(double f_N) {
		this.stressResultantValue = f_N;
	}

	/**
	 * Adds an arbitrary value to this stress resltant value.
	 * 
	 * @param value Sheraing force to be added to this value.
	 */
	public void addValue(double value) {
		this.stressResultantValue = this.stressResultantValue + value;
	}

	/**
	 * Multiplys this value by an arbitrary value
	 * 
	 * @param value
	 */
	public void multiplyValue(double value) {
		this.stressResultantValue = this.stressResultantValue * value;
	}

	/**
	 * The magnitute of this stress resultant.
	 * 
	 * @return stress resultant magnitute.
	 */
	public double getShearingForceValue() {
		return stressResultantValue;
	}

	/**
	 * 
	 * Repleaces the current value of this stress resultant with the new one.
	 * 
	 * @param shearingForceValue New value for this stress resultant.
	 */
	public void setShearingForceValue(double shearingForceValue) {
		this.stressResultantValue = shearingForceValue;
	}

	/**
	 * Marks that this stress resultant acts at a point of disconuinity. This Value is
	 * set when a {@link StressResultantTable} is initalized either by the
	 * {@link Qsolver}- or the {@link MSolver} class.
	 * 
	 * @return True if/ false if no disconuinity.
	 */
	public boolean isDiscontiunuity() {
		return isDiscontiunuity;
	}

	/**
	 * Marks that this stress resultant acts at a point of disconuinity. This Value is
	 * set when a {@link StressResultantTable} is initalized either by the
	 * {@link Qsolver}- or the {@link MSolver} class.
	 */
	public void setDiscontiunuity(boolean isDiscontiunuity) {
		this.isDiscontiunuity = isDiscontiunuity;
	}
	
	/**
	 * The unit of this stress resultant.
	 * @return	Unit e.g. N, Nm etc...
	 */
	public String getUnit() {
		return unit;
	}

	public boolean isZeroPoint() {
		return isZeroPoint;
	}

	public void setZeroPoint(boolean isZeroPoint) {
		this.isZeroPoint = isZeroPoint;
	}

	public double getShearingForceDeltaBy() {
		return shearingForceDeltaBy;
	}

	public void setShearingForceDeltaBy(double shearingForceDeltaBy) {
		this.shearingForceDeltaBy = shearingForceDeltaBy;
	}

	public boolean isMaxima() {
		return isMaxima;
	}

	public void setMaxima(boolean isMaxima) {
		this.isMaxima = isMaxima;
	}

	/**
	 * Name of the asociated load.
	 * 
	 * @return Name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this stress resultant.
	 * 
	 * @param name Name of the associated {@link Load}- object.
	 */
	public void setName(String name) {
		this.name = name;
	}

}
