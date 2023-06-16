package it.polito.tdp.nyc.model;

import com.javadocmd.simplelatlng.LatLng;

public class Location {

	private String location;
	private LatLng coordinate;
	
	public Location(String location, LatLng coordinate) {
		super();
		this.location = location;
		this.coordinate = coordinate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public LatLng getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(LatLng coordinate) {
		this.coordinate = coordinate;
	}
	
	
}
