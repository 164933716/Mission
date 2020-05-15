/*
 * Copyright 2013 Joan Zapata
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.versalinks.mission;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter {


    protected ViewInter inter;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemClickListener mOnItemLongClickListener;
    protected List<T> mList;

    public BaseAdapter(@LayoutRes final int layoutResId, @Nullable List<T> data) {
        ViewInter inter = new ViewInter() {
            @Override
            public View createView(ViewGroup viewGroup, int viewType) {
                return LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
            }
        };
        this.inter = inter;
        this.mList = data;
    }

    public BaseAdapter(ViewInter inter, @Nullable List<T> data) {
        this.inter = inter;
        this.mList = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(inter.createView(parent, viewType)) {
        };
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position, getItemViewType(position));
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemClick(v, position, getItemViewType(position));
                }
                return false;
            }
        });
        convert(holder.itemView, position, getItemViewType(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected abstract void convert(View helper, int position, int viewType);

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int viewType);
    }

    public interface ViewInter {
        View createView(ViewGroup viewGroup, int viewType);
    }
}
