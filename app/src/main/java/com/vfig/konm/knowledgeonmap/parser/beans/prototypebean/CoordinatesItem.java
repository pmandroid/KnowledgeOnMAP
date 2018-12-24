package com.vfig.konm.knowledgeonmap.parser.beans.prototypebean;

public class CoordinatesItem{
	private String globe;
	private double lon;
	private double lat;
	private String primary;

	public void setGlobe(String globe){
		this.globe = globe;
	}

	public String getGlobe(){
		return globe;
	}

	public void setLon(double lon){
		this.lon = lon;
	}

	public double getLon(){
		return lon;
	}

	public void setLat(double lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}

	public void setPrimary(String primary){
		this.primary = primary;
	}

	public String getPrimary(){
		return primary;
	}

	@Override
 	public String toString(){
		return 
			"CoordinatesItem{" + 
			"globe = '" + globe + '\'' + 
			",lon = '" + lon + '\'' + 
			",lat = '" + lat + '\'' + 
			",primary = '" + primary + '\'' + 
			"}";
		}
}
