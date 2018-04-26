package com.hwl.beta.ui.circle.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleUserHeadItemBinding;
import com.hwl.beta.databinding.CircleUserIndexItemBinding;
import com.hwl.beta.databinding.CircleUserItemNullBinding;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.db.ext.CircleExt;
import com.hwl.beta.ui.circle.action.ICircleUserItemListener;
import com.hwl.beta.ui.circle.holder.CircleUserHeadItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleUserIndexItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleUserItemNullViewHolder;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CircleUserIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CircleExt> circles;
    private ICircleUserItemListener itemListener;
    private LayoutInflater inflater;
    private int prevYear;
    private int prevMonth;
    private int prevDay;
    private String prevShowDate;

    public CircleUserIndexAdapter(Context context, List<CircleExt> circles, ICircleUserItemListener itemListener) {
        this.context = context;
        this.circles = circles;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
        prevShowDate = "";
        prevYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public int getItemViewType(int position) {
        switch (circles.get(position).getCircleItemType()) {
            case CircleExt.CircleNullItem:
            default:
                return 0;
            case CircleExt.CircleHeadItem:
                return 1;
            case CircleExt.CircleIndexItem:
                return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
            default:
                return new CircleUserItemNullViewHolder((CircleUserItemNullBinding) DataBindingUtil.inflate(inflater, R.layout.circle_user_item_null, parent, false));
            case 1:
                return new CircleUserHeadItemViewHolder((CircleUserHeadItemBinding) DataBindingUtil.inflate(inflater, R.layout.circle_user_head_item, parent, false));
            case 2:
                return new CircleUserIndexItemViewHolder((CircleUserIndexItemBinding) DataBindingUtil.inflate(inflater, R.layout.circle_user_index_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CircleExt info = circles.get(position);
        if (holder instanceof CircleUserItemNullViewHolder) {
            CircleUserItemNullViewHolder viewHolder = (CircleUserItemNullViewHolder) holder;
            viewHolder.setItemBinding(itemListener, (Calendar.getInstance().get(Calendar.MONTH) + 1) + "月", Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "");
        } else if (holder instanceof CircleUserHeadItemViewHolder) {
            CircleUserHeadItemViewHolder viewHolder = (CircleUserHeadItemViewHolder) holder;
            viewHolder.setItemBinding(new ImageViewBean(info.getViewUserImage(), info.getViewCircleBackImage()), info.getViewUserName(), info.getViewUserLifeNotes());
        } else if (holder instanceof CircleUserIndexItemViewHolder) {
            CircleUserIndexItemViewHolder viewHolder = (CircleUserIndexItemViewHolder) holder;
            String timeYear = "";
            String timeMonth = "";
            String timeDay = "";
            if (info.getInfo().getPublishTime() != null && !info.getInfo().getShowDate().equals(prevShowDate)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(info.getInfo().getPublishTime());
                if (calendar.get(Calendar.YEAR) != prevYear) {
                    timeYear = calendar.get(Calendar.YEAR) + "年";
                }
                if ((calendar.get(Calendar.MONTH) + 1) != prevMonth || calendar.get(Calendar.DAY_OF_MONTH) != prevDay) {
                    timeMonth = (calendar.get(Calendar.MONTH) + 1) + "月";
                    timeDay = calendar.get(Calendar.DAY_OF_MONTH) + "";
                }

                prevShowDate = info.getInfo().getShowDate();
                Calendar prev = Calendar.getInstance();
                prev.setTime(info.getInfo().getPublishTime());
                prevYear = prev.get(Calendar.YEAR);
                prevMonth = prev.get(Calendar.MONTH) + 1;
                prevDay = prev.get(Calendar.DAY_OF_MONTH);

                if (position != 1 || timeMonth.equals("")) {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.getItemBinding().llCircleContainer.getLayoutParams();
                    params.topMargin = 1;
                    viewHolder.getItemBinding().llCircleContainer.setLayoutParams(params);
                }
            }
            viewHolder.setItemBinding(timeYear, timeMonth, timeDay, info.getInfo().getContent(), info.getImages());
            viewHolder.getItemBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onItemViewClick(info);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }
}
