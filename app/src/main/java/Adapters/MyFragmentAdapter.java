package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by 吴航辰 on 2016/12/3.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> titleList;

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList,List<String> titleList) {
        super(fm);
        this.titleList=titleList;
        this.fragmentList=fragmentList;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }



    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
