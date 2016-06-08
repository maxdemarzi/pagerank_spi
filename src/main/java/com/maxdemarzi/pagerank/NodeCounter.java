package com.maxdemarzi.pagerank;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;

import java.util.Map;

public class NodeCounter {
    public int getNodeCount(GraphDatabaseService db) {
        Result result = db.execute( "CYPHER runtime=compiled MATCH (n) RETURN max(id(n)) AS maxId" );
        Map response = result.next();
        if (response.get("maxId") != null) {
            return ((Number) response.get("maxId")).intValue() + 1;
        } else {
            return 1;
        }
    }

    public int getRelationshipCount(GraphDatabaseService db) {
        Result result = db.execute( "CYPHER runtime=compiled MATCH ()-[r]->() RETURN max(id(r)) AS maxId" );
        Map response = result.next();
        if (response.get("maxId") != null) {
            return ((Number) response.get("maxId")).intValue() + 1;
        } else {
            return 1;
        }

    }
}