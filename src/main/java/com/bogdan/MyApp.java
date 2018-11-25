package com.bogdan;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subscriptions.Subscriptions;

public class MyApp {

  public static void main(String[] args) {
    final Observable<String> hello = delayed("hello");
    hello.subscribe(System.out::print);
  }

  static <T> Observable<T> delayed(T x) {
    return Observable.create(
        subscriber -> {
          Runnable r = () -> {
            sleep(5, TimeUnit.SECONDS);
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(x);
              subscriber.onCompleted();
            }
          };
          final Thread thread = new Thread(r);
          thread.start();
          subscriber.add(Subscriptions.create(thread::interrupt));
        }
    );
  }

  private static void sleep(int timeout, TimeUnit unit) {
    try {
      unit.sleep(timeout);
    } catch (InterruptedException ignored) {
      // ignored
    }
  }
}


