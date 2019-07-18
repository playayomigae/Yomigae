package org.yomigae.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;

public class PointCluster extends LXModel {
	public static final String MODEL_KEY = "point-cluster";

	public PointCluster(List<LXPoint> points) {
		this(points, ImmutableList.of());
	}

	public PointCluster(List<LXPoint> points, List<String> extraKeys) {
		super(points);

		String[] keys = new String[extraKeys.size() + 1];
		keys[0] = MODEL_KEY;

		for (int i = 0; i < extraKeys.size(); ++i) {
			keys[i + 1] = extraKeys.get(i);
		}

		setKeys(keys);
	}
}
