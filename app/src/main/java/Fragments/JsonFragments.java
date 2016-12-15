package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ActivityView.FragmentView;
import Adapters.MyAdapter;
import Items.RecycleViewDivider;
import Items.TopicsFromJson;
import Net.ConnectInternet;
import Presenter.PagesFragmentPresenter;
import whc.uniquestudio.v2exclient.MainActivity;
import whc.uniquestudio.v2exclient.R;

/**
 * Created by 吴航辰 on 2016/12/3.
 */

public class JsonFragments extends android.support.v4.app.Fragment implements FragmentView{

    private static final int SHOW_RESPONSE = 0;
    private static final int CONNECT_FAILED = 1;
    private static final int SHOW_PICTURE = 2;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayoutOfTheHottest;
    private PagesFragmentPresenter pagesFragmentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_topics_gson, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewOfTheHottest);
        swipeRefreshLayoutOfTheHottest = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutOfTheHottest);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL, R.drawable.divider));

        pagesFragmentPresenter=new PagesFragmentPresenter(getActivity(),this,getArguments().getString("url"));


        swipeRefreshLayoutOfTheHottest.setRefreshing(true);
        pagesFragmentPresenter.refresh();

        swipeRefreshLayoutOfTheHottest.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pagesFragmentPresenter.refresh();
            }
        });

        return view;
    }

    @Override
    public void setAdapter(MyAdapter recyclerViewAdapter) {
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void endFresh() {
        swipeRefreshLayoutOfTheHottest.setRefreshing(false);
    }
}
