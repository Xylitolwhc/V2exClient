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

    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    private List<TheHottest> theHottestList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayoutOfTheHottest;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_RESPONSE) {
                String response = (String) (msg.obj);
                theHottestList = GsonUtil.parseJsonArrayWithGson(response, TheHottest.class);

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
        ConnectInternet();


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
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = response.toString();
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
