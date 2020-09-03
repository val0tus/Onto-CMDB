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
import org.eclipse.rdf4j.sparqlbuilder.core.Prefix;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.ConstructQuery;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.core.query.SelectQuery;
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
			Model modelx = QueryResults.asModel(statements);
			modelx.setNamespace(RDF.NS);
			modelx.setNamespace(RDFS.NS);
			modelx.setNamespace(FOAF.NS); 
			modelx.setNamespace("ex", namespace);
			Rio.write(modelx, System.out, RDFFormat.TURTLE);

	
	
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
		
		/*
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
		
		*/

		/* Using Repositories.consume(), we do not explicitly begin or 
		 * commit a transaction. We don’t even open and close a connection explicitly –
		 *  this is all handled internally. The method also ensures that the transaction is rolled back 
		 *  if an exception occurs */
		//ValueFactory f = rep.getValueFactory();
		//IRI bob = f1.createIRI("http://example.org/bob");
		Repositories.consume(rep, conn -> {
		  //conn.add(bob, RDF.TYPE, FOAF.PERSON);
		  conn.add(john, RDFS.LABEL, f.createLiteral("Bob"));
		  conn.add(john, RDFS.LABEL, f.createLiteral("Alice"));
		  conn.add(john, RDFS.LABEL, f.createLiteral("Ted"));
	
		
		  /*RepositoryResult<Statement> statements = conn.getStatements(null, null, null);
		  Model model2 = QueryResults.asModel(statements);*/
		  
		  
		  	
			
		});
		
		rep.shutDown();
		//db.shutDown();
		}
	
	

}
