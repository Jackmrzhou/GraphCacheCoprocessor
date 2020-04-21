package org.gilmour.coprocessor.utils;

import com.google.common.util.concurrent.ListenableFuture;

public class WrapFuture<T> {
    public ListenableFuture<T> future;
    private boolean finished = false;
    public boolean destroyable() {
        return finished;
    }

    public void setFinished(boolean b) {
        finished = b;
    }
}
