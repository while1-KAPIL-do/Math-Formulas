package com.frazycrazy.kappu.mathspoint;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

class MyParentViewHolder extends GroupViewHolder {

    TextView listGroup;

    MyParentViewHolder(View itemView) {
        super(itemView);
        listGroup = itemView.findViewById(R.id.listParent);
    }

    void setParentTitle(ExpandableGroup group) {
        listGroup.setText(group.getTitle());
    }


}