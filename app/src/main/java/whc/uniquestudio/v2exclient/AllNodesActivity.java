package whc.uniquestudio.v2exclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import Adapters.nodesAdapter;
import Items.Nodes;
import Items.RecycleViewDivider;
import Net.ConnectInternet;

/**
 * Created by 吴航辰 on 2016/12/10.
 */

public class AllNodesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Nodes> nodesList=new ArrayList<>();
    private nodesAdapter nodesAdapter = new nodesAdapter(AllNodesActivity.this, nodesList);
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            nodesAdapter = new nodesAdapter(AllNodesActivity.this, nodesList);
            recyclerView.setAdapter(nodesAdapter);
            nodesAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allnodes);
        recyclerView = (RecyclerView) findViewById(R.id.nodesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL));

        recyclerView.setAdapter(nodesAdapter);
        connectInternet();
    }

    private void connectInternet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                nodesList = ConnectInternet.nodesList("https://www.v2ex.com/api/nodes/all.json");
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
