package feliperrm.reversiandroid.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Felipe on 13/09/2015.
 */
public class FragmentViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;
    FragmentManager fm;

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        this.fragments = fragments;
    }

    public FragmentViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
        this.fm = fm;
    }

    public void clearAll() //Clear all page
    {
        int tam = fragments.size();
        for(int i=0; i<tam; i++)
            fm.beginTransaction().remove(fragments.get(i)).commit();
        fragments.clear();
    }

    @Override
    public Fragment getItem(int index) {

        return fragments.get(index);
    }

    @Override
    public void finishUpdate(ViewGroup container){
        super.finishUpdate(container);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
