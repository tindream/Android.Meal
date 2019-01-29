package tinn.meal.ping.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import tinn.meal.ping.R;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;

public class CustomViewPager extends ViewPager {
    //不禁止滑动切换item
    public static boolean isCanScroll = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //不销毁的页卡数量，比最大数量小1，全部不销毁
        this.setOffscreenPageLimit(3);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);
    }
}
