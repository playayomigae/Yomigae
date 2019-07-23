package org.yomigae.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Arrays;

import com.google.common.collect.ImmutableList;

import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;

/**
 * Class to encapsulate temple structure and points.
 *
 * Glossary:
 *  - torii: gate structure comprised of two columns and a beam.
 *  - torii types: T1 through T6, indicate the relative height of the torii
 *  - column: vertical components of a torii
 *  - column opening-width: head-on inside length between columns of a torii
 *  - column width: head-on width of a torii's column
 *  - column height: inside height of a torii's column, not including beam thickness
 *  - column depth: side-view thickness of a column
 *  - beam: horizontal component of torii connecting columns
 *  - eave: portion of beam protruding past column
 *  - tunnel: contiguous section of T1 toriis
 *  - hall: contiguous section of T2-T6 toriis
 */
public class TempleModel extends LXModel {
	public static final String MODEL_KEY = "temple";
	public static final String TUNNEL_KEY = "tunnel-torii";
	public static final String HALL_KEY = "hall-torii";
	public static final String SIX_OCLOCK_KEY = "six-oclock";
	public static final String TWELVE_OCLOCK_KEY = "twelve-oclock";

	public static enum FilterFlags {
		TWELVE, SIX, NINE, THREE, HALL, TUNNEL
	}

	private Set<LXPoint> hallPoints = new HashSet<>();
	private Set<LXPoint> tunnelPoints = new HashSet<>();
	private Set<LXPoint> twelveOclockPoints = new HashSet<>();
	private Set<LXPoint> sixOclockPoints = new HashSet<>();
	private Set<LXPoint> nineOclockPoints = new HashSet<>();
	private Set<LXPoint> threeOclockPoints = new HashSet<>();

	private Set<LXPoint> allClusterPoints = new HashSet<>();
	private Set<LXPoint> hallClusterPoints = new HashSet<>();
	private Set<LXPoint> tunnelClusterPoints = new HashSet<>();
	private Set<LXPoint> twelveOclockClusterPoints = new HashSet<>();
	private Set<LXPoint> sixOclockClusterPoints = new HashSet<>();
	private Set<LXPoint> nineOclockClusterPoints = new HashSet<>();
	private Set<LXPoint> threeOclockClusterPoints = new HashSet<>();

	public TempleModel() {
		this(new LXTransform());
	}

	public TempleModel(LXTransform t) {
		this(t, ImmutableList.of());
	}

	public TempleModel(LXTransform t, List<String> extraKeys) {
		super(buildSubmodels(t));

		for (LXModel pointCluster : sub(PointCluster.MODEL_KEY)) {
			allClusterPoints.add(pointCluster.getPoints().get(0));
		}

		for (LXModel model : sub(TUNNEL_KEY)) {
			tunnelPoints.addAll(model.getPoints());

			for (LXModel pointCluster : model.sub(PointCluster.MODEL_KEY)) {
				tunnelClusterPoints.add(pointCluster.getPoints().get(0));
			}
		}
		for (LXModel model : sub(HALL_KEY)) {
			hallPoints.addAll(model.getPoints());

			for (LXModel pointCluster : model.sub(PointCluster.MODEL_KEY)) {
				hallClusterPoints.add(pointCluster.getPoints().get(0));
			}
		}
		for (LXModel model : sub(SIX_OCLOCK_KEY)) {
			sixOclockPoints.addAll(model.getPoints());

			for (LXModel pointCluster : model.sub(PointCluster.MODEL_KEY)) {
				sixOclockClusterPoints.add(pointCluster.getPoints().get(0));
			}
		}
		for (LXModel model : sub(TWELVE_OCLOCK_KEY)) {
			twelveOclockPoints.addAll(model.getPoints());

			for (LXModel pointCluster : model.sub(PointCluster.MODEL_KEY)) {
				twelveOclockClusterPoints.add(pointCluster.getPoints().get(0));
			}
		}
		for (LXModel model : sub(ToriiModel.THREE_OCLOCK_KEY)) {
			threeOclockPoints.addAll(model.getPoints());

			for (LXModel pointCluster : model.sub(PointCluster.MODEL_KEY)) {
				threeOclockClusterPoints.add(pointCluster.getPoints().get(0));
			}
		}
		for (LXModel model : sub(ToriiModel.NINE_OCLOCK_KEY)) {
			nineOclockPoints.addAll(model.getPoints());

			for (LXModel pointCluster : model.sub(PointCluster.MODEL_KEY)) {
				nineOclockClusterPoints.add(pointCluster.getPoints().get(0));
			}
		}

		String[] keys = new String[extraKeys.size() + 1];
		keys[0] = MODEL_KEY;

		for (int i = 0; i < extraKeys.size(); ++i) {
			keys[i + 1] = extraKeys.get(i);
		}

		setKeys(keys);
	}

	private static void buildHalfTemple(List<LXModel> submodels, int direction, LXTransform t) {
		t.translate((13 + 11 / 12.f) / 2.f, 0, 0);

		String directionTag = direction < 0 ? SIX_OCLOCK_KEY : TWELVE_OCLOCK_KEY;
		List<String> tunnelKeys = ImmutableList.of(TUNNEL_KEY, directionTag);
		List<String> hallKeys = ImmutableList.of(HALL_KEY, directionTag);

		for (int i = 0; i < 5; ++i) {
			submodels.add(createTorii(direction, ToriiModel.ToriiType.fromIndex(5 - i), t, hallKeys));
		}

		for (int i = 0; i < 5; ++i) {
			submodels.add(createTorii(direction, ToriiModel.ToriiType.T1, t, tunnelKeys));
			t.translate(i < 5 - 1 ? 1 + 11 / 12.f : 0, 0, 0);
		}
	}

	private static ToriiModel createTorii(int direction, ToriiModel.ToriiType type, LXTransform t, List<String> toriiKeys) {
		ToriiModel torii = new ToriiModel(type, direction, t, toriiKeys);
		t.translate(torii.columnDepth * direction, 0, 0);
		return torii;
	}

	private static LXModel[] buildSubmodels(LXTransform t) {
		List<LXModel> submodels = new ArrayList<>();

		t.push();
		buildHalfTemple(submodels, -1, t);
		t.pop();

		t.push();
		buildHalfTemple(submodels, 1, t);
		t.pop();


		return submodels.toArray(new LXModel[0]);
	}

	public Set<LXPoint> filterPoints(Set<FilterFlags> flags) {
		Set<LXPoint> filteredPoints = new HashSet<>(getPoints());

		if (!flags.contains(FilterFlags.TWELVE)) {
			filteredPoints.removeAll(twelveOclockPoints);
		}
		if (!flags.contains(FilterFlags.SIX)) {
			filteredPoints.removeAll(sixOclockPoints);
		}
		if (!flags.contains(FilterFlags.NINE)) {
			filteredPoints.removeAll(nineOclockPoints);
		}
		if (!flags.contains(FilterFlags.THREE)) {
			filteredPoints.removeAll(threeOclockPoints);
		}
		if (!flags.contains(FilterFlags.TUNNEL)) {
			filteredPoints.removeAll(tunnelPoints);
		}
		if (!flags.contains(FilterFlags.HALL)) {
			filteredPoints.removeAll(hallPoints);
		}

		return filteredPoints;
	}

	public Set<LXPoint> filterClusterPoints(Set<FilterFlags> flags) {
		Set<LXPoint> filteredPoints = new HashSet<>(allClusterPoints);

		if (!flags.contains(FilterFlags.TWELVE)) {
			filteredPoints.removeAll(twelveOclockClusterPoints);
		}
		if (!flags.contains(FilterFlags.SIX)) {
			filteredPoints.removeAll(sixOclockClusterPoints);
		}
		if (!flags.contains(FilterFlags.NINE)) {
			filteredPoints.removeAll(nineOclockClusterPoints);
		}
		if (!flags.contains(FilterFlags.THREE)) {
			filteredPoints.removeAll(threeOclockClusterPoints);
		}
		if (!flags.contains(FilterFlags.TUNNEL)) {
			filteredPoints.removeAll(tunnelClusterPoints);
		}
		if (!flags.contains(FilterFlags.HALL)) {
			filteredPoints.removeAll(hallClusterPoints);
		}

		return filteredPoints;
	}
}
