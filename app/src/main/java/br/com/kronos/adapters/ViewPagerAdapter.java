package br.com.kronos.adapters;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {

    View[] viewArray;

    public ViewPagerAdapter(View[] viewArray) {
        this.viewArray = viewArray;
    }

    @Override
    public int getCount() {
        return viewArray.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewArray[position];
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewArray[position]);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
