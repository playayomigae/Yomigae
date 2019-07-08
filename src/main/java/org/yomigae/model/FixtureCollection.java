package org.yomigae.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.google.common.collect.ForwardingList;
import heronarts.lx.model.LXPoint;
import heronarts.lx.model.LXFixture;


public class FixtureCollection<F extends LXFixture> extends ForwardingList<F> implements LXFixture {
	private List<F> fixtures = new ArrayList<>();
	private final List<F> fixturesUnmodifiable = Collections.unmodifiableList(fixtures);

	private List<LXPoint> points = null;

	public FixtureCollection(List<F> fixtures) {
		fixtures.addAll(fixtures);
	}

	private void collectPoints() {
		points = new ArrayList<>();

		for (F fixture : fixtures) {
			points.addAll(fixture.getPoints());
		}
	}

	public List<LXPoint> getPoints() {
		if (points == null) {
			collectPoints();
		}

		return points;
	}

	protected List<F>	delegate() {
		return fixtures;
	}
}
