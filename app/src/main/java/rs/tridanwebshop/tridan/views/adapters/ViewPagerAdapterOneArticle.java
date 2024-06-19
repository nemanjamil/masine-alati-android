package rs.tridanwebshop.tridan.views.adapters;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import rs.tridanwebshop.tridan.common.utils.Log;
import rs.tridanwebshop.tridan.fragments.OneArticleTabOne;
import rs.tridanwebshop.tridan.fragments.OneArticleTabThree;
import rs.tridanwebshop.tridan.fragments.OneArticleTabTwo;

public class ViewPagerAdapterOneArticle extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerAdapterOneArticle(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OneArticleTabOne tab1 = new OneArticleTabOne();
                Log.logInfo("LALALA.....", "1");
                return tab1;
            case 1:
                OneArticleTabThree tab2 = new OneArticleTabThree();
                Log.logInfo("LALALA.....", "1");
                return tab2;
            case 2:
                OneArticleTabTwo tab3 = new OneArticleTabTwo();
                Log.logInfo("LALALA.....", "3");
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
