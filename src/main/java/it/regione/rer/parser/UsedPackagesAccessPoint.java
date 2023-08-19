package it.regione.rer.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import it.regione.rer.output.excel.ExcelReader;
import it.regione.rer.output.excel.ExcelWriter;
import it.regione.rer.parser.xpath.ParserXML;
import it.regione.rer.parsers.util.Costanti;
import it.regione.rer.parsers.util.Query;
import it.regione.rer.parsers.util.QueryStringElaborator;

/*
 * Questo programma individua tutti i package in cui viene usata una determinata tabella.
 * Come input vuole un file excel con tutte le tabelle da cercare. Come output restituisce
 * la query con l'ambito in cui viene usata  
 * 
 * NomeVista	Ambito	Note	QueryOriginale
 * */

public class UsedPackagesAccessPoint {
	
	@Parameter(names={"--extension", "-e"})
    public String fileExtension = Costanti.DTSX_EXTENSION;

	@Parameter(names={"--inputDir", "-i"})
    public String inputPath = "src/main/resources";

	@Parameter(names={"--outputDir", "-o"})
    public String outputPath = "src/main/resources/output/ap_2";

    public void run() {  
    	
    	Long start = System.currentTimeMillis();
    	
    	
		List<String> checkTables = ExcelReader.readTablesFromExcel(this.inputPath.concat("/").concat(Costanti.XLSX_VISTE_INPUT));

    	System.out.println("INPUT PATH: " + this.inputPath);
    	System.out.println("OUTPUT PATH: " + this.outputPath);
    	System.out.println("FILES EXTENSION: " + this.fileExtension);
    	System.out.println("CHECK TABLES: " + checkTables);

    	
    	List<Query> queriesObj = new ArrayList<Query>();
        
		List<String> files = new ArrayList<String>();
		ParserXML xmlPars = null;
		
		File folder = new File(inputPath);
	    File[] listOfFiles = folder.listFiles();
	    for(int i = 0; i < listOfFiles.length; i++){
	        String filename = listOfFiles[i].getName();
	        if(filename.endsWith("." + this.fileExtension.toLowerCase()) || 
	        		filename.endsWith("." + this.fileExtension.toUpperCase())) {
	        	files.add(filename);
	        }
	    }
	    
	    int i=1;
	    Scanner scanner = new Scanner(System.in);
	    for (String file: files) {
	    	String ambito = file.substring(0, file.lastIndexOf("."));
	    	char c = 'X';
	    	do {
		    	System.out.println("file " + file + " trovato. Usare " + ambito + " come ambito? (S/N)?");
		    	c = (char) scanner.next().charAt(0);		    	
	    	} while (c!='S' && c!='s' && c!='Y' && c!='y' && c!='N' && c!='n');
	    	
	    	if (c=='N' || c=='n') {
	    		String str = "";
	    		do {
		    		try {
			    		System.out.println(i + " file " + fileExtension + ". Scegliere l'ambito.");
			    		str = scanner.next(); 
		    		} catch(Exception e) {
		    			e.printStackTrace();
		    		}
	    		} while (str.isEmpty());
	    		
	    		ambito = str;
	    	}

	    	// elaborazione i-simo file e recupero le query in stringa
	    	if(!this.inputPath.endsWith("/"))
	    		this.inputPath = this.inputPath.concat("/");
	    	
	    	xmlPars = new ParserXML(this.inputPath + file);
			List<String> queries = xmlPars.getQueriesFromXML();
	    	
			
			// trasformo le stringhe in oggetto
			queriesObj.addAll(QueryStringElaborator.computeStringsUsedPackagesAP(queries, ambito, checkTables));
			
	    	i++;
	    }
		
		scanner.close();

		ExcelWriter.writeQueriesOnExcel(queriesObj, this.outputPath);
		
		
    	Long end = start - System.currentTimeMillis();

		System.out.println("FINITO IN " + end + " ms");
    }
    
        
    
	public static void main(String[] args) {
		
		UsedPackagesAccessPoint acc = new UsedPackagesAccessPoint();
		JCommander.newBuilder()
			.addObject(acc)
			.build()
			.parse(args);
		
		acc.run();
	}


}
