package org.yomigae.effect;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import heronarts.lx.LX;
import heronarts.lx.model.LXPoint;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.LXParameter;
import heronarts.lx.parameter.BooleanParameter;

import org.yomigae.model.TempleModel;

public class TempleMaskEffect extends ModelEffect<TempleModel> {
	public final BooleanParameter twelve = new BooleanParameter("twelve", true);
	public final BooleanParameter six = new BooleanParameter("six", true);
	public final BooleanParameter nine = new BooleanParameter("nine", true);
	public final BooleanParameter three = new BooleanParameter("three", true);
	public final BooleanParameter tunnel = new BooleanParameter("tunnel", true);
	public final BooleanParameter hall = new BooleanParameter("hall", true);

	private boolean[] pointMask;

  public TempleMaskEffect(LX lx) {
    super(lx);

    addParameter(twelve);
    addParameter(six);
    addParameter(nine);
    addParameter(three);
    addParameter(tunnel);
    addParameter(hall);

		pointMask = new boolean[model.points.length];

		refreshPoints();
  }

	private void refreshPoints() {
		Set<TempleModel.FilterFlags> flags = new HashSet<>();

		if (twelve.isOn()) {
			flags.add(TempleModel.FilterFlags.TWELVE);
		}
		if (six.isOn()) {
			flags.add(TempleModel.FilterFlags.SIX);
		}
		if (nine.isOn()) {
			flags.add(TempleModel.FilterFlags.NINE);
		}
		if (three.isOn()) {
			flags.add(TempleModel.FilterFlags.THREE);
		}
		if (tunnel.isOn()) {
			flags.add(TempleModel.FilterFlags.TUNNEL);
		}
		if (hall.isOn()) {
			flags.add(TempleModel.FilterFlags.HALL);
		}

		Set<LXPoint> activePoints = model.filterPoints(flags);

		Arrays.fill(pointMask, false);
		for (LXPoint p : activePoints) {
			pointMask[p.index] = true;
		}
	}

	@Override
	public void onParameterChanged(LXParameter p) {
		refreshPoints();
	}

	@Override
  public void run(double deltaMs, double amount) {
		for (int i = 0; i < pointMask.length; ++i) {
			if (!pointMask[i]) {
				colors[i] = LXColor.ALPHA_MASK;
			}
		}
	}
}
