package com.maxdemarzi.pagerank;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;

public class PageRankTest {
    private static GraphDatabaseService db;
    private static Service service;
    public static final int CPUS = Runtime.getRuntime().availableProcessors();
    static ExecutorService pool = Utils.createPool(CPUS, CPUS * 25);
    public static final double EXPECTED = 2.87711;

    @BeforeClass
    public static void setUp() {
        db = new TestGraphDatabaseFactory().newImpermanentDatabase();
        service = new Service();
        populateDb(db);
    }

    private static void populateDb(GraphDatabaseService db) {
        try ( Transaction tx = db.beginTx()) {
            db.execute(TestObjects.COMPANIES_QUERY);
            tx.success();
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        db.shutdown();
    }

    @Test
    public void shouldGetPageRankArrayStorageSPI() throws IOException {
        PageRank pageRank = new PageRankArrayStorageParallelSPI(db, pool);
        pageRank.compute(20);
        long id = (long) getEntry("b").get("id");
        assertEquals(EXPECTED, pageRank.getResult(id),0.1D);

        //printAllEntries();

    }

    private Map<String, Object> getEntry(String name) {
        try (Result result = db.execute(TestObjects.COMPANY_RESULT_QUERY, Collections.<String, Object>singletonMap("name", name))) {
            return result.next();
        }
    }

    private void printAllEntries() {
        try (Result result = db.execute(TestObjects.COMPANIES_RESULT_QUERY)) {
            Map<String, Object> company;
            while (result.hasNext()) {
                company=  result.next();
                System.out.println(company);
            }
        }
    }


}
