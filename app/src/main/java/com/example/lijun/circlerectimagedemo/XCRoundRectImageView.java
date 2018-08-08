package com.example.lijun.circlerectimagedemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by lijun on 17-11-29.
 */

@SuppressLint("AppCompatCustomView")
public class XCRoundRectImageView extends ImageView {
    private Paint mPhotoPaint;
    private int mRoundRadius;
    private int mPhotoType;
    private static final int DEFAULT_TYPE = 0;
    private static final int ROUND = 1;
    private static final int ROUND_CORNERS_RECTANGLE = 2;

    public XCRoundRectImageView(Context context) {
        this(context, null);
    }

    public XCRoundRectImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XCRoundRectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPhotoPaint = new Paint();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundRectImageView, defStyleAttr, 0);

        int radius = a.getInteger(R.styleable.RoundRectImageView_rr_radius, 0);
        mRoundRadius = radius > 0 ? radius : 0;
        int type = a.getInteger(R.styleable.RoundRectImageView_rr_shape_type, 0);
        if (type > 0 && type < 3) {
            mPhotoType = type;
        }else{
            mPhotoType = DEFAULT_TYPE;
        }
    }

    /**
     * 绘制圆形或者圆角矩形图片
     * @author caizhiming
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (null != drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = getRoundBitmap(bitmap, mRoundRadius);
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
            mPhotoPaint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, mPhotoPaint);

        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 获取圆形或者圆角矩形图片
     *
     * @param bitmap
     * @param radius
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getRoundBitmap(Bitmap bitmap, int radius) {
        if(mPhotoType == DEFAULT_TYPE){
            return bitmap;
        }else{
            Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(outputBitmap);
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            mPhotoPaint.setAntiAlias(true);
            if(mPhotoType == ROUND) {
                canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, mPhotoPaint);
            }else{
                final RectF rectF = new RectF(rect);
                canvas.drawRoundRect(rectF, radius, radius, mPhotoPaint);
            }
            mPhotoPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, mPhotoPaint);
            return outputBitmap;
        }
    }
}