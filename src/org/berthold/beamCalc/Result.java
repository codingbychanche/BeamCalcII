package org.berthold.beamCalc;
/**
 * Contains the resulting forces at the bearings of an simply supported {@link Beam}.<p>
 * 
 * If errors (e.g. load outside of beam length) occur, an {link Error} object is
 * added which contains a description of the problem.<p>
 * 
 * Objects from this class are created by the {@link Solve}- Class
 * 
 * @return 	Check 'getters'
 * @see		Solve
 * @author 	Berthold
 *
 */
import java.util.ArrayList;
import java.util.List;

public class Result {
	
	private List <BeamCalcError> error;
	private double resultingForceAtLeftBearing_N, resultingForceAtRightBearing_N,sumOfHorizontalForcesIn_N;
	
	public Result(){
		error=new ArrayList();
	}
	
	public void addError(BeamCalcError error){
		this.error.add(error);
	}
	public int getErrorCount() {
		return error.size();
	}
	
	public BeamCalcError getError(int errorAtIndex){
		return error.get(errorAtIndex);
	}

	public double getResultingForceAtRightBearing_N() {
		return resultingForceAtRightBearing_N;
	}

	public void setResultingForceAtRightBearing_N(double resultingForceAtRightBearing_N) {
		this.resultingForceAtRightBearing_N = resultingForceAtRightBearing_N;
	}

	public double getResultingForceAtLeftBearing_N() {
		return resultingForceAtLeftBearing_N;
	}

	public void setResultingForceAtLeftBearingBearing_N(double resultingForceAtLeftBearing_N) {
		this.resultingForceAtLeftBearing_N = resultingForceAtLeftBearing_N;
	}
	
	public double getSumOfHorizontalForcesIn_N(){
		return sumOfHorizontalForcesIn_N;
	}
	
	public void setSumOfHorizontalForcesIn_N(double sumOfHorizontalForces){
		this.sumOfHorizontalForcesIn_N=sumOfHorizontalForces;
	}
}



