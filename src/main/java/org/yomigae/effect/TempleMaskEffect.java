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
	public final BooleanParameter north = new BooleanParameter("north", true);
	public final BooleanParameter south = new BooleanParameter("south", true);
	public final BooleanParameter west = new BooleanParameter("west", true);
	public final BooleanParameter east = new BooleanParameter("east", true);
	public final BooleanParameter tunnel = new BooleanParameter("tunnel", true);
	public final BooleanParameter hall = new BooleanParameter("hall", true);

	private boolean[] pointMask;

  public TempleMaskEffect(LX lx) {
    super(lx);

    addParameter(north);
    addParameter(south);
    addParameter(west);
    addParameter(east);
    addParameter(tunnel);
    addParameter(hall);

		pointMask = new boolean[model.points.length];

		refreshPoints();
  }

	private void refreshPoints() {
		Set<TempleModel.FilterFlags> flags = new HashSet<>();

		if (north.isOn()) {
			flags.add(TempleModel.FilterFlags.NORTH);
		}
		if (south.isOn()) {
			flags.add(TempleModel.FilterFlags.SOUTH);
		}
		if (west.isOn()) {
			flags.add(TempleModel.FilterFlags.WEST);
		}
		if (east.isOn()) {
			flags.add(TempleModel.FilterFlags.EAST);
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
