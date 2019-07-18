package org.yomigae.patterns;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BooleanParameter;

@LXCategory("Form")
public class CometPattern extends PositionedPattern {

  public final BooleanParameter flip = new BooleanParameter("Flip", false)
    .setDescription("Flip the direction of the comet tail");

  public CometPattern(LX lx) {
    super(lx);
    addParameter("flip", this.flip);
  }

  public void run(double deltaMs) {
  //   float pos = this.pos.getValuef();
  //   float falloff = getFalloff();
  //   float n = 0;
  //   boolean flip = this.flip.getValueb();

  //   for (LXPoint p : model.points) {
  //     n = getNFromPoint(p);
  //     if (flip ? n > pos : n < pos) {
  //       colors[p.index] = LXColor.gray(0);
  //     } else {
  //       colors[p.index] = LXColor.gray(Math.max(0, 100 - falloff * Math.abs(n - pos)));
  //     }
  //   }
  }
}
