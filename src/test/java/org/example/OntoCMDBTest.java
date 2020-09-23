package org.example;

import java.io.File;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.junit.After;
import org.junit.jupiter.api.Test;

class OntoCMDBTest {
    
	File dataDir = new File("C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/datadir");
	String index = "spoc,posc,cosp";
	Repository db = new SailRepository(new NativeStore(dataDir, index));
	
    @After
    public void after() {
    			
    	db.shutDown();
    }
    
	@Test
	public void testClasses() throws Exception {

		try (RepositoryConnection conn = db.getConnection()) {

			String queryString = "PREFIX : <http://www.semanticweb.org/defaultuser/ontologies/2020/7/Onto-CMDB#> \n"
		            +  "SELECT *  WHERE {?s ?y :MEM1800-64CF} \n";	
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL,queryString);
	
			try (TupleQueryResult res = tupleQuery.evaluate()) {
		         while (res.hasNext()) {
					BindingSet solution = res.next();
						System.out.println("?s = " + solution.getValue("s"));
						System.out.println("?y = " + solution.getValue("y"));
		         }
			}
			conn.close();
			db.shutDown();
		}
	}
	
	@Test
	public void testCountofFRU() throws Exception {

		try (RepositoryConnection conn = db.getConnection()) {

			String queryString = "PREFIX : <http://www.semanticweb.org/defaultuser/ontologies/2020/7/Onto-CMDB#> \n"
		            +  "SELECT *  WHERE {?s ?y :MEM1800-64CF} \n";	
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL,queryString);
			 
	
			try (TupleQueryResult res = tupleQuery.evaluate()) {
				int count = 0;
		         while (res.hasNext()) {
									BindingSet solution = res.next();
									System.out.println("?s = " + solution.getValue("s"));
									System.out.println("?y = " + solution.getValue("y"));
									  
									//res.next();
								      count++;
								      System.out.println(count);
				}
		         
			      
			}
			conn.close();
			db.shutDown();
		}
	}
}
