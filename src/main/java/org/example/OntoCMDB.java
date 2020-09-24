package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

public class OntoCMDB {

	public static void main(String[] args) {
		
		// Define the constants
		File dataDir = new File("C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/datadir");
		String filename = "C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/Onto-CMDB.ttl";
		
		// Define RDF4J NativeStore database
		String indexes = "spoc,posc,cosp";
		Repository db = new SailRepository(new NativeStore(dataDir, indexes));
		
		
		InputStream input = null;
		try {
			input = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Model model = null;
		
		// Parse turtle file to model
		try {
			model = Rio.parse(input, "", RDFFormat.TURTLE);
		} catch (RDFParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedRDFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Initialize the database
		
		db.init();
		
		try (RepositoryConnection conn = db.getConnection()) {
			
			// Add the model to database
			conn.add(model);
		
		}
			
		catch (RDF4JException e) {
			   // handle exception. This catch-clause is
			   // optional since RDF4JException is an unchecked exception
			}
		
		finally {
			// shut down the database
			db.shutDown();
			
		}
		
	}
	
}
