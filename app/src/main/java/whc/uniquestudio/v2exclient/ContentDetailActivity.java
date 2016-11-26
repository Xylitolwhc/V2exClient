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
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 吴航辰 on 2016/11/26.
 */

public class ContentDetailActivity extends Activity {
   private String url,title,content,username;
    private RecyclerView contentDetailRecyclerView;
    private TextView contentDetailTitle,contentDetailUsername,contentDetailDetail,contentDetailContent;
    private Bitmap idImage;
    private ImageView contentDetailIdImage;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentdetail);

        contentDetailRecyclerView=(RecyclerView)findViewById(R.id.contentDetailRecyclerView);
        contentDetailTitle=(TextView)findViewById(R.id.contentDetailTitle);
        contentDetailUsername=(TextView)findViewById(R.id.contentDetailUsername);
        contentDetailDetail=(TextView)findViewById(R.id.contentDetailDetail);
        contentDetailContent=(TextView)findViewById(R.id.contentDetailContent);
        contentDetailIdImage=(ImageView)findViewById(R.id.contentDetailIdImage);

        contentDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent=getIntent();
        if (intent.hasExtra("url")){
            url=intent.getStringExtra("url");
            content=intent.getStringExtra("content");
            title=intent.getStringExtra("title");
            username=intent.getStringExtra("username");
            idImage=intent.getParcelableExtra("idImage");

            contentDetailTitle.setText(title);
            contentDetailContent.setText(content);
            contentDetailUsername.setText(username);
            contentDetailIdImage.setImageBitmap(idImage);
            connectInternet(url);
        }else{
            finish();
        }
    }

    private void connectInternet(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
