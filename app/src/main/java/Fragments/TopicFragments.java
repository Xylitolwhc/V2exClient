package Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ActivityView.FragmentView;
import Adapters.TopicsAdapter;
import Items.RecycleViewDivider;
import Presenter.FragmentPresenterMain;
import whc.uniquestudio.v2exclient.R;

/**
 * Created by 吴航辰 on 2016/12/3.
 */

public class TopicFragments extends android.support.v4.app.Fragment implements FragmentView {

    private static final int SHOW_RESPONSE = 0;
    private static final int CONNECT_FAILED = 1;
    private static final int SHOW_PICTURE = 2;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayoutOfTheHottest;
    private FragmentPresenterMain fragmentPresenterMain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_topics_gson, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewOfTheHottest);
        swipeRefreshLayoutOfTheHottest = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutOfTheHottest);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL, R.drawable.divider));//加分割线

        fragmentPresenterMain = new FragmentPresenterMain(getActivity(), this, getArguments().getString("url"));

        init();



        return view;
    }

    private void init() {
        swipeRefreshLayoutOfTheHottest.setRefreshing(true);
        fragmentPresenterMain.init();

        swipeRefreshLayoutOfTheHottest.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragmentPresenterMain.refresh();
            }
        });
    }

    @Override
    public void setAdapter(TopicsAdapter recyclerViewAdapter) {
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void endFresh() {
        swipeRefreshLayoutOfTheHottest.setRefreshing(false);
    }
}
