Custom Page Rank
================

This is an unmanaged extension running a Custom Pagerank algorithm on Companies with a SIMILAR relationship score >= 0.80.


# Quick Start

1. Build it:

        mvn clean package

2. Copy target/pagerank-1.0.jar to the plugins/ directory of your Neo4j 2.3.0-M03 server.

        mv target/pagerank-1.0.jar neo4j/plugins/.

3. Configure Neo4j by adding a line to conf/neo4j-server.properties:

        org.neo4j.server.thirdparty_jaxrs_classes=com.maxdemarzi.pagerank=/v1
        
If you need to configure 2 extensions at the same time, use a comma between them:
        
        org.neo4j.server.thirdparty_jaxrs_classes=com.maxdemarzi.recruit=/v1,com.maxdemarzi.pagerank=/analytics

4. Start Neo4j server.

5. Create sample dataset:

        CREATE (a:Company {name:'a'})
        CREATE (b:Company {name:'b'})
        CREATE (c:Company {name:'c'})
        CREATE (d:Company {name:'d'})
        CREATE (e:Company {name:'e'})
        CREATE (f:Company {name:'f'})
        CREATE (g:Company {name:'g'})
        CREATE (h:Company {name:'h'})
        CREATE (i:Company {name:'i'})
        CREATE (j:Company {name:'j'})
        CREATE (k:Company {name:'k'})
        CREATE
          (b)-[:SIMILAR]->(c),
          (c)-[:SIMILAR]->(b),
          (d)-[:SIMILAR]->(a),
          (e)-[:SIMILAR]->(b),
          (e)-[:SIMILAR]->(d),
          (e)-[:SIMILAR]->(f),
          (f)-[:SIMILAR]->(b),
          (f)-[:SIMILAR]->(e),
          (g)-[:SIMILAR]->(b),
          (g)-[:SIMILAR]->(e),
          (h)-[:SIMILAR]->(b),
          (h)-[:SIMILAR]->(e),
          (i)-[:SIMILAR]->(b),
          (i)-[:SIMILAR]->(e),
          (j)-[:SIMILAR]->(e),
          (k)-[:SIMILAR]->(e);

6. Call the pagerank endpoint:

        :GET /v1/service/pagerank
        
You should see "PageRank for Company using SIMILAR relationship type Completed!"

7. Check the pageranks of some nodes:

        MATCH (n:Company) RETURN n.name, n.pagerank LIMIT 25;