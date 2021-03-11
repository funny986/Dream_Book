package com.dreambook;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.widget.RelativeLayout;
import androidx.core.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import com.xenione.libs.swipemaker.AbsCoordinatorLayout;
import com.xenione.libs.swipemaker.SwipeLayout;

public class TwoStepRightCoordinatorLayout extends AbsCoordinatorLayout {

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
        SwipeLayout mForegroundView = findViewById(R.id.foregroundView);
        RelativeLayout mBackgroundView =  findViewById(R.id.backgroundView);
        View mDelete = findViewById(R.id.delete);
        View mAction = findViewById(R.id.action);
        mForegroundView.anchor(-mAction.getLeft(), 0, mDelete.getLeft());
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
}

