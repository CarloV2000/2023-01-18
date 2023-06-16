package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<String, Location>locationNameMap;
	
	private List<Location>migliore;
	private int nPercorso;
	
	
	public Model() {
		this.dao = new NYCDao();
		this.allProviders = new ArrayList<>(dao.getProviders());
		this.allLocations = new ArrayList<>();
		this.locationNameMap = new HashMap<>();
	}
	
	public String creaGrafo(String provider, Double sogliaMIN) {
		
		this.grafo = new SimpleWeightedGraph<Location, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.allLocations = dao.getLocationVertici(provider);
		Graphs.addAllVertices(grafo, this.allLocations);
		
		for(Location x : this.allLocations) {
			this.locationNameMap.put(x.getLocation(), x);
		}
		
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
	
	/**
	 * Dato il grafo costruito al punto precedente, si vuole definire un percorso che parta da una localita a caso tra 
	 * quelle selezionate nel punto 1 e termini nella localita t (inserita) senza passare dalla localita s, 
	 * MASSIMIZZANDO IL NUMERO DI LOCALITA ATTRAVERSATE
	
	 */
	/**
	 * Metodo che calcola il Dream Team
	 */
	public void  calcolaPercorso(Location localitaT, String s) {
		this.nPercorso = 0;
		this.migliore = new ArrayList<Location>();
		
		Location arrivo = localitaT;
		Location rimuovi = this.locationNameMap.get(s);
		Location partenza = this.scegliUnaLocationACaso(arrivo, rimuovi);
		List<Location> rimanenti = new ArrayList<>(this.grafo.vertexSet());
		List<Location> parziale = new ArrayList<>();
		
		
		
		ricorsione(parziale, rimanenti, partenza, arrivo, rimuovi);
	}
	
	
	
	/**
	 * La ricorsione vera e propria
	 * @param parziale
	 * @param rimanenti
	 */
	private void ricorsione(List<Location> parziale, List<Location> rimanenti, Location p, Location a, Location r){
		
		//Condizione iniziale
		if(parziale.isEmpty()) {
			parziale.add(p);
			rimanenti.remove(p);
		}
		// Condizione Terminale
		if (parziale.get(parziale.size()-1).equals(a)) {
			//calcolo costo
			int numero = parziale.size();
			if (numero > this.nMaxVicini) {
				this.nMaxVicini = numero;
				this.migliore = new ArrayList<Location>(parziale);
			}
			return;
		}
		
		/*
		 * VERSIONE NON OTTIMIZZATA DELLA RICORSIONE
		 */
		/*
		 * Questa versione riguarda le stesse combinazioni di giocatori più volte, e richiede mooolto tempo.
		 * Riesce a terminare in tempi acettabili solo su grafi molto piccoli, con meno di 10 vertici. La versione 
		 * ottimizzata di sotto riesce a gestire velocemente anche grafi con 40-50 vertici.
		 */
       	for (Location x : rimanenti) {
       		if(!x.equals(r)) {
 				parziale.add(x);
 				rimanenti.remove(x);
 				ricorsione(parziale, rimanenti, p, a, r);
 				parziale.remove(parziale.size()-1);
 				rimanenti.add(x);
 			}
 		}
	}
			
	private Location scegliUnaLocationACaso(Location arrivo, Location rimuovi) {
		Location scelta = null;
		List<Location>a = this.trovaVerticiConPiuVicini();
		int n = a.size();
		int nScelto = (int)(Math.random()*n);
		scelta = a.get(nScelto);
		if(scelta.equals(arrivo) || scelta.equals(rimuovi)) {
			scelta = null;
		}
		return scelta;
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

	public Map<String, Location> getLocationNameMap() {
		return locationNameMap;
	}

	public List<Location> getMigliore() {
		return migliore;
	}

	public int getnPercorso() {
		return nPercorso;
	}
	
	

}
