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
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.markdown4j.Markdown4jProcessor;

import java.util.ArrayList;
import java.util.List;

import Adapters.ContentDetailAdapter;
import Items.ContentDetail;
import Items.RecycleViewDivider;
import Net.ConnectInternet;
import Net.imgGetter;

/**
 * Created by 吴航辰 on 2016/11/26.
 */

public class ContentDetailActivity extends Activity {
    private static final int SHOW_RESPONSE = 0;
    private static final int CONNECT_FAILED = 1;
    private static final int SHOW_PICTURE = 2;

    private String url;
    private RecyclerView contentDetailRecyclerView;
    private ContentDetailAdapter contentDetailAdapter;
    private List<ContentDetail> contentDetailList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayoutOfTheContent;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE: {
                    if (contentDetailList.isEmpty()){
                        Toast.makeText(ContentDetailActivity.this, "出错了", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    contentDetailAdapter = new ContentDetailAdapter(ContentDetailActivity.this, contentDetailList);
                    contentDetailRecyclerView.setAdapter(contentDetailAdapter);
                    getPicture();
                    contentDetailAdapter.notifyDataSetChanged();
                    break;
                }
                case CONNECT_FAILED: {
                    Toast.makeText(ContentDetailActivity.this, "出错了", Toast.LENGTH_SHORT).show();
                    finish();
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
        setContentView(R.layout.activity_contentdetail);

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
                    contentDetailList = ConnectInternet.getContentDetailList(url);
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = new Message();
                    message.what = CONNECT_FAILED;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getPicture() {

        for (int i = 0; i < contentDetailList.size(); i++) {//获取头像图片
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ContentDetail contentDetail = contentDetailList.get(j);
                    try {
                        contentDetail.setIdImage(ConnectInternet.getPicture("http:" + contentDetail.getImageUrl()));
                    } catch (Exception e) {
                        Message message=new Message();
                        message.what=CONNECT_FAILED;
                        handler.sendMessage(message);
                        e.printStackTrace();
                    } finally {
                        Message message = new Message();
                        message.what = SHOW_PICTURE;
                        handler.sendMessage(message);
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ContentDetail contentDetail = contentDetailList.get(j);
                    try {
                        Spanned replyContent = Html.fromHtml(contentDetail.getReplyContentHtml(), new imgGetter(), null);
                        contentDetail.setReplyContent(replyContent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Message message = new Message();
                        message.what = SHOW_PICTURE;
                        handler.sendMessage(message);
                    }
                }
            }).start();
        }
    }
}
