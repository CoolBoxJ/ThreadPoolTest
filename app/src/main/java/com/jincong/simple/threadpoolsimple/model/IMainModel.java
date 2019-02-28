package com.jincong.simple.threadpoolsimple.model;

public interface IMainModel {
    Runnable startThread();
    boolean endThread(Runnable a);
    int getMaxThreadNum();
    void shutDownNow();
    void startLogThread();
}
