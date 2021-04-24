package org.berthold.beamCalc;

/**
 * A support.
 * <p>
 * 
 * @author Berthold
 */

public class Support implements Comparable<Support> {

	private String nameOfSupport;
	private double distanceFromLeftEndOfBeam_m;

	private int supportType;
	public static final int ROLLER_SUPPORT = 1;
	public static final int PIN_SUPPORT = 2;

	/**
	 * Quick one, just pass the one and only parameter absolutely necessary.
	 * 
	 * @param distanceFromLeftEndOfBeam_m
	 */
	public Support(double distanceFromLeftEndOfBeam_m) {
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
		this.nameOfSupport = "-";
	}

	/**
	 * Use this if you want to give a name to the support.
	 * 
	 * @param nameOfSupport
	 * @param distanceFromLeftEndOfBeam_m
	 */
	public Support(String nameOfSupport, double distanceFromLeftEndOfBeam_m) {
		this.nameOfSupport = nameOfSupport;
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
	}

	public Support(String nameOfSupport, double distanceFromLeftEndOfBeam_m, int supportType) {
		this.nameOfSupport = nameOfSupport;
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
		this.supportType = supportType;
	}

	/**
	 * Distance from left end of beam.
	 * 
	 * @return Distance of the support from the left end of the beam.
	 */
	public double getDistanceFromLeftEndOfBeam_m() {
		return distanceFromLeftEndOfBeam_m;
	}

	/**
	 * Support name.
	 * 
	 * @return Name of the support.
	 */
	public String getNameOfSupport() {
		return nameOfSupport;
	}
	
	/**
	 * Type.
	 * 
	 * @return The support type.
	 */
	
	public int getType() {
		return this.supportType;
	}

	/*
	 * Compareable
	 */
	@Override
	public int compareTo(Support compareDistanceFromLeftEndOfBeam_m) {
		return (int) this.distanceFromLeftEndOfBeam_m
				- (int) compareDistanceFromLeftEndOfBeam_m.distanceFromLeftEndOfBeam_m;
	}
}
