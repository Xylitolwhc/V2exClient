package Presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ActivityView.TopicDetailView;
import Adapters.TopicDetailAdapter;
import Items.TopicDetail;
import Net.ConnectInternet;
import Net.imgGetter;

/**
 * Created by 吴航辰 on 2016/12/15.
 */

public class TopicDetailPresenterMain implements TopicDetailPresenter {
    private static final int SHOW_RESPONSE = 0;
    private static final int CONNECT_FAILED = 1;
    private static final int SHOW_PICTURE = 2;

    private String url;
    private Context context;
    private TopicDetailAdapter topicDetailAdapter;
    private List<TopicDetail> contentDetailList = new ArrayList<>();
    private TopicDetailView topicDetailView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE: {
                    if (contentDetailList.isEmpty()) {
                        topicDetailView.sendErrorMessage();
                    }
                    topicDetailAdapter = new TopicDetailAdapter(context, contentDetailList);
                    topicDetailView.setAdapter(topicDetailAdapter);
                    getPicture();
                    break;
                }
                case CONNECT_FAILED: {
                    topicDetailView.sendErrorMessage();
                    break;
                }
                case SHOW_PICTURE: {
                    topicDetailAdapter.notifyDataSetChanged();
                    break;
                }
            }
            topicDetailView.endFresh();
        }
    };

    public TopicDetailPresenterMain(Context context, String url, TopicDetailView topicDetailView) {
        this.context = context;
        this.url = url;
        this.topicDetailView = topicDetailView;
    }

    @Override
    public void refresh() {
        connectInternet();
    }

    @Override
    public void init() {
        refresh();
    }

    private void connectInternet() {
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
                    TopicDetail contentDetail = contentDetailList.get(j);
                    try {
                        contentDetail.setIdImage(ConnectInternet.getPicture("http:" + contentDetail.getImageUrl()));
                    } catch (Exception e) {
                        Message message = new Message();
                        message.what = CONNECT_FAILED;
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
                    TopicDetail contentDetail = contentDetailList.get(j);
                    try {
                        WindowManager winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                        int width = winManager.getDefaultDisplay().getWidth();
                        Spanned replyContent;
                        if (j == 0) {
                            replyContent = Html.fromHtml(contentDetail.getReplyContentHtml(), new imgGetter(width - 100), null);
                        } else {
                            replyContent = Html.fromHtml(contentDetail.getReplyContentHtml(), new imgGetter(width - 250), null);
                        }
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
