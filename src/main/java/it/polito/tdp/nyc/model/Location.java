package it.polito.tdp.nyc.model;

import java.util.Objects;

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
	@Override
	public int hashCode() {
		return Objects.hash(coordinate, location);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(coordinate, other.coordinate) && Objects.equals(location, other.location);
	}
	
	
}
