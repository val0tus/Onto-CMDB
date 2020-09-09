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
		File dataDir = new File("C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/datadir");
		String indexes = "spoc,posc,cosp";
		Repository db = new SailRepository(new NativeStore(dataDir, indexes));
		

		
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
		
		db.init();
		
		try (RepositoryConnection conn = db.getConnection()) {
			// add the model
			conn.add(model);

			String queryString = "SELECT ?subject ?object WHERE {?subject rdfs:subClassOf ?object } ";	
			TupleQuery tupleQuery = conn.prepareTupleQuery(queryString);
				   try (TupleQueryResult res = tupleQuery.evaluate()) {
					      while (res.hasNext()) {  // iterate over the result
					         BindingSet bindingSet = res.next();
					         Value valueOfX = bindingSet.getValue("subject");
					         Value valueOfY = bindingSet.getValue("object");
					         System.out.println("db: " + valueOfX +" "+ valueOfY);
					         
					      }
					      
					   }
			String CheckSparql = "SELECT (count(?s) as ?scount) where { ?s ?p ?o . }";
			//TupleQuery tupleQuery2 = conn.prepareTupleQuery(CheckSparql);

			TupleQuery PreparedSparql = conn.prepareTupleQuery(QueryLanguage.SPARQL, CheckSparql);
			TupleQueryResult Result = PreparedSparql.evaluate();
			BindingSet ResBindSet = Result.next();
			int TripleCount = Literals.getIntValue(ResBindSet.getValue("Firmware"), 2);
		
			System.out.println(TripleCount);
			
			ValueFactory vf = SimpleValueFactory.getInstance();
			
			ModelBuilder builder = new ModelBuilder();
			Model model1 = builder
			                  .setNamespace("ex", "http://www.semanticweb.org/defaultuser/ontologies/2020/7/Onto-CMDB")
					  .subject("ex:ManagedElement")
					       //.add(RDF.TYPE, "ex:Name")
					       .add(RDF.VALUE, "Cisco")
					  .build();
			conn.add(model1);
			
			Model model2 = builder
					     .setNamespace("ex", "http://www.semanticweb.org/defaultuser/ontologies/2020/7/Onto-CMDB/SoftwareIdentity")
					     .subject("ex:linux")
					 	// In English, this painting is called "The Potato Eaters"
					 	.add(DC.NAMESPACE, vf.createLiteral("The Potato Eaters"))
					 	// In Dutch, it's called "De Aardappeleters"
					 	//.add(DC.TITLE,  vf.createLiteral("De Aardappeleters", "nl"))
					     .build();
			conn.add(model2);
			
			try (RepositoryResult<Statement> result = conn.getStatements(null, null, null);) {
					for (Statement st: result) {
						System.out.println("db contains: " + st);
						Rio.write(model2, System.out, RDFFormat.TURTLE);
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
		
		}
	
	

}
