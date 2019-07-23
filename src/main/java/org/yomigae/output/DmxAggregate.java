package org.yomigae.output;

import java.util.List;
import java.util.Collection;

import com.google.common.collect.ImmutableList;

public class DmxAggregate extends DmxFragment {
	private List<DmxFragment> fragments;

	private DmxAggregate(int startChannel, int numChannels, List<DmxFragment> fragments) {
		super(startChannel, numChannels);

		this.fragments = fragments;
	}

	public static DmxAggregate fromFragments(Collection<DmxFragment> fragmentIter) {
		List<DmxFragment> fragments = ImmutableList.copyOf(fragmentIter);

		if (fragments.isEmpty()) {
			throw new IllegalArgumentException("Fragments list cannot be empty.");
		}

		int minStartChannel = 511;
		int maxEndOffset = 0;
		for (DmxFragment frag : fragments) {
			int endOffset = frag.getStartChannel() + frag.getNumChannels();
			if (endOffset > maxEndOffset) {
				maxEndOffset = endOffset;
			}

			int fragStartChannel = frag.getStartChannel();
			if (fragStartChannel < minStartChannel) {
				minStartChannel = fragStartChannel;
			}
		}

		if (minStartChannel > maxEndOffset) {
			throw new IndexOutOfBoundsException("Malformed fragment start/end channels.");
		}

		return new DmxAggregate(minStartChannel, maxEndOffset - minStartChannel, fragments);
	}

	@Override
	public void applyToBuffer(int[] colors, byte[] buffer, int offset) {
		for (DmxFragment frag : fragments) {
			frag.applyToBuffer(colors, buffer, offset);
		}
	}
}
