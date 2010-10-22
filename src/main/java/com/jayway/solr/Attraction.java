package com.jayway.solr;

import java.util.Random;

public class Attraction {

	static Random random = new Random();
	
	private String id = String.valueOf(random.nextLong());
	private Location location;
	private String type;
	private String name;
	 
	public Attraction(Location location, String type, String name) {
		super();
		this.location = location;
		this.type = type;
		this.name = name;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public String getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}
}
