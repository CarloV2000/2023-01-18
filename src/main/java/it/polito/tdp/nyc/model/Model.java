package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private List<String>allProviders;
	private Graph<Location, DefaultWeightedEdge>grafo;
	private List<Location>allLocations;
	private NYCDao dao;
	private int nMaxVicini;
	
	
	public Model() {
		this.dao = new NYCDao();
		this.allProviders = new ArrayList<>(dao.getProviders());
		this.allLocations = new ArrayList<>();
	}
	
	public String creaGrafo(String provider, Double sogliaMIN) {
		
		this.grafo = new SimpleWeightedGraph<Location, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.allLocations = dao.getLocationVertici(provider);
		Graphs.addAllVertices(grafo, this.allLocations);
		
		for(Location x : this.allLocations) {
			for(Location y : this.allLocations) {
				if(!x.equals(y)) {
					double peso = distance(x.getCoordinate(), y.getCoordinate());
					if(peso <= sogliaMIN) {
						Graphs.addEdge(grafo, x, y, peso);
					}
				}
			}	
		}
		return "Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi.";
	}
	
	public List<Location>trovaVerticiConPiuVicini() {
		this.nMaxVicini = 0;
		List<Location>result = new ArrayList<>();
		for(Location x : grafo.vertexSet()) {
			if(this.getNumeroVerticiVicini(x) > this.nMaxVicini) {
				result.clear();
				result.add(x);
				this.nMaxVicini = this.getNumeroVerticiVicini(x);
			}else if(this.getNumeroVerticiVicini(x) == this.nMaxVicini) {
				result.add(x);
			}
		}
		return result;
	}
	
	public Integer getNumeroVerticiVicini(Location l) {
		List<Location>vicini = Graphs.neighborListOf(grafo, l);
		return vicini.size();
	}

	private double distance(LatLng coordinate1, LatLng coordinate2) {
		return LatLngTool.distance(coordinate1, coordinate2, LengthUnit.KILOMETER);
	}

	public List<String> getAllProviders() {
		return allProviders;
	}

	public Graph<Location, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Location> getAllLocations() {
		return allLocations;
	}

	public int getnMaxVicini() {
		return nMaxVicini;
	}
	
	

}
