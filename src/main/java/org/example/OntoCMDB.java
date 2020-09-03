package org.example;
//package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
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
			rep.shutDown();
		}
		
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
			}
		}
		finally {
			// before our program exits, make sure the database is properly shut down.
			db.shutDown();
		}
		
		}
	
		

}
