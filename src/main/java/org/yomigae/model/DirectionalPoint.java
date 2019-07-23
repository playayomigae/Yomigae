package org.yomigae.model;

import heronarts.lx.model.LXPoint;

public class DirectionalPoint extends LXPoint {
	public final float[] direction = new float[3];
	public float lensAngle = (float)(Math.PI / 2);

	public DirectionalPoint(double x, double y, double z) {
		super((float)x, (float)y, (float)z);
	}

	public DirectionalPoint(double x, double y, double z, double dx, double dy, double dz) {
		this(x, y, z);

		setDirection(dx, dy, dz);
	}

	public DirectionalPoint(double x, double y, double z, double dx, double dy, double dz, double lensAngle) {
		this(x, y, z, dx, dy, dz);

		setLensAngle(lensAngle);
	}

	public DirectionalPoint setDirection(double dx, double dy, double dz) {
		double norm = Math.sqrt(dx * dx + dy * dy + dz * dz);
		direction[0] = (float)(dx / norm);
		direction[1] = (float)(dy / norm);
		direction[2] = (float)(dz / norm);
		return this;
	}

	public DirectionalPoint setLensAngle(double lensAngle) {
		this.lensAngle = (float)lensAngle;
		return this;
	}
}
