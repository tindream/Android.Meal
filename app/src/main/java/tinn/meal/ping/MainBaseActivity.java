package tinn.meal.ping;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import tinn.meal.ping.activity.LoginActivity;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.enums.requestType;
import tinn.meal.ping.fragments.Fragment_Home;
import tinn.meal.ping.fragments.Fragment_My;
import tinn.meal.ping.fragments.Fragment_Order;
import tinn.meal.ping.fragments.Fragment_Report;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.mqtt.MQTTService;
import tinn.meal.ping.mqtt.MyServiceConnection;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.FragmentAdapter;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.ui.BaseActivity;
import tinn.meal.ping.ui.View_Navigation;

public class MainBaseActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, ILoadListener {
    protected List<Fragment> fragmentList = new ArrayList<Fragment>();
    //菜单
    private PopupWindow mPopupWindow;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //通过requestCode来识别是否同一个请求
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意，执行操作
                Config.load(this);
            }
        }
    }

    @Override
    public void onReady(LoadInfo info) {
    }

    //横坚屏切换
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Config.load(this);
        if (Config.view_base != null)
            Config.view_base.autoSize();
    }

    protected void initFragmentsHome() {
        Fragment_Home fragment_home = new Fragment_Home();
        fragment_home.setListener(this);
        fragmentList.add(fragment_home);

        ViewPager vp = findViewById(R.id.mainViewPager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
        vp.setAdapter(fragmentAdapter);
    }

    protected void initFragmentsOther() {
        View_Navigation navigation = findViewById(R.id.view_Navigation);
        navigation.setCustomListener(this);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == requestType.loginResult) {
            Config.serviceConnection.setListener(this);
            switch (requestCode) {
                case requestType.login:
                    Bundle bundle = data.getExtras();  //取得来自子窗口的数据模块
                    String userId = bundle.getString("UserId");
                    initFragmentsOther();
                    onReady(new LoadInfo(LoadType.home));
                    break;
                case requestType.loginUpdate:
                    Fragment_My Fragment_my = (Fragment_My) fragmentList.get(3);
                    Fragment_my.loadUser();
                    break;
            }
        }
    }

    protected void onReadyBase(LoadInfo info) {
        switch (info.Types) {
            case load:
                Config.load();
                Config.serviceConnection = new MyServiceConnection();
                Config.serviceConnection.setListener(MainBaseActivity.this);
                Intent intent = new Intent(Config.context, MQTTService.class);
                bindService(intent, Config.serviceConnection, Context.BIND_AUTO_CREATE);
                if (Config.Admin.UserId == 0) {
                    intent = new Intent(this, LoginActivity.class);
                    //为了接受SecondActivity中的值，不用startActivity(intent)
                    startActivityForResult(intent, requestType.login);
                } else {
                    initFragmentsOther();
                    onReady(new LoadInfo(LoadType.home));
                }
                break;
            case Error:
                Fragment_Home fragment_home = (Fragment_Home) fragmentList.get(0);
                fragment_home.error(info);
                break;
            case complete:
                onReady(new LoadInfo(LoadType.home));
                fragment_home = (Fragment_Home) fragmentList.get(0);
                fragment_home.load(true);
                break;
        }
    }

    @Override
    public void onDestroy() {
        unbindService(Config.serviceConnection);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                popUpMyOverflow();
                break;
        }
        return false;
    }

    /**
     * 弹出自定义的popWindow
     */
    public void popUpMyOverflow() {
        //获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        //状态栏高度+toolbar的高度
        int yOffset = frame.top + toolbar.getHeight();

        if (null == mPopupWindow) {
            //初始化PopupWindow的布局
            View popView = getLayoutInflater().inflate(R.layout.action_popwindow, null);
            //popView即popupWindow的布局，ture设置focusAble.
            mPopupWindow = new PopupWindow(popView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            //点击外部关闭。
            mPopupWindow.setOutsideTouchable(true);
            //设置一个动画。
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            popView.findViewById(R.id.action_item1).setOnClickListener(this);
            popView.findViewById(R.id.action_item2).setOnClickListener(this);
            popView.findViewById(R.id.action_item3).setOnClickListener(this);
        }
        //设置Gravity，让它显示在右上角。
        mPopupWindow.showAtLocation(toolbar, Gravity.RIGHT | Gravity.TOP, 0, yOffset);
    }

    //PopupWindow的监听回调事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_item1:
                Method.hit("添加好友");
                break;
            case R.id.action_item2:
                Method.hit("发起群聊");
                break;
            case R.id.action_item3:
                Method.hit("帮助与反馈");
                break;
        }
        //点击PopWindow的item后,关闭此PopWindow
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onPageSelected(int position) {
        View_Navigation navigation = findViewById(R.id.view_Navigation);
        switch (position) {
            case 0:
                navigation.onClick(LoadType.home);
                break;
            case 1:
                navigation.onClick(LoadType.report);
                break;
            case 2:
                navigation.onClick(LoadType.order);
                break;
            case 3:
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
