package com.example.amit.uniconnexample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amit.uniconnexample.utils.Utils;

import java.util.ArrayList;

/**
 * Created by amit on 23/12/16.
 */

public class Messageadapter extends RecyclerView.Adapter<Messageadapter.ViewHolder> {
    private ArrayList<Message_model> dataset;

    public Messageadapter(ArrayList<Message_model> data) {
        dataset=data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView.findViewById(R.id.txt);
            imageView=(ImageView)itemView.findViewById(R.id.photo);
        }
    }
    @Override
    public Messageadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_messageitem, parent, false);
        Messageadapter.ViewHolder myViewHolder = new Messageadapter.ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(Messageadapter.ViewHolder holder, int position) {
      ImageView iview=holder.imageView;
      TextView tview=holder.mTextView;
    //  iview.setImageBitmap(Utils.decodeBase64(dataset.get(position).getImage()));
      tview.setText(dataset.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
