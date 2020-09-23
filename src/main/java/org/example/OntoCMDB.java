package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.Literals;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;

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
			// shut down the database properly.
			db.shutDown();
			
		}
		
	}
	
}
