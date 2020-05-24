package com.versalinks.mission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gyf.barlibrary.ImmersionBar;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected Context context;
    protected T binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        View view = createView(this);
        setContentView(view);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor("#000000")
                .init();
        binding = DataBindingUtil.bind(view);
        if (binding != null) {
            binding.setLifecycleOwner(this);
            onCreateByBinding(savedInstanceState);
        }

    }

    protected abstract void onCreateByBinding(Bundle savedInstanceState);

    @NonNull
    protected abstract View createView(Context context);

    protected View createViewByID(int id) {
        View inflate = LayoutInflater.from(this).inflate(id, null, false);
        return inflate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onResult4Setting(requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionsOk = permissionsOK(permissions);
        resultPermissions(permissionsOk, permissions, requestCode);
    }

    protected void resultPermissions(boolean isPermissionsOk, String[] permissions, int requestCode) {

    }

    protected void requestPermissions(String[] permissions, int requestCode, Runnable runnable) {
        if (!permissionsOK(permissions) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        } else {
            runnable.run();
        }
    }

    protected boolean permissionsOK(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if ("android.permission.FOREGROUND_SERVICE".equals(permission)) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    protected void onResult4Setting(int requestCode) {

    }

    protected void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public void jump2Activity(Class cls) {
        if (cls != null) {
            Intent intent = new Intent(BaseActivity.this, cls);
            startActivity(intent);
        }
    }

    public void jump2Activity(Intent intent) {
        if (intent != null) {
            startActivity(intent);
        }
    }

    protected void hideSoftInput() {
        try {
            View view = getCurrentFocus();
            if (view == null)
                view = new View(BaseActivity.this);
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (imm == null)
                return;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
