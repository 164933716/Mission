package com.versalinks.mission;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.interfaces.IPickerViewData;

import java.util.ArrayList;
import java.util.List;

public class MarkerTypeChoose {

    public interface ChooseListener {
        void choose(Model_MarkerType type);
    }

    private final Context context;

    public MarkerTypeChoose(Context context) {
        this.context = context;
    }

    public void showChoose(ChooseListener chooseListener) {
        List<Model_MarkerType> options1Items = new ArrayList<>();
        options1Items.add(new Model_MarkerType("默认动物"));
        options1Items.add(new Model_MarkerType("默认植物"));
        options1Items.add(new Model_MarkerType("牛"));
        options1Items.add(new Model_MarkerType("鸡"));
        options1Items.add(new Model_MarkerType("鸟"));
        options1Items.add(new Model_MarkerType("兔"));
        options1Items.add(new Model_MarkerType("羊"));
        options1Items.add(new Model_MarkerType("猪"));
        options1Items.add(new Model_MarkerType("马"));
        options1Items.add(new Model_MarkerType("桃木"));
        options1Items.add(new Model_MarkerType("杏树"));
        options1Items.add(new Model_MarkerType("桃树"));
        options1Items.add(new Model_MarkerType("水生植物"));
        options1Items.add(new Model_MarkerType("藤条植物"));
        OptionsPickerView<Model_MarkerType> pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (chooseListener != null) {
                    chooseListener.choose(options1Items.get(options1));
                }
            }
        })
                .setTitleText("类型选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .isDialog(false) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .setCyclic(true, false, false)
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                    }
                })
                .build();

        pvOptions.setPicker(options1Items);

        Dialog mDialog = pvOptions.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvOptions.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
        pvOptions.show(true);
    }
}
