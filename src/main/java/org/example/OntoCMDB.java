package org.example;
//package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;




public class OntoCMDB {

	public static void main(String[] args) {
		File dataDir = new File("C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/datadir");
		String indexes = "spoc,posc,cosp";
		Repository rep = new SailRepository(new NativeStore(dataDir, indexes));
		
		rep.init();
		String namespace = "http://example.org/"; 
		ValueFactory f = rep.getValueFactory();
		IRI john = f.createIRI(namespace, "john");
		try (RepositoryConnection conn = rep.getConnection()) {
			conn.add(john, RDF.TYPE, FOAF.PERSON); 
			conn.add(john, RDFS.LABEL, f.createLiteral("John"));
			
			RepositoryResult<Statement> statements = conn.getStatements(null, null, null);
			Model model = QueryResults.asModel(statements);
			Rio.write(model, System.out, RDFFormat.TURTLE);
			model.setNamespace(RDF.NS);
			model.setNamespace(RDFS.NS);
			model.setNamespace(FOAF.NS); 
			model.setNamespace("ex", namespace);
			Rio.write(model, System.out, RDFFormat.TURTLE);
			//rep.shutDown();
		}
		/*
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		OWLOntology ontology = null;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File("C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/Onto-CMDB.ttl"));
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		System.out.println("Ontology : " + ontology.getOntologyID());
		System.out.println("Format      : " + manager.getOntologyFormat(ontology)); 
		
		*/
		String filename = "C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/Onto-CMDB.ttl";
		
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
		
		Repository db = new SailRepository(new MemoryStore());
		db.init();
		
		try (RepositoryConnection conn = db.getConnection()) {
			// add the model
			conn.add(model);

			// let's check that our data is actually in the database
			try (RepositoryResult<Statement> result = conn.getStatements(null, null, null);) {
				while (result.hasNext()) {
					Statement st = result.next();
					System.out.println("db contains: " + st);
				}
				/*
				String queryString = "SELECT ?x ?y WHERE { ?x ?p ?y } ";	
				TupleQuery tupleQuery = conn.prepareTupleQuery(queryString);
				   try (TupleQueryResult res = tupleQuery.evaluate()) {
					      while (res.hasNext()) {  // iterate over the result
					         BindingSet bindingSet = res.next();
					         Value valueOfX = bindingSet.getValue("x");
					         Value valueOfY = bindingSet.getValue("y");
					         System.out.println("db: " + valueOfX + valueOfY);
					      }
					   }
				*/
			}
			
	
		}
		catch (RDF4JException e) {
			   // handle exception. This catch-clause is
			   // optional since RDF4JException is an unchecked exception
			}
		
		
		finally {
			// before our program exits, make sure the database is properly shut down.
			db.shutDown();
			
		}
		/*
		File inputFile1 = new File("/path/to/example1.rdf");
		String baseURI1 = "http://example.org/example1/";
		File inputFile2 = new File("/path/to/example2.rdf");
		String baseURI2 = "http://example.org/example2/";
		
		try (RepositoryConnection con = db.getConnection()) {
			   // start a transaction
			   con.begin();
			   try {
			      // Add the first file
			      try {
					con.add(inputFile1, baseURI1, RDFFormat.RDFXML);
				} catch (RDFParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			      // Add the second file
			      try {
					con.add(inputFile2, baseURI2, RDFFormat.RDFXML);
				} catch (RDFParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			      // If everything went as planned, we can commit the result
			      con.commit();
			   }
			   catch (RepositoryException e) {
			      // Something went wrong during the transaction, so we roll it back
			      con.rollback();
			   }
			}
		*/
		/* As you can see, using Repositories.consume(), we do not explicitly begin or 
		 * commit a transaction. We don’t even open and close a connection explicitly –
		 *  this is all handled internally. The method also ensures that the transaction is rolled back 
		 *  if an exception occurs */
		ValueFactory f1 = rep.getValueFactory();
		IRI bob = f1.createIRI("urn:bob");
		Repositories.consume(rep, conn -> {
		  conn.add(bob, RDF.TYPE, FOAF.PERSON);
		  conn.add(bob, RDFS.LABEL, f.createLiteral("Bob"));
		  RepositoryResult<Statement> statements = conn.getStatements(null, null, null);
			Model model2 = QueryResults.asModel(statements);
			Rio.write(model2, System.out, RDFFormat.TURTLE);
		  
			
			
		});

		}
	
		

}
