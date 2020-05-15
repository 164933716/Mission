package com.versalinks.mission;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LayerChoose extends LinearLayout {
    private Set<Layer.Item> choose = new HashSet<>();

    public LayerChoose(@NonNull Context context) {
        this(context, null);
    }

    public LayerChoose(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public LayerChoose(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPadding(AndroidUtil.dp2Px(15), AndroidUtil.dp2Px(15), AndroidUtil.dp2Px(15), AndroidUtil.dp2Px(15));
        setOrientation(VERTICAL);
        setBackgroundColor(Color.parseColor("#ffffff"));
        Space spaceTop = new Space(context);
        addView(spaceTop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidUtil.dp2Px(20)));

        TextView textViewTitle = new TextView(context);
        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        textViewTitle.setTypeface(Typeface.DEFAULT_BOLD);
        textViewTitle.setTextColor(Color.parseColor("#333333"));
        textViewTitle.setText("图层选择工具");
        addView(textViewTitle, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Space spaceBottom = new Space(context);
        addView(spaceBottom, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidUtil.dp2Px(20)));


        NestedScrollView nestedScrollView = new NestedScrollView(context);
        addView(nestedScrollView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(VERTICAL);
        nestedScrollView.addView(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        Space spaceTitle = new Space(context);
        linearLayout.addView(spaceTitle, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidUtil.dp2Px(10)));
        List<Layer> layers = new ArrayList<>();
        layers.add(Layer.create1());
        layers.add(Layer.create2());
        layers.add(Layer.create3());
        for (Layer layer : layers) {
            TextView textView = new TextView(context);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(Color.parseColor("#666666"));
            textView.setText(layer.label);
            linearLayout.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Space space = new Space(context);
            linearLayout.addView(space, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidUtil.dp2Px(10)));
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            BaseAdapter<Layer.Item> baseAdapter = new BaseAdapter<Layer.Item>(new BaseAdapter.ViewInter() {
                @Override
                public View createView(ViewGroup viewGroup, int viewType) {
                    return new LayerView(context);
                }
            }, layer.items) {
                @Override
                protected void convert(View helper, int position, int viewType) {
                    LayerView layerView = (LayerView) helper;
                    Layer.Item item = layer.items.get(position);
                    if (choose.contains(item)) {
                        layerView.check(true);
                    } else {
                        layerView.check(false);
                    }
                    layerView.bind(item);
                }
            };
            baseAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, int viewType) {
                    Layer.Item item = layer.items.get(position);
                    if (choose.contains(item)) {
                        choose.remove(item);
                        baseAdapter.notifyItemChanged(position);
                    } else {
                        choose.add(item);
                        baseAdapter.notifyItemChanged(position);
                    }
                }
            });
            recyclerView.setAdapter(baseAdapter);
            recyclerView.addItemDecoration(new GridDividerItemDecoration());
            baseAdapter.notifyDataSetChanged();
            linearLayout.addView(recyclerView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Space spaceItem = new Space(context);
            linearLayout.addView(spaceItem, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidUtil.dp2Px(20)));
        }
    }


    static class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(AndroidUtil.dp2Px(5), AndroidUtil.dp2Px(5), AndroidUtil.dp2Px(5), AndroidUtil.dp2Px(5));
        }
    }

}
