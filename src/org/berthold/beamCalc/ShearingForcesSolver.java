package org.berthold.beamCalc;
/**
 * This returns a list of coordinates where the load magnitute changes. Left end of beam is x=0.
 * 
 * 		F1		F2
 *  x----|-------|------------x
 *  	 |		 |
 *  | l1 |  l2   |      l3    |
 *  
 *  
 * 
 * @author Berthold
 *
 */

import java.util.ArrayList;
import java.util.List;

public class ShearingForcesSolver {

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
		Bearing bearing = beam.getBearing(0);
		double distanceFromLeftEnd = bearing.getDistanceFromLeftEndOfBeam_m();
		Load supportLeft_N = new Load("FA", result.getResultingForceAtLeftBearing_N(), distanceFromLeftEnd, 0, 0);
		Qbeam.addLoad(supportLeft_N);

		bearing = beam.getBearing(1);
		distanceFromLeftEnd = bearing.getDistanceFromLeftEndOfBeam_m();
		Load supportRight_N = new Load("FB", result.getResultingForceAtRightBearing_N(), distanceFromLeftEnd, 0,0);
		Qbeam.addLoad(supportRight_N);

		// Convert all loads to single loads
		int n = beam.getNumberOfLoads() - 1;
		Load singleLoad_N;
		for (int i = 0; i <= n; i++) {
			Load l = beam.getLoad(i);
			
			if (l.getLengthOfLineLoad_m() > 0) {
				//						Name	Force		Dist		Ang.	Length
				singleLoad_N = new Load("-", l.getForce_N(), l.getDistanceFromLeftEndOfBeam_m(),0,l.getLengthOfLineLoad_m());
				Qbeam.addLoad(singleLoad_N);
				
				singleLoad_N = new Load("-", l.getForce_N(),l.getDistanceFromLeftEndOfBeam_m() + l.getLengthOfLineLoad_m(), 0, l.getLengthOfLineLoad_m());
				Qbeam.addLoad(singleLoad_N);
			} else {
				singleLoad_N = new Load("-", l.getForce_N(), l.getDistanceFromLeftEndOfBeam_m(), 0,
						l.getLengthOfLineLoad_m());
				Qbeam.addLoad(singleLoad_N);
			}
		}
		
		// Get single loads sorted...
		List<Load> singleLoads_N = new ArrayList();
		singleLoads_N = Qbeam.getLoadsSortedByDistanceFromLeftSupportDesc();

		// test
		for (Load sl : singleLoads_N)
			System.out.println("SINGLE:" + sl.getForce_N() + "Dist:" + sl.getDistanceFromLeftEndOfBeam_m());

		// Calculate sections between single loads
		List<Double> loadSections = new ArrayList<>();
		double posOfLoad_m = 0;
		double posOfNextLoad_m = 0;
		double sectionLength_m;

	
		int sizeOfSingleLoads = singleLoads_N.size()-1;
		for (int i = 0; i < sizeOfSingleLoads; i++) {
			posOfLoad_m = singleLoads_N.get(i).getDistanceFromLeftEndOfBeam_m();
			posOfNextLoad_m = singleLoads_N.get(i + 1).getDistanceFromLeftEndOfBeam_m();
			sectionLength_m = posOfNextLoad_m - posOfLoad_m;
			loadSections.add(sectionLength_m);
		}
		loadSections.add(0.0);
		
		// test
		System.out.println();
		for (Double ls : loadSections)
			System.out.println("LOAD SECTION:" + ls);
		System.out.println();

		// Create a beam containing all shearing forces
		Beam QbeamSolved = new Beam(Qbeam.getLength());

		int numberOfLoads_N = loadSections.size()- 1;

		double Qs=0;
		double Qn = 0;
		double Qn1;
		
		double Dl=0;
		double distanceFromLeftEndOfBeam=0;
		for (int i = 0; i <= numberOfLoads_N; i++) {

			
			double lengthOfLineLoad_m = singleLoads_N.get(i).getLengthOfLineLoad_m();
			Qn1 = singleLoads_N.get(i).getForce_N();
			Dl = loadSections.get(i);
			
			if (lengthOfLineLoad_m == 0) {
				Qs = Qn+Qn1;
			} else 	{
				Qs =Qn+Qn1 * Dl;
			}
			distanceFromLeftEndOfBeam=singleLoads_N.get(i).getDistanceFromLeftEndOfBeam_m();
			
			
			System.out.println("Distance from left end:"+distanceFromLeftEndOfBeam+ "    Qn="+Qn+"   Qn+1="+Qn1+" Dl="+Dl+"  Q'="+Qs);
			Qn=Qs;
		}
		
		// Finaly:
		// Create a beam containing the shearing forces 
		Beam beamWithResultingShearingForces=new Beam (beam.getLength());
		beamWithResultingShearingForces.addBearing(beam.getBearing(0));
		beamWithResultingShearingForces.addBearing(beam.getBearing(1));
		
		
		return (beamWithResultingShearingForces);
	}
}
