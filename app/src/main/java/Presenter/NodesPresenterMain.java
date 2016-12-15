package Presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import ActivityView.NodesView;
import Adapters.NodesAdapter;
import Items.Nodes;
import Net.ConnectInternet;
import whc.uniquestudio.v2exclient.AllNodesActivity;

/**
 * Created by 吴航辰 on 2016/12/15.
 */

public class NodesPresenterMain implements NodesPresenter {
    private NodesView nodesView;
    private List<Nodes> nodesList;
    private NodesAdapter nodesAdapter;
    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            nodesAdapter = new NodesAdapter(context, nodesList);
           nodesView.setAdapter(nodesAdapter);
        }
    };

    public NodesPresenterMain(NodesView nodesView,Context context) {
        this.nodesView = nodesView;
        this.context=context;
    }

    @Override
    public void init() {
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
}
