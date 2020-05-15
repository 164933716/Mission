package com.versalinks.mission;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = createView(this);
        setContentView(view);
        T binding = DataBindingUtil.bind(view);
        if (binding != null) {
            binding.setLifecycleOwner(this);
            onCreate(binding);
        }
    }

    protected abstract void onCreate(@NonNull T binding);

    @NonNull
    protected abstract View createView(Context context);

    protected View createViewByID(int id) {
        View inflate = LayoutInflater.from(this).inflate(id, null, false);
        return inflate;
    }
}
