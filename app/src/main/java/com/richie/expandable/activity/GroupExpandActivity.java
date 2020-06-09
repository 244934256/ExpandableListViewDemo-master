package com.richie.expandable.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.richie.expandable.Constant;
import com.richie.expandable.R;
import com.richie.expandable.adapter.GroupExpandableListAdapter;
import com.richie.expandable.adapter.IndicatorExpandableListAdapter;
import com.richie.expandable.entity.CategoryEntity;
import com.richie.expandable.entity.GroupEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 Indicator 的 ExpandableListView
 */
public class GroupExpandActivity extends AppCompatActivity {
    private static final String TAG = "IndicatorExpandActivity";
    private List<CategoryEntity> categoryEntityList;
    private List<List<GroupEntity>> groupEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);
        final ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandable_list);
        categoryEntityList = new ArrayList<>();
        groupEntityList = new ArrayList<>();
        initData();
        final GroupExpandableListAdapter adapter = new GroupExpandableListAdapter(categoryEntityList, groupEntityList);
        listView.setAdapter(adapter);

        // 清除默认的 Indicator
        listView.setGroupIndicator(null);

        //  设置分组项的点击监听事件
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG, "onGroupClick: groupPosition:" + groupPosition + ", id:" + id);
                boolean groupExpanded = parent.isGroupExpanded(groupPosition);
                adapter.setIndicatorState(groupPosition, groupExpanded);
                // 请务必返回 false，否则分组不会展开
                return false;
            }
        });

        //  设置子选项点击监听事件
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(GroupExpandActivity.this, groupEntityList.get(groupPosition).get(childPosition).name, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 4; i++) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.name = "类别" + i;
            categoryEntity.type = i;
            categoryEntityList.add(categoryEntity);
        }
        for (int i = 0; i < 4; i++) {
            List<GroupEntity> _groupEntityList = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                GroupEntity groupEntity = new GroupEntity();
                groupEntity.type = i;
                groupEntity.name = "群组" + j;
                groupEntity.puid = "puid" + j;
                groupEntity.yunXinId = "yunXinId" + j;
                _groupEntityList.add(groupEntity);
            }
            groupEntityList.add(_groupEntityList);
        }
    }

}
