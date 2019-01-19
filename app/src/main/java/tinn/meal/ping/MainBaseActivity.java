package tinn.meal.ping;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.fragments.Fragment_Home;
import tinn.meal.ping.fragments.Fragment_Load;
import tinn.meal.ping.fragments.Fragment_My;
import tinn.meal.ping.fragments.Fragment_Order;
import tinn.meal.ping.fragments.Fragment_Report;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.AsyncWait;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.FragmentAdapter;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.ui.BaseActivity;
import tinn.meal.ping.ui.View_Navigation;
import tinn.meal.ping.view.View_About;

public class MainBaseActivity extends BaseActivity implements ILoadListener {
    protected List<Fragment> fragmentList = new ArrayList<Fragment>();

    @Override
    public void onReady(LoadInfo info) {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Config.load(this);
        if (Config.view_base != null)
            Config.view_base.autoSize();
    }

    protected void initFragmentsLoad() {
        Fragment_Load fragment_load = new Fragment_Load();
        fragment_load.setListener(this);
        fragmentList.add(fragment_load);

        ViewPager vp = findViewById(R.id.mainViewPager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
        vp.setAdapter(fragmentAdapter);
    }

    protected void initFragmentsOther() {
        View_Navigation navigation = findViewById(R.id.view_Navigation);
        navigation.setCustomListener(this);

        Fragment_Home fragment_home = new Fragment_Home();
        fragment_home.setListener(this);
        fragmentList.add(fragment_home);

        Fragment_Report fragment_report = new Fragment_Report();
        fragment_report.setListener(this);
        fragmentList.add(fragment_report);

        Fragment_Order fragment_order = new Fragment_Order();
        fragment_order.setListener(this);
        fragmentList.add(fragment_order);

        Fragment_My fragment_my = new Fragment_My();
        fragment_my.setListener(this);
        fragmentList.add(fragment_my);

        ViewPager vp = findViewById(R.id.mainViewPager);
        vp.getAdapter().notifyDataSetChanged();

//        vp.setCurrentItem(1, false);
//        adapter.removeItem(0);
    }

    protected void onReadyBase(LoadInfo info) {
        switch (info.type) {
            case load:
                Config.load();
                Config.client.setListener(this);
                initFragmentsOther();
                onReady(new LoadInfo(LoadType.home));
                break;
            case complete:
                break;
        }
    }
}
