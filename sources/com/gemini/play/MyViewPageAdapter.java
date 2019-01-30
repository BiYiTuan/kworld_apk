package com.gemini.play;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import java.util.List;

public class MyViewPageAdapter extends PagerAdapter {
    private static final String SHAREDPREFERENCES_NAME = "first_pref";
    private Activity activity;
    private List<View> views;

    public MyViewPageAdapter(List<View> views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }

    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) this.views.get(arg1));
    }

    public void finishUpdate(View arg0) {
    }

    public int getCount() {
        if (this.views != null) {
            return this.views.size();
        }
        return 0;
    }

    public Object instantiateItem(View arg0, int arg1) {
        return this.views.get(arg1);
    }

    private void goHome() {
    }

    private void setGuided() {
        Editor editor = this.activity.getSharedPreferences(SHAREDPREFERENCES_NAME, 0).edit();
        editor.putBoolean("isFirstIn", false);
        editor.commit();
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    public Parcelable saveState() {
        return null;
    }

    public void startUpdate(View arg0) {
    }
}
