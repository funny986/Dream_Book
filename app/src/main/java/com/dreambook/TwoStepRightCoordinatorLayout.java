package com.dreambook;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.widget.RelativeLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.xenione.libs.swipemaker.AbsCoordinatorLayout;
import com.xenione.libs.swipemaker.SwipeLayout;



/**
 * Created on 06/04/16.
 */
public class TwoStepRightCoordinatorLayout extends AbsCoordinatorLayout {

    private ImageView mBg;
    private View mDelete;
    private View mAction;
    private SwipeLayout mForegroundView;

    public enum Color {
        PINK(R.color.colorAccent),
        BLUE(R.color.colorEditMean);

        private final int resId;
        private ColorFilter color;

        Color(int resId) {
            this.resId = resId;
        }

        public ColorFilter getColor(Context context) {
            if (color == null) {
                color = new PorterDuffColorFilter(
                        ResourcesCompat.getColor(context.getResources(), resId, context.getTheme()), PorterDuff.Mode.MULTIPLY);
            }
            return color;
        }
    }

    public TwoStepRightCoordinatorLayout(Context context) {
        super(context);
    }

    public TwoStepRightCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoStepRightCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TwoStepRightCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void doInitialViewsLocation() {
        mForegroundView = findViewById(R.id.foregroundView);
        RelativeLayout mBackgroundView =  findViewById(R.id.backgroundView);
//        mBg = (ImageView) findViewById(R.id.bg_disc);
        mDelete = findViewById(R.id.delete);
        mAction = findViewById(R.id.action);

//        mForegroundView.anchor(-mBackgroundView.getLeft(), -mBackgroundView.getRight());
//        mForegroundView.anchor(this.getRight(), this.getLeft());
//        mForegroundView.anchor(-this.getRight(), 0, this.getWidth());
//        mForegroundView.setPivotX(this.getWidth() / 2);
//        mForegroundView.setPivotY(0);
//        mForegroundView.anchor(this.getLeft(), this.getRight());
        mForegroundView.anchor(-mAction.getLeft(), -mDelete.getLeft(), 0);
    }

    @Override
    public void onTranslateChange(float global, int index, float relative) {
        if (index == 0) {
            if (relative == 0) {
//                removeColorFilter();
            } else {
//                applyColorFilter(Color.PINK);
            }
        } else if (index == 1 && relative > 0) {
//            applyColorFilter(Color.BLUE);
        }
    }

    private void removeColorFilter(){
        mBg.clearColorFilter();
    }

    private void applyColorFilter(Color color) {
//        if (DrawableCompat.getColorFilter(mBg.getDrawable()) == color.getColor(getContext())) {
//            return;
//        }
//        mBg.setColorFilter(color.getColor(getContext()));
    }
}
