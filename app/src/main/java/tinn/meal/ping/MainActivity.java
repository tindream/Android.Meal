package tinn.meal.ping;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.CrashHandlers;
import tinn.meal.ping.support.FragmentAdapter;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.ui.View_Navigation;

public class MainActivity extends MainReceived implements ViewPager.OnPageChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Method.log("Version=" + this.getString(R.string.version));
        Config.load(this);
        CrashHandlers.getInstance().init();
        super.layoutResID = R.layout.activity_main;
        super.onCreate(savedInstanceState);
        Config.context = this;
        initFragmentsLoad();
        Method.requestPower(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ViewPager vp = findViewById(R.id.mainViewPager);
        vp.addOnPageChangeListener(this);
    }

    @Override
    public void onReady(LoadInfo info) {
        onReadyBase(info);
        ViewPager vp = findViewById(R.id.mainViewPager);
        switch (info.type) {
            case home:
                boolean first = vp.getCurrentItem() != 0;
                vp.setCurrentItem(1, first);
                break;
            case report:
                vp.setCurrentItem(2, true);
                break;
            case order:
                vp.setCurrentItem(3, true);
                break;
            case my:
                vp.setCurrentItem(4, true);
                break;
            case received:
                received(info.msg);
                break;
        }
        switch (info.type) {
            case home:
            case report:
            case order:
            case my:
                View_Navigation navigation = findViewById(R.id.view_Navigation);
                navigation.onClick(info.type);
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        Method.log("onPageSelected=" + position);
        View_Navigation navigation = findViewById(R.id.view_Navigation);
        switch (position) {
            case 1:
                navigation.onClick(LoadType.home);
                break;
            case 2:
                navigation.onClick(LoadType.report);
                break;
            case 3:
                navigation.onClick(LoadType.order);
                break;
            case 4:
                navigation.onClick(LoadType.my);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
