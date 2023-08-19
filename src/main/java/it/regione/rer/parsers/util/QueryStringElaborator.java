package it.regione.rer.parsers.util;

import java.util.ArrayList;
import java.util.List;

public class QueryStringElaborator {

	public static List<Query> computeStringsUsedTablesAP(final List<String> queriesString, final String ambito) {
		List<Query> queriesObject = new ArrayList<Query>();
						
		for(String query: queriesString) {
			String queryTemp = query;
			String user = "";
			String table = "";
			String where = "";

			if (query.toUpperCase().contains("JOIN"))
				where = where.concat(new String ("WARNING: Controllare la riga perche la query contiene join.\n"));

			System.out.println("\n\n\n\n************************");
			System.out.println("ORIGINAL QUERY: " + query);
			
			/* 1 - calcoliamo innanzitutto la user e la table*/
			int indexFromUpper = query.lastIndexOf("FROM");
			int indexFromLower = query.lastIndexOf("from");
			
			if(indexFromUpper == -1 && indexFromLower > 0) {
				queryTemp = query.substring(indexFromLower + 4);
			} else if (indexFromLower == -1 && indexFromUpper > 0) {
				queryTemp = query.substring(indexFromUpper + 4);				
			} else {
				// siamo costretti a modificare la query e portarla tutta maiuscola
				query = query.toUpperCase();
				indexFromUpper = query.lastIndexOf("FROM");
				if (indexFromUpper == -1) {
					try {
						throw new Exception();						
					} catch (Exception e) {
						System.out.println("ATTENZIONE: Problema con la query: " + query);
						e.printStackTrace();
					}
				} else 
					queryTemp = query.substring(indexFromUpper + 4);
			}
			
			if(queryTemp.contains(".")) {
				user = queryTemp.substring(0, queryTemp.lastIndexOf("."));
				table = queryTemp.substring(queryTemp.lastIndexOf(".") + 1, queryTemp.length());				
			} else {
				where = where.concat("WARNING: CONTROLLARE MANUALMENTE QUESTA QUERY SENZA USER");
			}
			
			
			user = user.replace("\"", "");
			table = table.replace("\"", "");
			
			if(table.toUpperCase().contains("WHERE")) {
				table = table.substring(0, table.toUpperCase().indexOf("WHERE"));
			}
			
			queryTemp.trim();
			System.out.println("QUERY TEMP: " + queryTemp);
			System.out.println("USER: " + user);
			System.out.println("TABLE: " + table);
			
			
			/* 2 - ora vediamo come sistemare le note, della clausola WHERE */
			int indexWhereUpper = query.lastIndexOf("WHERE");
			int indexWhereLower = query.lastIndexOf("where");
			
			if(indexWhereUpper == -1 && indexWhereLower > 0) {
				where = where.concat(query.substring(indexWhereLower));
			} else if (indexWhereLower == -1 && indexWhereUpper > 0) {
				where = where.concat(query.substring(indexWhereUpper));
			} else {
				// siamo costretti a modificare la query e portarla tutta maiuscola
				query = query.toUpperCase();
				indexWhereUpper = query.lastIndexOf("WHERE");
				if (indexWhereUpper != -1) 
					where = where.concat(query.substring(indexWhereUpper));
			}
		
			
		// rimuovo gli spazi in eccesso
		user = user.trim();
		table = table.trim();
		where = where.trim();

		queriesObject.add(new Query(query, user, table, ambito, where));
		System.out.println("NOTES: " + where);
		System.out.println("************************\n");
		}
		
		return queriesObject;
	}
	
	public static List<Query> computeStringsUsedPackagesAP(final List<String> queriesString,
			final String ambito, final List<String> checkTables) {
		
		List<Query> queriesObject = new ArrayList<Query>();
		String user = "N/A";


		for(String query: queriesString) {
			
			for(String table: checkTables) {
				if(query.toUpperCase().contains(table)) {

					String notes = "";

					System.out.println("\n\n\n\n************************");
					System.out.println("ORIGINAL QUERY: " + query);
					

					System.out.println("QUERY: " + query);
					System.out.println("USER: " + user);
					System.out.println("TABLE: " + table);
					
					
					// rimuovo gli spazi in eccesso
					user = user.trim();
					table = table.trim();
					notes = notes.trim();
	
					queriesObject.add(new Query(query, user, table, ambito, notes));
					System.out.println("NOTES: " + notes);
					System.out.println("************************\n");
				}				
			}

		}
		
		return queriesObject;
	}
	
	
	public static List<Query> computeStringsCreatedTablesAP(final List<String> queriesString) {
		List<Query> queriesObject = new ArrayList<Query>();
						
		for(String query: queriesString) {
			String user = "";
			String table = query.trim();
			String where = "";
			String ambito = "";

		queriesObject.add(new Query(query, user, table, ambito, where));
		System.out.println("NOTES: " + where);
		System.out.println("************************\n");
		}
		
		return queriesObject;
	}

}
