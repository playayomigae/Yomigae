package org.yomigae.output;

import java.util.Vector;

import heronarts.lx.color.LXColor;

public class SpotlightDmxFragment extends DmxFragment implements RGBWADmxFragment {
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

		byte[] rgbwa = mapRGBtoRGBWA(c);

		// buffer[fragmentOffset + CHANNEL_R] = LXColor.red(c);
		// buffer[fragmentOffset + CHANNEL_G] = LXColor.green(c);
		// buffer[fragmentOffset + CHANNEL_B] = LXColor.blue(c);
		// buffer[fragmentOffset + CHANNEL_W] = 0;
		// buffer[fragmentOffset + CHANNEL_A] = 0;

		buffer[fragmentOffset + CHANNEL_R] = rgbwa[0];
		buffer[fragmentOffset + CHANNEL_G] = rgbwa[1];
		buffer[fragmentOffset + CHANNEL_B] = rgbwa[2];
		buffer[fragmentOffset + CHANNEL_W] = rgbwa[3];
		buffer[fragmentOffset + CHANNEL_A] = rgbwa[4];
	}

	public byte[] mapRGBtoRGBWA(int color) {
		byte[] ret = new byte[5];

		// map RGB back to K

		// map K into kPoints to get the 2 surrounding indices, i and j

		// linearly interpolate between each RGBWA channel in rgbwaMap[i] and [j]

		return ret;
	}

	private static int[] kPoints = new int[] {
		1879,
		2136,
		2364,
		2785,
		3584,
		4000,
		4651,
		5555,
		6869,
		9090,
		13333,
		25000
	};

  private static int[][] rgbwaMap = new int[][] {
		/* 1879  */ new int[] { 209, 167, 0, 45, 255 },
		/* 2136  */ new int[] { 245, 207, 0, 118, 255 },
		/* 2364  */ new int[] { 221, 175, 2, 118, 255 },
		/* 2785  */ new int[] { 72, 154, 0, 219, 240 },
		/* 3584  */ new int[] { 90, 177, 14, 211, 255 },
		/* 4000  */ new int[] { 27, 136, 15, 251, 222 },
		/* 4651  */ new int[] { 94, 191, 43, 255, 255 },
		/* 5555  */ new int[] { 97, 214, 79, 255, 237 },
		/* 6869  */ new int[] { 100, 230, 123, 255, 123 },
		/* 9090  */ new int[] { 50, 224, 164, 255, 150 },
		/* 13333 */ new int[] { 0, 229, 239, 255, 150 },
		/* 25000 */ new int[] { 0, 235, 255, 255, 100 }
	};
}
