package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Literals;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

class OntoCMDBTest {
    //Connection connection;
	File dataDir = new File("C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/datadir");
	String indexes = "spoc,posc,cosp";
	Repository db = new SailRepository(new NativeStore(dataDir, indexes));
	//db.init();

    @Before
    public void before() throws FileNotFoundException {
		File dataDir = new File("C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/datadir");
		String indexes = "spoc,posc,cosp";
		String filename = "C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/Onto-CMDB.ttl";
		
		InputStream input = null;
		input = new FileInputStream(filename);
			
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
		
		Repository db = new SailRepository(new NativeStore(dataDir, indexes));
		db.init();
        //connection = SybaseDBConnection.getConnection("ase");
		try (RepositoryConnection conn = db.getConnection()) {
			// add the model
			conn.add(model);}
    }

    @After
    public void after() {
    	db.shutDown();
    }
    
	@Test
	public void testAssert() throws Exception {
		/*File dataDir = new File("C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/datadir");
		String indexes = "spoc,posc,cosp";
		String filename = "C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/Onto-CMDB.ttl";
		
		InputStream input = null;
		input = new FileInputStream(filename);
			
		Model model = null;
		model = Rio.parse(input, "", RDFFormat.TURTLE);
		
		Repository db = new SailRepository(new NativeStore(dataDir, indexes));
		db.init();
		//ValueFactory f = db.getValueFactory();
	*/
		//update.append("INSERT { ?x rdfs:label ?y . } WHERE { ?x rdfs:label ?y }");
		try (RepositoryConnection conn = db.getConnection()) {
			// add the model
			//conn.add(model);

			String queryString = "SELECT ?subject ?object WHERE {?subject rdfs:subClassOf ?object } ";	
			TupleQuery tupleQuery = conn.prepareTupleQuery(queryString);
			
			BindingSet bindingSet = tupleQuery.getBindings();
			//Value valueOfD = bindingSet.getValue("ManagedElement");	   
			assertEquals("ManagedElement", bindingSet.getValue("ManagedElement"));
			//assertEquals("ManagedElement", Literals.getLabel(ResBingSet.getValue("scount"))
			
			//System.out.println("db: " + valueOfD);
			/*
			try (TupleQueryResult res = tupleQuery.evaluate()) {
					      while (res.hasNext()) {  // iterate over the result
					         BindingSet bindingSet = res.next();
					         Value valueOfX = bindingSet.getValue("subject");
					         Value valueOfY = bindingSet.getValue("object");
					         System.out.println("db: " + valueOfX +" "+ valueOfY);
					         
					      }
					      
					      
					   }
	*/
			

		
	}
	}
}
