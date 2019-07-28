package org.yomigae.output;

import heronarts.lx.color.LXColor;

public class WallWasherDmxFragment extends DmxFragment implements RGBWADmxFragment {
	public static final int CHANNEL_COUNT = 35;

	/*
	DMX channels

	0 		: master dimmer
	1 		: strobe
	2 		: R 1
	3 		: G 1
	4 		: B 1
	5 		: W 1
	6 		: A 1
	7-11 	: RGBWA 2
	12-16 : RGBWA 3
	17-21 : RGBWA 4
	22-26 : RGBWA 5
	27-31 : RGBWA 6
	32 		: color wheel
	33 		: effect selection
	34 		: effect speed
	*/

	private static final int CHANNEL_DIMMER = 0;
	private static final int CHANNEL_R = 2;
	private static final int CHANNEL_G = 3;
	private static final int CHANNEL_B = 4;
	private static final int CHANNEL_W = 5;
	private static final int CHANNEL_A = 6;

	private static final int COLOR_CHANNEL_COUNT = 5;

	private final int[] indexBuffer;

	public WallWasherDmxFragment(int startChannel, int[] indexBuffer) {
		super(startChannel, CHANNEL_COUNT);

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

		int pixelOffset = 0;

		for (int i = 0; i < indexBuffer.length; ++i) {
			int index = indexBuffer[i];
			int c = colors[index];

			pixelOffset = fragmentOffset + i * COLOR_CHANNEL_COUNT;

			buffer[pixelOffset + CHANNEL_R] = LXColor.red(c);
			buffer[pixelOffset + CHANNEL_G] = LXColor.green(c);
			buffer[pixelOffset + CHANNEL_B] = LXColor.blue(c);
			buffer[pixelOffset + CHANNEL_W] = 0;
			buffer[pixelOffset + CHANNEL_A] = 0;
		}
	}
}
