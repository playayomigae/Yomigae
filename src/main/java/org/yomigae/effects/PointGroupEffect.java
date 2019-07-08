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

public class PointGroupEffect extends ModelEffect<TempleModel> {
  public PointGroupEffect(LX lx) {
    super(lx);
  }

	@Override
  public void run(double deltaMs, double amount) {
		int[][] pixelGroups = model.getPointGroups();

		for (int[] indices : pixelGroups) {
			int r = 0, g = 0, b = 0;
			for (int index : indices) {
				int c = colors[index];
				r += LXColor.red(c);
				g += LXColor.green(c);
				b += LXColor.blue(c);
			}

			r /= indices.length;
			g /= indices.length;
			b /= indices.length;

			int avgColor = LXColor.rgb(r, g, b);

			for (int index : indices) {
				colors[index] = avgColor;
			}
		}
	}
}
