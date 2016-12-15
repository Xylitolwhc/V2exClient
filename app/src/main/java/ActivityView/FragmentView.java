package ActivityView;

import Adapters.TopicsAdapter;

/**
 * Created by 吴航辰 on 2016/12/15.
 */

public interface FragmentView {
    void setAdapter(TopicsAdapter recyclerViewAdapter);
    void endFresh();
}
