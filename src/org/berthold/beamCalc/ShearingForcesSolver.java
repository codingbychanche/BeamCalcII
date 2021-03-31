package org.berthold.beamCalc;
/**
 * Calculates the shearing forces magintutes.
 *
 */

import java.util.ArrayList;
import java.util.List;

public class ShearingForcesSolver {

	/**
	 * This returns a list of coordinates where the load magnitute changes. Left
	 * end of beam is x=0.
	 * <p>
	 * 
	 * @param beam
	 *            {@link Beam} object to solve.
	 * @param result
	 *            {@link BeamResult} object containing the valid result of the
	 *            {@link Beam} parameter
	 * @return A new {@link Beam} object containing the shearing force
	 *         magnitutes.
	 * 
	 * @author Berthold
	 */
	public static Beam solve(Beam beam, BeamResult result) {

		// A new beam is used in order to calculate the shearing forces.
		// This is done bacause the supporting forces have to be added to the
		// beam as acting forces. All following calculations are done
		// using Qbeam.
		//
		// Qbeam can be used draw the searing forces or create a table of
		// them.....
		Beam Qbeam = new Beam(beam.getLength());

		// Add supporting forces as loads
		Support bearing = beam.getBearing(0);
		double distanceFromLeftEnd = bearing.getDistanceFromLeftEndOfBeam_m();
		Load supportLeft_N = new Load("FA", result.getResultingForceAtLeftBearing_N(), distanceFromLeftEnd, 0, 0);
		Qbeam.addLoad(supportLeft_N);

		bearing = beam.getBearing(1);
		distanceFromLeftEnd = bearing.getDistanceFromLeftEndOfBeam_m();
		Load supportRight_N = new Load("FB", result.getResultingForceAtRightBearing_N(), distanceFromLeftEnd, 0, 0);
		Qbeam.addLoad(supportRight_N);

		// Convert all loads to single loads
		int n = beam.getNumberOfLoads() - 1;
		Load singleLoad_N;
		for (int i = 0; i <= n; i++) {
			Load l = beam.getLoad(i);

			if (l.getLengthOfLineLoad_m() > 0) {
				// Name Force Dist Ang. Length
				singleLoad_N = new Load("-", l.getForceStart_N(), l.getDistanceFromLeftEndOfBeam_m(), 0, l.getLengthOfLineLoad_m());
				Qbeam.addLoad(singleLoad_N);

				singleLoad_N = new Load("-", l.getForceEnd_N(),
						l.getDistanceFromLeftEndOfBeam_m() + l.getLengthOfLineLoad_m(), 0, 0);
				Qbeam.addLoad(singleLoad_N);
			} else {
				singleLoad_N = new Load("-", l.getForce_N(), l.getDistanceFromLeftEndOfBeam_m(), 0,
						l.getLengthOfLineLoad_m());
				Qbeam.addLoad(singleLoad_N);
			}
		}
		// Get single loads sorted...
		List<Load> singleLoadsSorted_N = new ArrayList<>();
		singleLoadsSorted_N = Qbeam.getLoadsSortedByDistanceFromLeftSupportDesc();

		// Calculate sections between single loads
		List<Double> loadSections = new ArrayList<>();
		double posOfLoad_m = 0;
		double posOfNextLoad_m = 0;
		double sectionLength_m;

		int sizeOfSingleLoads = singleLoadsSorted_N.size() - 1;
		for (int i = 0; i < sizeOfSingleLoads; i++) {
			posOfLoad_m = singleLoadsSorted_N.get(i).getDistanceFromLeftEndOfBeam_m();
			posOfNextLoad_m = singleLoadsSorted_N.get(i + 1).getDistanceFromLeftEndOfBeam_m();
			sectionLength_m = posOfNextLoad_m - posOfLoad_m;
			loadSections.add(sectionLength_m);
		}
		loadSections.add(0.0);

		// test
		System.out.println();
		for (Double ls : loadSections)
			System.out.println("LOAD SECTION:" + ls);
		System.out.println();

		// Create solver table
		n = singleLoadsSorted_N.size();
		List<ShearingForcesQTable> qTab = new ArrayList<>();
		for (int i = 0; i <= n - 1; i++) {
			singleLoad_N = singleLoadsSorted_N.get(i);

			double force_N = singleLoad_N.getForce_N();
			double xstart = singleLoad_N.getDistanceFromLeftEndOfBeam_m();
			double sectionLength=loadSections.get(i);
			double length = singleLoad_N.getLengthOfLineLoad_m();
			
			if (singleLoad_N.getLengthOfLineLoad_m()==0)
				sectionLength=0;
			
			double xend = singleLoad_N.getDistanceFromLeftEndOfBeam_m() + sectionLength;
			
			double q = 0;

			ShearingForcesQTable l = new ShearingForcesQTable(force_N, xstart, xend, length, q);
			qTab.add(l);

		}

		// test
		for (ShearingForcesQTable t : qTab) {
			System.out.println("F=" + t.getLoad_N() + "  xstart=" + t.getxStart_m() + "   xend=" + t.getxEnd_m()
					+ "   l=" + t.getLegngth_m());
		}

		// Create a beam containing all shearing forces
		// Beam QbeamSolved = new Beam(Qbeam.getLength());
		int numberOfLoads_N = loadSections.size() - 1;

		return (null);
	}
}
