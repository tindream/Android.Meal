package tinn.meal.ping;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.AsyncLoad;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.CrashHandlers;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.ui.View_Navigation;

public class MainActivity extends MainReceived {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Method.log("Version=" + this.getString(R.string.version));
        Config.load(this);
        CrashHandlers.getInstance().init();
        super.layoutResID = R.layout.activity_main;
        super.onCreate(savedInstanceState);
        Config.context = this;
        new AsyncLoad().setListener(this);
        initFragmentsHome();
        Method.requestPower(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ViewPager vp = findViewById(R.id.mainViewPager);
        vp.addOnPageChangeListener(this);
    }

    @Override
    public void onReady(LoadInfo info) {
        onReadyBase(info);
        ViewPager vp = findViewById(R.id.mainViewPager);
        switch (info.Types) {
            case home:
                vp.setCurrentItem(0, true);
                break;
            case report:
                vp.setCurrentItem(1, true);
                break;
            case order:
                vp.setCurrentItem(2, true);
                break;
            case my:
                vp.setCurrentItem(3, true);
                break;
            case received:
                received(info.Message);
                break;
        }
        switch (info.Types) {
            case home:
            case report:
            case order:
            case my:
                View_Navigation navigation = findViewById(R.id.view_Navigation);
                navigation.onClick(info.Types);
                break;
        }
    }
}
