package tinn.meal.ping.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tinn.meal.ping.R;
import tinn.meal.ping.enums.ILoadListener;
import tinn.meal.ping.enums.LoadType;
import tinn.meal.ping.info.eventInfo.EventInfo;
import tinn.meal.ping.support.Config;
import tinn.meal.ping.support.Method;

public class View_Navigation extends LinearLayout implements View.OnClickListener {
    private int lastId;
    private Paint mPaint = new Paint();
    protected ILoadListener listener;

    public void setCustomListener(ILoadListener listener) {
        this.listener = listener;
    }

    protected void onListener(LoadType type) {
        try {
            if (listener != null)
                listener.onReady(new EventInfo(type));
        } catch (Exception e) {
            Method.log(e);
        }
    }

    public View_Navigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.view_navigation, null));
        mPaint.setColor(context.getColor(R.color.colorGrayBlack));

        findViewById(R.id.line1).setOnClickListener(this);
        findViewById(R.id.line2).setOnClickListener(this);
        findViewById(R.id.line3).setOnClickListener(this);
        findViewById(R.id.line4).setOnClickListener(this);

        TextView textView = findViewById(R.id.line1_text);
        TextPaint paint = textView.getPaint();
        int width = (int) paint.measureText((String) textView.getText());
        int navSize = getResources().getDimensionPixelSize(R.dimen.navSize);
        if (navSize > width) width = navSize;
        int left = Config.display.widthPixels - width * 4 - (int) (Config.display.density * 4);
        left /= 8;
        int top = (int) (Config.display.density * 3);
        findViewById(R.id.line1).setPadding(left, top, left, top);
        findViewById(R.id.line2).setPadding(left, top, left, top);
        findViewById(R.id.line3).setPadding(left, top, left, top);
        findViewById(R.id.line4).setPadding(left, top, left, top);
    }

    @Override
    public void onClick(View v) {
        onClick(v.getId());
    }

    public void onClick(LoadType type) {
        switch (type) {
            case home:
                onClick(R.id.line1);
                break;
            case report:
                onClick(R.id.line2);
                break;
            case order:
                onClick(R.id.line3);
                break;
            case my:
                onClick(R.id.line4);
                break;
        }
    }

    private void onClick(int newId) {
        if (lastId == newId) return;
        switch (lastId) {
            case R.id.line1:
                setBackground(R.id.line1_image, R.drawable.ic_home, R.id.line1_text, R.color.colorBlack);
                break;
            case R.id.line2:
                setBackground(R.id.line2_image, R.drawable.ic_report, R.id.line2_text, R.color.colorBlack);
                break;
            case R.id.line3:
                setBackground(R.id.line3_image, R.drawable.ic_order, R.id.line3_text, R.color.colorBlack);
                break;
            case R.id.line4:
                setBackground(R.id.line4_image, R.drawable.ic_my, R.id.line4_text, R.color.colorBlack);
                break;
        }
        lastId = newId;
        switch (lastId) {
            case R.id.line1:
                setBackground(R.id.line1_image, R.drawable.ic_home_selected, R.id.line1_text, R.color.colorPrimary);
                onListener(LoadType.home);
                break;
            case R.id.line2:
                setBackground(R.id.line2_image, R.drawable.ic_report_selected, R.id.line2_text, R.color.colorPrimary);
                onListener(LoadType.report);
                break;
            case R.id.line3:
                setBackground(R.id.line3_image, R.drawable.ic_order_selected, R.id.line3_text, R.color.colorPrimary);
                onListener(LoadType.order);
                break;
            case R.id.line4:
                setBackground(R.id.line4_image, R.drawable.ic_my_selected, R.id.line4_text, R.color.colorPrimary);
                onListener(LoadType.my);
                break;
        }
    }

    private void setBackground(int imageId, int drawableId, int textId, int colorId) {
        ImageView imageView = findViewById(imageId);
        imageView.setImageResource(drawableId);
        TextView textView = findViewById(textId);
        textView.setTextColor(Config.context.getColor(colorId));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 0, getWidth(), 0, mPaint);
    }
}
