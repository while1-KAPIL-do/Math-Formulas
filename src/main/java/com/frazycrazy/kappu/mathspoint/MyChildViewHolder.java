package com.frazycrazy.kappu.mathspoint;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

class MyChildViewHolder extends ChildViewHolder {

   TextView listChild2;

    MyChildViewHolder(View itemView) {
        super(itemView);
        listChild2 = itemView.findViewById(R.id.listChild);

    }

    void onBind(String Sousdoc) {
        listChild2.setText(Sousdoc);

    }


}