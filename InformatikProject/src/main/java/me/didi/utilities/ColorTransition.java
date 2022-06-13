package me.didi.utilities;

import javax.annotation.Nonnull;

import org.bukkit.Color;

public class ColorTransition {
	private final int startRGB;
	private final int endRGB;

	private Color startRGBColor;
	private Color endRGBColor;

	private final int maxSteps;
	private int currentStep;

	public ColorTransition(int startRGB, int endRGB, int maxSteps) {
		this.startRGB = startRGB;
		this.endRGB = endRGB;
		this.maxSteps = maxSteps;
		this.startRGBColor = Color.fromRGB(startRGB);
		this.endRGBColor = Color.fromRGB(endRGB);
	}

	public ColorTransition(@Nonnull Color startRGBColor, @Nonnull Color endRGBColor, int maxSteps) {
		this.startRGBColor = startRGBColor;
		this.endRGBColor = endRGBColor;
		this.startRGB = this.startRGBColor.asRGB();
		this.endRGB = this.endRGBColor.asRGB();
		this.maxSteps = maxSteps;
	}

	public Color nextColor() {
		Color result = blend(startRGBColor, endRGBColor, (double) currentStep / (double) maxSteps);
		if (currentStep < maxSteps) {
			currentStep += 1;
		}
		return result;
	}

	public Color nextColor(int skipAmount) {
		skip(skipAmount);
		return nextColor();
	}

	public void skip(int amount) {
		currentStep += amount;
		if (currentStep < 0) {
			currentStep = 0;
		}
	}

	public void revert(int amount) {
		currentStep -= amount;
		if (currentStep < 0) {
			currentStep = 0;
		}
	}

	public ColorTransition clone() {
		ColorTransition clone = new ColorTransition(startRGB, endRGB, maxSteps);
		clone.currentStep = this.currentStep;
		return clone;
	}

	private static Color blend(Color color1, Color color2, double ratio) {
		float r = (float) ratio;
		float ir = (float) 1.0 - r;

		float rgb1[] = getColorComponents(color1);
		float rgb2[] = getColorComponents(color2);

		float red = rgb1[0] * r + rgb2[0] * ir;
		float green = rgb1[1] * r + rgb2[1] * ir;
		float blue = rgb1[2] * r + rgb2[2] * ir;

		if (red < 0) {
			red = 0;
		} else if (red > 255) {
			red = 255;
		}
		if (green < 0) {
			green = 0;
		} else if (green > 255) {
			green = 255;
		}
		if (blue < 0) {
			blue = 0;
		} else if (blue > 255) {
			blue = 255;
		}

		Color color = null;
		try {
			color = Color.fromRGB((int) red, (int) green, (int) blue);
		} catch (IllegalArgumentException exp) {
			exp.printStackTrace();
		}
		return color;
	}

	private static float[] getColorComponents(Color color1) {
		return new float[] { color1.getRed(), color1.getGreen(), color1.getBlue() };
	}

}
