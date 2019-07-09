package org.yomigae.patterns;

import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.EnumParameter;

public abstract class PositionedPattern extends LXPattern {

  public enum Axis {
    X, Y
  };

  public final EnumParameter<Axis> axis = new EnumParameter<Axis>("Axis", Axis.X)
    .setDescription("Which axis the pattern is drawn across");

  // runs from -1 to 2 to allow the pattern to run fully off the model, even at full width
  public final CompoundParameter pos = new CompoundParameter("Pos", 0.5, -1, 2)
    .setDescription("Position of the center of the pattern");

  public final CompoundParameter width = new CompoundParameter("Width", .4, 0, 1)
    .setDescription("Thickness of the pattern");

  public PositionedPattern(LX lx) {
    super(lx);
    addParameter("axis", this.axis);
    addParameter("pos", this.pos);
    addParameter("width", this.width);
  }

  protected float getFalloff() {
    return 100 / this.width.getValuef();
  }

  protected float getNFromPoint(LXPoint p) {
    switch (this.axis.getEnum()) {
      case X: return p.xn;
      case Y: return p.yn;
      // case Z: return p.zn;
      default: return 0;
    }
  }
}
