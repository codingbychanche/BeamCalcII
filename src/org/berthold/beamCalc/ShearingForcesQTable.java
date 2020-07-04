package org.berthold.beamCalc;

public class ShearingForcesQTable {
	private double load_N,xStart_m,xEnd_m,legngth_m,SharingForce_N;

	public ShearingForcesQTable(double load_N, double xStart_m, double xEnd_m, double legngth_m,
			double sharingForce_N) {
		super();
		this.load_N = load_N;
		this.xStart_m = xStart_m;
		this.xEnd_m = xEnd_m;
		this.legngth_m = legngth_m;
		SharingForce_N = sharingForce_N;
	}

	public double getLoad_N() {
		return load_N;
	}

	public void setLoad_N(double load_N) {
		this.load_N = load_N;
	}

	public double getxStart_m() {
		return xStart_m;
	}

	public void setxStart_m(double xStart_m) {
		this.xStart_m = xStart_m;
	}

	public double getxEnd_m() {
		return xEnd_m;
	}

	public void setxEnd_m(double xEnd_m) {
		this.xEnd_m = xEnd_m;
	}

	public double getLegngth_m() {
		return legngth_m;
	}

	public void setLegngth_m(double legngth_m) {
		this.legngth_m = legngth_m;
	}

	public double getSharingForce_N() {
		return SharingForce_N;
	}

	public void setSharingForce_N(double sharingForce_N) {
		SharingForce_N = sharingForce_N;
	}

}
