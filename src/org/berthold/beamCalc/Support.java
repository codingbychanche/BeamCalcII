package org.berthold.beamCalc;
/**
 * A Bearing.<p>
 * 
 * Defines kind of suport and it's position in [m] relative to to left end
 * of the {@link Beam} it is supporting.
 * 
 * @see		Beam
 * @author	Berthold
 */

public class Support implements Comparable <Support>{

	private String nameOfSupport;
	private double distanceFromLeftEndOfBeam_m;

	/**
	 * Quick one, just pass the one and only parameter absulutely
	 * necessary.
	 * 
	 * @param distanceFromLeftEndOfBeam_m
	 */
	public Support(double distanceFromLeftEndOfBeam_m) {
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
	}
	
	/** 
	 * Use this if you want to give a name to the support.
	 * 
	 * @param nameOfSupport
	 * @param distanceFromLeftEndOfBeam_m
	 */
	public Support(String nameOfSupport,double distanceFromLeftEndOfBeam_m) {
		this.nameOfSupport=nameOfSupport;
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
	}

	public double getDistanceFromLeftEndOfBeam_m() {
		return distanceFromLeftEndOfBeam_m;
	}
	
	public String getNameOfSupport(){
		return nameOfSupport;
	}
	
	/*
	 * Compareable
	 */
	@Override
	public int compareTo(Support compareDistanceFromLeftEndOfBeam_m){
		return (int)this.distanceFromLeftEndOfBeam_m-(int)compareDistanceFromLeftEndOfBeam_m.distanceFromLeftEndOfBeam_m;
	}
}
