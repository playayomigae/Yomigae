package org.yomigae.effect;

import java.util.List;

import heronarts.lx.LX;
import heronarts.lx.LXEffect;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.LXParameter;
import heronarts.lx.parameter.BooleanParameter;

import org.yomigae.model.PointCluster;

public class PointGroupEffect extends LXEffect {
  public PointGroupEffect(LX lx) {
    super(lx);
  }

	@Override
  public void run(double deltaMs, double amount) {
		List<LXModel> pointClusters = model.sub(PointCluster.MODEL_KEY);

		for (LXModel pointCluster : pointClusters) {
			List<LXPoint> points = pointCluster.getPoints();

			int r = 0, g = 0, b = 0, a = 0;
			for (LXPoint point : points) {
				int index = point.index;
				int c = colors[index];
				r += LXColor.red(c) & 0xff;
				g += LXColor.green(c) & 0xff;
				b += LXColor.blue(c) & 0xff;
				a += LXColor.alpha(c) & 0xff;
			}

			r /= points.size();
			g /= points.size();
			b /= points.size();
			a /= points.size();

			int avgColor = LXColor.rgba(r, g, b, a);

			for (LXPoint point : points) {
				colors[point.index] = avgColor;
			}
		}
	}
}
