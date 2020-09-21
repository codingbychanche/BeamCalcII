package org.berthold.beamCalc;
/**
 * Load.
 * 
 * Defines a single or a line load in [N] acting on a {@link Beam}
 *
 * Sign:<p> 
 *
 * Vertical loads: Force acting downwards (-) Load acting upwards (+).
 * Horizontal force acts to the left (+), force acts to the right (-)<p>
 * 
 * Angle in degrees:<p>
 *                
 * Right side angles range from 0 to 180 deg, clockwise.
 * Left side angles range from 0 to -179 deg, counterclokwise.<p>
 *                  
 * Samples<p>
 * 
 * 		+90 deg.  load acting to left, horizontally<br>
 * 		+45 deg.  load acting diagonally from upper right to bottom left<br>
 *      +135 deg. same as +45 but acting from lower right to upper left<p>
 *      A  negative load at an angle of 180 deg. is the same as a positive load acting at 0 deg.<p>
 *      
 * @see		Beam
 * @author 	Berthold
 */
public class Load  implements  Comparable <Load>{
	private String nameOfLoad;
	private double force_N;
	private double distanceFromLeftEndOfBeam_m;
	private double angleOfLoad_degrees;
	private double lengthOfLineLoad_m;
	private boolean includeThisLoadIntoCaclulation;
	private boolean thisLoadHasAnError;

	/**
	 * Create a load acting on the beam.
	 * 
	 * @param nameOfLoad						Any name allowed (F1, q1 etc..).
	 * @param force_N							The force acting (-) downwards. (+) upwards.
	 * @param distanceFromLeftEndOfBeam_m		Position of force acting.
	 * @param angleOfLoad_degrees				Angle.
	 * @param lengthOfLineLoad_m				Length, if line load.
	 * @param includeThisLoadIntoCaclulation	Use where aplicable....
	 * @param thisLoadHasAnError				Use where aplicable.... 
	 */
	public Load(String nameOfLoad,double force_N, double distanceFromLeftEndOfBeam_m, double angleOfLoad_degrees,double lengthOfLineLoad_m,boolean includeThisLoadIntoCaclulation,boolean thisLoadHasAnError) {
		this.nameOfLoad=nameOfLoad;
		this.force_N = force_N;
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
		this.angleOfLoad_degrees=angleOfLoad_degrees;
		this.lengthOfLineLoad_m = lengthOfLineLoad_m;
		this.includeThisLoadIntoCaclulation=includeThisLoadIntoCaclulation;
		this.thisLoadHasAnError=thisLoadHasAnError;
	}
	
	/*
	 * This constructor requires no angle for the load. 
	 * Included this to make updates of projects using older versions 
	 * of this library easier.
	 */
	public Load(String nameOfLoad,double force_N, double distanceFromLeftEndOfBeam_m, double lengthOfLineLoad_m,boolean includeThisLoadIntoCaclulation,boolean thisLoadHasAnError, double angleOfLoad_degrees) {
		this.nameOfLoad=nameOfLoad;
		this.force_N = force_N;
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
		this.angleOfLoad_degrees=angleOfLoad_degrees;
		this.lengthOfLineLoad_m = lengthOfLineLoad_m;
		this.includeThisLoadIntoCaclulation=includeThisLoadIntoCaclulation;
		this.thisLoadHasAnError=thisLoadHasAnError;
		this.angleOfLoad_degrees=0;
	}

	/*
	 * Default constructor which does not require flags (load has error, include load into calc.) as
	 * parameters.
	 */
	public Load(String nameOfLoad,double force_N, double distanceFromLeftEndOfBeam_m, double angleOfLoad_degrees,double lengthOfLineLoad_m) {
		this.nameOfLoad=nameOfLoad;
		this.force_N = force_N;
		this.distanceFromLeftEndOfBeam_m = distanceFromLeftEndOfBeam_m;
		this.angleOfLoad_degrees=angleOfLoad_degrees;
		this.lengthOfLineLoad_m = lengthOfLineLoad_m;
		this.includeThisLoadIntoCaclulation=includeThisLoadIntoCaclulation;
		this.thisLoadHasAnError=thisLoadHasAnError;
	}
	
	/*
	 * Getters
	 */

	public String getName(){
		return nameOfLoad;
	}
	
	public double getForce_N() {
		return force_N;
	}

	public double getDistanceFromLeftEndOfBeam_m() {
		return distanceFromLeftEndOfBeam_m;
	}
	
	public double getAngleOfLoad_degrees() {
		return angleOfLoad_degrees;
	}

	public double getLengthOfLineLoad_m() {
		return lengthOfLineLoad_m;
	}

	public boolean getIncludeThisLoadIntoCalculation(){
		return includeThisLoadIntoCaclulation;
	}
	
	public boolean getError(){
		return thisLoadHasAnError;
	}
	
	/*
	 * Setters
	 */

	public void changeNameTo(String nameOfLoad){
		this.nameOfLoad=nameOfLoad;
	}
	
	public void setIncludeIntoCalculation(boolean include){
		includeThisLoadIntoCaclulation=include;
	}
	
	public void setError(){
		thisLoadHasAnError=true;
	}
	
	public void hasNoError(){
		thisLoadHasAnError=false;
	}
	
	/*
	 * Compareable
	 */

	@Override
	public int compareTo(Load compareDistanceFromLeftSupport_m){
		return (int)this.distanceFromLeftEndOfBeam_m-(int)compareDistanceFromLeftSupport_m.distanceFromLeftEndOfBeam_m;
	}
}
