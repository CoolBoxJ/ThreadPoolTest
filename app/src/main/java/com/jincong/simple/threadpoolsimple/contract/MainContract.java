package com.jincong.simple.threadpoolsimple.contract;

/*
 * @author jc on 2019/02/22
 */
import com.jincong.simple.threadpoolsimple.base.BasePresenter;
import com.jincong.simple.threadpoolsimple.base.BaseView;

public interface MainContract {
    interface IMainView extends BaseView {

    }


    abstract class AbstractHomePresenter extends BasePresenter<IMainView> {
        public abstract Runnable startThread();
        public abstract boolean endThread(Runnable a);
        public abstract int getMaxThreadNum();
        public abstract void endAllThread();
        public abstract void startLogThread();
    }
}
