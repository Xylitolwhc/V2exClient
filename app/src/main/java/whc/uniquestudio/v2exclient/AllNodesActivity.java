package whc.uniquestudio.v2exclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ActivityView.NodesView;
import Adapters.NodesAdapter;
import Items.Nodes;
import Items.RecycleViewDivider;
import Net.ConnectInternet;
import Presenter.NodesPresenter;
import Presenter.NodesPresenterMain;

/**
 * Created by 吴航辰 on 2016/12/10.
 */

public class AllNodesActivity extends AppCompatActivity implements NodesView{
    private RecyclerView recyclerView;
    private NodesPresenter nodesPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allnodes);

        getSupportActionBar().setTitle("NODES");

        recyclerView = (RecyclerView) findViewById(R.id.nodesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL));

        nodesPresenter=new NodesPresenterMain(this,this);
        nodesPresenter.init();
    }

    @Override
    public void setAdapter(NodesAdapter nodesAdapter) {
        recyclerView.setAdapter(nodesAdapter);
    }

    @Override
    public void onBackPressed() {
        sendResult();
        super.onBackPressed();
    }
    private void sendResult(){
        Intent intent=new Intent();
        setResult(1,intent);
    }
}
