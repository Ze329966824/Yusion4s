package com.yusion.shanghai.yusion4s.event;

/**
 * Created by ice on 2017/9/6.
 */

public enum  MainActivityEvent {

    showOrderManager(-1),showReadicon;

    public int position;



    MainActivityEvent(){

    }
    MainActivityEvent(int position) {
        this.position = position;
    }
}
