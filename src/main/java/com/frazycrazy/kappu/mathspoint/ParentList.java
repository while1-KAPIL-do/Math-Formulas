package com.frazycrazy.kappu.mathspoint;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

class ParentList extends ExpandableGroup<ChildList> {



    ParentList(String title, List<ChildList> items) {
        super(title, items);
    }

}