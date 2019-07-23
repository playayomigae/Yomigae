package org.yomigae.output;

public abstract class DmxFragment {

	protected final int startChannel;
	protected final int numChannels;

	public DmxFragment(int startChannel, int numChannels) {
		this.startChannel = startChannel;
		this.numChannels = numChannels;
	}

	/**
	 * First channel used by fragment.
	 *
	 * @return start channel offset
	 */
	public int getStartChannel() {
		return startChannel;
	}

	/**
	 * Number of channels used by fragment.
	 *
	 * @return number of channels
	 */
	public int getNumChannels() {
		return numChannels;
	}

	/**
	 * Write fragment data to DMX buffer.
	 *
	 * @param colors color buffer
	 * @param buffer DMX buffer
	 * @param offset DMX data start offset
	 */
	public abstract void applyToBuffer(int[] colors, byte[] buffer, int offset);
}
