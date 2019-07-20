package org.yomigae.output;

import heronarts.lx.color.LXColor;

public class SpotlightDmxFragment extends DmxFragment {
	public static final int COLOR_CHANNELS = 5;

	private final int[] indexBuffer;

	public SpotlightDmxFragment(int startChannel, int[] indexBuffer) {
		super(startChannel, COLOR_CHANNELS * indexBuffer.length);

		this.indexBuffer = indexBuffer;
	}

	@Override
	public void applyToBuffer(int[] colors, byte[] buffer, int offset) {
		for (int i = 0; i < indexBuffer.length; ++i) {
			int index = indexBuffer[i];
			int c = colors[index];

			buffer[offset + i * COLOR_CHANNELS + 0] = LXColor.red(c);
			buffer[offset + i * COLOR_CHANNELS + 1] = LXColor.blue(c);
			buffer[offset + i * COLOR_CHANNELS + 2] = LXColor.green(c);
			buffer[offset + i * COLOR_CHANNELS + 3] = 0;
			buffer[offset + i * COLOR_CHANNELS + 4] = 0;
		}
	}
}
