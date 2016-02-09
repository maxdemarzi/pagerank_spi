package com.maxdemarzi.pagerank;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.impl.store.NeoStores;

public class NodeCounter {
    public int getNodeCount(GraphDatabaseService db) {
        NeoStores neoStore = ((GraphDatabaseAPI) db).getDependencyResolver().resolveDependency(NeoStores.class);
        return (int) neoStore.getNodeStore().getHighId();
    }
    public int getRelationshipCount(GraphDatabaseService db) {
        NeoStores neoStore = ((GraphDatabaseAPI) db).getDependencyResolver().resolveDependency(NeoStores.class);
        return (int) neoStore.getRelationshipStore().getHighId();
    }
}