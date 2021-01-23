package com.dreambook;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;
import com.xenione.libs.swipemaker.AbsCoordinatorLayout;
import com.xenione.libs.swipemaker.SwipeLayout;

public class BothSideCoordinatorLayout extends AbsCoordinatorLayout {

    private ImageButton mDelete;
    private ImageButton mAction;
    public SwipeLayout mForegroundView;

    public BothSideCoordinatorLayout(Context context) {
        super(context);
    }

    public BothSideCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public BothSideCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BothSideCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void doInitialViewsLocation() {
        mForegroundView = findViewById(R.id.foregroundView);
        mDelete = findViewById(R.id.delete);
        mAction = findViewById(R.id.action);
        mForegroundView.anchor(-mAction.getWidth(), 0, mDelete.getRight());
    }

    @Override
    public void onTranslateChange(float global, int index, float relative) {
        if (index == 0) {
            if (relative == 0) {

            } else {
            }
        } else {
            if (index == 1 && relative > 0) {
            }
            if (index == 1 && relative == 1){
            }
        }
    }


}
