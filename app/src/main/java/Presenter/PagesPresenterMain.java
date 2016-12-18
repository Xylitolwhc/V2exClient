package Presenter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Map<String, ?> stringMap = sharedPreferences.getAll();
        for (Map.Entry<String, ?> stringEntry : stringMap.entrySet()) {
            Log.d(stringEntry.getKey(), stringEntry.getValue().toString());
            addFragment(stringEntry.getValue().toString(), stringEntry.getKey());
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
