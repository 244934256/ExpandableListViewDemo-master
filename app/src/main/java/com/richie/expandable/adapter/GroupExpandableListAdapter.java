package com.richie.expandable.adapter;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.richie.expandable.R;
import com.richie.expandable.entity.CategoryEntity;
import com.richie.expandable.entity.GroupEntity;

import java.util.List;

/**
 * @author Richie on 2017.07.31
 * 改过 Indicator 的 ExpandableListView 的适配器
 */
public class GroupExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "IndicatorExpandableList";
    private List<CategoryEntity> groupData;
    private List<List<GroupEntity>> childData;
    //                用于存放Indicator的集合
    private SparseArray<ImageView> mIndicators;
    private OnGroupExpandedListener mOnGroupExpandedListener;

    public GroupExpandableListAdapter(List<CategoryEntity> groupData, List<List<GroupEntity>> childData) {
        this.groupData = groupData;
        this.childData = childData;
        mIndicators = new SparseArray<>();
    }

    public void setOnGroupExpandedListener(OnGroupExpandedListener onGroupExpandedListener) {
        mOnGroupExpandedListener = onGroupExpandedListener;
    }

    //            根据分组的展开闭合状态设置指示器
    public void setIndicatorState(int groupPosition, boolean isExpanded) {
        if (isExpanded) {
            mIndicators.get(groupPosition).setImageResource(R.drawable.ic_expand_less);
        } else {
            mIndicators.get(groupPosition).setImageResource(R.drawable.ic_expand_more);
        }
    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_group_indicator, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_group_indicator);
            groupViewHolder.ivIndicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
            groupViewHolder.ll_check = convertView.findViewById(R.id.ll_check);
            groupViewHolder.iv_check = convertView.findViewById(R.id.iv_check);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final CategoryEntity categoryEntity = (groupData.get(groupPosition));
        groupViewHolder.tvTitle.setText(categoryEntity.name);
        if (categoryEntity.check == 1) {
            groupViewHolder.iv_check.setBackgroundResource(R.mipmap.nim_contact_checkbox_checked_green);
        } else {
            groupViewHolder.iv_check.setBackgroundResource(R.mipmap.nim_checkbox_not_checked);
        }
        groupViewHolder.ll_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryEntity.check == 0) {
                    categoryEntity.check = 1;
                    setChildCheck(groupPosition, 1);
                } else {
                    categoryEntity.check = 0;
                    setChildCheck(groupPosition, 0);
                }
                notifyDataSetChanged();
            }
        });
        //      把位置和图标添加到Map
        mIndicators.put(groupPosition, groupViewHolder.ivIndicator);
        //      根据分组状态设置Indicator
        setIndicatorState(groupPosition, isExpanded);
        return convertView;
    }

    private void setChildCheck(int potion, int check) {
        List<GroupEntity> groupEntityList = childData.get(potion);
        for (GroupEntity groupEntity : groupEntityList) {
            groupEntity.check = check;
        }
    }

    private void setGroupCheck(int potion, int check) {
        CategoryEntity categoryEntityList = groupData.get(potion);
        categoryEntityList.check = check;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_child);
            childViewHolder.ll_check = convertView.findViewById(R.id.ll_check);
            childViewHolder.iv_check = convertView.findViewById(R.id.iv_check);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final GroupEntity groupEntity = childData.get(groupPosition).get(childPosition);
        childViewHolder.tvTitle.setText(groupEntity.name);
        if (groupEntity.check == 1) {
            childViewHolder.iv_check.setBackgroundResource(R.mipmap.nim_contact_checkbox_checked_green);
        } else {
            childViewHolder.iv_check.setBackgroundResource(R.mipmap.nim_checkbox_not_checked);
        }
        childViewHolder.ll_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupEntity.check == 0) {
                    groupEntity.check = 1;
                    int check = 0;
                    for (GroupEntity groupEntity1 : childData.get(groupPosition)) {
                        if (groupEntity1.check == 1) {
                            check++;
                            if (check == childData.get(groupPosition).size()) {
                                setGroupCheck(groupPosition, 1);
                            }
                        }
                    }
                } else {
                    groupEntity.check = 0;
                    setGroupCheck(groupPosition, 0);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        Log.d(TAG, "onGroupExpanded() called with: groupPosition = [" + groupPosition + "]");
        if (mOnGroupExpandedListener != null) {
            mOnGroupExpandedListener.onGroupExpanded(groupPosition);
        }
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        Log.d(TAG, "onGroupCollapsed() called with: groupPosition = [" + groupPosition + "]");
    }

    private static class GroupViewHolder {
        TextView tvTitle;
        ImageView ivIndicator;
        LinearLayout ll_check;
        ImageView iv_check;
    }

    private static class ChildViewHolder {
        TextView tvTitle;
        LinearLayout ll_check;
        ImageView iv_check;
    }
}
