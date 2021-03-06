package com.hwl.beta.ui.group.adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwl.beta.R;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

public class GroupUserAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<GroupUserInfo> users;

    public GroupUserAdapter(Context context, List<GroupUserInfo> users) {
        this.users = users;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public GroupUserInfo getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupUserInfo user = users.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_user_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivHeader = convertView.findViewById(R.id.iv_header);
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (user.getId() == -1) {
            viewHolder.ivHeader.setBackgroundResource(R.drawable.layout_border);
            viewHolder.ivHeader.setImageResource(R.drawable.ic_add);
            viewHolder.tvName.setText("");
        } else {
            viewHolder.ivHeader.setBackgroundResource(0);
            ImageViewBean.loadImage(viewHolder.ivHeader, user.getUserHeadImage());
            if (StringUtils.isBlank(user.getUserName())) {
                viewHolder.tvName.setText("--");
            } else {
                viewHolder.tvName.setText(StringUtils.cutString(user.getUserName(), 5));
            }
        }
        return convertView;
    }


    class ViewHolder {
        ImageView ivHeader;
        TextView tvName;
    }
}
