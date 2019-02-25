package com.jincong.simple.threadpoolsimple.model;

import java.util.concurrent.Future;

public interface IMainModel {
    Future startThread();
    boolean endThread(Future a);
    int getMaxThreadNum();
    void shutDownNow();
    void startLogThread();
}
