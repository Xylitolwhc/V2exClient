package whc.uniquestudio.v2exclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Adapters.ContentDetailAdapter;
import FromGson.TheHottest;
import FromJsoup.ContentDetail;

/**
 * Created by 吴航辰 on 2016/11/26.
 */

public class ContentDetailActivity extends Activity {
    public static final int SHOW_RESPONSE = 0;
    public static final int CONNECT_FAILED = 1;
    public static final int SHOW_PICTURE = 2;

    private String url, title, content, username;
    private RecyclerView contentDetailRecyclerView;
    private TextView contentDetailTitle, contentDetailUsername, contentDetailDetail, contentDetailContent;
    private Bitmap idImage=null;
    private ImageView contentDetailIdImage;
    private ContentDetailAdapter contentDetailAdapter;
    private List<ContentDetail> contentDetailList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayoutOfTheContent;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE: {
                    String s = (String) msg.obj;
                    ((TextView) findViewById(R.id.testText2)).setText(s);
                    getPicture();
                    contentDetailAdapter.notifyDataSetChanged();
                    Toast.makeText(ContentDetailActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
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

        //doFindViewById();
        doGetIntent();
        swipeRefreshLayoutOfTheContent = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutOfTheContent);
        contentDetailRecyclerView = (RecyclerView) findViewById(R.id.contentDetailRecyclerView);

        contentDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentDetailAdapter = new ContentDetailAdapter(ContentDetailActivity.this, contentDetailList, title, content, username, idImage);
        contentDetailRecyclerView.setAdapter(contentDetailAdapter);
        swipeRefreshLayoutOfTheContent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                connectInternet(getIntent().getStringExtra("url"));
            }
        });
        //connectInternet(getIntent().getStringExtra("url"));
        connectInternet(getIntent().getStringExtra("url"));
    }

//    private void doFindViewById() {
//        contentDetailRecyclerView = (RecyclerView) findViewById(R.id.contentDetailRecyclerView);
//        contentDetailTitle = (TextView) findViewById(R.id.contentDetailTitle);
//        contentDetailUsername = (TextView) findViewById(R.id.contentDetailUsername);
//        contentDetailDetail = (TextView) findViewById(R.id.contentDetailDetail);
//        contentDetailContent = (TextView) findViewById(R.id.contentDetailContent);
//        contentDetailIdImage = (ImageView) findViewById(R.id.contentDetailIdImage);
//    }

    private void doGetIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("url")) {
            url = intent.getStringExtra("url");
            content = intent.getStringExtra("content");
            title = intent.getStringExtra("title");
            username = intent.getStringExtra("username");
            idImage = intent.getParcelableExtra("idImage");

        } else {
            finish();
        }
    }


    private void connectInternet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Element userBox = document.getElementsByClass("box").get(1);
                    Elements allUsers = userBox.getElementsByClass("cell");

                    StringBuffer stringBuffer = new StringBuffer();
                    //stringBuffer.append(userNames.toString());
                    for (int i = 0; i < allUsers.size(); i++) {
                        if (allUsers.get(i).id().toString().length() != 0) {
                            Element user = allUsers.get(i);
                            String imgUrl = user.select("img[src]").first().attr("src").toString();
                            String replyContent = user.getElementsByClass("reply_content").first().text();
                            String userName = user.select("a[href]").first().text();

                            ContentDetail contentDetail = new ContentDetail();
                            contentDetail.setString("replyContent", replyContent);
                            contentDetail.setString("imageUrl", imgUrl);
                            contentDetail.setString("username", userName);
                            contentDetailList.add(contentDetail);
                            stringBuffer.append(imgUrl + "\n" + userName + "\n" + replyContent + "\n");
                        }
                    }
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = stringBuffer.toString();
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getPicture() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                for (int i = 0; i < contentDetailList.size(); i++) {//获取头像图片
                    try {
                        ContentDetail contentDetail = contentDetailList.get(i);
                        URL avatar_miniUrl = new URL("http:" + contentDetail.getString("imageUrl"));
                        connection = (HttpURLConnection) avatar_miniUrl.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        InputStream In = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(In);
                        contentDetail.setBitmap(bitmap);

//                        URL avatar_normalUrl = new URL("http:" + theHottest.getString("avatar_normal") + ".png");
//                        connection = (HttpURLConnection) avatar_normalUrl.openConnection();
//                        connection.setRequestMethod("GET");
//                        connection.setConnectTimeout(8000);
//                        connection.setReadTimeout(8000);
//                        InputStream avatar_normalIn = connection.getInputStream();
//                        Bitmap avatar_normalBitmap = BitmapFactory.decodeStream(avatar_normalIn);
//                        theHottest.setBitmap("avatar_normal", avatar_normalBitmap);
//
//                        URL avatar_largeUrl = new URL("http:" + theHottest.getString("avatar_large") + ".png");
//                        connection = (HttpURLConnection) avatar_largeUrl.openConnection();
//                        connection.setRequestMethod("GET");
//                        connection.setConnectTimeout(8000);
//                        connection.setReadTimeout(8000);
//                        InputStream avatar_largeIn = connection.getInputStream();
//                        Bitmap avatar_largeBitmap = BitmapFactory.decodeStream(avatar_largeIn);
//                        theHottest.setBitmap("avatar_large", avatar_largeBitmap);
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
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
