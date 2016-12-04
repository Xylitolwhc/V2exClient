package whc.uniquestudio.v2exclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapters.ContentDetailAdapter;
import Items.ContentDetail;
import Items.RecycleViewDivider;
import Net.ConnectInternet;

/**
 * Created by 吴航辰 on 2016/11/26.
 */

public class ContentDetailActivity extends Activity {
    private static final int SHOW_RESPONSE = 0;
    private static final int CONNECT_FAILED = 1;
    private static final int SHOW_PICTURE = 2;

    private String url;
    private RecyclerView contentDetailRecyclerView;
    private ConnectInternet connectInternet = ConnectInternet.getInstance();
    private ContentDetailAdapter contentDetailAdapter;
    private List<ContentDetail> contentDetailList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayoutOfTheContent;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE: {
                    contentDetailAdapter = new ContentDetailAdapter(ContentDetailActivity.this, contentDetailList);
                    contentDetailRecyclerView.setAdapter(contentDetailAdapter);
                    getPicture();
                    contentDetailAdapter.notifyDataSetChanged();
                    break;
                }
                case CONNECT_FAILED: {
                    Toast.makeText(ContentDetailActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                    break;
                }
                case SHOW_PICTURE: {
                    contentDetailAdapter.notifyDataSetChanged();
                    break;
                }

            }
            swipeRefreshLayoutOfTheContent.setRefreshing(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentdetail);

        doGetIntent();
        swipeRefreshLayoutOfTheContent = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutOfTheContent);
        contentDetailRecyclerView = (RecyclerView) findViewById(R.id.contentDetailRecyclerView);

        contentDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentDetailRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));

        swipeRefreshLayoutOfTheContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                connectInternet(url);
            }
        });
        connectInternet(url);
    }

    private void doGetIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("url")) {
            url = intent.getStringExtra("url");
        } else {
            finish();
        }
    }


    private void connectInternet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    contentDetailList = connectInternet.getContentDetailList(url);
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.what = CONNECT_FAILED;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private void getPicture() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < contentDetailList.size(); i++) {//获取头像图片
                    try {
                        ContentDetail contentDetail = contentDetailList.get(i);
                        contentDetail.setIdImage(connectInternet.getPicture("http:" + contentDetail.getImageUrl()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Message message = new Message();
                        message.what = SHOW_PICTURE;
                        handler.sendMessage(message);
                    }
                }
            }
        }).start();
    }
}
