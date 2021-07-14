package org.berthold.beamCalc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
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
	private String rightSupportDistanceFromLeftEnd;
	private Beam beam;
	private StressResultantTable stressResultantsTable;
	private int height_px, width_px, padX_px, padY_px;
	private String numberFormat;
	private int y0_px;
	double yMax, yMin, xMax;

	double leftSupportX, rightSupportX;
	String leftSupportName, rightSupportName;

	// Constants for the GFX- window
	private final int Y_OFFSET_ANNOTATION = 15;
	private final int PADDING_TOP_PX = 20; // A constant enabeling text at the
											// top of the diagram to be seen....
	private final int PADDING_BOTTOM_PX = 20;

	/**
	 * Creates a new image.
	 * 
	 * @param name             File name.
	 * @param beam             The asociated {@link Beam}- object.
	 * @param stressResultants The stress resultants to be drawn (
	 *                         {@link StressResultantTable}).
	 * @param height_px        Height of the image in pixels.
	 * @param width_px         The width of the image in pixels.
	 * @param padX_px          Padding in pixels at the left and right side of the
	 *                         image.
	 * @param padY_px          Padding in pixels at the top and bottom of the image.
	 * @param numberFormat     Number format... (e.g. %.2f = two decimal places).
	 */
	public StressResultantDraw(String name, Beam beam, StressResultantTable stressResultants, int height_px,
			int width_px, int padX_px, int padY_px, String numberFormat) {
		super();

		this.name = name;
		this.beam = beam;
		this.stressResultantsTable = stressResultants;
		this.padX_px = padX_px;
		this.padY_px = padY_px;
		this.numberFormat = numberFormat;

		this.height_px = height_px;
		this.width_px = width_px;

		// Beam
		leftSupportX = beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc().get(0).getDistanceFromLeftEndOfBeam_m();
		leftSupportName = beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc().get(0).getNameOfSupport();

		rightSupportX = beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc().get(1).getDistanceFromLeftEndOfBeam_m();
		rightSupportName = beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc().get(1).getNameOfSupport();
		rightSupportDistanceFromLeftEnd = beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc().get(1) + "m";

		// Constants for the gfx- window
		y0_px = (height_px / 2) + PADDING_TOP_PX;
		yMax = stressResultantsTable.getAbsMax();
		yMin = stressResultantsTable.getAbsMin();
		xMax = stressResultantsTable.getAbsMaxX();
	}

	public void draw() {
		try {
			// Create an in memory Image
			// This is a image used only to determine the width of the image +
			// the width of the
			// right supports name and the dimensions.... Determines right
			// padding...
			int paddingRight_px = 0;

			BufferedImage himg = new BufferedImage(width_px, height_px, BufferedImage.TYPE_INT_ARGB);
			Graphics2D hg = himg.createGraphics();

			paddingRight_px = getWidthOfStringIn_px(rightSupportName + " " + rightSupportX + "m", hg);

			// This is the image used by the renderer
			BufferedImage img = new BufferedImage(width_px + paddingRight_px + padX_px,
					height_px + PADDING_TOP_PX + PADDING_BOTTOM_PX, BufferedImage.TYPE_INT_ARGB);

			// Grab the graphics object off the image
			Graphics2D graphics = img.createGraphics();
			graphics = assignRenderingHints(graphics);

			// Draw background
			Color c1 = new Color(255, 255, 255); // Gradient start color
			Color c2 = new Color(150, 150, 150); // Gradient end color

			GradientPaint gradient = new GradientPaint((float) 1, 0, c1, (float) (0),
					height_px + PADDING_TOP_PX + PADDING_BOTTOM_PX, c2);
			graphics.setPaint(gradient);
			graphics.fillRect(0, 0, width_px + paddingRight_px, height_px + PADDING_TOP_PX + PADDING_BOTTOM_PX);

			Stroke stroke = new BasicStroke(1.5f);
			graphics.setStroke(stroke);
			graphics.setColor(Color.BLACK);

			// Datum
			graphics.drawLine(padX_px, y0_px + PADDING_TOP_PX, width_px - padX_px, y0_px + PADDING_TOP_PX);

			// Supports
			String dimFormated;
			int xTLeft = (int) getXT(leftSupportX);
			graphics.drawLine(xTLeft, (int) padY_px, xTLeft, (int) height_px - padY_px + PADDING_TOP_PX);
			dimFormated = String.format(numberFormat, leftSupportX);
			graphics.drawString(leftSupportName + " " + dimFormated + " m", xTLeft,
					(int) getYT(0) + Y_OFFSET_ANNOTATION);

			int xTRight = (int) getXT(rightSupportX);
			graphics.drawLine(xTRight, (int) padY_px, xTRight, (int) height_px - padY_px + PADDING_TOP_PX);
			dimFormated = String.format(numberFormat, rightSupportX);
			graphics.drawString(rightSupportName + " " + dimFormated + " m", xTRight,
					(int) getYT(0) + Y_OFFSET_ANNOTATION);

			// Draw stress resultants.
			for (StressResultant r : stressResultantsTable.sfValues) {

				// Transform
				double y = r.getShearingForce();
				double x = r.getX_m();

				// The graph
				graphics.setColor(Color.RED);
				graphics.drawLine((int) getXT(x), (int) getYT(y), (int) getXT(x), (int) getYT(y));

				String shFormated;
				if (r.isDiscontiunuity()) {
					graphics.setColor(Color.GRAY);
					graphics.drawLine((int) getXT(x), padY_px, (int) getXT(x), height_px - padY_px + PADDING_TOP_PX);

					Color c = new Color(100, 0, 0);
					graphics.setColor(c);

					shFormated = String.format(numberFormat, r.getShearingForce());
					graphics.drawString(shFormated + " " + r.getUnit(), (int) getXT(x), (int) getYT(y));

				}

				if (r.isMaxima()) {
					graphics.setColor(Color.BLUE);
					graphics.drawLine((int) getXT(x), padY_px, (int) getXT(x), height_px - padY_px + PADDING_TOP_PX);

					shFormated = String.format(numberFormat, r.getShearingForce());
					graphics.drawString(shFormated + " " + r.getUnit(), (int) getXT(x), (int) getYT(y));
				}
			}

			// Save to file.
			File outputfile = new File(this.name + ".png");
			ImageIO.write(img, "png", outputfile);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("IO- Error " + e.toString());
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
			return (y0_px - ((y0_px - padY_px) / yMax) * y) + PADDING_TOP_PX;
		if (y < 0)
			return (y0_px + ((height_px - padY_px - y0_px) / yMin) * y) + PADDING_TOP_PX;

		return (double) y0_px + PADDING_TOP_PX;
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

	/**
	 * Display width of a string in pixels.
	 * 
	 * @param string The string containing the text of which the width in pixels to
	 *               be determined.
	 * @param g      Associated {@link Graphics2D} object.
	 * @return Width of string in pixels.
	 */
	private int getWidthOfStringIn_px(String string, Graphics g) {
		FontMetrics f = g.getFontMetrics();
		return f.stringWidth(string);
	}

	/**
	 * Adds rendering hints for the graphics display.
	 * 
	 * @param graphics The {@link Graphics2D}- object to which the rendering
	 *                 settings are to be added.
	 * @return The {@link Graphics2D}- object containing the new rendering settings.
	 */
	private Graphics2D assignRenderingHints(Graphics2D graphics) {
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		graphics.setRenderingHints(rh);

		return graphics;
	}
}
