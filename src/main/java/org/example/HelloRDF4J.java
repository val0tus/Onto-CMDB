package org.example;

import java.io.File;

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
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class HelloRDF4J {

	public static void main(String[] args) {
		Repository rep = new SailRepository(new MemoryStore());
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
		}
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		OWLOntology ontology = null;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File("C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/Onto-CMDB.owl"));
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		System.out.println("Ontology : " + ontology.getOntologyID()); 
		System.out.println("Format      : " + manager.getOntologyFormat(ontology)); 

	}

}
