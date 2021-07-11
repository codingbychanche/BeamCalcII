package org.berthold.beamCalc;

/**
 * Description of an error.
 * 
 * @author Berthold
 *
 */
public class BeamCalcError {

	public static final int LOAD_ERROR=1;		// Origin of error.....
	public static final int SUPPORT_ERROR=2;
	
	private int originOfError;
	private int indexOfError; 					// Index of load or bearing in source data list that may have caused the error
	private String errorDescription;
	
	/**
	 * Creates a detailed description of the nature of an error.	
	 *
	 * @param originOfError		What caused the problem? Load, Bearing.....?
	 * @param indexOfError		Index of element in list in which it was stored.
	 * @param errorDescription	Description of error in plain text.
	 */
	public BeamCalcError (int originOfError,int indexOfError,String errorDescription){
		this.originOfError=originOfError;
		this.indexOfError=indexOfError;
		this. errorDescription= errorDescription;
	}
	
	/**
	 * What caused the error?
	 * 
	 * @return Detailed description of the nature of an error.
	 */
	public int getOriginOfError(){
		return originOfError;
	}
	
	/**
	 * The index of the error. For a load object, this is the index of the 
	 * load inside the list in which it was stored. Same as load number.
	 * 
	 * @return Index.
	 */
	public int getIndexOfError() {
		return indexOfError;
	}

	public void setIndexOfError(int indexOfError) {
		this.indexOfError = indexOfError;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
}
