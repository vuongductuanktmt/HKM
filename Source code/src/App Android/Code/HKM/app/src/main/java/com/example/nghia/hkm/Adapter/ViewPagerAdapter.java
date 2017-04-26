package com.example.nghia.hkm.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nghia.hkm.Fragment.FragmentSPBCN;
//import com.example.nghia.hkm.Fragment.FragmentSPDMNN;
//import com.example.nghia.hkm.Fragment.FragmentSPDNNT;
import com.example.nghia.hkm.Fragment.FragmentSPDYT;

import java.util.ArrayList;
import java.util.List;

//import com.example.nghia.hkm.Fragment.FragmentSPDYT;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments = new ArrayList<Fragment>();
    List<String> titleFragment = new ArrayList<String>();

    public ViewPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        FragmentSPDYT fragmentSPDYT = new FragmentSPDYT();
        fragmentSPDYT.setArguments(bundle);
        fragments.add(fragmentSPDYT);
        FragmentSPBCN fragmentSPBCN = new FragmentSPBCN();
        fragmentSPBCN.setArguments(bundle);
        fragments.add(new FragmentSPBCN());
//        fragments.add(new FragmentSPDNNT());
//        fragments.add(new FragmentSPDMNN());


       titleFragment.add("Khuyến mãi");
      titleFragment.add("sự kiện ");
//      titleFragment.add("Top 50 nhiều người tích");
//      titleFragment.add("Top 50 được mua nhiều");
//       titleFragment.add("Xu hướng");

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titleFragment.get(position);
    }
}