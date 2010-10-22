package com.jayway.solr;

public class Location {

	public Location(double lat, double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}

	double lat = 55.672;
	double lng = 13.069;
	
	public double getLat() {
		return lat;
	}
	
	public double getLng() {
		return lng;
	}
}
