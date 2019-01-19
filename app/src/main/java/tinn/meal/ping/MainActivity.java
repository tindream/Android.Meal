package tinn.meal.ping;

import android.Manifest;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.loadInfo.LoadInfo;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.CrashHandlers;
import tinn.meal.ping.support.Method;
import tinn.meal.ping.ui.View_Navigation;

public class MainActivity extends MainReceived implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private PopupWindow mPopupWindow;

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
        Method.log("" + v.getId());
        switch (v.getId()) {
            case R.id.action_item1:
                Method.hit(MainActivity.this, "添加好友");
                break;
            case R.id.action_item2:
                Method.hit(MainActivity.this, "发现");
                break;
            case R.id.action_item3:
                Method.hit(MainActivity.this, "发起群聊");
                break;
        }
        //点击PopWindow的item后,关闭此PopWindow
        if (null != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
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
