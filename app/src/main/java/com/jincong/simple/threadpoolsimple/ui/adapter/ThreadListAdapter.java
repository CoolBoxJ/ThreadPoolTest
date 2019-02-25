package com.jincong.simple.threadpoolsimple.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jincong.simple.threadpoolsimple.R;
import com.jincong.simple.threadpoolsimple.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.concurrent.Future;

public class ThreadListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Future> mThreadList;
    private MainPresenter mPresenter;

    public ThreadListAdapter(MainPresenter presenter) {
        mThreadList = new ArrayList<>();
        mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thread, parent, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int ps = position;
        ((ListItemViewHolder) holder).bind(mThreadList.get(position), new callBack() {
            @Override
            public void onEvent(Future a) {
                if (mPresenter.endThread(mThreadList.get(ps))) {
                    mThreadList.remove(a);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mThreadList.size();
    }

    public interface callBack {
        void onEvent(Future a);
    }

    public void addThread(Future a) {
        if (a != null) {
            mThreadList.add(a);
            notifyDataSetChanged();
        }
    }

    public static class ListItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mThreadName;
        private Button mEndThread;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            mThreadName = itemView.findViewById(R.id.txName);
            mEndThread = itemView.findViewById(R.id.btEnd);
        }

        public void bind(final Future run, final callBack back) {
            String name = run.toString();
            mThreadName.setText(name); //.substring(name.indexOf("$"),name.length())
            mEndThread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back.onEvent(run);
                }
            });
        }
    }
}
