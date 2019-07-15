package org.yomigae.patterns;

import org.yomigae.*;
import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.CompoundParameter;


@LXCategory("Color")
public class MoonTemperaturePattern extends PositionedPattern {

  public final CompoundParameter minTemp = new CompoundParameter("minTemp", 1900f, 1900f, 7000f)
    .setDescription("Minimum white temperature (Kelvins)");
  public final CompoundParameter maxTemp = new CompoundParameter("maxTemp", 7000f, 1900f, 7000f)
    .setDescription("Maximum white temperature (Kelvins)");

  public MoonTemperaturePattern(LX lx) {
    super(lx);
    addParameter("minTemp", this.minTemp);
    addParameter("maxTemp", this.maxTemp);
  }

  public void run(double deltaMs) {
    // float pos = this.pos.getValuef();
    // float falloff = getFalloff();
    // float n = 0;

    // float minTemp = this.minTemp.getValuef();
    // float maxTemp = this.maxTemp.getValuef();

    // float value = 0;
    // float valueK = 0;
    // int[] rgb = new int[3];

    // for (LXPoint p : model.points) {
    //   n = getNFromPoint(p);
    //   value = Math.max(0, 100 - falloff * Math.abs(n - pos));
    //   valueK = MathUtils.map(value, 0, 100, minTemp, maxTemp);
    //   ColorTemp.convertKToRGB(valueK, rgb);

    //   colors[p.index] = LXColor.rgb(rgb[0], rgb[1], rgb[2]);
    // }
  }
}
