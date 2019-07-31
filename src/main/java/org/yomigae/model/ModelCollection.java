package org.yomigae.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collection;
import java.util.Arrays;

import com.google.common.reflect.TypeToken;
import com.google.common.collect.ForwardingList;

import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

public class ModelCollection<M extends LXModel> extends ForwardingList<M> {
	private List<M> models;

	public ModelCollection(Collection<M> modelsIter) {
		models = new ArrayList<>(modelsIter);
	}

	public static ModelCollection<LXModel> filterChildren(List<LXModel> models, String query) {
		return filterChildren(models, query, LXModel.class);
	}

	public static <T extends LXModel> ModelCollection<T> filterChildren(List<? extends LXModel> models, String query, Class<T> modelClass) {
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

		List<T> typedModels = new ArrayList<>();
		for (LXModel m : filtered) {
			try {
				if (modelClass.isAssignableFrom(m.getClass())) {
					typedModels.add((T)m);
				}
			} catch (ClassCastException e) { /* pass */ }
		}

		return new ModelCollection<T>(typedModels);
	}

	public static ModelCollection<LXModel> filterChildren(LXModel model, String query) {
		return filterChildren(Arrays.asList(model.children), query, LXModel.class);
	}

	public static <T extends LXModel> ModelCollection<T> filterChildren(LXModel model, String query, Class<T> modelClass) {
		return filterChildren(Arrays.asList(model.children), query, modelClass);
	}

	public ModelCollection<LXModel> filterChildren(String query) {
		return filterChildren(query, LXModel.class);
	}

	public <T extends LXModel> ModelCollection<T> filterChildren(String query, Class<T> modelClass) {
		return filterChildren(models, query, modelClass);
	}

	@Override
	protected List<M>	delegate() {
		return models;
	}
}
