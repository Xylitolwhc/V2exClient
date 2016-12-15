package ActivityView;

import Adapters.MyAdapter;

/**
 * Created by 吴航辰 on 2016/12/15.
 */

public interface FragmentView {
    void setAdapter(MyAdapter recyclerViewAdapter);
    void endFresh();
}
