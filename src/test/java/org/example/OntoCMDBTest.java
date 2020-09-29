package org.example;

import static org.junit.Assert.assertEquals;
import java.io.File;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.junit.jupiter.api.Test;

class OntoCMDBTest {
    
	File dataDir = new File("C:/Users/Default User.Lenovo/Documents/yamk/YAMK/THESIS/datadir");
	String index = "spoc,posc,cosp";
	Repository db = new SailRepository(new NativeStore(dataDir, index));
	
	@Test
	public void testCountofClasses() throws Exception {      

		try (RepositoryConnection conn = db.getConnection()) {

			String query = "PREFIX : <http://www.semanticweb.org/defaultuser/ontologies/2020/7/Onto-CMDB#> \n"
		            +  "SELECT DISTINCT ?s  WHERE {?s a owl:Class} \n";	
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL,query);
	
			try (TupleQueryResult res = tupleQuery.evaluate()) {
				int count = 0;
		         while (res.hasNext()) {
					BindingSet solution = res.next();
						System.out.println("?s = " + solution.getValue("s"));
						count++;
		         }
		         assertEquals(9, count);
			} finally {
			conn.close();
			db.shutDown();
			}
		}
	}
	
	@Test
	public void testCountofFRU() throws Exception {

		try (RepositoryConnection conn = db.getConnection()) {

			String query = "PREFIX : <http://www.semanticweb.org/defaultuser/ontologies/2020/7/Onto-CMDB#> \n"
		            +  "SELECT *  WHERE {?s ?y :MEM1800-64CF} \n";	
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL,query);
			 
			try (TupleQueryResult res = tupleQuery.evaluate()) {
				int count = 0;
		        while (res.hasNext()) {
		        	 BindingSet solution = res.next();
		        	 System.out.println("?s = " + solution.getValue("s"));
		        	 count++;
		    }
		         assertEquals(2, count);
			} finally {
			conn.close();
			db.shutDown();
			}
		}
	}
	
	@Test
	public void testCountofProduct() throws Exception {

		try (RepositoryConnection conn = db.getConnection()) {

			String query = "PREFIX : <http://www.semanticweb.org/defaultuser/ontologies/2020/7/Onto-CMDB#> \n"
		            +  "SELECT ?s  WHERE {?s  rdf:type :Product } \n";	
			TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL,query);
 
			try (TupleQueryResult res = tupleQuery.evaluate()) {
				int count = 0;
		        while (res.hasNext()) {
		        	 BindingSet solution = res.next();
		        	 System.out.println("?s = " + solution.getValue("s"));
		        	 count++;
		        }
		         assertEquals(3, count);
			} finally {
			conn.close();
			db.shutDown();
			}
		}
	}
	
	@Test
	public void testIfhasLocation() throws Exception {

		try (RepositoryConnection conn = db.getConnection()) {

			String query = "PREFIX : <http://www.semanticweb.org/defaultuser/ontologies/2020/7/Onto-CMDB#> \n"
		            +  "ASK { :CISCO1841_001 :hasLocation :M1_Riihimaki } \n";	
			BooleanQuery booleanQuery = conn.prepareBooleanQuery(QueryLanguage.SPARQL,query);
			
			assert(booleanQuery.evaluate());

			conn.close();
			db.shutDown();
		}
	}
	
	@Test
	public void testIfhasSWIDandProductTitle() throws Exception {

		try (RepositoryConnection conn = db.getConnection()) {

			String query = "PREFIX : <http://www.semanticweb.org/defaultuser/ontologies/2020/7/Onto-CMDB#> \n"
		            +  "ASK {  :CISCO_IOS_12_4 :hasSWID :Cisco_IOS .  :Cisco_IOS :ProductTitle \"Cisco IOS\"^^xsd:string } \n";	
			BooleanQuery booleanQuery = conn.prepareBooleanQuery(QueryLanguage.SPARQL,query);
			
			assert(booleanQuery.evaluate());

			conn.close();
			db.shutDown();
		}
	}
	
	@Test
	public void testIfhasPhysicalElement() throws Exception {

		try (RepositoryConnection conn = db.getConnection()) {

			String query = "PREFIX : <http://www.semanticweb.org/defaultuser/ontologies/2020/7/Onto-CMDB#> \n"
		            +  "ASK { :CISCO1841_002 :hasPhysicalElement :Cisco_1841_chassis . } \n";	
			BooleanQuery booleanQuery = conn.prepareBooleanQuery(QueryLanguage.SPARQL,query);
			
			assert(booleanQuery.evaluate());

			conn.close();
			db.shutDown();
		}
	}
}
