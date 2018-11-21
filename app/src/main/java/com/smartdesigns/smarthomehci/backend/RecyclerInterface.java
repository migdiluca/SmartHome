package com.smartdesigns.smarthomehci.backend;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.List;

public interface RecyclerInterface {

    public String getName();
    public int getBackground();
    public void setBackground(int color);
    public String getMeta();
    public String getId();
    public void onClickAction(Serializable arg, Context context);
}
