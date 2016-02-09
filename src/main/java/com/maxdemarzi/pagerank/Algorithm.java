package com.maxdemarzi.pagerank;

public abstract interface Algorithm {

    public abstract void compute(int iterations);
    public abstract double getResult(long node);
    public abstract long numberOfNodes();
    public abstract String getPropertyName();

}