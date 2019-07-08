package org.yomigae;

import heronarts.lx.output.LXOutput;

import org.yomigae.model.TempleModel;
import org.yomigae.model.TempleBuilder;

public class YomigaeLayout {

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

	private final TempleBuilder builder = new TempleBuilder();

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


		builder.build();
	}

	private void buildHalfTemple(TempleBuilder builder, int northDmxUniverse, int southDmxUniverse) {
		builder.setOffset(0).addGap((13 + 11 / 12.) / 2.);

		int dmxOffset = 0;

		for (int i = 0; i < 4; ++i) {
			builder.addTorii(TempleModel.ToriiType.fromIndex(5 - i))
				//.assignOutputForGroup(3, 6, new DmxWallWasherOutput(northDmxUniverse, dmxOffset))
				//.assignOutputForGroup(3, 6, new DmxWallWasherOutput(northDmxUniverse, dmxOffset + 35))
				//.assignOutputForGroup(3, 6, new DmxWallWasherOutput(southDmxUniverse, dmxOffset))
				//.assignOutputForGroup(3, 6, new DmxWallWasherOutput(southDmxUniverse, dmxOffset + 35))
			;
			dmxOffset += 35 * 2;
		}

		builder.addTorii(TempleModel.ToriiType.T2)
			//.assignOutputForGroup(3, 6, new DmxWallWasherOutput(northDmxUniverse, dmxOffset))
			//.assignOutputForGroup(3, 6, new DmxWallWasherOutput(southDmxUniverse, dmxOffset))
		;
		dmxOffset += 35;

		for (int i = 0; i < 5; ++i) {
			builder.addTorii(TempleModel.ToriiType.T1)
				.addGap(i < 5 - 1 ? 1 + 11 / 12. : 0)
				//.assignOutputForGroup(1, 1, new DmxSpotlightOutput(northDmxUniverse, dmxOffset))
				//.assignOutputForGroup(1, 1, new DmxSpotlightOutput(southDmxUniverse, dmxOffset))
			;
			dmxOffset += 10;
		}
	}

	public TempleModel getModel() {
		return builder.getModel();
	}

	public LXOutput getOutput() {
		return null;
	}
}
