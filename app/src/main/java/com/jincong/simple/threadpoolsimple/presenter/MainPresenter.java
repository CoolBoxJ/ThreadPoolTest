package com.jincong.simple.threadpoolsimple.presenter;

import android.content.Context;

import com.jincong.simple.threadpoolsimple.contract.MainContract;
import com.jincong.simple.threadpoolsimple.model.IMainModel;
import com.jincong.simple.threadpoolsimple.model.impl.MainModelImpl;


public class MainPresenter extends MainContract.AbstractHomePresenter {
    private Context mContext;

    private IMainModel mModel = new MainModelImpl();

    public MainPresenter(Context mContext) {

        this.mContext = mContext;
    }

    @Override
    public Runnable startThread() {
        return mModel.startThread();
    }

    @Override
    public boolean endThread(Runnable a) {
        return mModel.endThread(a);
    }

    @Override
    public int getMaxThreadNum() {
        return mModel.getMaxThreadNum();
    }

    @Override
    public void endAllThread() {
        mModel.shutDownNow();
    }

    @Override
    public void startLogThread() {
        mModel.startLogThread();
    }
}
