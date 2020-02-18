package org.berthold.beamCalc;
/**
 * A Bearing.<p>
 * 
 * Defines kind of bearing and it's position in [m] relative to to left end
 * of the {@link Beam} it is supporting.
 * 
 * @param 	distanceFromLeftEndOfBeam_m		Position relative to left end of beam.
 * @see		Beam
 * @author	Berthold
 */

public class Bearing implements Comparable <Bearing>{

	private double distanceFromLeftEndOfBeam_m;

	public Bearing(double distanceFromLeftEndOfBeam_m) {
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
	}

	public double getDistanceFromLeftEndOfBeam_m() {
		return distanceFromLeftEndOfBeam_m;
	}
	
	/*
	 * Compareable
	 */

	@Override
	public int compareTo(Bearing compareDistanceFromLeftEndOfBeam_m){
		return (int)this.distanceFromLeftEndOfBeam_m-(int)compareDistanceFromLeftEndOfBeam_m.distanceFromLeftEndOfBeam_m;
	}
}
