package org.agmip.translators.annotated.sidecar2;

import org.agmip.translators.annotated.sidecar2.components.Sc2File;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Relation.Sc2RelationPart;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;
import org.agmip.translators.annotated.sidecar2.resolve.DataContext;
import org.agmip.translators.annotated.sidecar2.resolve.DataRegistry;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sidecar2 {
	private final List<Sc2File> _validFiles;
	private final List<Sc2File> _invalidFiles;
	private final List<Sc2Relation> _validRelations;
	private final List<Sc2Relation> _invalidRelations;
	private final boolean _valid;
	private final boolean _allFilesValid;
	private final boolean _anyFilesValid;
	private final boolean _allRelationsValid;
	private final boolean _anyRelationsValid;
	private final DataRegistry _registry;

	public Sidecar2(List<Sc2File> files, List<Sc2Relation> relations) {
		_validFiles = files.stream().filter(f -> f.isValid()).collect(Collectors.toList());
		_invalidFiles = files.stream().filter(f -> !f.isValid()).collect(Collectors.toList());
		_validRelations = relations.stream().filter(r -> r.isValid()).collect(Collectors.toList());
		_invalidRelations = relations.stream().filter(r -> !r.isValid()).collect(Collectors.toList());
		_allFilesValid = _invalidFiles.isEmpty();
		_anyFilesValid = (_validFiles.size() > 0);
		_allRelationsValid = _invalidRelations.isEmpty();
		_anyRelationsValid = (_validRelations.size() > 0);
		_registry = buildRegistry();
		_valid = _allFilesValid && _allRelationsValid;
	}

	public List<Sc2File> files() {
		return _validFiles;
	}

	public List<Sc2File> allFiles() {
		return Stream.concat(_validFiles.stream(), _invalidFiles.stream()).collect(Collectors.toList());
	}

	public List<Sc2File> invalidFiles() {
		return _invalidFiles;
	}

	public boolean isValid() {
		return _valid;
	}

	public boolean areAnyFilesValid() {
		return _anyFilesValid;
	}

	public boolean areAllFilesValid() {
		return _allFilesValid;
	}

	public boolean areAnyRelationsValid() {
		return _anyRelationsValid;
	}

	public boolean areAllRelationsValid() {
		return _allRelationsValid;
	}

	private DataRegistry buildRegistry() {
		DataRegistry reg = new DataRegistry();
		for(Sc2File f : _validFiles) {
			for(Sc2Sheet s: f.sheets()) {
				reg.add(f, s);
			}
		}
		return reg;
	}

	public List<String> getRealizationOrder() {
		Graph<DataContext, DefaultEdge> rGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
		boolean addedPrimary;
		boolean addedForeign;
		for(Sc2Relation rel: _validRelations) {
			Sc2RelationPart primary = rel.getPrimary();
			Sc2RelationPart foreign = rel.getForeign();
			String primaryKey = primary.getFile() + "$$" + primary.getSheet();
			String foreignKey = foreign.getFile() + "$$" + foreign.getSheet();
			DataContext p = _registry.get(primaryKey);
			DataContext f = _registry.get(foreignKey);
			rGraph.addVertex(p);
			rGraph.addVertex(f);
			rGraph.addEdge(p, f);
		}
		List<String> relOrder = new ArrayList<>();
		Iterator<DataContext> iter = new TopologicalOrderIterator<>(rGraph);
		iter.forEachRemaining(v -> relOrder.add(v.toString(false) + " - " + rGraph.degreeOf(v) + "[" + v.maxBound() + "]"));
		Collections.reverse(relOrder);
		return relOrder;
	}
}
