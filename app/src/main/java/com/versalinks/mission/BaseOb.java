package com.versalinks.mission;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ksy
 */

public abstract class BaseOb<T> implements Observer<T> {

    public BaseOb() {
    }


    public void onBind(Disposable disposable) {

    }

    @Override
    public void onSubscribe(Disposable disposable) {
        onBind(disposable);

    }

    public void onStart() {

    }

    public abstract void onDataDeal(T data, String message);

    @Override
    public void onNext(T value) {
        if (value != null) {
            onDataDeal(value, null);
        } else {
            onDataDeal(null, null);
        }
    }

    @Override
    public void onError(Throwable e) {
        onDataDeal(null, e.getMessage());
        onFinish();
    }

    @Override
    public void onComplete() {
        onFinish();
    }

    public void onFinish() {
    }

    public Observable<T> bindObed(Observable<T> obed) {
        onStart();
        obed.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this);
        return obed;
    }

    public Observable<T> bindObedOnMain(Observable<T> obed) {
        onStart();
        obed.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this);
        return obed;
    }
}
