package org.yomigae.model;

import java.util.List;
import java.util.ArrayList;

import com.google.common.collect.ImmutableList;

import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;

public class WallWasherModel extends LXModel {
	public static final String MODEL_KEY = "wall-washer";

	// width of "wall washer" fixture, measured from first to last LED center
	private static final float WIDTH_FEET = 3; // TODO: use actual width
	private static final float LENS_ANGLE_DEG = 40;

	public WallWasherModel() {
		this(new LXTransform());
	}

	public WallWasherModel(LXTransform t) {
		this(t, new LXTransform().translate(0, 0, 1), ImmutableList.of());
	}

	public WallWasherModel(LXTransform t, List<String> extraKeys) {
		this(t, new LXTransform().translate(0, 0, 1), extraKeys);
	}

	public WallWasherModel(LXTransform t, LXTransform r) {
		this(t, r, ImmutableList.of());
	}

	public WallWasherModel(LXTransform t, LXTransform r, List<String> extraKeys) {
		super(buildSubmodels(t, r));

		String[] keys = new String[extraKeys.size() + 1];
		keys[0] = MODEL_KEY;

		for (int i = 0; i < extraKeys.size(); ++i) {
			keys[i + 1] = extraKeys.get(i);
		}

		setKeys(keys);
	}

	private static LXModel[] buildSubmodels(LXTransform t, LXTransform r) {
		List<LXModel> submodels = new ArrayList<>();

		t.push();

		for (int i = 0; i < 6; ++i) {
			List<LXPoint> points = new ArrayList<>();

			for (int j = 0; j < 3; ++j) {
				t.translate(WIDTH_FEET / (18 - 1), 0, 0);
				points.add(new DirectionalPoint(
						t.x(), t.y(), t.z(),
						r.x(), r.y(), r.z(),
						Math.toRadians(LENS_ANGLE_DEG)
				));
			}

			submodels.add(new PointCluster(points));
		}

		t.pop();

		return submodels.toArray(new LXModel[0]);
	}
}
