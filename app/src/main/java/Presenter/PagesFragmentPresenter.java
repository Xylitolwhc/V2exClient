package Presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import ActivityView.FragmentView;
import Adapters.MyAdapter;
import Items.TopicsFromJson;
import Net.ConnectInternet;

/**
 * Created by 吴航辰 on 2016/12/15.
 */

public class PagesFragmentPresenter implements FragmentPresenter {
    private static final int SHOW_RESPONSE = 0;
    private static final int CONNECT_FAILED = 1;
    private static final int SHOW_PICTURE = 2;

    private Context context;
    private FragmentView fragmentView;
    private String url;
    private List<TopicsFromJson> topicsFromJsonList;
    private MyAdapter myAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE: {
                    myAdapter = new MyAdapter(context, topicsFromJsonList);
                    fragmentView.setAdapter(myAdapter);
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
            fragmentView.endFresh();
        }
    };

    public PagesFragmentPresenter(Context context, FragmentView fragmentView,String url) {
        this.context = context;
        this.fragmentView = fragmentView;
        this.url=url;
    }

    @Override
    public void refresh() {
        ConnectInternet();
    }

    @Override
    public void init() {
        refresh();
    }

    private void ConnectInternet() {
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
