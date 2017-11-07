package com.example.pdtProject;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;


@Component
public class DatabaseCalls {

	private DataSource dataSource;
	
	public DatabaseCalls(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getVersion() {
		String query = "select version();";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.execute();
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				return resultSet.getString(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "default";
	}
	
	public String searchInArea(boolean doctor, boolean dentist, boolean hospital, boolean clinic, boolean pharmacy,
			String polygon)
	{
		String amenity = amenity(doctor, dentist, hospital, clinic, pharmacy);
		
		String query = "SELECT ST_X (a.way), ST_Y (a.way), a.amenity, a.name " + 
				"FROM planet_osm_point as a " + 
				"where ST_Contains( " + 
				"ST_GeomFromText("+polygon+"),a.way) " + 
				"   and (" +amenity+ ") "+ 
				"   and " + 
				"   (select ST_DistanceSphere(b.way, a.way) as distance\r\n" + 
				"    from planet_osm_point as b where b.amenity = 'parking' or b.amenity = 'parking_entrance' or b.amenity = 'parking_space' order by distance ASC limit 1) < 500";
				
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		StringBuilder result = new StringBuilder();
		result.append("[");
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			int x = 0;
			while(resultSet.next())
			{
				x++;
				double resLng = resultSet.getDouble(1);
				double resLat = resultSet.getDouble(2);
				String resAmenity = resultSet.getString(3);
				String resName = resultSet.getString(4);
				
				result.append(toGeoJson(resLng, resLat, resAmenity, resName)+",");
			}
			result.setLength(result.length()-1);
			result.append("]");
			
			//Zero results
			if(x == 0)
			{
				result.setLength(0);
				result.append("null");
			}
			//One result - need to delete []
			else if(x == 1)
			{
				result.setCharAt(0, ' ');
				result.setCharAt(result.length()-1, ' ');
			}
			
			return result.toString();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "null";
	}

	public String searchFromPoint(boolean doctor, boolean dentist, boolean hospital, boolean clinic, boolean pharmacy,
			String point)
	{		
		String amenity = amenity(doctor, dentist, hospital, clinic, pharmacy);
		 
		Double lat = Double.parseDouble(point.split(",")[0].substring(7, point.split(",")[0].length()));
		Double lng = Double.parseDouble(point.split(",")[1].substring(0, point.split(",")[1].length()-1));
		
		String query = "SELECT ST_X (way), ST_Y (way), amenity, name, " + 
				"ST_DistanceSphere(st_makepoint(?,?), way) " + 
				"FROM planet_osm_point " + 
				"where " + amenity;
				
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		StringBuilder result = new StringBuilder();
		result.append("[");
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDouble(1, lng);
			preparedStatement.setDouble(2, lat);
			resultSet = preparedStatement.executeQuery();
			
			int x = 0;
			while(resultSet.next())
			{
				x++;
				double resLng = resultSet.getDouble(1);
				double resLat = resultSet.getDouble(2);
				String resAmenity = resultSet.getString(3);
				String resName = resultSet.getString(4);
				double resDistance = resultSet.getDouble(5);
				
				result.append(toGeoJson(resLng, resLat, resAmenity, resName, resDistance)+",");
			}
			result.setLength(result.length()-1);
			result.append("]");
			
			if(x == 0)
			{
				result.setLength(0);
				result.append("null");
			}
			else if(x == 1)
			{
				result.setCharAt(0, ' ');
				result.setCharAt(result.length(), ' ');
			}
			
			return result.toString();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "null";
	}
	
	public String searchTriples(boolean doctor, boolean dentist, boolean hospital, boolean clinic, boolean pharmacy,
			String polygon)
	{
		ArrayList<String> amenity = new ArrayList<String>();
		if(pharmacy)
			amenity.add("pharmacy");
		if(doctor)
			amenity.add("doctors");
		if(dentist)
			amenity.add("dentist");
		if(clinic)
			amenity.add("clinic");
		if(hospital)
			amenity.add("hospital");
		
		String query = "WITH pointA AS (\r\n" + 
				"  SELECT a.osm_id, a.way FROM planet_osm_point a\r\n" + 
				"  WHERE ST_Contains(\r\n" + 
				"ST_GeomFromText("+polygon+"), a.way)\r\n" + 
				"    and a.amenity = '"+amenity.get(0)+"'\r\n" + 
				"),pointB AS (\r\n" + 
				"  SELECT b.osm_id, b.way FROM planet_osm_point b\r\n" + 
				"  WHERE ST_Contains(\r\n" + 
				"ST_GeomFromText("+polygon+"), b.way)\r\n" + 
				"    and b.amenity = '"+amenity.get(1)+"'\r\n" + 
				"),pointC AS (\r\n" + 
				"  SELECT c.osm_id, c.way FROM planet_osm_point c\r\n" + 
				"  WHERE ST_Contains(\r\n" + 
				"ST_GeomFromText("+polygon+"), c.way)\r\n" + 
				"    and c.amenity = '"+amenity.get(2)+"'\r\n" +
				
				"), crossAB AS (\r\n" + 
				"select pointA.osm_id as pointA_id, pointA.way as pointA_way, pointB.osm_id as pointB_id, pointB.way as pointB_way,\r\n" + 
				"ST_DistanceSphere(pointA.way, pointB.way) as distanceAB\r\n" + 
				"from pointA\r\n" + 
				"cross join pointB\r\n" + 
				"order by distanceAB asc\r\n" +
				
				"), finalResult AS (\r\n" + 
				"select crossAB.pointA_id, crossAB.pointB_id, pointC.osm_id as pointC_id, crossAB.distanceAB,\r\n" + 
				"crossAB.distanceAB + ST_DistanceSphere(crossAB.pointA_way, pointC.way) as trol\r\n" + 
				"from crossAB\r\n" + 
				"cross join pointC\r\n" + 
				"order by trol asc\r\n" + 
				"limit 1)\r\n" +
				
				"select ST_X (way), ST_Y (way), amenity, name\r\n" + 
				"from planet_osm_point\r\n" + 
				"where osm_id = (select pointA_id from finalResult) or\r\n" + 
				"osm_id = (select pointB_id from finalResult) or\r\n" + 
				"osm_id = (select pointC_id from finalResult)";
				
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		StringBuilder result = new StringBuilder();
		result.append("[");
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			int x = 0;
			while(resultSet.next())
			{
				x++;
				double resLng = resultSet.getDouble(1);
				double resLat = resultSet.getDouble(2);
				String resAmenity = resultSet.getString(3);
				String resName = resultSet.getString(4);
				
				result.append(toGeoJson(resLng, resLat, resAmenity, resName)+",");
			}
			result.setLength(result.length()-1);
			result.append("]");
			
			if(x == 0)
			{
				result.setLength(0);
				result.append("null");
			}
			else if(x == 1)
			{
				result.setCharAt(0, ' ');
				result.setCharAt(result.length()-1, ' ');
			}
			
			return result.toString();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "null";
	}
	
	//Create string of amenity
	private String amenity(boolean doctor, boolean dentist, boolean hospital, boolean clinic, boolean pharmacy)
	{
		StringBuilder amenity = new StringBuilder();
		if(doctor)
			amenity.append(amenity.length() == 0 ? "amenity = \'doctors\' " : "or amenity = \'doctors\' ");
		if(dentist)
			amenity.append(amenity.length() == 0 ? "amenity = \'dentist\' " : "or amenity = \'dentist\' ");
		if(hospital)
			amenity.append(amenity.length() == 0 ? "amenity = \'hospital\' " : "or amenity = \'hospital\' ");
		if(clinic)
			amenity.append(amenity.length() == 0 ? "amenity = \'clinic\' " : "or amenity = \'clinic\' ");
		if(pharmacy)
			amenity.append(amenity.length() == 0 ? "amenity = \'pharmacy\' " : "or amenity = \'pharmacy\' ");
		
		return amenity.toString();
	}
	
	//Convert to geoJSon
	private String toGeoJson(double lng, double lat, String amenity, String name)
	{		
		String color = null;
		if(amenity.equals("doctors"))
		{
			color = "blue";
		}
		else if(amenity.equals("dentist"))
		{
			color = "yellow";
		}
		else if(amenity.equals("hospital"))
		{
			color = "grey";
		}
		else if(amenity.equals("clinic"))
		{
			color = "brown";
		}
		else if(amenity.equals("pharmacy"))
		{
			color = "violet";
		}
		
		StringBuilder str = new StringBuilder("	{ " + 
				"\"type\": \"Feature\", " + 
				"\"properties\": { " + 
				"\"name\": \""+name+"\", " + 
				"\"amenity\": \""+amenity+"\" " + 
				"}, " + 
				"\"geometry\": { " + 
				"\"type\": \"Point\", " + 
				"\"coordinates\": ["+lng+", "+lat+"] " + 
				"}, "+ 
				"\"style\": { " + 
				"		\"fill\": \""+color+"\" " + 
				"	} " + 
				"}");
		
		return str.toString();
	}
	
	//Convert to geoJSon
	private String toGeoJson(double lng, double lat, String amenity, String name, double distance)
	{
		//Color by distance
		String color = distance < 500 ? "green" : distance < 1000 ? "orange" : "red";
		
		StringBuilder str = new StringBuilder("	{ " + 
				"\"type\": \"Feature\", " + 
				"\"properties\": { " + 
				"\"name\": \""+name+"\", " + 
				"\"amenity\": \""+amenity+"\" " + 
				"}, " + 
				"\"geometry\": { " + 
				"\"type\": \"Point\", " + 
				"\"coordinates\": ["+lng+", "+lat+"] " + 
				"}, "+ 
				"\"style\": { " + 
				"		\"fill\": \""+color+"\" " + 
				"	} " + 
				"}");
		
		return str.toString();
	}
}
