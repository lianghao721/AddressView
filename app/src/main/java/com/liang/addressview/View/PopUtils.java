package com.liang.addressview.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/*
 * PopupWindow工具类
 * Author:Lianghao
 * CreateTime:2020/09/11
 */
public class PopUtils extends PopupWindow {
    private Activity activity;
    private int layout;
    private View view;
    private int styleId;
    private int width;
    private int height;

    public PopUtils(Activity activity, int layout, int styleId) {
        this.activity = activity;
        this.layout = layout;
        this.styleId = styleId;
        this.width = ViewGroup.LayoutParams.MATCH_PARENT;
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        initPop();
    }

    public PopUtils(Activity activity, int layout, int styleId, int width, int height) {
        this.activity = activity;
        this.layout = layout;
        this.styleId = styleId;
        this.width = width;
        this.height = height;
        initPop();
    }

    public View getview() {
        return view;
    }

    public <T extends View> T findView(int resId) {
        return (T) getview().findViewById(resId);
    }

    @SuppressLint("WrongConstant")
    private void initPop() {
        view = activity.getLayoutInflater().inflate(layout, null);
        setWidth(width);
        setHeight(height);
        setClippingEnabled(false);//设置沉浸
        setFocusable(true);
        setOutsideTouchable(true);
//        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(view);
        if (styleId != 0) {
            setAnimationStyle(styleId);
        }
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//      消失的时候设置窗体背景变亮
        setOnDismissListener(this::dismiss);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        backgroundAlpha(1f);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        backgroundAlpha(0.8f);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        backgroundAlpha(0.8f);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.8f);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1f);
    }

    /*
     * 设置添加屏幕的背景透明度
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}
