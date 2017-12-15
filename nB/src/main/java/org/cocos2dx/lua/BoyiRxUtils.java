package org.cocos2dx.lua;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Jiang on 2017/3/23.
 */

public class BoyiRxUtils {

    public static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                            subscriber.onCompleted();
                        } catch (Exception ex) {
                            Log.e("makeObservable", "Error from the makeObservable", ex);
                            subscriber.onError(ex);
                        }
                    }
                });
    }

    public static Observable<View> throttleClick(View  view) {
        return Observable.create(
                new ViewClickOnSubscribe(view));
    }

    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if(CommonConstant.isDebug) {
                                    Logger.showLog("错误:-----------" + throwable.getMessage(), "error:");

                                    Toast.makeText(
                                            APPAplication.instance,
                                            "错误:" + throwable.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        ;
            }
        };
    }

    private static class ViewClickOnSubscribe implements Observable.OnSubscribe<View> {
        private View view;

        public ViewClickOnSubscribe(View view) {
            this.view = view;
        }

        @Override
        public void call(final Subscriber<? super View> subscriber) {
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //订阅没取消
                    if (!subscriber.isUnsubscribed()) {
                        //发送消息
                        subscriber.onNext(v);
                    }
                }
            };
            view.setOnClickListener(onClickListener);
        }
    }

    public static class MySubscriber<T> extends Subscriber<T> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(
                    APPAplication.instance,
                    e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(T o) {

        }
    }

}
