package com.maxdemarzi.pagerank;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.api.exceptions.legacyindex.AutoIndexingKernelException;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.kernel.api.DataWriteOperations;
import org.neo4j.kernel.api.exceptions.EntityNotFoundException;
import org.neo4j.kernel.api.exceptions.InvalidTransactionTypeKernelException;
import org.neo4j.kernel.api.exceptions.schema.ConstraintValidationKernelException;
import org.neo4j.kernel.api.exceptions.schema.IllegalTokenNameException;
import org.neo4j.kernel.api.properties.DefinedProperty;
import org.neo4j.kernel.impl.core.ThreadToStatementContextBridge;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Path("/service")
public class Service {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final int WRITE_BATCH = 10_000;
    public static final int CPUS = Runtime.getRuntime().availableProcessors();
    static ExecutorService pool = Utils.createPool(CPUS, CPUS*25);


    @GET
    @Path("/pagerank")
    public Response pageRank(@DefaultValue("20") @QueryParam("iterations") int iterations,
                           @Context GraphDatabaseService db) throws IOException {

        PageRankArrayStorageParallelSPI pageRank = new PageRankArrayStorageParallelSPI(db, pool);
        pageRank.compute(iterations);
        writeBackResults(db, pageRank);

        Map<String, String> results = new HashMap<String, String>() {{
            put("results", "PageRank Completed!");
        }};
        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();

    }


    public void writeBackResults(final GraphDatabaseService db, final Algorithm algorithm) {
        final ThreadToStatementContextBridge ctx = ((GraphDatabaseAPI)db).getDependencyResolver().resolveDependency(ThreadToStatementContextBridge.class);
        final int propertyNameId = getPropertyNameId(db, algorithm, ctx);
        final long nodes = algorithm.numberOfNodes();
        int batches = (int) nodes / WRITE_BATCH;
        List<Future> futures = new ArrayList<>(batches);
        for (int node = 0; node < nodes; node += WRITE_BATCH) {
            final int start = node;
            Future future = pool.submit(new Runnable() {
                public void run() {
                    try (Transaction tx = db.beginTx()) {
                        DataWriteOperations ops = ctx.get().dataWriteOperations();
                        for (long i = 0; i < WRITE_BATCH; i++) {
                            long node = i + start;
                            if (node >= nodes) break;
                            double value = algorithm.getResult(node);
                            if (value > 0) {
                                ops.nodeSetProperty(node, DefinedProperty.doubleProperty(propertyNameId, value));
                            }
                        }
                        tx.success();
                    } catch (AutoIndexingKernelException | ConstraintValidationKernelException | InvalidTransactionTypeKernelException | EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            futures.add(future);
        }
        Utils.waitForTasks(futures);
    }

    private int getPropertyNameId(GraphDatabaseService db, Algorithm algorithm, ThreadToStatementContextBridge ctx) {
        int propertyNameId;
        try (Transaction tx = db.beginTx()) {
            propertyNameId = ctx.get().tokenWriteOperations().propertyKeyGetOrCreateForName(algorithm.getPropertyName());
            tx.success();
        } catch (IllegalTokenNameException e) {
            throw new RuntimeException(e);
        }
        return propertyNameId;
    }
}
