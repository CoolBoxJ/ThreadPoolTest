package com.jincong.simple.threadpoolsimple.base;

/*
 * @author jc on 2019/02/22
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(setLayoutId());

        mPresenter = createPresenter();

        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }

        initView();

        initData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
        }

    }

    /**
     * 设置资源ID
     *
     */
    protected abstract int setLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 创建Presenter
     */
    protected abstract T createPresenter();
}
