package tinn.meal.ping.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import tinn.meal.ping.support.Method;

/**
 * 自定义的圆角矩形ImageView，可以直接当组件在布局中使用。
 *
 * @author caizhiming
 */
public class RoundImageView extends android.support.v7.widget.AppCompatImageView {

    protected Paint paint;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
    }

    /**
     * 绘制圆角矩形图片
     *
     * @author caizhiming
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (null != drawable && drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap == null) return;
            Bitmap b = getRoundBitmap(bitmap);
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
            paint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, paint);

        } else {
            super.onDraw(canvas);
        }
    }

    protected Bitmap getRoundBitmap(Bitmap bitmap) {
        return Method.getRoundBitmap(paint, bitmap, 20);
    }
}
