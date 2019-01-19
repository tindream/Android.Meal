package tinn.meal.ping.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import tinn.meal.ping.support.Method;

/**
 * 自定义的上圆角矩形ImageView，可以直接当组件在布局中使用。
 *
 * @author caizhiming
 */
public class RoundTopImageView extends RoundImageView {

    public RoundTopImageView(Context context) {
        this(context, null);
    }

    public RoundTopImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundTopImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
    }

    @Override
    protected Bitmap getRoundBitmap(Bitmap bitmap) {
        return Method.getRoundTopBitmap(paint, bitmap, 20);
    }
}
