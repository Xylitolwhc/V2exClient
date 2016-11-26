package whc.uniquestudio.v2exclient;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Adapters.MyAdapter;
import FromGson.GsonUtil;
import FromGson.TheHottest;

public class MainActivity extends Activity {
    public static final int SHOW_RESPONSE = 0;
    public static final int SHOW_BITMAP = 1;

    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    private List<TheHottest> theHottestList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayoutOfTheHottest;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_RESPONSE) {
                myAdapter = new MyAdapter(MainActivity.this, theHottestList);
                recyclerView.setAdapter(myAdapter);

                myAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                swipeRefreshLayoutOfTheHottest.setRefreshing(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewOfTheHottest);
        swipeRefreshLayoutOfTheHottest = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutOfTheHottest);


        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        //ConnectInternet();


        swipeRefreshLayoutOfTheHottest.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectInternet();
            }
        });
    }

    private void ConnectInternet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("https://www.v2ex.com/api/topics/hot.json");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    theHottestList = GsonUtil.parseJsonArrayWithGson(response.toString(), TheHottest.class);
                    for (int i = 0; i < theHottestList.size(); i++) {//获取头像图片
                        TheHottest theHottest = theHottestList.get(i);
                        URL avatar_miniUrl = new URL("http:" + theHottest.getString("avatar_mini") + ".png");
                        connection = (HttpURLConnection) avatar_miniUrl.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        InputStream avatar_miniIn = connection.getInputStream();
                        Bitmap avatar_miniBitmap = BitmapFactory.decodeStream(avatar_miniIn);
                        theHottest.setBitmap("avatar_mini", avatar_miniBitmap);

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
                    }
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    handler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
