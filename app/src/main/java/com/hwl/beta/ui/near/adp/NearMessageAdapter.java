package com.hwl.beta.ui.near.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.NearMessageItemBinding;
import com.hwl.beta.db.entity.NearCircleMessage;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.near.holder.NearMessageItemViewHolder;

import java.util.List;

public class NearMessageAdapter extends RecyclerView.Adapter<NearMessageItemViewHolder> {
    private Context context;
    private List<NearCircleMessage> messages;
    private LayoutInflater inflater;

    public NearMessageAdapter(Context context, List<NearCircleMessage> messages) {
        this.context = context;
        this.messages = messages;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NearMessageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NearMessageItemViewHolder((NearMessageItemBinding) DataBindingUtil.inflate(inflater, R.layout.near_message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NearMessageItemViewHolder holder, final int position) {
        holder.setItemBinding(messages.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
