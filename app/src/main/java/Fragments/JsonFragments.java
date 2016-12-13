package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapters.MyAdapter;
import Items.RecycleViewDivider;
import Items.TopicsFromJson;
import Net.ConnectInternet;
import whc.uniquestudio.v2exclient.MainActivity;
import whc.uniquestudio.v2exclient.R;

/**
 * Created by 吴航辰 on 2016/12/3.
 */

public class JsonFragments extends android.support.v4.app.Fragment {

    private static final int SHOW_RESPONSE = 0;
    private static final int CONNECT_FAILED = 1;
    private static final int SHOW_PICTURE = 2;

    private String url;
    private Context context;
    private RecyclerView recyclerView;
    private List<TopicsFromJson> topicsFromJsonList;
    private MyAdapter myAdapter;
    private SwipeRefreshLayout swipeRefreshLayoutOfTheHottest;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE: {
                    myAdapter = new MyAdapter(context, topicsFromJsonList);
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                    getPicture();
                    break;
                }
                case CONNECT_FAILED: {
                    Toast.makeText(context, "刷新失败", Toast.LENGTH_SHORT).show();
                    break;
                }
                case SHOW_PICTURE: {
                    myAdapter.notifyDataSetChanged();
                    break;
                }
            }
            swipeRefreshLayoutOfTheHottest.setRefreshing(false);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics_gson, container, false);

        context = getActivity();
        topicsFromJsonList = new ArrayList<>();
        myAdapter = new MyAdapter(context, topicsFromJsonList);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewOfTheHottest);
        swipeRefreshLayoutOfTheHottest = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutOfTheHottest);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayout.HORIZONTAL, R.drawable.divider));
        url = getArguments().getString("url");
        ConnectInternet(url);
        swipeRefreshLayoutOfTheHottest.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectInternet(url);
            }
        });

        return view;
    }

    private void ConnectInternet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    topicsFromJsonList = ConnectInternet.getTheHottestList(url);

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
                if (topicsFromJsonList != null) {
                    for (int i = 0; i < topicsFromJsonList.size(); i++) {//获取头像图片
                        try {
                            TopicsFromJson topicsFromJson = topicsFromJsonList.get(i);
                            topicsFromJson.setAvatar_mini(ConnectInternet.getPicture("http:" + topicsFromJson.member.getAvatar_mini()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            Message message = new Message();
                            message.what = SHOW_PICTURE;
                            handler.sendMessage(message);
                        }
                    }
                }
            }
        }).start();
    }
}
