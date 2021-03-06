package com.hwl.beta.ui.chat.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hwl.beta.R;
import com.hwl.beta.badge.Badge;
import com.hwl.beta.databinding.RecordItemBinding;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.ui.widget.BadgeNumber;
import com.hwl.beta.utils.DateUtils;
import com.hwl.beta.utils.DisplayUtils;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<ChatRecordMessage> records;
    private IAdapterListener adapterListener;
    private int totalMessageCount = 0;
    private int imageSize = 100;

    public RecordAdapter(Context context, List<ChatRecordMessage> records, IAdapterListener adapterListener) {
        this.context = context;
        this.records = records;
        this.adapterListener = adapterListener;
        inflater = LayoutInflater.from(context);
        imageSize = DisplayUtils.dp2px(context, imageSize);
    }

    @Override
    public RecordAdapter.RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordViewHolder((RecordItemBinding) DataBindingUtil.inflate(inflater, R.layout.record_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecordAdapter.RecordViewHolder holder, final int position) {
        RecordItemBinding itemBinding = holder.getItemBinding();
        ChatRecordMessage record = records.get(position);
        itemBinding.setRecord(record);
        itemBinding.setPosition(position);
        itemBinding.tvTime.setText(DateUtils.getChatShowTime(record.getSendTime()));
        itemBinding.ivNotify.setVisibility(record.getIsShield() ? View.VISIBLE : View.GONE);
        holder.badge.setBadgeNumber(record.getUnreadCount());

        switch (record.getRecordType()) {
            case MQConstant.CHAT_RECORD_TYPE_GROUP:
                itemBinding.ivGroupImage.setVisibility(View.VISIBLE);
                itemBinding.ivRecordImage.setVisibility(View.GONE);
                itemBinding.ivGroupImage.setImagesData(record.getGroupUserImages());
                break;
            default:
                itemBinding.ivGroupImage.setVisibility(View.GONE);
                itemBinding.ivRecordImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(record.getRecordImage())
                        .placeholder(R.drawable.empty_photo)
                        .error(R.drawable.empty_photo)
                        .into(itemBinding.ivRecordImage);
                break;
        }

        if (position == 0) {
            totalMessageCount = 0;
        }

        if (record.getUnreadCount() > 0) {
            totalMessageCount = record.getUnreadCount() + totalMessageCount;
        }

        if ((position + 1) == getItemCount() && adapterListener != null) {
            adapterListener.onLoadComplete(totalMessageCount);
        }
        itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.onItemClick(position);
            }
        });
        itemBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                adapterListener.onItemLongClick(v, position);
                return false;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return records.get(position).getRecordId();
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void updateGroupImage(String groupGuid, List<String> groupUserImages) {
        if (StringUtils.isBlank(groupGuid) || groupUserImages == null || groupUserImages.size() <= 0)
            return;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getRecordType() == MQConstant.CHAT_RECORD_TYPE_GROUP && records.get(i).getGruopGuid().equals(groupGuid)) {
                records.get(i).setGroupUserImages(groupUserImages);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public interface IAdapterListener {
        void onLoadComplete(int messageTotalCount);

        void onItemClick(int position);

        void onItemLongClick(View view, int position);
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        RecordItemBinding itemBinding;
        Badge badge;

        public RecordViewHolder(RecordItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            badge = BadgeNumber.bindBadgeView(context, itemBinding.llImageContainer);
//            badge = new QBadgeView(context).bindTarget(itemBinding.llImageContainer);
//            badge.setBadgeGravity(Gravity.TOP | Gravity.END);
//            badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
//                @Override
//                public void onDragStateChanged(int dragState, Badge badge, View targetView) {
//                    if (dragState == STATE_SUCCEED) {
//                    }
//                }
//            });
        }

        public RecordItemBinding getItemBinding() {
            return itemBinding;
        }
    }
}
