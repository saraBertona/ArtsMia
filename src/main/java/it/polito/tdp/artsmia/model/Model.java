package it.polito.tdp.artsmia.model;

import java.util.*;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject,DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer,ArtObject> idMap;
	public Model() {
		dao=new ArtsmiaDAO();
		idMap=new HashMap<Integer,ArtObject>();
	}
	
	public void creaGrafo() {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
//		dato che creaGrafo lo chiamo nel
//		controller ogni volta che clicco sul bottone,
//		in questo modo il mio grafo viene distrutto e ricreato
//		ogni volta, almeno se modifico la mia base dati ho tutto
		
		
//		aggiungo vertici
		dao.listObjects(idMap);//cambio metodo dao, mando mappa da riempire
		Graphs.addAllVertices(this.grafo, idMap.values());
		
//		Approccio 1:doppio ciclo
		//TROPPO PESANTE!!!!!!! TROPPI VERTICI DOPPIA QUERY
		//LO USO SOLO SE HO POCHI VERTICI
//		for(ArtObject a1: this.grafo.vertexSet()) {
//			for(ArtObject a2: this.grafo.vertexSet()) {
//				if(!a1.equals(a2)&& !this.grafo.containsEdge(a1,a2)) {
////					chiedo al db se devo collegarli
//					int peso=dao.getPeso(a1,a2);
////					se peso>0 aggiungo il mio arco
//					if(peso>0) {
//						Graphs.addEdgeWithVertices(this.grafo, a1, a2, peso);
//					}
//					
//				}
//			}
//		}
		for(Adiacenza a: dao.getAdiacenze(idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());

		}
		
		System.out.println("Grafo creato");
		System.out.println("Vertici "+this.grafo.vertexSet().size());
		System.out.println("Archi "+this.grafo.edgeSet().size());
		
	
		
		
	}
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}

	public ArtObject getObject(int objectId) {
		return idMap.get(objectId);
	}

	public int getComponenteConnessa(ArtObject vertice) {
		Set<ArtObject> visitati=new HashSet<>();
		DepthFirstIterator<ArtObject,DefaultWeightedEdge> it=new DepthFirstIterator<ArtObject,DefaultWeightedEdge>(this.grafo, vertice);
		
		while(it.hasNext()) {
			visitati.add(it.next());
			
		}
		return visitati.size();
	}
	
}
