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

  private int setAlpha(int color, int a) {
    int RGB_MASK = ~LXColor.ALPHA_MASK;
    return
      ((a & 0xff) << LXColor.ALPHA_SHIFT) |
      (color & RGB_MASK);
  }

  public void run(double deltaMs) {
    float pos = this.pos.getValuef();
    float colorFalloff = getFalloff(this.colorWidth);
    float alphaFalloff = getFalloff(this.alphaWidth);
    float n = 0;
    int color = 0;
    int alpha = 0;
    for (LXPoint p : model.points) {
      n = getNFromPoint(p);
      color = LXColor.gray(Math.max(0, 100 - colorFalloff * Math.abs(n - pos)));
      alpha = Math.round(Math.max(0, 100 - alphaFalloff * Math.abs(n - pos)));
      color = setAlpha(color, alpha);
      colors[p.index] = color;
    }
  }
}
