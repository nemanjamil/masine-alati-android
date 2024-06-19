package rs.tridanwebshop.tridan.views.adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import rs.tridanwebshop.tridan.common.config.AppConfig;
import rs.tridanwebshop.tridan.fragments.FirstTab;
import rs.tridanwebshop.tridan.fragments.SecondTab;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FirstTab tab1 = new FirstTab();
                return tab1;
            case 1:
                SecondTab tab2 = new SecondTab();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return AppConfig.TAB_NUMBER;
    }
}
