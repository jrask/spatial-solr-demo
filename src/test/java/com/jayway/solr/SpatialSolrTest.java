package com.jayway.solr;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SpatialSolrTest {

	private static final NumberFormat kmDistanceFormat = new DecimalFormat("###.00");
	private static SpatialSolrClient client;

	/*
	 * Lomma is your current location or location from where you are interested
	 * in finding attractions so when we do our searches we will use this location
	 */
	Location Lomma = new Location(55.672, 13.069);
	
	/*
	 * Staffanstorp and Malmo are two locations which will will use to index
	 * our attractions 
	 */
	Location Staffanstorp = new Location(55.642972, 13.206255);
	Location Malmo = new Location(55.605599, 13.026653);
	
	
	@BeforeClass
	public static void setup() throws SolrServerException, IOException {
		client = new SpatialSolrClient("http://localhost:8983/solr");
	}
	
	/**
	 * Clear index between tests
	 */
	@Before
	@After
	public void clearIndex() throws SolrServerException, IOException {
		client.clearIndex();
		client.commit();
	}
	
	
	@Test
	public void testSpatialSearch() throws SolrServerException, IOException {
		
		// Create and index two attractions along with their location
		client.index(new Attraction(Staffanstorp, "cafe", "Staffanstorp coffee shop"));
		client.index(new Attraction(Malmo, "cafe", "Malmo coffee shop"));
		client.commit();
		
		/*
		 * Our first test wants to find all cafes within 30km radius from
		 * Lomma (center)
		 * Since both Malmš and Staffanstorp are within that range we expect
		 * to get 2 documents back
		 */
		assertEquals(2, client.search("type:cafe",Lomma,10).size());
		
		// Get the list
		List<SolrDocument> list = client.search("type:cafe",Lomma,30);
		verifyDocument(list.get(0),"Staffanstorp coffee shop",9.1,9.3);
		verifyDocument(list.get(1),"Malmo coffee shop",0,20);
	}
	
	private void verifyDocument(SolrDocument doc, String name, double min, double max) {
		// Verify name
		assertEquals(doc.get("name"), name);

		System.out.println("From search point (Lomma) to "+ doc.getFieldValue("name") +"; "
				+ kmDistanceFormat.format((Double)doc.getFieldValue("geo_distance")) + " km");
		
		/*
		 * Important stuff, verify that the distance is what you expect.
		 * Here I just test that it is between two values
		 */
		assertTrue(
				between((Double)doc.get("geo_distance"),min,max));		
	}
	
	private boolean between(double value, double min, double max) {
		return (value > min && value < max);
	}
}
