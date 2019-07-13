package org.yomigae.effect;

import java.awt.Color;

import org.yomigae.ColorTemp;
import org.yomigae.MathUtils;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.LXEffect;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.CompoundParameter;

@LXCategory(LXCategory.COLOR)
public class ColorizeEffect extends LXEffect {
  public final CompoundParameter minTemp = new CompoundParameter("minTemp", 1900f, 1900f, 7000f)
    .setDescription("Minimum white temperature (K)");
  public final CompoundParameter maxTemp = new CompoundParameter("maxTemp", 7000f, 1900f, 7000f)
		.setDescription("Maximum white temperature (K)");
	  public final CompoundParameter minIntensity = new CompoundParameter("minIntensity", 0.0f, 0.0f, 1.0f)
    .setDescription("Minimum intensity");
  public final CompoundParameter maxIntensity = new CompoundParameter("maxIntensity", 1.0f, 0.0f, 1.0f)
    .setDescription("Maximum intensity");

  public ColorizeEffect(LX lx) {
		super(lx);

		addParameter(minTemp);
		addParameter(maxTemp);
		addParameter(minIntensity);
		addParameter(maxIntensity);
  }

  public void run(double deltaMs, double amount) {
		float minTemp = this.minTemp.getValuef();
		float maxTemp = this.maxTemp.getValuef();
		float minIntensity = this.minIntensity.getValuef();
    float maxIntensity = this.maxIntensity.getValuef();

		int color = 0;
		float k = 0;
		float intensity = 0;
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		for (int i = 0; i < colors.length; ++i) {
			color = colors[i];

			// System.out.println(i);
			// System.out.println("\tcolor :\t" + MathUtils.toHexString(color));

			k = MathUtils.map(LXColor.b(color), 0, 100, minTemp, maxTemp);
			intensity = LXColor.alpha(color) / 255.0f;

			// System.out.println("\tk :\t" + k);
			// System.out.println("\tint :\t" + intensity);

			ColorTemp.convertKToRGB(k, rgb);
			Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

			// colors[i] = LXColor.rgb(rgb[0], rgb[1], rgb[2]);
			// colors[i] = LXColor.hsb(hsb[0] *360.0f, hsb[1] * 100.0f, hsb[2] * 100.0f);

			// System.out.println("\trgb :\t" + rgb[0] + "\t" + rgb[1] + "\t" +  rgb[2]);
			// System.out.println("\thsb :\t" + hsb[0] + "\t" + hsb[1] + "\t" +  hsb[2]);

			// colors[i] = LXColor.hsb(hsb[0] *360.0f, hsb[1] * 100.0f, hsb[2] * 100.0f);
			colors[i] = LXColor.hsb(hsb[0] *360.0f, hsb[1] * 100.0f, hsb[2] * intensity * 100.0f);
		}
	}
}
