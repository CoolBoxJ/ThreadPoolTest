package com.jincong.simple.threadpoolsimple.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jincong.simple.threadpoolsimple.R;
import com.jincong.simple.threadpoolsimple.base.BaseActivity;
import com.jincong.simple.threadpoolsimple.contract.MainContract;
import com.jincong.simple.threadpoolsimple.presenter.MainPresenter;
import com.jincong.simple.threadpoolsimple.ui.adapter.ThreadListAdapter;

public class MainActivity extends BaseActivity<MainContract.IMainView, MainPresenter> implements MainContract.IMainView{


    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        RecyclerView threadListView = (RecyclerView) findViewById(R.id.listThread);
        final ThreadListAdapter adapter = new ThreadListAdapter(mPresenter);
        threadListView.setLayoutManager(new LinearLayoutManager(this));
        threadListView.setAdapter(adapter);
        Button startButton = (Button) findViewById(R.id.btStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() < mPresenter.getMaxThreadNum()) {
                    adapter.addThread(mPresenter.startThread());
                } else {
                    showToast("线程池已满");
                }
            }
        });
        Button endButton = (Button) findViewById(R.id.btEnd);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.endAllThread();
            }
        });
    }

    @Override
    protected void initData() {
        checkPermission();
        mPresenter.startLogThread();
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(getApplicationContext());
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .READ_EXTERNAL_STORAGE)) {
                showToast("请开通相关权限，否则无法正常使用本应用！");
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10001);

        } else {
            mPresenter.startLogThread();
        }
    }
}
