package whc.uniquestudio.v2exclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapters.ContentDetailAdapter;
import FromJsoup.ContentDetail;

/**
 * Created by 吴航辰 on 2016/11/26.
 */

public class ContentDetailActivity extends Activity {
    public static final int SHOW_RESPONSE = 0;
    public static final int CONNECT_FAILED = 1;
    public static final int SHOW_PICTURE=2;

    private String url, title, content, username;
    private RecyclerView contentDetailRecyclerView;
    private TextView contentDetailTitle, contentDetailUsername, contentDetailDetail, contentDetailContent;
    private Bitmap idImage;
    private ImageView contentDetailIdImage;
    private ContentDetailAdapter contentDetailAdapter;
    private List<ContentDetail> contentDetailList=new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_RESPONSE:{
                    String s=(String)msg.obj;
                    ((TextView)findViewById(R.id.testText2)).setText(s);
                    contentDetailAdapter=new ContentDetailAdapter(ContentDetailActivity.this,contentDetailList);
                    contentDetailRecyclerView.setAdapter(contentDetailAdapter);
                    break;
                }
                case CONNECT_FAILED:{
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentdetail);

        //doFindViewById();
        //doGetIntent();
        contentDetailRecyclerView=(RecyclerView)findViewById(R.id.contentDetailRecyclerView);
        contentDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

//    private void doGetIntent() {
//        Intent intent = getIntent();
//        if (intent.hasExtra("url")) {
//            url = intent.getStringExtra("url");
//            content = intent.getStringExtra("content");
//            title = intent.getStringExtra("title");
//            username = intent.getStringExtra("username");
//            idImage = intent.getParcelableExtra("idImage");
//
//            contentDetailTitle.setText(title);
//            contentDetailContent.setText(content);
//            contentDetailUsername.setText(username);
//            contentDetailIdImage.setImageBitmap(idImage);
//            connectInternet(url);
//        } else {
//            finish();
//        }
//    }

    private void connectInternet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements nodes=document.getElementsByClass("reply_content");
                    Element userNames=document.getElementsByClass("box").get(1);

                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i=0;i<nodes.size();i++){
                        Element element2=nodes.get(i).select("a href").first();
                        ContentDetail contentDetail=new ContentDetail();
                        contentDetail.setString("replyContent",nodes.get(i).text().toString());
                        contentDetailList.add(contentDetail);
                        stringBuffer.append(nodes.get(i).text().toString()+"\n");
                    }
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj=stringBuffer.toString();
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
