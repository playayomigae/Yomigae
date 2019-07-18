package org.yomigae;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Arrays;

import com.google.common.collect.ImmutableSet;

import heronarts.lx.model.LXPoint;
import heronarts.lx.output.LXDatagram;
import heronarts.lx.color.LXColor;

import org.yomigae.model.TempleModel;
import org.yomigae.output.StreamingACNDatagram;
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

	private static final double WALL_WASHER_DMX_CHANNELS = 35;
	private static final double SPOTLIGHT_DMX_CHANNELS = 10;

	private static final String METADATA_KEY_DMX_UNIVERSE = "dmx-universe";
	private static final String METADATA_KEY_DMX_CHANNEL_OFFSET = "dmx-channel-offset";

	//private final TempleBuilder builder = new TempleBuilder();
	private final TempleModel model;


	public YomigaeLayout() {
		model = new TempleModel();

		// build DMX outputs
		List<Set<LXPoint>> universePoints = new ArrayList<>();
		List<int[]> universeIndices = new ArrayList<>();

		// currently assumes points were created in the correct order
		universePoints.add(model.filterPoints(ImmutableSet.of(TempleModel.FilterFlags.TWELVE, TempleModel.FilterFlags.THREE)));
		universePoints.add(model.filterPoints(ImmutableSet.of(TempleModel.FilterFlags.TWELVE, TempleModel.FilterFlags.NINE)));
		universePoints.add(model.filterPoints(ImmutableSet.of(TempleModel.FilterFlags.SIX, TempleModel.FilterFlags.THREE)));
		universePoints.add(model.filterPoints(ImmutableSet.of(TempleModel.FilterFlags.SIX, TempleModel.FilterFlags.NINE)));

		for (int i = 0; i < universePoints.size(); ++i) {
			List<LXPoint> points = new ArrayList<>(universePoints.get(i));

			int[] indices = new int[points.size()];
			universeIndices.add(indices);

			for (int j = 0; j < points.size(); ++j) {
				indices[j] = points.get(j).index;
			}

			Arrays.sort(indices);
		}

		for (int i = 0; i < universePoints.size(); ++i) {
			LXDatagram d = new StreamingACNDatagram(i + 1, 512, universeIndices.get(i)) {
				@Override
				protected LXDatagram copyPoints(int[] colors, byte[] glut, int[] indexBuffer, int offset) {
					for (int index : indexBuffer) {
						int color = (index >= 0) ? colors[index] : 0;
						this.buffer[offset + 0] = glut[((color >> 16) & 0xff)]; // R
						this.buffer[offset + 1] = glut[((color >> 8) & 0xff)]; // G
						this.buffer[offset + 2] = glut[(color & 0xff)]; // B
						this.buffer[offset + 3] = 0; // A
						this.buffer[offset + 4] = (byte)(LXColor.b(color) / 100.f * 255); // W
						offset += 5;
					}
					return this;
				}
			};

			int universeHighByte = 0;
			int universeLowByte = i + 1;

			try {
				d.setAddress(SACN_ADDRESS_BASE + universeHighByte + "." + universeLowByte);
			}
			catch (Exception e) {
				System.err.println("Error when setting DMX IP address: " + e.getMessage());
				e.printStackTrace();
			}

			model.addDatagram(d);
		}
	}

	/*
	public YomigaeLayout() {
		builder.defineFixtureType(FIXTURE_TYPE_SPOTLIGHT)
			.addPoint(0, 0, 0)
			.withDirection(0, 1, 0)
			.withLensAngle(Math.toRadians(40));

		TempleBuilder.FixtureTypeBuilder wallWasherBuilder = builder.defineFixtureType(FIXTURE_TYPE_WALL_WASHER);

		for (int i = 0; i < 6; ++i) {
			wallWasherBuilder.newPointGroup();

			for (int j = 0; j < 3; ++j) {
				double x = (3 * i + j) * WALL_WASHER_WIDTH_FEET / (18 - 1);
				wallWasherBuilder.addPoint(x, 0, 0)
					.withDirection(0, 0, 1)
					.withLensAngle(Math.toRadians(40));
			}
		}

		for (int i = 0; i < 6; ++i) {
			TempleBuilder.ToriiTypeBuilder b = builder.defineToriiType(TempleModel.ToriiType.fromIndex(i))
				.withOpeningWidth(TORII_OPENING_WIDTH_FEET[i])
				.withColumnWidth(TORII_COLUMN_WIDTH_FEET[i])
				.withColumnHeight(TORII_COLUMN_HEIGHT_FEET[i])
				.withColumnDepth(TORII_COLUMN_DEPTH_FEET[i])
				.withBeamHeight(TORII_BEAM_HEIGHT_FEET[i])
				.withBeamLength(TORII_BEAM_LENGTH_FEET[i]);

			switch (i) {
				case 0: // T1
					b.addFixture(FIXTURE_TYPE_SPOTLIGHT)
							.withPositionFromColumnBase(0, 0, 6 + 6.75 / 12.) // 2 meters out
							//.withRotation()
					;
					break;
				case 1: // T2
					b.addFixture(FIXTURE_TYPE_WALL_WASHER)
							.withPositionFromEaveEnd(0, 0, 6 / 12.)
							//.withRotation()
					;
					break;
				default: // T3-T6
					b.addFixture(FIXTURE_TYPE_WALL_WASHER)
							.withPositionFromEaveEnd(-1.5, 0, 6 / 12.)
							//.withRotation()
					;
					b.addFixture(FIXTURE_TYPE_WALL_WASHER)
							.withPositionFromEaveEnd(1.5, 0, 6 / 12.)
							//.withRotation()
					;
			}
		}

		// building left side
		builder.setDirection(-1);
		buildHalfTemple(builder, 1, 3);

		// building right side
		builder.setDirection(1);
		buildHalfTemple(builder, 2, 4);

		model = builder.build();

		// build DMX outputs
		List<Set<LXPoint>> universePoints = new ArrayList<>();
		List<int[]> universeIndices = new ArrayList<>();

		// currently assumes points were created in the correct order
		universePoints.add(model.filterPoints(ImmutableSet.of(FilterFlags.NORTH, FilterFlags.EAST)));
		universePoints.add(model.filterPoints(ImmutableSet.of(FilterFlags.NORTH, FilterFlags.WEST)));
		universePoints.add(model.filterPoints(ImmutableSet.of(FilterFlags.SOUTH, FilterFlags.EAST)));
		universePoints.add(model.filterPoints(ImmutableSet.of(FilterFlags.SOUTH, FilterFlags.WEST)));

		for (int i = 0; i < universePoints.size(); ++i) {
			Set<LXPoint> points = universePoints[i];

			int[] indices = new int[points.size()];
			universeIndices.add(indices);

			for (int j = 0; j < points.size(); ++j) {
				indices[j] = points.get(j).index;
			}

			Arrays.sort(indices);
		}

		//model.addDatagram();
	}

	private void buildHalfTemple(TempleBuilder builder, int leftDmxUniverse, int rightDmxUniverse) {
		builder.setOffset(0).addGap((13 + 11 / 12.) / 2.);

		int dmxOffset = 0;

		for (int i = 0; i < 4; ++i) {
			builder.addTorii(TempleModel.ToriiType.fromIndex(5 - i))
					.assignFixtureMetadata(0, ImmutableMap.of(
							METADATA_KEY_DMX_UNIVERSE, leftDmxUniverse.toString(),
							METADATA_KEY_DMX_CHANNEL_OFFSET, dmxOffset.toString()
					))
					.assignFixtureMetadata(1, ImmutableMap.of(
							METADATA_KEY_DMX_UNIVERSE, leftDmxUniverse.toString(),
							METADATA_KEY_DMX_CHANNEL_OFFSET, (dmxOffset + WALL_WASHER_DMX_CHANNELS).toString()
					))
					.assignFixtureMetadata(2, ImmutableMap.of(
							METADATA_KEY_DMX_UNIVERSE, rightDmxUniverse.toString(),
							METADATA_KEY_DMX_CHANNEL_OFFSET, dmxOffset.toString()
					))
					.assignFixtureMetadata(1, ImmutableMap.of(
							METADATA_KEY_DMX_UNIVERSE, rightDmxUniverse.toString(),
							METADATA_KEY_DMX_CHANNEL_OFFSET, (dmxOffset + WALL_WASHER_DMX_CHANNELS).toString()
					))
			;

			dmxOffset += WALL_WASHER_DMX_CHANNELS * 2;
		}

		builder.addTorii(TempleModel.ToriiType.T2)
				.assignFixtureMetadata(0, ImmutableMap.of(
						METADATA_KEY_DMX_UNIVERSE, leftDmxUniverse.toString(),
						METADATA_KEY_DMX_CHANNEL_OFFSET, dmxOffset.toString()
				))
				.assignFixtureMetadata(1, ImmutableMap.of(
						METADATA_KEY_DMX_UNIVERSE, rightDmxUniverse.toString(),
						METADATA_KEY_DMX_CHANNEL_OFFSET, dmxOffset.toString()
				))
		;
		dmxOffset += WALL_WASHER_DMX_CHANNELS;


		for (int i = 0; i < 5; ++i) {
			builder.addTorii(TempleModel.ToriiType.T1)
					.addGap(i < 5 - 1 ? 1 + 11 / 12. : 0)
					.assignFixtureMetadata(0, ImmutableMap.of(
							METADATA_KEY_DMX_UNIVERSE, leftDmxUniverse.toString(),
							METADATA_KEY_DMX_CHANNEL_OFFSET, dmxOffset.toString()
					))
					.assignFixtureMetadata(1, ImmutableMap.of(
							METADATA_KEY_DMX_UNIVERSE, rightDmxUniverse.toString(),
							METADATA_KEY_DMX_CHANNEL_OFFSET, dmxOffset.toString()
					))
			;
			dmxOffset += SPOTLIGHT_DMX_CHANNELS;

		}
	}
	*/

	public TempleModel getModel() {
		return model;
	}
}
