package com.smartdesigns.smarthomehci.backend;

import android.support.v4.app.Fragment;

public interface RecyclerInterface {

    public String getName();
    public String getMeta();
    public String getId();
    public Fragment getChildFragment();
}
