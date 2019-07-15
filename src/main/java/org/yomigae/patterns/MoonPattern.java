package org.yomigae.patterns;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;

@LXCategory("Form")
public class MoonPattern extends PositionedPattern {

  public MoonPattern(LX lx) {
    super(lx);
  }

  public void run(double deltaMs) {
    float pos = this.pos.getValuef();
    float falloff = getFalloff();
    float n = 0;
    for (LXPoint p : model.points) {
      n = getNFromPoint(p);
      colors[p.index] = LXColor.gray(Math.max(0, 100 - falloff * Math.abs(n - pos)));
    }
  }
}
