/**
 * A simple driver
 * 
 * Create a beam, add loads, solve.
 * 
 */
import org.berthold.beamCalc.*;
import java.util.*;
import org.berthold.beamCalc.Beam;
import org.berthold.beamCalc.BeamCalcError;
import org.berthold.beamCalc.Bearing;
import org.berthold.beamCalc.Load;
import org.berthold.beamCalc.Result;
import org.berthold.beamCalc.Solve;

public class MainBeamCalculator {
	
	public static void main(String[] args) {

		Beam myBeam = new Beam(12);

		myBeam.addBearing(new Bearing(0));
		myBeam.addBearing(new Bearing(5));

		myBeam.addLoad(new Load("F1",-5, 2.5, 180,0));
		myBeam.addLoad(new Load("F2",1, 0,90, 0));
		myBeam.addLoad(new Load("F3",3,4,-90,0));

		Result result = Solve.getResults(myBeam);

		if (result.getErrorCount() == 0) {
			System.out.println("Beam. Loads:" + myBeam.getNumberOfLoads());
			System.out.println("Left bearing:" + result.getResultingForceAtLeftBearing_N() + " N. Right bearing:"
					+ result.getResultingForceAtRightBearing_N() + " N.");
			System.out.println("Horizontal:"+result.getSumOfHorizontalForcesIn_N());
		} else
			showErrors(result);
		
		// Get loads sorted by distance from left end of beam, sort asc...
		List<Load> sortedLoads=new ArrayList();
		sortedLoads=myBeam.getLoadsSortedByDistanceFromLeftSupportDesc();
		
		System.out.println("Loads sorted by distance from left end of beam:");
		for (int i=0;i<=sortedLoads.size()-1;i++){
			double forceIn_N=sortedLoads.get(i).getDistanceFromLeftEndOfBeam_m();
			System.out.println("Load "+i+" Distance:"+forceIn_N);
		}
		
		// Get bearings sorted by distance from left end of beam, sort asc...
				List<Bearing> sortedBearings=new ArrayList();
				sortedBearings=myBeam.getBearingsSortedByDistanceFromLeftEndOfBeamDesc();
			
				System.out.println("Bearings sorted by distance from left end of beam:");
				for (int i=0;i<=sortedBearings.size()-1;i++){
					double distanceFromLeftEndOfBeam_m=sortedBearings.get(i).getDistanceFromLeftEndOfBeam_m();
					System.out.println("Bearing "+i+" Distance:"+distanceFromLeftEndOfBeam_m);
				}
		
		// Get biggest load
		Double maxLoad=myBeam.getMaxLoadIn_N();
		System.out.println("Max load:"+maxLoad);
	}
	
	/*
	 * Show description of all errors occurred.....
	 */
	private static void showErrors(Result result){
			int errorCount=result.getErrorCount();
			System.out.println("Errors:"+errorCount);
			
			for (int i=0;i<=errorCount-1;i++){
				BeamCalcError error=result.getError(i);
				if (error.getOriginOfError()==BeamCalcError.LOAD_ERROR)
					System.out.println("Error for load #"+error.getIndexOfError()+" Reason:"+error.getErrorDescription());
				if (error.getOriginOfError()==BeamCalcError.BEARING_ERROR)
					System.out.println("Error for bearing:"+error.getErrorDescription());
			}
	}
	 
}
