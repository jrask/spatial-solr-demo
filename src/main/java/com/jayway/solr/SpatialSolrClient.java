package com.jayway.solr;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;

public class SpatialSolrClient {

	/**
	 * Base query, %s replaced in search
	 */
	private static final String QUERY = 
		"{!spatial lat=%s long=%s radius=%s unit=km threadCount=2} %s"; 
	
	String s = "{!spatial circles=%s,%s,%s} %s";
	
	private SolrServer solr;
	
	
	public SpatialSolrClient(String url) throws MalformedURLException {
		solr =  new CommonsHttpSolrServer(url);
	}
	
	public void commit() throws SolrServerException, IOException {
		solr.commit();
	}
	
	public SolrDocumentList search(String query) throws SolrServerException {
		ModifiableSolrParams params = new ModifiableSolrParams();
	    params.set("q", query);
	    QueryResponse response = solr.query(params);
	    return response.getResults();
	}
	
	public SolrDocumentList search(String query, Location loc, double radius) throws SolrServerException {
		return search(String.format(s,loc.getLat(),loc.getLng(),radius,query));		
	}
	
	public void index(Attraction attraction) throws SolrServerException, IOException {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", attraction.getId());
		doc.addField("type",attraction.getType());
		doc.addField("name", attraction.getName());
		doc.addField("lat", attraction.getLocation().getLat());
		doc.addField("lng", attraction.getLocation().getLng());
		solr.add(doc);
	}
	
	public void clearIndex() throws SolrServerException, IOException {
		solr.deleteByQuery("*:*");
	}


}
