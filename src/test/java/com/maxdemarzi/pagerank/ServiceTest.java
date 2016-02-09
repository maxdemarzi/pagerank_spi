package com.maxdemarzi.pagerank;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class ServiceTest {
    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withExtension("/v1", Service.class);

    @Test
    public void shouldRespondToPagerankCompany() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/service/pagerank").toString());
        HashMap actual = response.content();
        assertTrue(actual.equals(expected));
    }

    private static final HashMap expected = new HashMap<String, Object>() {{
        put("results", "PageRank Completed!");
    }};
}
