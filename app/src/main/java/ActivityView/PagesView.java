package ActivityView;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import Adapters.MyFragmentAdapter;

/**
 * Created by 吴航辰 on 2016/12/13.
 */

public interface PagesView {
    void setAdapter(MyFragmentAdapter myFragmentAdapter);
    SharedPreferences getTheSharedPreferences(String name, int mode);
    FragmentManager getTheFragmentManager();
}
