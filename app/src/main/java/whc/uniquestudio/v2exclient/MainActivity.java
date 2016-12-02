package whc.uniquestudio.v2exclient;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapters.MyAdapter;
import Items.TheHottest;
import Net.ConnectInternet;

public class MainActivity extends Activity {
    private static final int SHOW_RESPONSE = 0;
    private static final int CONNECT_FAILED = 1;
    private static final int SHOW_PICTURE = 2;

    private RecyclerView recyclerView;
    private List<TheHottest> theHottestList=new ArrayList<>();
    private MyAdapter myAdapter = new MyAdapter(MainActivity.this, theHottestList);
    private SwipeRefreshLayout swipeRefreshLayoutOfTheHottest;
    private ConnectInternet connectInternet=ConnectInternet.getInstance();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE: {
                    myAdapter=new MyAdapter(MainActivity.this,theHottestList);
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                    getPicture();
                    Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    break;
                }
                case CONNECT_FAILED: {
                    Toast.makeText(MainActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                    break;
                }
                case SHOW_PICTURE: {
                    Log.d("M","OK");
                    myAdapter.notifyDataSetChanged();
                    break;
                }
            }
            swipeRefreshLayoutOfTheHottest.setRefreshing(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewOfTheHottest);
        swipeRefreshLayoutOfTheHottest = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutOfTheHottest);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(myAdapter);

        ConnectInternet("https://www.v2ex.com/api/topics/hot.json");
        swipeRefreshLayoutOfTheHottest.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectInternet("https://www.v2ex.com/api/topics/hot.json");
            }
        });
    }

    private void ConnectInternet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    theHottestList = connectInternet.getTheHottestList(url);

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < theHottestList.size(); i++) {//获取头像图片
                    try {
                        TheHottest theHottest = theHottestList.get(i);
                        theHottest.setAvatar_mini(connectInternet.getPicture("http:" + theHottest.member.getAvatar_mini()));
                        Log.d("M",theHottest.getUrl());
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
