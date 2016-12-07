package whc.uniquestudio.v2exclient;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.ImageView;

import org.markdown4j.Markdown4jProcessor;

import java.util.ArrayList;
import java.util.List;

import Adapters.MyFragmentAdapter;
import Fragments.JsonFragments;
import Fragments.JsoupFragments;

public class MainActivity extends FragmentActivity {

    private ViewPager mainViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        mainViewPager.setOffscreenPageLimit(4);

        addJsonFragment("https://www.v2ex.com/api/topics/hot.json","最热主题");
        addJsonFragment("https://www.v2ex.com/api/topics/latest.json","最新主题");
        addJsoupFragment("https://www.v2ex.com/go/python","Python");
        addJsoupFragment("https://www.v2ex.com/go/programmer","程序员");

        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList,titleList);
        mainViewPager.setAdapter(myFragmentAdapter);
    }

    private void addJsonFragment(String url,String title){
        JsonFragments jsonFragment=new JsonFragments();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        jsonFragment.setArguments(bundle);
        fragmentList.add(jsonFragment);
        titleList.add(title);
    }
    private void addJsoupFragment(String url,String title){
        JsoupFragments jsoupFragment=new JsoupFragments();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        jsoupFragment.setArguments(bundle);
        fragmentList.add(jsoupFragment);
        titleList.add(title);
    }
}
