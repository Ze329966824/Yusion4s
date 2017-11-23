package com.yusion.shanghai.yusion4s.ui.entrance;

/**
 * Created by LX on 2017/11/7.
 */

public enum OrderManagerFragmentEvent {
    showFragment(-1);

    public int position;

    OrderManagerFragmentEvent(int position) {
        this.position = position;
    }
}
