package com.fiquedeolho.nfcatividadeapp.pager.addTag;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAddTagAdapter  extends FragmentPagerAdapter {

    private final ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

    public PagerAddTagAdapter(FragmentManager manager) {
        super(manager);
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
