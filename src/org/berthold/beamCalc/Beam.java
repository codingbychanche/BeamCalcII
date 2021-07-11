package org.berthold.beamCalc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A beam.
 * 
 * @author Berthold
 *
 */
public class Beam {
	private double lengthOfBeam_m;
	private List<Support> support;
	private int numberOfSupports;
	private List<Load> loads;

	/**
	 * A new beam.
	 * 
	 * @param lengthOfBeam_m
	 */
	public Beam(double lengthOfBeam_m) {
		this.lengthOfBeam_m = lengthOfBeam_m;
		support = new ArrayList<Support>();
		numberOfSupports = 0;
		loads = new ArrayList<Load>();
	}

	/**
	 * Adds a load which either can be a point load or a distributed load.
	 * 
	 * @param load The {@link Load}- object.
	 */
	public void addLoad(Load load) {
		loads.add(load);
	}

	/**
	 * Add's a support.
	 * 
	 * @param {@Support}- object.
	 */
	public void addBearing(Support bearing) {
		support.add(bearing);
		numberOfSupports++;
	}

	/**
	 * Checks if any given length is bigger as the length of this beam.
	 * 
	 * @param distanceFromLeftEndOfBeam_m
	 * @return true if the given length is equal or smaller than the length of
	 *         this beam. false if otherwise.
	 * 
	 */
	public boolean isInsideOfBeamLength(double distanceFromLeftEndOfBeam_m) {
		if (distanceFromLeftEndOfBeam_m >= 0 && distanceFromLeftEndOfBeam_m <= lengthOfBeam_m)
			return true;
		else
			return false;
	}

	/**
	 * Load.
	 * 
	 * @param atIndex
	 * @return A {@link Load}- object from the specified index.
	 */
	public Load getLoad(int atIndex) {
		return loads.get(atIndex);
	}

	/**
	 * Length of beam.
	 * 
	 * @return The length of this beam.
	 */
	public double getLength() {
		return lengthOfBeam_m;
	}

	/**
	 * Count all loads acting on this beam. 
	 * 
	 * @return Number of point and/ or distributed loads.
	 */
	public int getNumberOfLoads() {
		return loads.size();
	}

	/**
	 * Count all supports.
	 * 
	 * @return Number of all supports for this beam.
	 */
	public int getNumberOfBearings() {
		return numberOfSupports;
	}

	/**
	 * Gets the specified support.
	 * 
	 * @param bearingIndexFromLeftEndOfBeam
	 * @return The {@link Support}- Object.
	 */
	public Support getBearing(int bearingIndexFromLeftEndOfBeam) {
		return support.get(bearingIndexFromLeftEndOfBeam);
	}

	/**
	 * Biggest load acting on this beam.
	 * 
	 * @return The load maginitute.
	 */
	public double getMaxLoadIn_N() {
		double maxLoad = 0;
		for (int i = 0; i <= loads.size() - 1; i++) {
			double load = Math.abs(loads.get(i).getForce_N());
			if (load > maxLoad)
				maxLoad = load;
		}
		return maxLoad;
	}
	
	/**
	 * Counts the number of point loads acting on this beam.
	 * 
	 * @return Number of point loads.
	 */
	public int getNumberOfSingleLoads() {
		int numberOfSingleLoads = 0;

		for (Load l : loads) {
			if (l.getLengthOfLineLoad_m() == 0)
				numberOfSingleLoads++;
		}
		return numberOfSingleLoads;
	}

	/**
	 * Counts the number of distributed loads acting on this beam.
	 * 
	 * @return Number of distributed loads.
	 */
	public int getNumberOfLineLoads() {
		int numberOfLineLoads = 0;

		for (Load l : loads) {
			if (l.getLengthOfLineLoad_m() > 0)
				numberOfLineLoads++;
		}
		return numberOfLineLoads;
	}

	/**
	 * List of all loads acting on this beam.
	 * 
	 * @return A {@link List} of {@link Load} objects.
	 */
	public List<Load> getLoads() {
		return loads;
	}

	/**
	 * Sort loads descending by distance from left end of beam.
	 * 
	 * @return Sorted list of {@link Load}- objects.
	 */
	public List<Load> getLoadsSortedByDistanceFromLeftSupportDesc() {
		Collections.sort(this.loads);
		return this.loads;
	}

	/**
	 * Sort supports descending by distance from left end of beam.
	 * 
	 * @return Sorted list of {@link Support}- objects.
	 */
	public List<Support> getSupportsSortedByDistanceFromLeftEndOfBeamDesc() {
		Collections.sort(this.support);
		return this.support;
	}
}
