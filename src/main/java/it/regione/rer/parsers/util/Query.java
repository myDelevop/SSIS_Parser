package it.regione.rer.parsers.util;

public class Query {
	
	private String query;
	private String queryUser;
	private String queryTable;
	private String ambito = "";
	private String notes = "";
	

	public Query(String query, String queryUser, String queryTable, String ambito, String notes) {
		this.query = query;
		this.queryUser = queryUser;
		this.queryTable = queryTable;
		this.ambito= ambito;
		this.notes = notes;
	}

	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public String getQueryUser() {
		return queryUser;
	}


	public void setQueryUser(String queryUser) {
		this.queryUser = queryUser;
	}


	public String getQueryTable() {
		return queryTable;
	}


	public void setQueryTable(String queryTable) {
		this.queryTable = queryTable;
	}

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}	
}
