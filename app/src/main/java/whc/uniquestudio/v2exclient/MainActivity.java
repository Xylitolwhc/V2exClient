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

        JsonFragments theHottestFragment = new JsonFragments();
        theHottestFragment.setUrl("https://www.v2ex.com/api/topics/hot.json");
        JsonFragments theNewestFragment = new JsonFragments();
        theNewestFragment.setUrl("https://www.v2ex.com/api/topics/latest.json");
        JsoupFragments pythonJsoup=new JsoupFragments();
        pythonJsoup.setUrl("https://www.v2ex.com/go/python");
        JsoupFragments programmerJsoup=new JsoupFragments();
        programmerJsoup.setUrl("https://www.v2ex.com/go/programmer");

        fragmentList.add(theHottestFragment);
        titleList.add("最热主题");
        fragmentList.add(theNewestFragment);
        titleList.add("最新主题");
        fragmentList.add(pythonJsoup);
        titleList.add("Python");
        fragmentList.add(programmerJsoup);
        titleList.add("程序员");

        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList,titleList);
        mainViewPager.setAdapter(myFragmentAdapter);
    }


}
