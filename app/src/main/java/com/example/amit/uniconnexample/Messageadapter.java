package com.example.amit.uniconnexample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.uniconnexample.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 23/12/16.
 */

public class Messageadapter extends RecyclerView.Adapter<Messageadapter.MyViewHolder> {
    private ArrayList<Message_model> dataset;

    public Messageadapter(ArrayList<Message_model> data) {
        this.dataset=data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.txt);
            imageView=(ImageView)itemView.findViewById(R.id.photo);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_messageitem, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
      ImageView iview=holder.imageView;
      TextView tview=holder.mTextView;
      iview.setImageBitmap(Utils.decodeBase64(dataset.get(position).getImage()));
      tview.setText(dataset.get(position).getMsg());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
