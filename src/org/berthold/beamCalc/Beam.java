package org.berthold.beamCalc;
/**
 * A Beam.
 * 
 * @author Berthold
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Berthold
 *
 */
public class Beam {
	private double lengthOfBeam_m;
	private List<Bearing> support;
	private int numberOfBearings;
	private List<Load> stress;

	public Beam(double lengthOfBeam_m) {
		this.lengthOfBeam_m = lengthOfBeam_m;
		support = new ArrayList<Bearing>();
		numberOfBearings = 0;
		stress = new ArrayList<Load>();
	}

	public void addLoad(Load load) {
		stress.add(load);
	}

	public void addBearing(Bearing bearing) {
		support.add(bearing);
		numberOfBearings++;
	}

	public boolean isInsideOfBeamLength(double distanceFromLeftEndOfBeam_m) {
		if (distanceFromLeftEndOfBeam_m >= 0 && distanceFromLeftEndOfBeam_m <= lengthOfBeam_m)
			return true;
		else
			return false;
	}

	public Load getLoad(int atIndex) {
		return stress.get(atIndex);
	}

	public double getLength() {
		return lengthOfBeam_m;
	}

	public int getNumberOfLoads() {
		return stress.size();
	}

	public int getNumberOfBearings() {
		return numberOfBearings;
	}

	public Bearing getBearing(int bearingIndexFromLeftEndOfBeam) {
		return support.get(bearingIndexFromLeftEndOfBeam);
	}

	public double getMaxLoadIn_N() {
		double maxLoad = 0;
		for (int i = 0; i <= stress.size() - 1; i++) {
			double load = Math.abs(stress.get(i).getForce_N());
			if (load > maxLoad)
				maxLoad = load;
		}
		return maxLoad;
	}

	public int getNumberOfSingleLoads() {
		int numberOfSingleLoads = 0;

		for (Load l : stress) {
			if (l.getLengthOfLineLoad_m() == 0)
				numberOfSingleLoads++;
		}
		return numberOfSingleLoads;
	}

	public int getNumberOfLineLoads() {
		int numberOfLineLoads = 0;

		for (Load l : stress) {
			if (l.getLengthOfLineLoad_m() > 0)
				numberOfLineLoads++;
		}
		return numberOfLineLoads;
	}

	/*
	 * Sorting
	 * 
	 * These methods my come in handy, if one uses this package with a GUI, when
	 * creating a drawing of the result.
	 * 
	 * E.g. When drawing line loads the drawing is much clearer when the bigger
	 * load is drawn first, then the smaller one. Otherwise the bigger load
	 * would overlap the smaller one.....
	 */

	public List<Load> getLoadsSortedByDistanceFromLeftSupportDesc() {
		Collections.sort(this.stress);
		return this.stress;
	}

	public List<Bearing> getBearingsSortedByDistanceFromLeftEndOfBeamDesc() {
		Collections.sort(this.support);
		return this.support;
	}
}
