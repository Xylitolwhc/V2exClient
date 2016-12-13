package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapters.JsoupItemAdapter;
import Items.RecycleViewDivider;
import Items.TopicsFromJsoup;
import Net.ConnectInternet;
import whc.uniquestudio.v2exclient.R;

/**
 * Created by 吴航辰 on 2016/12/3.
 */

public class JsoupFragments extends Fragment {

    private static final int SHOW_RESPONSE = 0;
    private static final int CONNECT_FAILED = 1;
    private static final int SHOW_PICTURE = 2;

    private String url = null;
    private Context context;
    private List<TopicsFromJsoup> topicsFromJsoupList;
    private JsoupItemAdapter jsoupItemAdapter;
    private SwipeRefreshLayout swipeRefreshLayoutOfTheJsoup;
    private RecyclerView recyclerViewOfTheJsoup;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE: {
                    jsoupItemAdapter = new JsoupItemAdapter(context, topicsFromJsoupList);
                    recyclerViewOfTheJsoup.setAdapter(jsoupItemAdapter);
                    jsoupItemAdapter.notifyDataSetChanged();
                    getPicture();
                    break;
                }
                case CONNECT_FAILED: {
                    Toast.makeText(context, "出错了", Toast.LENGTH_SHORT).show();
                    break;
                }
                case SHOW_PICTURE: {
                    jsoupItemAdapter.notifyDataSetChanged();
                    break;
                }
            }
            swipeRefreshLayoutOfTheJsoup.setRefreshing(false);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topics_jsoup, container, false);

        context = getActivity();
        topicsFromJsoupList = new ArrayList<>();
        jsoupItemAdapter = new JsoupItemAdapter(context, topicsFromJsoupList);
        recyclerViewOfTheJsoup = (RecyclerView) view.findViewById(R.id.recyclerViewOfTheJsoup);
        swipeRefreshLayoutOfTheJsoup = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutOfTheJsoup);

        recyclerViewOfTheJsoup.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewOfTheJsoup.setAdapter(jsoupItemAdapter);
        recyclerViewOfTheJsoup.addItemDecoration(new RecycleViewDivider(context, LinearLayout.HORIZONTAL, R.drawable.divider));

        url = getArguments().getString("url");
        ConnectInternet(url);
        swipeRefreshLayoutOfTheJsoup.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                    topicsFromJsoupList = ConnectInternet.getTopicsFromJsoup(url);

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

        for (int i = 0; i < topicsFromJsoupList.size(); i++) {//获取头像图片
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TopicsFromJsoup topicsFromJsoup = topicsFromJsoupList.get(j);
                    topicsFromJsoup.setBitmap(ConnectInternet.getPicture("http:" + topicsFromJsoup.getImgUrl()));
                    Message message = new Message();
                    message.what = SHOW_PICTURE;
                    handler.sendMessage(message);
                }
            }).start();
        }

    }
}
