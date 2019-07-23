package org.yomigae.output;

import heronarts.lx.color.LXColor;

public class SpotlightDmxFragment extends DmxFragment {
	public static final int CHANNEL_COUNT = 10;

	/*
	DMX channels

	0 : master dimmer
	1 : strobe
	2 : R
	3 : G
	4 : B
	5 : W
	6 : A
	7 : color wheel
	8 : effect selection
	9 : effect speed
	*/

	private static final int CHANNEL_DIMMER = 0;
	private static final int CHANNEL_R = 2;
	private static final int CHANNEL_G = 3;
	private static final int CHANNEL_B = 4;
	private static final int CHANNEL_W = 5;
	private static final int CHANNEL_A = 6;

	private final int[] indexBuffer;

	public SpotlightDmxFragment(int startChannel, int[] indexBuffer) {
		super(startChannel, CHANNEL_COUNT);

		if (indexBuffer.length > 1) {
			throw new RuntimeException("SpotlightDmxFragment should correspond to only 1 point.");
		}

		this.indexBuffer = indexBuffer;
	}

	@Override
	public void applyToBuffer(int[] colors, byte[] buffer, int offset) {
		int fragmentOffset = offset + startChannel;

		for (int ch = 0; ch < CHANNEL_COUNT; ch++) {
			// These are probably already 0, but just to be sure.
			buffer[fragmentOffset + ch] = 0;
		}

		// Currently set hardware dimmer to full.
		// We will eventually control this via LX Studio UI controls, one per fixture type.
		buffer[fragmentOffset + CHANNEL_DIMMER] = (byte)0xff;

		int index = indexBuffer[0];
		int c = colors[index];

		buffer[fragmentOffset + CHANNEL_R] = LXColor.red(c);
		buffer[fragmentOffset + CHANNEL_G] = LXColor.green(c);
		buffer[fragmentOffset + CHANNEL_B] = LXColor.blue(c);
		buffer[fragmentOffset + CHANNEL_W] = 0;
		buffer[fragmentOffset + CHANNEL_A] = 0;
	}
}
