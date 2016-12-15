package ActivityView;

import Adapters.TopicDetailAdapter;

/**
 * Created by 吴航辰 on 2016/12/15.
 */

public interface TopicDetailView {
    void endFresh();
    void setAdapter(TopicDetailAdapter topicDetailAdapter);
    void sendErrorMessage();
}
