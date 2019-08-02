package com.example.gymmyapplication.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gymmyapplication.R;
import com.example.gymmyapplication.model.Scheda;

import java.util.List;

public class SchedeAdapter extends RecyclerView.Adapter<SchedeAdapter.MyViewHolder> {
    private Context context;
    private List<Scheda> schedaList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView schedaText;
        public MyViewHolder(View view) {
            super(view);
            schedaText = view.findViewById(R.id.schedaText);
        }
    }
    public SchedeAdapter(Context context, List<Scheda> schedaList) {
        this.context = context;
        this.schedaList = schedaList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scheda_list_row,parent,false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Scheda scheda=schedaList.get(position);
        holder.schedaText.setText(scheda.getNome_scheda());
    }
    @Override
    public int getItemCount() {
        return schedaList.size();
    }
}