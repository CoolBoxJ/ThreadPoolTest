package com.jincong.simple.threadpoolsimple.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/*
 * @author jc on 2019/02/22
 */
public abstract class BasePresenter<T> {
    private Reference<T> mViewRef;

    public void attachView(T mView) {
        mViewRef = new WeakReference<>(mView);
    }

    protected T getView() {
        if(isViewAttached()){
            return mViewRef.get();
        }else {
            return null;
        }

    }

    private boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {

        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
