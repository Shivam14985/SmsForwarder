package com.example.shivambhardwaj.asifhood.smsforwarder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shivambhardwaj.asifhood.smsforwarder.Model.ForwardedHistory;
import com.example.shivambhardwaj.asifhood.smsforwarder.R;
import com.example.shivambhardwaj.asifhood.smsforwarder.databinding.HistoryRecyclerviewDesignBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.viewHolder> {
    ArrayList<ForwardedHistory> list;
    Context context;

    public HistoryAdapter(ArrayList<ForwardedHistory> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.history_recyclerview_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ForwardedHistory model=list.get(position);
        holder.binding.message.setText(model.getMessage());
        holder.binding.Sender.setText(model.getSender());
        String time= TimeAgo.using(model.getForwardedAt());
        holder.binding.time.setText(time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        HistoryRecyclerviewDesignBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = HistoryRecyclerviewDesignBinding.bind(itemView);
        }
    }
}
