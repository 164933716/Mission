package com.versalinks.mission;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.versalinks.mission.databinding.ActivityGpsMarkBinding;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.RealmList;

public class GPSMarkActivity extends BaseActivity<ActivityGpsMarkBinding> {
    private Handler handler = new Handler(Looper.getMainLooper());
    private GPSService gpsService;
    private List<String> allList = new ArrayList<>();
    private BaseAdapter<String> adapter;
    private int MaxSize = 8;
    GPSService.GPSListener gpsListener = new GPSService.GPSListener() {
        @Override
        public void gps(Model_GPS modelGps) {
            Log.e("TAG", "gps " + modelGps);
            if (modelGps != null) {
                binding.tvGps.setText(modelGps.toShow());
            } else {
                binding.tvGps.setText(null);
            }
            binding.tvGps.setTag(modelGps);
        }
    };

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("TAG", "GPSRecordActivity onServiceConnected");
            if (service instanceof GPSService.GPSBinder) {
                gpsService = ((GPSService.GPSBinder) service).getService();
                gpsService.addGPSListener(gpsListener);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (gpsService != null) {
                gpsService.removeGPSListener(gpsListener);
                gpsService.stopLocate();
            }
            gpsService = null;
        }
    };
    private long createTime;

    @Override
    protected void onCreateByBinding(Bundle savedInstanceState) {
        binding.vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false);
        binding.recycler.setLayoutManager(layoutManager);
        initAdapter();
        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    String name = binding.etName.getText().toString();
                    Model_GPS tag = (Model_GPS) binding.tvGps.getTag();
                    Model_MarkerType tag1 = (Model_MarkerType) binding.tvType.getTag();

                    Model_Marker item = new Model_Marker();
                    item.createTime = createTime;
                    item.name = name;
                    item.type = tag1;
                    item.gps = tag;
                    item.photos = new RealmList<>();
                    item.photos.addAll(allList);
                    Observable<Model_Marker> o = DataUtils.getInstance().saveMarker(item);
                    BaseOb<Model_Marker> baseOb = new BaseOb<Model_Marker>() {
                        @Override
                        public void onDataDeal(Model_Marker data, String message) {
                            TipDialog tipDialog = new TipDialog(context, "本次标注已保存", "可在我的-我的标注里进行管理");
                            tipDialog.show();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tipDialog.dismiss();
                                    finish();
                                }
                            }, 1000);
                        }
                    };
                    baseOb.bindObed(o);

                }
            }
        });
        binding.tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                MarkerTypeChoose markerTypeChoose = new MarkerTypeChoose(context);
                markerTypeChoose.showChoose(new MarkerTypeChoose.ChooseListener() {
                    @Override
                    public void choose(Model_MarkerType type) {
                        binding.tvType.setTag(type);
                        binding.tvType.setText(type.getPickerViewText());
                    }
                });
            }
        });
        createTime = DataUtils.getNowMills();
        String s = DataUtils.convertToDate(createTime, new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss"));
        binding.etName.setText(s);
        binding.tvDate.setText(s);
        binding.tvType.setText("默认动物");
        binding.tvType.setTag(new Model_MarkerType("默认动物"));
//        binding.etName.requestFocus();
        Intent intent = new Intent(this, GPSService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private boolean check() {
        if (TextUtils.isEmpty(binding.etName.getText())) {
            ToastUtils.showShort("请输入标注名称");
            return false;
        }
        if (!(binding.tvType.getTag() instanceof Model_MarkerType)) {
            ToastUtils.showShort("请选择标注类型");
            return false;
        }
        if (!(binding.tvGps.getTag() instanceof Model_GPS)) {
            ToastUtils.showShort("定位信息未找到，请稍后重试");
            return false;
        }
//        if (allList.isEmpty()) {
//            ToastUtils.showShort("请添加照片");
//            return false;
//        }

        return true;
    }

    @NonNull
    @Override
    protected View createView(Context context) {
        return createViewByID(R.layout.activity_gps_mark);
    }

    private void initAdapter(List<String> files) {
        if (files != null) {
            allList.addAll(files);
        }
        initAdapter();
    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new BaseAdapter<String>(new BaseAdapter.ViewInter() {
                @Override
                public View createView(ViewGroup viewGroup, int viewType) {
                    View view;
                    if (viewType == -1) {
                        view = LayoutInflater.from(context).inflate(R.layout.item_take_empty, viewGroup, false);
                    } else {
                        view = LayoutInflater.from(context).inflate(R.layout.item_take_image, viewGroup, false);
                    }
                    return view;
                }
            }, allList) {
                @Override
                public int getItemViewType(int position) {
                    if (allList.size() >= MaxSize) {
                        return 1;
                    } else {
                        if (position == getItemCount() - 1) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }

                @Override
                public int getItemCount() {
                    if (allList.size() < MaxSize) {
                        return allList.size() + 1;
                    }
                    return MaxSize;
                }

                @Override
                protected void convert(View helper, int position, int viewType) {
                    if (viewType == 1) {
                        String path = allList.get(position);
                        File file = new File(path);
                        ImageView imageView = helper.findViewById(R.id.imageView);
                        ImageView ivDelete = helper.findViewById(R.id.ivDelete);
                        Glide.with(context).load(file).into(imageView);
                        ivDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                allList.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            };
            adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, int viewType) {
                    if (viewType == -1) {
                        DataUtils.chooseImage(GPSMarkActivity.this, MaxSize - allList.size(), 999);
                    } else if (viewType == 1) {
                        String filePath = allList.get(position);
                        File fileTarget = new File(filePath);
                    }
                }
            });
            binding.recycler.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            Log.d("Matisse", "Uris: " + Matisse.obtainResult(data));
            List<String> strings = Matisse.obtainPathResult(data);
            Log.d("Matisse", "Paths: " + strings);
            Log.e("Matisse", "Use the selected photos with original: " + String.valueOf(Matisse.obtainOriginalState(data)));
            initAdapter(strings);
        }
    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (gpsService != null) {
            gpsService.removeGPSListener(gpsListener);
            unbindService(connection);
        }
        super.onDestroy();
    }

}
