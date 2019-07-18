package org.yomigae.patterns;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BooleanParameter;;

@LXCategory("Form")
public class MoonPattern extends PositionedPattern {

  public final BooleanParameter debugAlpha = new BooleanParameter("debugAlpha", false)
    .setDescription("Shows the alpha as green instead");

  public MoonPattern(LX lx) {
    super(lx);
    addParameter(debugAlpha);
  }

  private int setAlpha(int color, int a) {
    int mask = this.debugAlpha.getValueb() ? ~LXColor.G_MASK : ~LXColor.ALPHA_MASK;
    int shift = this.debugAlpha.getValueb() ? LXColor.G_SHIFT : LXColor.ALPHA_SHIFT;
    return
      ((a & 0xff) << shift) |
      (color & mask);
  }

  public void run(double deltaMs) {
    float pos = this.pos.getValuef();
    float colorFalloff = getFalloffForParameter(this.colorWidth);
    float alphaFalloff = getFalloffForParameter(this.alphaWidth);
    float n = 0;
    int color = 0;
    int alpha = 0;
    for (LXPoint p : model.points) {
      n = getNFromPoint(p);
      color = LXColor.gray(Math.max(0, 100 - colorFalloff * Math.abs(n - pos)));
      alpha = Math.round(Math.max(0, 255 - alphaFalloff * Math.abs(n - pos)));
      color = setAlpha(color, alpha);
      colors[p.index] = color;
    }
  }
}
