package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.nyc.model.Hotspot;
import it.polito.tdp.nyc.model.Location;

public class NYCDao {
	
	public List<Hotspot> getAllHotspot(){
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		List<Hotspot> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}

	public List<Location> getLocationVertici(String provider){
		String sql = "SELECT l.Location, l.Latitude, l.Longitude "
				+ "FROM nyc_wifi_hotspot_locations l "
				+ "WHERE l.Provider = ? "
				+ "GROUP BY l.Location ";
		List<Location> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, provider);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String location = res.getString("Location");
				Double latitude = this.getLatitudeLocation(provider, location);
				Double longitude =this.getLongitudeLocation(provider, location);
				LatLng coord = new LatLng(latitude, longitude);
				Location l = new Location(location, coord);
				result.add(l);
			}			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	public Double getLongitudeLocation(String provider, String location) {
		String sql = "SELECT l.Longitude "
				+ "FROM nyc_wifi_hotspot_locations l "
				+ "WHERE l.Provider = ? "
				+ "AND l.Location = ? ";
		List<Double> listaLon = new ArrayList<>();
		Double sommaLon = 0.0;
		Double mediaLon = 0.0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, provider);
			st.setString(2, location);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Double longitude = res.getDouble("Longitude");
				listaLon.add(longitude);
			}			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		for(Double x : listaLon) {
			sommaLon += x;
		}
		mediaLon = sommaLon/listaLon.size();

		return mediaLon;
	}
	
	public Double getLatitudeLocation(String provider, String location) {
		String sql = "SELECT l.Latitude "
				+ "FROM nyc_wifi_hotspot_locations l "
				+ "WHERE l.Provider = ? "
				+ "AND l.Location = ? ";
		List<Double> listaLat = new ArrayList<>();
		Double sommaLat = 0.0;
		Double mediaLat = 0.0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, provider);
			st.setString(2, location);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Double latitude = res.getDouble("Latitude");
				listaLat.add(latitude);
			}			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		for(Double x : listaLat) {
			sommaLat += x;
		}
		mediaLat = sommaLat/listaLat.size();

		return mediaLat;
	}

	public List<String> getProviders(){
		String sql = "SELECT DISTINCT l.Provider "
				+ "FROM nyc_wifi_hotspot_locations l ";
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String provider = res.getString("Provider");
				result.add(provider);
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}


}
