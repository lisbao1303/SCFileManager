package com.metaconsultoria.root.scfilemanager;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import me.dm7.barcodescanner.core.DisplayUtils;
import me.dm7.barcodescanner.core.IViewFinder;

////Custom View Leitor de QR
public class CustomZXingScannerView extends View implements IViewFinder {

    private Rect mFramingRect;

    private static final float PORTRAIT_WIDTH_RATIO = 6f / 8;
    private static final float PORTRAIT_WIDTH_HEIGHT_RATIO = 0.75f;

    private static final float LANDSCAPE_HEIGHT_RATIO = 5f / 8;
    private static final float LANDSCAPE_WIDTH_HEIGHT_RATIO = 1.4f;
    private static final int MIN_DIMENSION_DIFF = 50;

    private static final float SQUARE_DIMENSION_RATIO = 5f / 8;

    private static final int[] SCANNER_ALPHA = {100,80,60,40,20,20,40,60,80,100};
    private int scannerAlpha;
    private static final long ANIMATION_DELAY = 1;

    private final int mDefaultMaskColor = getResources().getColor(R.color.viewfinder_mask);
    private final int mDefaultBorderColor = getResources().getColor(R.color.viewfinder_border);
    private int mDefaultBorderStrokeWidth = 30;
    private int mDefaultBorderLineLength = 110;

    protected Paint mFinderMaskPaint;
    protected Paint mBorderPaint;
    protected int mBorderLineLength;
    protected boolean mSquareViewFinder;
    private Context context;

    public CustomZXingScannerView(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public CustomZXingScannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    private void init() {
        mDefaultBorderLineLength = (int)dp(45,context);
        mDefaultBorderStrokeWidth = (int)dp(6,context);
        //set up laser paint

        //finder mask paint
        mFinderMaskPaint = new Paint();
        mFinderMaskPaint.setColor(mDefaultMaskColor);

        //border paint
        mBorderPaint = new Paint();
        mBorderPaint.setColor(mDefaultBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mDefaultBorderStrokeWidth);

        mBorderLineLength = mDefaultBorderLineLength;
    }


    @Override
    public void setLaserColor(int i) {

    }

    public void setMaskColor(int maskColor) {
        mFinderMaskPaint.setColor(maskColor);
    }

    public void setBorderColor(int borderColor) {
        mBorderPaint.setColor(borderColor);
    }

    public void setBorderStrokeWidth(int borderStrokeWidth) {
        mBorderPaint.setStrokeWidth(borderStrokeWidth);
    }

    public void setBorderLineLength(int borderLineLength) {
        mBorderLineLength = borderLineLength;
    }

    @Override
    public void setLaserEnabled(boolean b) {

    }

    @Override
    public void setBorderCornerRounded(boolean b) {

    }

    @Override
    public void setBorderAlpha(float v) {

    }

    @Override
    public void setBorderCornerRadius(int i) {

    }

    @Override
    public void setViewFinderOffset(int i) {

    }

    public void setSquareViewFinder(boolean set) {
        mSquareViewFinder = set;
    }

    public void setupViewFinder() {
        updateFramingRect();
        invalidate();
    }

    @Override
    public Rect getFramingRect() {
        return mFramingRect;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (getFramingRect() == null) {
            return;
        }

        drawViewFinderMask(canvas);
        drawViewFinderBorder(canvas);
        drawAnimation(canvas);
    }

    public static float dp(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public void drawViewFinderMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Rect framingRect = getFramingRect();

        canvas.drawRect(0, 0, width, framingRect.top, mFinderMaskPaint);
        canvas.drawRect(0, framingRect.top, framingRect.left, framingRect.bottom , mFinderMaskPaint);
        canvas.drawRect(framingRect.right , framingRect.top, width, framingRect.bottom , mFinderMaskPaint);
        canvas.drawRect(0, framingRect.bottom , width, height, mFinderMaskPaint);
    }

    public void drawViewFinderBorder(Canvas canvas) {
        Rect framingRect = getFramingRect();

        canvas.drawLine(framingRect.left -(mDefaultBorderStrokeWidth/2) , framingRect.top -mDefaultBorderStrokeWidth, framingRect.left -(mDefaultBorderStrokeWidth/2) , framingRect.top  + mBorderLineLength, mBorderPaint);
        canvas.drawLine(framingRect.left -mDefaultBorderStrokeWidth, framingRect.top -(mDefaultBorderStrokeWidth/2) , framingRect.left  + mBorderLineLength, framingRect.top -(mDefaultBorderStrokeWidth/2), mBorderPaint);

        canvas.drawLine(framingRect.left -(mDefaultBorderStrokeWidth/2), framingRect.bottom +mDefaultBorderStrokeWidth, framingRect.left -(mDefaultBorderStrokeWidth/2), framingRect.bottom  - mBorderLineLength, mBorderPaint);
        canvas.drawLine(framingRect.left -mDefaultBorderStrokeWidth, framingRect.bottom +(mDefaultBorderStrokeWidth/2), framingRect.left  + mBorderLineLength, framingRect.bottom +(mDefaultBorderStrokeWidth/2), mBorderPaint);

        canvas.drawLine(framingRect.right+(mDefaultBorderStrokeWidth/2), framingRect.top -mDefaultBorderStrokeWidth, framingRect.right+(mDefaultBorderStrokeWidth/2) , framingRect.top  + mBorderLineLength, mBorderPaint);
        canvas.drawLine(framingRect.right +mDefaultBorderStrokeWidth, framingRect.top -(mDefaultBorderStrokeWidth/2) , framingRect.right  - mBorderLineLength, framingRect.top -(mDefaultBorderStrokeWidth/2) , mBorderPaint);

        canvas.drawLine(framingRect.right +(mDefaultBorderStrokeWidth/2), framingRect.bottom +mDefaultBorderStrokeWidth, framingRect.right+(mDefaultBorderStrokeWidth/2) , framingRect.bottom  - mBorderLineLength, mBorderPaint);
        canvas.drawLine(framingRect.right +mDefaultBorderStrokeWidth , framingRect.bottom +(mDefaultBorderStrokeWidth/2), framingRect.right  - mBorderLineLength, framingRect.bottom +(mDefaultBorderStrokeWidth/2), mBorderPaint);
    }
    public void drawAnimation(Canvas canvas) {
        Rect framingRect = getFramingRect();
        Bitmap bipmap5 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_souzacruzqr);
        Paint paint = new Paint();
        paint.setAlpha(100);
        Point center = new Point((framingRect.width() / 2)+framingRect.left, (framingRect.height() / 2)+framingRect.top);
        int rectW = mBorderLineLength+(mDefaultBorderStrokeWidth/2)+ (int)dp(20,context);
        int rectH = (int)(rectW*0.60);
        int left = center.x - (rectW / 2);
        int top = center.y - (rectH / 2);
        int right = center.x + (rectW / 2);
        int bottom = center.y + (rectH / 2);
        Rect rect = new Rect(left, top, right, bottom);

        canvas.drawBitmap(getResizedBitmap(bipmap5,rectW,rectH),null,rect,paint);

        //postInvalidateDelayed(ANIMATION_DELAY);
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        updateFramingRect();
    }

    public synchronized void updateFramingRect() {
        Point viewResolution = new Point(getWidth(), getHeight());
        int width;
        int height;
        int orientation = DisplayUtils.getScreenOrientation(getContext());

        if (mSquareViewFinder) {
            if (orientation != Configuration.ORIENTATION_PORTRAIT) {
                height = (int) (getHeight() * SQUARE_DIMENSION_RATIO);
                width = height;
            } else {
                width = (int) (getWidth() * SQUARE_DIMENSION_RATIO);
                height = width;
            }
        } else {
            if (orientation != Configuration.ORIENTATION_PORTRAIT) {
                height = (int) (getHeight() * LANDSCAPE_HEIGHT_RATIO);
                width = (int) (LANDSCAPE_WIDTH_HEIGHT_RATIO * height);
            } else {
                width = (int) (getWidth() * PORTRAIT_WIDTH_RATIO);
                height = (int) (PORTRAIT_WIDTH_HEIGHT_RATIO * width);
            }
        }

        if (width > getWidth()) {
            width = getWidth() - MIN_DIMENSION_DIFF;
        }

        if (height > getHeight()) {
            height = getHeight() - MIN_DIMENSION_DIFF;
        }

        int leftOffset = (viewResolution.x - width) / 2;
        int topOffset = (viewResolution.y - height) / 2;
        mFramingRect = new Rect(leftOffset, topOffset-(int)dp(30,context), leftOffset + width, topOffset + height+(int)dp(30,context));    }
}