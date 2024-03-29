package org.berthold.beamCalc;

/**
 * Contains result a simply supported {@link Beam}.
 * 
 * If errors (e.g. load outside of beam length) occur, an {@link Error} object is
 * added which contains a description of the problem.
 * 
 * Objects from this class are created by the {@link BeamSolver}- Class
 * 
 * @author 	Berthold
 */
import java.util.ArrayList;
import java.util.List;

public class BeamResult {

	private List<BeamCalcError> error;
	private double resultingForceAtLeftBearing_N, resultingForceAtRightBearing_N, sumOfHorizontalForcesIn_N;
	private String solutionTermForRightBearing,solutionTermForLeftBearing,solutionTermForHorizontalForces;
	
	/**
	 * Creates a new result.
	 * 
	 */
	public BeamResult() {
		this.error = new ArrayList<>();
	}

	public void addError(BeamCalcError error) {
		this.error.add(error);
	}

	public int getErrorCount() {
		return error.size();
	}

	public BeamCalcError getError(int errorAtIndex) {
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

	public double getResultingHorizontalForceAtRightSupport_N() {
		return sumOfHorizontalForcesIn_N;
	}

	public void setResultingHorizontalForceAtRightSupport_N(double sumOfHorizontalForces) {
		this.sumOfHorizontalForcesIn_N = sumOfHorizontalForces;
	}
	

	/**
	 * Adds the solution term for the sum of the horizonztal force.
	 * 
	 * @param solutionTermForHorizontalFarces
	 */
	public void addSolutionTermForHorizontalForce(String solutionTermForHorizontalFarces){
		this.solutionTermForHorizontalForces=solutionTermForHorizontalFarces;
	}
	
	/**
	 * Adds a mathematical term showing the solution in detail.
	 * This method is invoked by the {@link BeamSolver} class
	 * 
	 * @param solutionTermForRightBearing 	Mathematical term containing an in detail slution.
	 */
	public void addSolutionTermForRightBearing(String solutionTermForRightBearing){
		this.solutionTermForRightBearing=solutionTermForRightBearing;
	}
	
	/**
	 * Returns the mathematical term containing the solution
	 * for the resulting force at the rightmost bearing of the beam.
	 * 
	 * @return Term with solution
	 */
	public String getSolutionTermForRightBearing(){
		return solutionTermForRightBearing;
	}
	
	/**
	 * Adds a mathematical term showing the solution in detail.
	 * This method is invoked by the {@link BeamSolver} class
	 * 
	 * @param solutionTermForLeftBearing Term containing a detailed solution.
	 */
	public void addSolutionTermForLeftBearing(String solutionTermForLeftBearing){
		this.solutionTermForLeftBearing=solutionTermForLeftBearing;
	}
	
	/**
	 * Returns the mathematical term showing the solution for the sum
	 * of the horizontal forces in detail.
	 * 
	 * @return Mathematical term showing the solution for the sum of the horizontal
	 * forces.
	 */
	public String getSolutionTermForHorizontalForces(){
		return solutionTermForHorizontalForces;
	}
	
	/**
	 * Returns the mathematical term containing the solution
	 * for the resulting force at the leftmost bearing of the beam.
	 * 
	 * @return	Detailed solution for the left bearing
	 */
	public String getSolutionTermForLeftBearing(){
		return solutionTermForLeftBearing;
	}
}
