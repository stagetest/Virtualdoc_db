# import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

public class VI {
	 private static final String DB_PATH = "target/neo4j-VI-db";

	    public String diagnosis;

	    public GraphDatabaseService graphDb;
	    Node[] disease= new Node[10];
	    Node[][]symptom= new Node[10][5];
	    Relationship relationship;
	    String util ;

		private String rows;

	    private static enum RelTypes implements RelationshipType
	    {
	        causes
	    }

	    public static void main( final String[] args ) throws IOException
	    {
	        VI doctor = new VI();
	        doctor.createDb();
	    }

	    @SuppressWarnings("deprecation")
		void createDb() throws IOException
	    {
	        FileUtils.deleteRecursively( new File( DB_PATH ) );

	        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
	        registerShutdownHook( graphDb );

	        try ( Transaction tx = graphDb.beginTx() )
	        {
		       
		        
		        int i=0,j=0 , l=0, k=0;
		        for (i=0;i<2;i++)
		        {
		        disease[i] = graphDb.createNode();
		        disease[i].setProperty( "disease", data.remplissage(i).disease);
		        j=0;
		        while (data.remplissage(i).symptom[j]!=null)
		        {
		        	for( k=0; k<i; k++)
		        		for( l=0; l<j; l++)
		        		{
		        			if (data.remplissage(i).symptom[j]!= data.remplissage(k).symptom[l]) ;
		        			else 
		        			{
		    			    	relationship = disease[i].createRelationshipTo( symptom[k][l], RelTypes.causes );
		    			    	relationship.setProperty( "message", "causes " );
		    			    	
		    			        System.out.print( symptom[k][l].getProperty( "symptom" )+" " );
		    			        System.out.print( relationship.getProperty( "message" ) );
		    			        System.out.print( disease[i].getProperty( "disease" ) +"\n");
		    			        l=j+1;
		    			        j++;
		        				
		        			}
		        		}
		        	if(l!=j+1)
		        	{symptom[i][j] = graphDb.createNode();
			    	symptom[i][j].setProperty( "symptom", data.remplissage(i).symptom[j]);
	
			    	relationship = disease[i].createRelationshipTo( symptom[i][j], RelTypes.causes );
			    	relationship.setProperty( "message", "causes " );
			    	util = data.remplissage(i).symptom[j];
			    	
			        System.out.print( symptom[i][j].getProperty( "symptom" )+" " );
			        System.out.print( relationship.getProperty( "message" ) );
			        System.out.print( disease[i].getProperty( "disease" ) +"\n");
			        j++;
		        	}
		        }
		        }
	
		        //diagnosis = ( (String) symptom1.getProperty( "symptom" ) )
		          //             + ( (String) relationship.getProperty( "message" ) )
		            //           + ( (String) disease1.getProperty( "disease" ) );
			    
		        Map<String, Object> params = new HashMap<String, Object>();
			    params.put( "symptom", "high temperature" );
			    //String query = "MATCH n WHERE ({ symptom:'high temperature' })-->(n) RETURN n";
			    //String suppress_duplicate = "MATCH (n:symptom) WITH n.symptom AS symptom , collect(n) AS nodes WHERE size(nodes) > 1 FOREACH (n in tail(nodes) | DELETE n)";
			    String query = "MATCH (n) WHERE  ({symptom: 'high temperature'}) -[:causes]- (n) RETURN n";
			    //String query = "MATCH (n) RETURN n";
			    Result result = graphDb.execute( query );
			    System.out.print( result.resultAsString());
	
	            tx.success();
	      
		        
	        }

		  }
	
		    private static void registerShutdownHook( final GraphDatabaseService graphDb )
		    {
		   
		        Runtime.getRuntime().addShutdownHook( new Thread()
		        {
		            @Override
		            public void run()
		            {
		                graphDb.shutdown();
		            }
		        } );
		    }
		}
