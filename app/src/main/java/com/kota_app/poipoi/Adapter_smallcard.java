package com.kota_app.poipoi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kota327 on 12/18/2017.
 */

public class Adapter_smallcard extends RecyclerView.Adapter<Adapter_smallcard.ViewHolder> {

    private List<String> mId;
    private List<String> mPlace;
    private List<String> mView;
    private List<String> mDate;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_id;
        TextView textView_place;
        TextView textView_view;
        TextView textView_date;

        ViewHolder(View view){
            super(view);
            textView_id = (TextView) view.findViewById(R.id.textView_adapter_small_id);
            textView_place = (TextView) view.findViewById(R.id.textView_adapter_small_place);
            textView_view = (TextView) view.findViewById(R.id.textView_adapter_small_view);
            textView_date = (TextView) view.findViewById(R.id.textView_adapter_small_date);
        }
    }

    Adapter_smallcard(List<String> itemId,List<String> itemPlace, List<String> itemView, List<String> itemDate){
        this.mId = itemId;
        this.mPlace = itemPlace;
        this.mView = itemView;
        this.mDate = itemDate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int Position){
        holder.textView_id.setText(mId.get(Position));
        holder.textView_place.setText(mPlace.get(Position));
        holder.textView_view.setText(mView.get(Position).toString());
        holder.textView_date.setText(mDate.get(Position));
    }

    @Override
    public int getItemCount(){
        return mPlace.size();
    }
}
