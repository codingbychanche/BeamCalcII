package org.berthold.beamCalc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Draws the graphical representation from a {@link StressResultantTable}.
 *  
 * @author Berthold
 *
 */

public class StressResultantDraw {

	private String name;
	private Beam beam;
	private StressResultantTable stressResultantsTable;
	private int height_px, width_px, padX_px, padY_px;
	private String numberFormat;
	private int y0_px;
	double yMax, yMin, xMax;

	double leftSupportX, rightSupportX;
	String leftSupportName,rightSupportName;
	
	// Constants for the GFX- window
	private final int Y_OFFSET_ANOTATION=15;
	
	/**
	 * Creates a new image.
	 * 
	 * @param name File name.
	 * @param beam The asociated {@link Beam}- object.
	 * @param stressResultants	The stress resultants to be drawn ({@link StressResultantTable}).
	 * @param height_px	Height of the image in pixels.
	 * @param width_px	The width of the image in pixels.
	 * @param padX_px	Padding in pixels at the left and right side of the image.
	 * @param padY_px	Padding in pixels at the top and bottom of the image.
	 * @param numberFormat	Number format... (e.g. %.2f = two decimal places).
	 */
	public StressResultantDraw(String name,Beam beam,StressResultantTable stressResultants, int height_px, int width_px, int padX_px,
			int padY_px,String numberFormat) {
		super();
		
		this.name=name;
		this.beam=beam;
		this.stressResultantsTable=stressResultants;
		this.height_px = height_px;
		this.width_px = width_px;
		this.padX_px = padX_px;
		this.padY_px = padY_px;
		this.numberFormat=numberFormat;

		// Beam
		leftSupportX=beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc().get(0).getDistanceFromLeftEndOfBeam_m();
		leftSupportName=beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc().get(0).getNameOfSupport();
		
		rightSupportX=beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc().get(1).getDistanceFromLeftEndOfBeam_m();
		rightSupportName=beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc().get(1).getNameOfSupport();
		
		// Constants efining the gfx- window
		y0_px = height_px / 2;
		yMax = stressResultantsTable.getAbsMax();
		yMin =  stressResultantsTable.getAbsMin();
		xMax =  stressResultantsTable.getAbsMaxX();
	}

	public void draw() {
		try {
			// Create an in memory Image
			BufferedImage img = new BufferedImage(width_px,height_px, BufferedImage.TYPE_INT_ARGB);

			// Grab the graphics object off the image
			Graphics2D graphics = img.createGraphics();
			
			graphics.setBackground(Color.WHITE);

			Stroke stroke = new BasicStroke(.1f);
			graphics.setStroke(stroke);
			graphics.setColor(Color.BLACK);
			
			// Datum
			graphics.drawLine(padX_px,y0_px,width_px-padX_px,y0_px);
			
			// Supports
			String dimFormated;
			int xTLeft=(int)getXT(leftSupportX);
			graphics.drawLine(xTLeft,(int)padY_px,xTLeft,(int)height_px-padY_px);
			dimFormated=String.format(numberFormat,leftSupportX);
			graphics.drawString(leftSupportName+" "+dimFormated+" m",xTLeft,(int)getYT(0)+Y_OFFSET_ANOTATION);
			
			int xTRight=(int)getXT(rightSupportX);
			graphics.drawLine(xTRight,(int)padY_px,xTRight,(int)height_px-padY_px);
			dimFormated=String.format(numberFormat,rightSupportX);
			graphics.drawString(rightSupportName+" "+dimFormated+" m",xTRight,(int)getYT(0)+Y_OFFSET_ANOTATION);
			
			// Draw stress resultants.
			for (StressResultant r: stressResultantsTable.sfValues) {
				// Stress resultant
				graphics.setColor(Color.RED);
				
				// Transform
				double y=r.getShearingForce();
				double x=r.getX_m();
				
				graphics.drawLine((int)getXT(x),(int) getYT(y),(int) getXT(x),(int) getYT(y));
				
				String shFormated;
				if (r.isDiscontiunuity()) {
					graphics.setColor(Color.GRAY);
					graphics.drawLine((int)getXT(x),padY_px,(int)getXT(x),height_px-padY_px);
					
					graphics.setColor(Color.RED);
					
					shFormated=String.format(numberFormat,r.getShearingForce());					
					graphics.drawString(shFormated+" "+r.getUnit(),(int)getXT(x),(int)getYT(y));
				}
				
				if(r.isMaxima()) {
					graphics.setColor(Color.BLUE);
					graphics.drawLine((int)getXT(x),padY_px,(int)getXT(x),height_px-padY_px);
				}
			}
			
			// Save to file.
			File outputfile = new File(name+".png");
			ImageIO.write(img, "png", outputfile);
		} catch (Exception e) {
		}
	}

	/**
	 * Transforms a y- koordinate.
	 * 
	 * @param y y- koordinate
	 * @return Transformed y- koordinate.
	 */
	private double getYT(double y) {

		if (y > 0)
			return y0_px - ((y0_px - padY_px) / yMax) * y;
		if (y < 0)
			return y0_px + ((height_px - padY_px - y0_px) / yMin) * y;

		return (double) y0_px;
	}

	/**
	 * Transforms a x- koordinate.
	 * 
	 * @param x
	 * @return Transformed x- koordinate.
	 */
	private double getXT(double x) {
		return x * ((width_px - 2 * padX_px) / xMax) + padX_px;
	}
}
