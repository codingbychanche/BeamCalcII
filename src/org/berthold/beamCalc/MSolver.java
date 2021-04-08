package org.berthold.beamCalc;

/**
 * Calculates the bending moments along the length of a {@link Beam}- object.
 * 
 * @author Berthold
 *
 */
public class MSolver {

	public static ShearingForceTable solve(ShearingForceTable qTable,Beam beam) {
		
		ShearingForceTable mTable=new ShearingForceTable(beam,qTable.getSectionLength_m());
		
		double m_Nm, m1_Nm;
		double q_N,q1_N;
		double deltaM_Nm=0;
		double sectionLength_m=qTable.getSectionLength_m();
		double x=0;
		
		for (int n = 0; n <= qTable.getLength() - 2; n++) {
			
			
			q_N=qTable.getShearingForceAtIndex(n).getShearingForce();
			q1_N=qTable.getShearingForceAtIndex(n+1).getShearingForce();
			
			if (Math.signum(q_N)==Math.signum(q1_N)) {
				m_Nm=q_N*x;
				m1_Nm=q1_N*(x+sectionLength_m);
				deltaM_Nm=m1_Nm-m_Nm;
			} else {
				q1_N=q1_N*-1;
				m_Nm=q_N*x;
				m1_Nm=q1_N*(x+sectionLength_m);
				deltaM_Nm=m1_Nm-m_Nm;
			}
			
			ShearingForceValue m=new ShearingForceValue(x,deltaM_Nm);
		
			m_Nm=mTable.getShearingForceAtIndex(n).getShearingForce();
			mTable.getShearingForceAtIndex(n+1).setShearingForce(m_Nm+deltaM_Nm);
			
			x=x+sectionLength_m;
		}
		return mTable;
				
	}
}
