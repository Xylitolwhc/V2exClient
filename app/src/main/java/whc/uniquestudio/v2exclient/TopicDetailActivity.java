package whc.uniquestudio.v2exclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ActivityView.TopicDetailView;
import Adapters.TopicDetailAdapter;
import Items.TopicDetail;
import Items.RecycleViewDivider;
import Net.ConnectInternet;
import Net.imgGetter;
import Presenter.TopicDetailPresenter;
import Presenter.TopicDetailPresenterMain;

/**
 * Created by 吴航辰 on 2016/11/26.
 */

public class TopicDetailActivity extends Activity implements TopicDetailView {
    private static final int SHOW_RESPONSE = 0;
    private static final int CONNECT_FAILED = 1;
    private static final int SHOW_PICTURE = 2;

    private String url;
    private RecyclerView contentDetailRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayoutOfTheContent;
    private TopicDetailPresenter topicDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentdetail);

        swipeRefreshLayoutOfTheContent = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutOfTheContent);
        contentDetailRecyclerView = (RecyclerView) findViewById(R.id.contentDetailRecyclerView);

        contentDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentDetailRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));//添加分割线

        topicDetailPresenter = new TopicDetailPresenterMain(this, getIntent().getStringExtra("url"), this);
        topicDetailPresenter.init();
        swipeRefreshLayoutOfTheContent.setRefreshing(true);

        swipeRefreshLayoutOfTheContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                topicDetailPresenter.refresh();
            }
        });
    }

    public TopicDetailActivity() {
        super();
    }

    @Override
    public void setAdapter(TopicDetailAdapter topicDetailAdapter) {
        contentDetailRecyclerView.setAdapter(topicDetailAdapter);
    }

    @Override
    public void sendErrorMessage() {
        Toast.makeText(this, "出错了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void endFresh() {
        swipeRefreshLayoutOfTheContent.setRefreshing(false);
    }
}
