package org.berthold.beamCalc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/** 
 * A beam
 * 
 * @author Berthold
 *
 */
public class Beam {
	private double lengthOfBeam_m;
	private List<Support> support;
	private int numberOfSupports;
	private List<Load> force;

	/**
	 * A new beam.
	 * 
	 * @param lengthOfBeam_m
	 */
	public Beam(double lengthOfBeam_m) {
		this.lengthOfBeam_m = lengthOfBeam_m;
		support = new ArrayList<Support>();
		numberOfSupports = 0;
		force = new ArrayList<Load>();
	}

	public void addLoad(Load load) {
		force.add(load);
	}

	public void addBearing(Support bearing) {
		support.add(bearing);
		numberOfSupports++;
	}

	public boolean isInsideOfBeamLength(double distanceFromLeftEndOfBeam_m) {
		if (distanceFromLeftEndOfBeam_m >= 0 && distanceFromLeftEndOfBeam_m <= lengthOfBeam_m)
			return true;
		else
			return false;
	}

	public Load getLoad(int atIndex) {
		return force.get(atIndex);
	}

	public double getLength() {
		return lengthOfBeam_m;
	}

	public int getNumberOfLoads() {
		return force.size();
	}

	public int getNumberOfBearings() {
		return numberOfSupports;
	}

	public Support getBearing(int bearingIndexFromLeftEndOfBeam) {
		return support.get(bearingIndexFromLeftEndOfBeam);
	}

	public double getMaxLoadIn_N() {
		double maxLoad = 0;
		for (int i = 0; i <= force.size() - 1; i++) {
			double load = Math.abs(force.get(i).getForce_N());
			if (load > maxLoad)
				maxLoad = load;
		}
		return maxLoad;
	}

	public int getNumberOfSingleLoads() {
		int numberOfSingleLoads = 0;

		for (Load l : force) {
			if (l.getLengthOfLineLoad_m() == 0)
				numberOfSingleLoads++;
		}
		return numberOfSingleLoads;
	}

	public int getNumberOfLineLoads() {
		int numberOfLineLoads = 0;

		for (Load l : force) {
			if (l.getLengthOfLineLoad_m() > 0)
				numberOfLineLoads++;
		}
		return numberOfLineLoads;
	}

	/*
	 *
	 */
	
	/**
	 * Sort loads descending by distance from left end of beam.
	 * 
	 * @return Sorted list of loads.
	 */
	public List<Load> getLoadsSortedByDistanceFromLeftSupportDesc() {
		Collections.sort(this.force);
		return this.force;
	}

	/**
	 * Sort supports descending by distance from left end of beam.
	 * @return
	 */
	public List<Support> getSupportsSortedByDistanceFromLeftEndOfBeamDesc() {
		Collections.sort(this.support);
		return this.support;
	}
}
