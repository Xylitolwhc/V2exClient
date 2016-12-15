package Presenter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ActivityView.PagesView;
import Adapters.MyFragmentAdapter;
import Fragments.TopicFragments;

import static android.content.Context.MODE_APPEND;

/**
 * Created by 吴航辰 on 2016/12/13.
 */

public class PagesPresenterMain implements PagesPresenter {
    private PagesView pagesView;
    private List<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();

    public PagesPresenterMain(PagesView pagesView) {
        this.pagesView = pagesView;
    }

    @Override
    public void refresh() {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        addFragment("https://www.v2ex.com/api/topics/hot.json", "最热主题");
        addFragment("https://www.v2ex.com/api/topics/latest.json", "最新主题");
        SharedPreferences sharedPreferences = pagesView.getTheSharedPreferences("nodes", MODE_APPEND);
        if (sharedPreferences.contains("nodes_number")) {
            for (int i = 1; i <= sharedPreferences.getInt("nodes_number", 1); i++) {
                String name = sharedPreferences.getString("" + i, "none");
                String nodeUrl = sharedPreferences.getString(name, "");
                addFragment(nodeUrl, name);
            }
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("nodes_number", 0);
            editor.commit();
        }
         MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(pagesView.getTheFragmentManager(), fragmentList, titleList);
        pagesView.setAdapter(myFragmentAdapter);

    }

    private void addFragment(String url, String title) {
        TopicFragments jsonFragment = new TopicFragments();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        jsonFragment.setArguments(bundle);
        fragmentList.add(jsonFragment);
        titleList.add(title);
    }

    @Override
    public void init() {

    }

    @Override
    public void editNodes() {

    }
}
