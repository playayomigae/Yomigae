package org.yomigae.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Arrays;

import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;

import heronarts.lx.model.LXPoint;
import heronarts.lx.model.LXModel;

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

	private final List<Torii> toriis = new ArrayList<>();
	private final List<Torii> toriisUnmodifiable = Collections.unmodifiableList(toriis);

	public static enum FilterFlags {
		NORTH, SOUTH, WEST, EAST, HALL, TUNNEL
	}

	Set<LXPoint> hallPoints = new HashSet<>();
	Set<LXPoint> tunnelPoints = new HashSet<>();
	Set<LXPoint> northPoints = new HashSet<>();
	Set<LXPoint> southPoints = new HashSet<>();
	Set<LXPoint> westPoints = new HashSet<>();
	Set<LXPoint> eastPoints = new HashSet<>();

	public TempleModel() {
		this(new ArrayList<>());
	}

	public TempleModel(List<Torii> toriis) {
		super(toriis.toArray(new LXModel[0]));

		this.toriis.addAll(toriis);

		int midpointIndex = toriis.size() / 2;
		for (int i = 0; i < toriis.size(); ++i) {
			Torii torii = toriis.get(i);

			if (torii.getType() == ToriiType.T1) {
				tunnelPoints.addAll(torii.getPoints());
			}
			else {
				hallPoints.addAll(torii.getPoints());
			}

			if (i < midpointIndex) {
				westPoints.addAll(torii.getPoints());
			}
			else {
				eastPoints.addAll(torii.getPoints());
			}

			northPoints.addAll(torii.getNorthPoints());
			southPoints.addAll(torii.getSouthPoints());
		}
	}

	public Set<LXPoint> filterPoints(Set<FilterFlags> flags) {
		Set<LXPoint> filteredPoints = new HashSet<>(getPoints());

		if (!flags.contains(FilterFlags.NORTH)) {
			filteredPoints.removeAll(northPoints);
		}
		if (!flags.contains(FilterFlags.SOUTH)) {
			filteredPoints.removeAll(southPoints);
		}
		if (!flags.contains(FilterFlags.WEST)) {
			filteredPoints.removeAll(westPoints);
		}
		if (!flags.contains(FilterFlags.EAST)) {
			filteredPoints.removeAll(eastPoints);
		}
		if (!flags.contains(FilterFlags.TUNNEL)) {
			filteredPoints.removeAll(tunnelPoints);
		}
		if (!flags.contains(FilterFlags.HALL)) {
			filteredPoints.removeAll(hallPoints);
		}

		return filteredPoints;
	}

	public int[][] getPointGroups() {
		return new int[0][];
	}

	public List<Torii> getToriis() {
		return toriisUnmodifiable;
	}

	public static enum ToriiType {
		T1, T2, T3, T4, T5, T6;

		private static ToriiType[] values = values();

		public static ToriiType fromIndex(int i) {
			return values[i];
		}
	}

	public static class ToriiConfig {
	}

	public static class Torii extends LXModel {
		private TempleModel.ToriiType type;
		private float centerOffset;
		private float openingWidth;
		private float columnWidth, columnHeight, columnDepth;
		private float beamHeight, beamLength;
		private float eaveDepth; // calculated

		private List<DirectionalPoint> frontPoints;
		private List<DirectionalPoint> rearPoints;

		public Torii(ToriiType type, float centerOffset, float openingWidth,
				float columnWidth, float columnHeight, float columnDepth,
				float beamHeight, float beamLength,
				List<DirectionalPoint> frontPoints, List<DirectionalPoint> rearPoints) {

			super(Lists.newArrayList(Iterables.concat(frontPoints, rearPoints)));

			this.type = type;
			this.centerOffset = centerOffset;
			this.openingWidth = openingWidth;
			this.columnWidth = columnWidth;
			this.columnHeight = columnHeight;
			this.columnDepth = columnDepth;
			this.beamHeight = beamHeight;
			this.beamLength = beamLength;

			eaveDepth = (beamLength - 2 * columnWidth - openingWidth) / 2;

			this.frontPoints = frontPoints;
			this.rearPoints = rearPoints;
		}

		public TempleModel.ToriiType getType() {
			return type;
		}

		public List<DirectionalPoint> getNorthPoints() {
			return rearPoints;
		}

		public List<DirectionalPoint> getSouthPoints() {
			return frontPoints;
		}
	}
}
