package com.liang.addressview.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.google.android.material.tabs.TabLayout;
import com.liang.addressview.R;
import com.xuexiang.xutil.resource.ResourceUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * 四级地址选择器-省、市、区县、镇
 * Author:Lianghao
 * CreateTime:2020/9/28
 */

public class AddressPickerView extends RelativeLayout {
    // recyclerView 选中Item 的颜色
    private int defaultSelectedColor = 0xffCD853F;
    // recyclerView 未选中Item 的颜色
    private int defaultUnSelectedColor = 0xff262626;
    // TabLayout字体-指示器颜色
    private int defaultTabSelectedColor = 0xffCD853F;
    // TabLayout字体大小
    private int tabSelectTextSize = 14;
    // RecyclerView Item字体大小
    private int recyclerItemTvSize = 14;

    private TabLayout mTabLayout; // tabLayout
    private RecyclerView mRvList; // 显示数据的RecyclerView
    private String defaultProvince = "请选择"; //显示在上面tab中的省份
    private String defaultCity = ""; //显示在上面tab中的城市
    private String defaultDistrict = ""; //显示在上面tab中的区县
    private String defaultTown = ""; //显示在上面tab中的镇
    private List<AddressBean.AddressItemBean> mRvData; // 用来在recyclerview显示的数据
    private AddressAdapter mAdapter;   // recyclerview 的 adapter

    private AddressBean mAddressBean; // 总数据
    private AddressBean.AddressItemBean mSelectProvice; //选中 省份 bean
    private AddressBean.AddressItemBean mSelectCity;//选中 城市  bean
    private AddressBean.AddressItemBean mSelectDistrict;//选中 区县  bean
    private AddressBean.AddressItemBean mSelectTown;//选中 镇  bean

    private int mSelectProvicePosition = 0; //选中 省份 位置
    private int mSelectCityPosition = 0;//选中 城市  位置
    private int mSelectDistrictPosition = 0;//选中 区县  位置
    private int mSelectTownPosition = 0;//选中 镇  位置

    private OnAddressListener onAddressListener;

    public AddressPickerView(Context context) {
        super(context);
        init(context);
    }

    public AddressPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddressPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    @SuppressLint("WrongConstant")
    private void init(Context context) {
        mRvData = new ArrayList<>();
        // UI
        View rootView = LayoutInflater.from(context).inflate(R.layout.address_picker_view, this);
        // tablayout初始化
        mTabLayout = rootView.findViewById(R.id.tlTabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText(defaultProvince));
        mTabLayout.addTab(mTabLayout.newTab().setText(defaultCity));
        mTabLayout.addTab(mTabLayout.newTab().setText(defaultDistrict));
        mTabLayout.addTab(mTabLayout.newTab().setText(defaultTown));

        mTabLayout.addOnTabSelectedListener(tabSelectedListener);
//        setTabLayoutTextFont(mTabLayout, new Typeface());//设置TabLayout自定义字体
        // recyclerview adapter的绑定
        mRvList = rootView.findViewById(R.id.rvList);
        mRvList.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new AddressAdapter();
        mRvList.setAdapter(mAdapter);
        // 初始化默认的本地数据  也提供了方法接收外面数据
        mRvList.post(this::initData);
    }

    /**
     * 设置TabLayout选项卡的字体
     *
     * @param tabLayout 选项卡
     * @param typeface  字体
     */
    public static void setTabLayoutTextFont(TabLayout tabLayout, Typeface typeface) {
        if (tabLayout == null || typeface == null) {
            return;
        }
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int i = 0; i < tabsCount; i++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(i);
            int tabCount = vgTab.getChildCount();
            for (int j = 0; j < tabCount; j++) {
                View tabViewChild = vgTab.getChildAt(j);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }

    /**
     * 初始化数据
     * 拿assets下的json文件
     */
    private void initData() {
        // 将数据转换为对象
        mAddressBean = JSON.parseObject(ResourceUtils.readStringFromAssert("address.json", "utf-8"), AddressBean.class);
        if (mAddressBean != null) {
            mRvData.clear();
            mRvData.addAll(mAddressBean.getProvince());
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 开放给外部传入数据
     * 暂时就用这个Bean模型，如果数据不一致就需要各自根据数据来生成这个bean了
     */
    public void initData(AddressBean bean) {
        if (bean != null) {
            mSelectTown = null;
            mSelectDistrict = null;
            mSelectCity = null;
            mSelectProvice = null;
            Objects.requireNonNull(mTabLayout.getTabAt(0)).select();

            mAddressBean = bean;
            mRvData.clear();
            mRvData.addAll(mAddressBean.getProvince());
            mAdapter.notifyDataSetChanged();

        }
    }

    //点确定
    private void sure() {
        if (mSelectProvice != null &&
                mSelectCity != null &&
                mSelectDistrict != null && mSelectTown != null) {
            //   回调接口
            if (onAddressListener != null) {
                onAddressListener.onSureClick(mSelectProvice.getName() + " " + mSelectCity.getName() + " " + mSelectDistrict.getName() + " " + mSelectTown.getName(),
                        mSelectProvice.getName(), mSelectCity.getName(), mSelectDistrict.getName(), mSelectTown.getName());
            }
        } else {
            ToastUtils.toast("地址还没有选完整哦");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAddressBean = null;
    }

    /**
     * TabLayout 切换事件
     */
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            mRvData.clear();
            switch (tab.getPosition()) {
                case 0:
                    try {
                        mRvData.addAll(mAddressBean.getProvince());
                        mAdapter.notifyDataSetChanged();
                        // 滚动到这个位置
                        mRvList.smoothScrollToPosition(mSelectProvicePosition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    // 点到城市的时候要判断有没有选择省份
                    if (mSelectProvice != null) {
                        for (AddressBean.AddressItemBean itemBean : mAddressBean.getCity()) {
                            if (itemBean.getProvince().equals(mSelectProvice.getProvince()))
                                mRvData.add(itemBean);
                        }
                        if (mRvData.size() == 0) {
                            mSelectProvice.setCity("01");
                            mRvData.add(mSelectProvice);
                        }
                    } else {
                        ToastUtils.toast("请您先选择省份");
                    }
                    mAdapter.notifyDataSetChanged();
                    // 滚动到这个位置
                    mRvList.smoothScrollToPosition(mSelectCityPosition);
                    break;
                case 2:
                    // 点到区的时候要判断有没有选择省份与城市
                    if (mSelectProvice != null && mSelectCity != null) {
                        for (AddressBean.AddressItemBean itemBean : mAddressBean.getDistrict()) {
                            if (itemBean.getProvince().equals(mSelectCity.getProvince()) && itemBean.getCity().equals(mSelectCity.getCity()))
                                mRvData.add(itemBean);
                        }
                    } else {
                        ToastUtils.toast("请先选择省份与城市");
                    }
                    mAdapter.notifyDataSetChanged();
                    // 滚动到这个位置
                    mRvList.smoothScrollToPosition(mSelectDistrictPosition);
                    break;
                case 3:
                    if (mSelectProvice != null && mSelectCity != null && mSelectDistrict != null) {
                        for (AddressBean.AddressItemBean itemBean : mAddressBean.getTown()) {
                            if (itemBean.getProvince().equals(mSelectCity.getProvince()) && itemBean.getCity().equals(mSelectCity.getCity()) && itemBean.getArea().equals(mSelectDistrict.getArea()))
                                mRvData.add(itemBean);
                        }
                    } else {
                        ToastUtils.toast("请先选择省份与城市");
                    }
                    mAdapter.notifyDataSetChanged();
                    // 滚动到这个位置
                    mRvList.smoothScrollToPosition(mSelectTownPosition);
                    break;
            }


        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };


    /**
     * 下面显示数据的adapter
     */
    class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_address_text, parent, false));
        }

        @SuppressLint("WrongConstant")
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final int tabSelectPosition = mTabLayout.getSelectedTabPosition();
            holder.mTitle.setText(mRvData.get(position).getName());
            holder.mTitle.setTextColor(defaultUnSelectedColor);
//            Typeface typeface = XUI.getDefaultTypeface();
//            holder.mTitle.setTypeface(typeface);//设置字体不加粗
            // 设置选中效果的颜色
            switch (tabSelectPosition) {
                case 0:
                    if (mRvData.get(position) != null &&
                            mSelectProvice != null &&
                            mRvData.get(position).getCode().equals(mSelectProvice.getCode())) {
                        holder.mTitle.setTextColor(defaultSelectedColor);
//                        holder.mTitle.setTypeface(typeface);//设置字体加粗
                    }
                    break;
                case 1:
                    if (mRvData.get(position) != null &&
                            mSelectCity != null &&
                            mRvData.get(position).getCode().equals(mSelectCity.getCode())) {
                        holder.mTitle.setTextColor(defaultSelectedColor);
//                        holder.mTitle.setTypeface(typeface);//设置字体加粗
                    }
                    break;
                case 2:
                    if (mRvData.get(position) != null &&
                            mSelectDistrict != null &&
                            mRvData.get(position).getCode().equals(mSelectDistrict.getCode())) {
                        holder.mTitle.setTextColor(defaultSelectedColor);
//                        holder.mTitle.setTypeface(typeface);//设置字体加粗
                    }
                    break;
                case 3:
                    if (mRvData.get(position) != null &&
                            mSelectTown != null &&
                            mRvData.get(position).getCode().equals(mSelectTown.getCode())) {
                        holder.mTitle.setTextColor(defaultSelectedColor);
//                        holder.mTitle.setTypeface(typeface);//设置字体加粗
                    }
                    break;
            }
            // 设置点击之后的事件
            holder.mTitle.setOnClickListener(v -> {
                // 点击 分类别
                switch (tabSelectPosition) {
                    case 0:
                        mSelectProvice = mRvData.get(position);
                        // 清空后面两个的数据
                        mSelectCity = null;
                        mSelectDistrict = null;
                        mSelectTown = null;
                        mSelectCityPosition = 0;
                        mSelectDistrictPosition = 0;
                        mSelectTownPosition = 0;
                        Objects.requireNonNull(mTabLayout.getTabAt(1)).setText("请选择");
                        Objects.requireNonNull(mTabLayout.getTabAt(2)).setText("");
                        Objects.requireNonNull(mTabLayout.getTabAt(3)).setText("");
                        // 设置这个对应的标题
                        Objects.requireNonNull(mTabLayout.getTabAt(0)).setText(mSelectProvice.getName());
                        // 跳到下一个选择
                        Objects.requireNonNull(mTabLayout.getTabAt(1)).select();
                        mSelectProvicePosition = position;
                        break;
                    case 1:
                        mSelectCity = mRvData.get(position);
                        // 清空后面一个的数据
                        mSelectDistrict = null;
                        mSelectTown = null;
                        mSelectDistrictPosition = 0;
                        mSelectTownPosition = 0;
                        Objects.requireNonNull(mTabLayout.getTabAt(2)).setText("请选择");
                        Objects.requireNonNull(mTabLayout.getTabAt(3)).setText("");
                        // 设置这个对应的标题
                        Objects.requireNonNull(mTabLayout.getTabAt(1)).setText(mSelectCity.getName());
                        // 跳到下一个选择
                        Objects.requireNonNull(mTabLayout.getTabAt(2)).select();
                        mSelectCityPosition = position;
                        break;
                    case 2:
                        mSelectTown = null;
                        mSelectTownPosition = 0;
                        mSelectDistrict = mRvData.get(position);
                        Objects.requireNonNull(mTabLayout.getTabAt(3)).setText("请选择");
                        Objects.requireNonNull(mTabLayout.getTabAt(2)).setText(mSelectDistrict.getName());
                        // 跳到下一个选择
                        Objects.requireNonNull(mTabLayout.getTabAt(3)).select();
                        notifyDataSetChanged();
                        mSelectDistrictPosition = position;
                        break;
                    case 3:
                        mSelectTown = mRvData.get(position);
                        // 没了，选完了，这个时候可以点确定了
                        Objects.requireNonNull(mTabLayout.getTabAt(3)).setText(mSelectTown.getName());
                        notifyDataSetChanged();
                        mSelectTownPosition = position;
                        sure();
                        break;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRvData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTitle;

            ViewHolder(View itemView) {
                super(itemView);
                mTitle = itemView.findViewById(R.id.itemTvTitle);
            }
        }
    }

    public void setOnAddressListener(OnAddressListener listener) {
        this.onAddressListener = listener;
    }

    public interface OnAddressListener {
        /**
         * 选择地址
         *
         * @param address      完成地址
         * @param provinceCode 省
         * @param cityCode     市
         * @param districtCode 区县
         * @param townCode     镇
         */
        void onSureClick(String address, String provinceCode, String cityCode, String districtCode, String townCode);
    }
}
