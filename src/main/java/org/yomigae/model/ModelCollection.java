package org.yomigae.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collection;
import java.util.Arrays;

import com.google.common.collect.ForwardingList;

import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

public class ModelCollection extends ForwardingList<LXModel> {
	private List<LXModel> models;

	public ModelCollection(Collection<LXModel> modelsIter) {
		models = new ArrayList<>(modelsIter);
	}

	public ModelCollection(LXModel model) {
		models = Arrays.asList(model.children);
	}

	public static ModelCollection filterChildren(List<LXModel> models, String query) {
		List<LXModel> unfiltered = new ArrayList<>(models);
		List<LXModel> filtered = new ArrayList<>();

		String[] parts = query.split(" ");
		for (String part : parts) {
			filtered.clear();
			List<String> requiredKeys = Arrays.asList(part.split("\\."));

			for (LXModel m : unfiltered) {
				if (Arrays.asList(m.getKeys()).containsAll(requiredKeys)) {
					filtered.add(m);
				}
			}

			unfiltered.clear();
			for (LXModel m : filtered) {
				unfiltered.addAll(Arrays.asList(m.children));
			}
		}

		return new ModelCollection(filtered);
	}

	public static ModelCollection filterChildren(LXModel model, String query) {
		return filterChildren(Arrays.asList(model.children), query);
	}

	public ModelCollection filterChildren(String query) {
		return filterChildren(models, query);
	}

	@Override
	protected List<LXModel>	delegate() {
		return models;
	}
}
