package org.yomigae;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Arrays;

import com.google.common.collect.ImmutableSet;

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
//import org.yomigae.model.TempleBuilder;

public class YomigaeLayout {

	private static final String SACN_ADDRESS_BASE = "239.255.";

	private static final String FIXTURE_TYPE_SPOTLIGHT = "spotlight";
	private static final String FIXTURE_TYPE_WALL_WASHER = "wall-washer";

	private static final double[] TORII_OPENING_WIDTH_FEET = new double[] {
			13 + 0.5 / 12., 17 + 4.5 / 12., 21 + 8.5 / 12., 26 + 0.5 / 12., 30 + 4.5 / 12., 34 + 8.5 / 12.
	};
	private static final double[] TORII_COLUMN_WIDTH_FEET = new double[] {
			3 + 1.5 / 12., 3 + 1.5 / 12., 3 + 1.5 / 12., 3 + 1.5 / 12., 3 + 1.5 / 12., 3 + 1.5 / 12.
	};
	private static final double[] TORII_COLUMN_HEIGHT_FEET = new double[] {
			14 + 11.24 / 12., 19 + 11.24 / 12., 24 + 11.24 / 12., 29 + 11.24 / 12., 34 + 11.24 / 12., 39 + 11.24 / 12.
	};
	private static final double[] TORII_COLUMN_DEPTH_FEET = new double[] {
			8 + 1.5 / 12., 8 + 1.5 / 12., 8 + 1.5 / 12., 8 + 1.5 / 12., 8 + 1.5 / 12., 8 + 1.5 / 12.
	};
	private static final double[] TORII_BEAM_HEIGHT_FEET = new double[] {
			3 + 1.5 / 12., 3 + 1.5 / 12., 3 + 1.5 / 12., 3 + 1.5 / 12., 3 + 1.5 / 12., 3 + 1.5 / 12.
	};
	private static final double[] TORII_BEAM_LENGTH_FEET = new double[] {
			26 + 11.5 / 12., 34 + 2.75 / 12., 41 + 8.75 / 12., 48 + 1.5 / 12., 55 + 11.25 / 12., 60 + 2.5 / 12.
	};

	// width of "wall washer" fixture, measured from first to last LED center
	private static final double WALL_WASHER_WIDTH_FEET = 3; // TODO: use actual width

	private static final int SPOTLIGHT_DMX_START_CHANNEL = 0;
	private static final int WALL_WASHER_DMX_START_CHANNEL = -1;

	private static final String METADATA_KEY_DMX_UNIVERSE = "dmx-universe";
	private static final String METADATA_KEY_DMX_CHANNEL_OFFSET = "dmx-channel-offset";

	//private final TempleBuilder builder = new TempleBuilder();
	private final TempleModel model;


	public YomigaeLayout() {
		model = new TempleModel();

		// build DMX outputs
		List<Set<LXPoint>> universePoints = new ArrayList<>();
		List<int[]> universeIndices = new ArrayList<>();

		String[] universeSpotlightQueries = new String[] {
			"torii.twelve-oclock spotlight.three-oclock",
			"torii.six-oclock spotlight.three-oclock",
			"torii.twelve-oclock spotlight.nine-oclock",
			"torii.six-oclock spotlight.nine-oclock"
		};
		String[] universeWallWasherQueries = new String[] {
			"torii.twelve-oclock wall-washer.three-oclock",
			"torii.six-oclock wall-washer.three-oclock",
			"torii.twelve-oclock wall-washer.nine-oclock",
			"torii.six-oclock wall-washer.nine-oclock"
		};

		for (int universeIndex = 0; universeIndex < universeSpotlightQueries.length; ++universeIndex) {
			int universeNumber = universeIndex + 1;
			List<LXModel> spotlights = ModelCollection.filterChildren(model, universeSpotlightQueries[universeIndex]);
			List<LXModel> wallWashers = ModelCollection.filterChildren(model, universeWallWasherQueries[universeIndex]);

			List<DmxFragment> spotlightFragments = new ArrayList<>();
			List<DmxFragment> wallWasherFragments = new ArrayList<>();

			System.out.println("Constructing output for universe " + universeNumber);

			int lastDmxOffset = 0;
			if (SPOTLIGHT_DMX_START_CHANNEL >= 0) {
				lastDmxOffset = SPOTLIGHT_DMX_START_CHANNEL;
			}

			System.out.println("Spotlight start address: " + lastDmxOffset);
			for (LXModel m : spotlights) {
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

			System.out.println("Wall-washer start address: " + lastDmxOffset);
			for (LXModel m : wallWashers) {
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
