package org.yomigae.model;


public interface ITempleBuilder {
	default ITempleBuilder getParentBuilder() {
		return null;
	}

	default TempleBuilder.FixtureTypeBuilder defineFixtureType(String type) {
		return getParentBuilder().defineFixtureType(type);
	}

	default TempleBuilder.ToriiTypeBuilder defineToriiType(TempleModel.ToriiType type) {
		return getParentBuilder().defineToriiType(type);
	}

	default TempleBuilder setDirection(int direction) {
		return getParentBuilder().setDirection(direction);
	}

	default TempleBuilder setOffset(double offset) {
		return getParentBuilder().setOffset(offset);
	}

	default TempleBuilder addGap(double gap) {
		return getParentBuilder().addGap(gap);
	}

	default TempleBuilder addTorii(TempleModel.ToriiType type) {
		return getParentBuilder().addTorii(type);
	}

	default TempleModel build() {
		return getParentBuilder().build();
	}

	default TempleModel getModel() {
		return getParentBuilder().getModel();
	}
}
