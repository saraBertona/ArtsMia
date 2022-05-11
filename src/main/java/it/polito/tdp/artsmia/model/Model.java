package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject,DefaultWeightedEdge> grafo; 
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap;
	
	public Model () {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer,ArtObject>();
	}
	
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		dao.listObjects(idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//aggiungo gli archi
		
		//APPROCCIO 1 -> NON GIUNGE A TERMINE
		/*for (ArtObject a1 : this.grafo.vertexSet()) {
			for (ArtObject a2 : this.grafo.vertexSet()) {
				if(!a1.equals(a2) && !this.grafo.containsEdge(a1,a2)) {
					//"chiedo" al db se devo collegare a1 e a2
					int peso = dao.getPeso(a1,a2);
					if(peso > 0) {
						Graphs.addEdgeWithVertices(this.grafo, a1, a2, peso);
					}
				}
			}
		}*/
		//APPROCCIO 2
		for(Adiacenza a : this.dao.getAdiacenze(idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), 
					a.getA2(), a.getPeso());
		}
		
		System.out.println("Grafo creato!");
		System.out.println("# VERTICI: " + this.grafo.vertexSet().size());
		System.out.println("# ARCHI: " + this.grafo.edgeSet().size());
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public ArtObject getObject(int objectId) {
		return idMap.get(objectId);
	}

	public int getComponenteConnessa(ArtObject vertice) {
		Set<ArtObject> visitati = new HashSet<>();
		
		DepthFirstIterator<ArtObject,DefaultWeightedEdge> it = 
				new DepthFirstIterator<ArtObject, DefaultWeightedEdge>(this.grafo, vertice);
		while (it.hasNext()) {
			visitati.add(it.next());
		}
		
		return visitati.size();
		
	}
}
