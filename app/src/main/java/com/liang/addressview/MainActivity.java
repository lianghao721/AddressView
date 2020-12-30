package com.liang.addressview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.liang.addressview.View.AddressPickerView;
import com.liang.addressview.View.PopUtils;

public class MainActivity extends AppCompatActivity {
    Context mContext;
    PopUtils pop;
    TextView tvTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        tvTips = findViewById(R.id.tv_tips);
    }

    private void initSpecPopWindow() {
        pop = new PopUtils(this, R.layout.pop_choose_address, R.style.AnimBottom, ViewGroup.LayoutParams.MATCH_PARENT, dp2px(540));
        View view = pop.getview();
        ImageView close = view.findViewById(R.id.iv_close);
        close.setOnClickListener(v -> {
            pop.dismiss();
        });
        AddressPickerView addressView = view.findViewById(R.id.addressView);
        addressView.setOnAddressListener((address, provinceCode, cityCode, districtCode, townCode) -> {
            tvTips.setText(address);
            tvTips.setTextColor(Color.RED);
            pop.dismiss();
        });
    }

    public void ShowView(View v) {
        initSpecPopWindow();
        pop.showAtLocation(tvTips, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 尺寸dip
     * @return 像素值
     */
    public int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}