package org.gilmour.coprocessor;

public class testMain {
    public static void main(String[] args) throws Exception{
        GraphCacheObserver observer = new GraphCacheObserver();
        observer.preBatchMutate(null,null);
        System.in.read();
    }
}
