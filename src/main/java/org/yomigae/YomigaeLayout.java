package org.yomigae;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Arrays;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.output.LXDatagram;
import heronarts.lx.color.LXColor;

import org.yomigae.model.TempleModel;
import org.yomigae.model.ModelCollection;
import org.yomigae.output.StreamingACNDatagram;
import org.yomigae.output.DmxFragment;
import org.yomigae.output.SpotlightDmxFragment;
import org.yomigae.output.WallWasherDmxFragment;
import org.yomigae.output.DmxAggregate;

public class YomigaeLayout {

	private static final String SACN_ADDRESS_BASE = "239.255.";

	private static final int SPOTLIGHT_DMX_START_CHANNEL = 1 - 1;
	private static final int WALL_WASHER_DMX_START_CHANNEL = 101 - 1;

	private final TempleModel model;

	public YomigaeLayout() {
		model = new TempleModel();

		// build DMX outputs
		List<Set<LXPoint>> universePoints = new ArrayList<>();
		List<int[]> universeIndices = new ArrayList<>();

		String[] universeSpotlightQueries = new String[] {
			"torii.twelve-oclock spotlight.three-oclock",
			"torii.six-oclock spotlight.three-oclock",
			"torii.six-oclock spotlight.nine-oclock",
			"torii.twelve-oclock spotlight.nine-oclock"
		};
		String[] universeWallWasherQueries = new String[] {
			"torii.twelve-oclock wall-washer.three-oclock",
			"torii.six-oclock wall-washer.three-oclock",
			"torii.six-oclock wall-washer.nine-oclock",
			"torii.twelve-oclock wall-washer.nine-oclock"
		};

		for (int universeIndex = 0; universeIndex < universeSpotlightQueries.length; ++universeIndex) {
			int universeNumber = universeIndex + 1;
			List<LXModel> spotlights = ModelCollection.filterChildren(model, universeSpotlightQueries[universeIndex]);
			List<LXModel> wallWashers = ModelCollection.filterChildren(model, universeWallWasherQueries[universeIndex]);

			List<DmxFragment> spotlightFragments = new ArrayList<>();
			List<DmxFragment> wallWasherFragments = new ArrayList<>();

			System.out.println("\nConstructing output for universe " + universeNumber);

			int lastDmxOffset = 0;
			if (SPOTLIGHT_DMX_START_CHANNEL >= 0) {
				lastDmxOffset = SPOTLIGHT_DMX_START_CHANNEL;
			}

			System.out.println("Spotlight start address: " + (lastDmxOffset + 1));
			for (LXModel m : Lists.reverse(spotlights)) {
				List<LXModel> pointClusters = ModelCollection.filterChildren(m, "point-cluster");
				int[] indexBuffer = new int[pointClusters.size()];
				for (int i = 0; i < pointClusters.size(); ++i) {
					indexBuffer[i] = pointClusters.get(i).getPoints().get(0).index;
				}

				DmxFragment frag = new SpotlightDmxFragment(lastDmxOffset, indexBuffer);
				lastDmxOffset += frag.getNumChannels();
				spotlightFragments.add(frag);
			}

			System.out.println("Spotlight end address: " + lastDmxOffset);

			if (WALL_WASHER_DMX_START_CHANNEL >= 0) {
				lastDmxOffset = WALL_WASHER_DMX_START_CHANNEL;
			}

			System.out.println("Wall-washer start address: " + (lastDmxOffset + 1));
			for (LXModel m : Lists.reverse(wallWashers)) {
				List<LXModel> pointClusters = ModelCollection.filterChildren(m, "point-cluster");
				int[] indexBuffer = new int[pointClusters.size()];
				for (int i = 0; i < pointClusters.size(); ++i) {
					indexBuffer[i] = pointClusters.get(i).getPoints().get(0).index;
				}

				DmxFragment frag = new WallWasherDmxFragment(lastDmxOffset, indexBuffer);
				lastDmxOffset += frag.getNumChannels();
				wallWasherFragments.add(frag);
			}

			System.out.println("Wall-washer end address: " + lastDmxOffset);

			System.out.println("Spotlights: " + spotlightFragments.size());
			System.out.println("Wall-washers: " + wallWasherFragments.size());

			List<DmxFragment> allFragments = new ArrayList<>(spotlightFragments);
			allFragments.addAll(wallWasherFragments);
			LXDatagram d = new StreamingACNDatagram(universeNumber, DmxAggregate.fromFragments(allFragments));

			int universeHighByte = (0xff00 & universeNumber) >> 8;
			int universeLowByte = 0xff & universeNumber;

			try {
				String address = SACN_ADDRESS_BASE + universeHighByte + "." + universeLowByte;
				System.out.println("Adding DMX datagram for address " + address);
				d.setAddress(address);
			}
			catch (Exception e) {
				System.err.println("Error when setting DMX IP address: " + e.getMessage());
				e.printStackTrace();
			}

			model.addDatagram(d);
		}
	}

	public TempleModel getModel() {
		return model;
	}
}
