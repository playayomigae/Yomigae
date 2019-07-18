package org.yomigae.model;

import java.util.List;
import java.util.ArrayList;

import com.google.common.collect.ImmutableList;

import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;

public class SpotlightModel extends LXModel {
	public static final String MODEL_KEY = "spotlight";

	private static final float LENS_ANGLE_DEG = 40;

	public SpotlightModel() {
		this(new LXTransform());
	}

	public SpotlightModel(LXTransform t) {
		this(t, new LXTransform().translate(0, 0, 1), ImmutableList.of());
	}

	public SpotlightModel(LXTransform t, List<String> extraKeys) {
		this(t, new LXTransform().translate(0, 0, 1), extraKeys);
	}

	public SpotlightModel(LXTransform t, LXTransform r) {
		this(t, r, ImmutableList.of());
	}

	public SpotlightModel(LXTransform t, LXTransform r, List<String> extraKeys) {
		super(buildPoints(t, r));

		String[] keys = new String[extraKeys.size() + 1];
		keys[0] = MODEL_KEY;

		for (int i = 0; i < extraKeys.size(); ++i) {
			keys[i + 1] = extraKeys.get(i);
		}

		setKeys(keys);
	}

	private static List<LXPoint> buildPoints(LXTransform t, LXTransform r) {
		List<LXPoint> points = new ArrayList<>();

		t.push();

		points.add(new DirectionalPoint(
				t.x(), t.y(), t.z(),
				r.x(), r.y(), r.z(),
				Math.toRadians(LENS_ANGLE_DEG)
		));

		t.pop();

		return points;
	}
}
