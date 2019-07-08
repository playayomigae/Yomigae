package org.yomigae.model;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;


public class TempleBuilder implements ITempleBuilder {
	private List<TempleModel.Torii> toriis = new ArrayList<>();
	private Map<TempleModel.ToriiType, ToriiTypeBuilder> toriiTypeDefs = new HashMap<>();
	private Map<String, FixtureTypeBuilder> fixtureTypeDefs = new HashMap<>();
	private float lastToriiOffset = 0;
	private int growthDirection = 0;
	private TempleModel model;

	@Override
	public FixtureTypeBuilder defineFixtureType(String type) {
		if (fixtureTypeDefs.containsKey(type))
			return fixtureTypeDefs.get(type);

		FixtureTypeBuilder ftb = new FixtureTypeBuilder(type);
		fixtureTypeDefs.put(type, ftb);
		return ftb;
	}

	@Override
	public ToriiTypeBuilder defineToriiType(TempleModel.ToriiType type) {
		ToriiTypeBuilder ttb = new ToriiTypeBuilder(type);
		toriiTypeDefs.put(type, ttb);
		return ttb;
	}

	@Override
	public TempleBuilder setDirection(int direction) {
		if (direction != -1 && direction != 1) {
			throw new RuntimeException("Torii direction must be either -1 or 1.");
		}

		growthDirection = direction;
		return this;
	}

	@Override
	public TempleBuilder setOffset(double offset) {
		lastToriiOffset = (float)offset;
		return this;
	}

	@Override
	public TempleBuilder addGap(double gap) {
		lastToriiOffset += (float)gap * growthDirection;
		return this;
	}

	@Override
	public TempleBuilder addTorii(TempleModel.ToriiType type) {
		ToriiTypeBuilder ttb = toriiTypeDefs.get(type);
		if (ttb == null) {
			throw new RuntimeException("Torii type '" + type + "' used before it was defined.");
		}

		float toriiCenter = lastToriiOffset + ttb.columnDepth / 2 * growthDirection;

		List<DirectionalPoint> frontPoints = new ArrayList<>();
		List<DirectionalPoint> rearPoints = new ArrayList<>();

		for (ToriiTypeBuilder.FixtureBuilder fb : ttb.fixtures) {
			FixtureTypeBuilder ftb = fixtureTypeDefs.get(fb.type);
			for (List<FixtureTypeBuilder.PointBuilder> pbs : ftb.pointGroups) {
				for (FixtureTypeBuilder.PointBuilder pb : pbs) {
					frontPoints.add(new DirectionalPoint(
							pb.x - ftb.cx + fb.x + toriiCenter,
							pb.y - ftb.cy + fb.y,
							pb.z - ftb.cz + fb.z,
							pb.dx, pb.dy, pb.dz, pb.lensAngle));
					rearPoints.add(new DirectionalPoint(
							pb.x - ftb.cx + fb.x + toriiCenter,
							pb.y - ftb.cy + fb.y,
							-(pb.z - ftb.cz + fb.z),
							pb.dx, pb.dy, -pb.dz, pb.lensAngle));
				}
			}
		}

		TempleModel.Torii torii = new TempleModel.Torii(type, toriiCenter, ttb.openingWidth,
					ttb.columnWidth, ttb.columnHeight, ttb.columnDepth,
					ttb.beamHeight, ttb.beamLength, frontPoints, rearPoints);

		toriis.add(torii);

		lastToriiOffset += ttb.columnDepth * growthDirection;

		return this;
	}

	@Override
	public TempleModel build() {
		model = new TempleModel(toriis);
		return model;
	}

	public TempleModel getModel() {
		return model;
	}

	public class ToriiTypeBuilder implements ITempleBuilder {
		private TempleModel.ToriiType type;

		private float openingWidth;
		private float columnWidth, columnHeight, columnDepth;
		private float beamHeight, beamLength;
		private List<FixtureBuilder> fixtures = new ArrayList<>();


		public ToriiTypeBuilder(TempleModel.ToriiType type) {
			this.type = type;
		}

		@Override
		public TempleBuilder getParentBuilder() {
			return TempleBuilder.this;
		}

		public ToriiTypeBuilder withOpeningWidth(double openingWidth) {
			this.openingWidth = (float)openingWidth;
			return this;
		}

		public ToriiTypeBuilder withColumnWidth(double columnWidth) {
			this.columnWidth = (float)columnWidth;
			return this;
		}

		public ToriiTypeBuilder withColumnHeight(double columnHeight) {
			this.columnHeight = (float)columnHeight;
			return this;
		}

		public ToriiTypeBuilder withColumnDepth(double columnDepth) {
			this.columnDepth = (float)columnDepth;
			return this;
		}

		public ToriiTypeBuilder withBeamHeight(double beamHeight) {
			this.beamHeight = (float)beamHeight;
			return this;
		}

		public ToriiTypeBuilder withBeamLength(double beamLength) {
			this.beamLength = (float)beamLength;
			return this;
		}

		public FixtureBuilder addFixture(String type) {
			FixtureBuilder fb = new FixtureBuilder(type);
			fixtures.add(fb);
			return fb;
		}

		public class FixtureBuilder implements ITempleBuilder {
			private float x, y, z;
			private String type;

			public FixtureBuilder(String type) {
				this.type = type;
			}

			@Override
			public ToriiTypeBuilder getParentBuilder() {
				return ToriiTypeBuilder.this;
			}

			public FixtureBuilder withPositionFromColumnBase(double x, double y, double z) {
				this.x = (float)x;
				this.y = (float)y;
				this.z = (float)z - openingWidth / 2 - columnWidth;
				return this;
			}

			public FixtureBuilder withPositionFromEaveEnd(double x, double y, double z) {
				this.x = (float)x;
				this.y = (float)y + columnHeight;
				this.z = (float)z - beamLength / 2;
				return this;
			}

			public FixtureBuilder addFixture(String type) {
				return ToriiTypeBuilder.this.addFixture(type);
			}
		}
	}

	public class FixtureTypeBuilder implements ITempleBuilder {
		private String type;
		private float cx = 0, cy = 0, cz = 0;
		private List<List<PointBuilder>> pointGroups = new ArrayList<>();
		private List<PointBuilder> lastPointGroup = new ArrayList<>();

		List<DirectionalPoint> frontPoints = new ArrayList<>();
		List<DirectionalPoint> rearPoints = new ArrayList<>();

		public FixtureTypeBuilder(String type) {
			this.type = type;

			pointGroups.add(lastPointGroup);
		}

		public FixtureTypeBuilder newPointGroup() {
			if (!lastPointGroup.isEmpty()) {
				lastPointGroup = new ArrayList<>();
				pointGroups.add(lastPointGroup);
			}

			return this;
		}

		@Override
		public TempleBuilder getParentBuilder() {
			return TempleBuilder.this;
		}

		private void updateCenter() {
			float xs = 0;
			float ys = 0;
			float zs = 0;

			int i = 0;
			for (List<PointBuilder> pbs : pointGroups) {
				for (PointBuilder pb : pbs) {
					xs += pb.x;
					ys += pb.y;
					zs += pb.z;
					++i;
				}
			}

			cx = xs / i;
			cy = ys / i;
			cz = zs / i;
		}

		public PointBuilder addPoint(double x, double y, double z) {
			PointBuilder pb = new PointBuilder(x, y, z);
			lastPointGroup.add(pb);
			updateCenter();
			return pb;
		}

		public class PointBuilder implements ITempleBuilder {
			private float x, y, z, dx, dy, dz, lensAngle;

			public PointBuilder(double x, double y, double z) {
				this.x = (float)x;
				this.y = (float)y;
				this.z = (float)z;
			}

			@Override
			public TempleBuilder getParentBuilder() {
				return TempleBuilder.this;
			}

			public PointBuilder withDirection(double dx, double dy, double dz) {
				this.dx = (float)dx;
				this.dy = (float)dy;
				this.dz = (float)dz;
				return this;
			}

			public PointBuilder withLensAngle(double lensAngle) {
				this.lensAngle = (float)lensAngle;
				return this;
			}

			public FixtureTypeBuilder newPointGroup() {
				return FixtureTypeBuilder.this.newPointGroup();
			}

			public PointBuilder addPoint(double x, double y, double z) {
				return FixtureTypeBuilder.this.addPoint(x, y, z);
			}
		}
	}
}
