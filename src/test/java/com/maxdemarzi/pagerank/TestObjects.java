package com.maxdemarzi.pagerank;

public class TestObjects {

    public static final String COMPANY_RESULT_QUERY = "MATCH (c:Company) " +
            "WHERE c.name = {name} " +
            "RETURN id(c) AS id, " +
            "c.pagerank AS pagerank";

    public static final String COMPANIES_RESULT_QUERY = "MATCH (c:Company) " +
            "RETURN c.name AS name, " +
            "c.pagerank AS pagerank";

    public static final String COMPANIES_QUERY = "CREATE (a:Company {name:'a'})\n" +
            "CREATE (b:Company {name:'b'})\n" +
            "CREATE (c:Company {name:'c'})\n" +
            "CREATE (d:Company {name:'d'})\n" +
            "CREATE (e:Company {name:'e'})\n" +
            "CREATE (f:Company {name:'f'})\n" +
            "CREATE (g:Company {name:'g'})\n" +
            "CREATE (h:Company {name:'h'})\n" +
            "CREATE (i:Company {name:'i'})\n" +
            "CREATE (j:Company {name:'j'})\n" +
            "CREATE (k:Company {name:'k'})\n" +

            "CREATE\n" +
            "  (b)-[:SIMILAR {score:0.80}]->(c),\n" +
            "  (c)-[:SIMILAR {score:0.80}]->(b),\n" +
            "  (d)-[:SIMILAR {score:0.80}]->(a),\n" +
            "  (e)-[:SIMILAR {score:0.80}]->(b),\n" +
            "  (e)-[:SIMILAR {score:0.80}]->(d),\n" +
            "  (e)-[:SIMILAR {score:0.80}]->(f),\n" +
            "  (f)-[:SIMILAR {score:0.80}]->(b),\n" +
            "  (f)-[:SIMILAR {score:0.80}]->(e),\n" +
            "  (g)-[:SIMILAR {score:0.80}]->(b),\n" +
            "  (g)-[:SIMILAR {score:0.80}]->(e),\n" +
            "  (h)-[:SIMILAR {score:0.80}]->(b),\n" +
            "  (h)-[:SIMILAR {score:0.80}]->(e),\n" +
            "  (i)-[:SIMILAR {score:0.80}]->(b),\n" +
            "  (i)-[:SIMILAR {score:0.80}]->(e),\n" +
            "  (j)-[:SIMILAR {score:0.80}]->(e),\n" +
            "  (k)-[:SIMILAR {score:0.80}]->(e)\n";

}
