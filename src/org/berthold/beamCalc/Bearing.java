package org.berthold.beamCalc;
/**
 * A Bearing.<p>
 * 
 * Defines kind of bearing and it's position in [m] relative to to left end
 * of the {@link Beam} it is supporting.
 * 
 * @see		Beam
 * @author	Berthold
 */

public class Bearing implements Comparable <Bearing>{

	private String nameOfBearing;
	private double distanceFromLeftEndOfBeam_m;

	/**
	 * Quick one, just pass the one and only parameter absulutely
	 * necessary.
	 * 
	 * @param distanceFromLeftEndOfBeam_m
	 */
	public Bearing(double distanceFromLeftEndOfBeam_m) {
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
	}
	
	/** 
	 * Use this if you want to give a name to the bearing.
	 * 
	 * @param nameOfBearing
	 * @param distanceFromLeftEndOfBeam_m
	 */
	public Bearing(String nameOfBearing,double distanceFromLeftEndOfBeam_m) {
		this.nameOfBearing=nameOfBearing;
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
	}

	public double getDistanceFromLeftEndOfBeam_m() {
		return distanceFromLeftEndOfBeam_m;
	}
	
	public String getNameOfBearing(){
		return nameOfBearing;
	}
	
	/*
	 * Compareable
	 */

	@Override
	public int compareTo(Bearing compareDistanceFromLeftEndOfBeam_m){
		return (int)this.distanceFromLeftEndOfBeam_m-(int)compareDistanceFromLeftEndOfBeam_m.distanceFromLeftEndOfBeam_m;
	}
}
