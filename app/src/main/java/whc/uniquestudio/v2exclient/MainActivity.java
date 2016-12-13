package whc.uniquestudio.v2exclient;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ActivityView.PagesView;
import Adapters.MyFragmentAdapter;
import Fragments.JsonFragments;
import Fragments.JsoupFragments;
import Presenter.PagesPresenter;
import Presenter.PagesPresenterMain;

public class MainActivity extends FragmentActivity implements PagesView {
    private ViewPager mainViewPager;
    private Button showNodes;
    private MyFragmentAdapter myFragmentAdapter;
    private PagerTabStrip mainViewPagerTitle;

    PagesPresenter pagesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showNodes = (Button) findViewById(R.id.getNodes);
        mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        mainViewPagerTitle=(PagerTabStrip)findViewById(R.id.mainViewPagerTitle);

        pagesPresenter = new PagesPresenterMain(this);
        pagesPresenter.refresh();

        showNodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllNodesActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public void setAdapter(MyFragmentAdapter myFragmentAdapter) {
        mainViewPager.setAdapter(myFragmentAdapter);
        mainViewPager.setOffscreenPageLimit(4);
        mainViewPager.invalidate();
    }

    @Override
    public SharedPreferences getTheSharedPreferences(String name, int mode) {
        return getSharedPreferences(name, mode);
    }

    @Override
    public FragmentManager getTheFragmentManager() {
        return getSupportFragmentManager();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        pagesPresenter.refresh();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("On", "Resume");
    }
}
