package com.dreambook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import static com.dreambook.MainActivity.database;
import static com.dreambook.dataBase.Base.setDataBase;


public class Splash extends Activity implements ExecutorService {

    final Queue<Runnable> tasks = new ArrayDeque<>();
    private ExecutorService execut;
    private Runnable active;
    private ProgressBar progressBar;
    private TextView percent, prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
         percent = findViewById(R.id.text_percent);
        progressBar = findViewById(R.id.progressBar);
        prompt = findViewById(R.id.text_splash2);
        final int MAX = 100;
        progressBar.setMax(MAX);
        percent.setText("0 %");
        execut = Executors.newFixedThreadPool(2);
        Runnable service1 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < MAX; i++) {
                    final int progress = i + 1;
                    SystemClock.sleep(200);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progress);
                            int perc = (progress * 100) / MAX;
                            percent.setText(perc + " %");
                            if (progress == MAX) {
                                percent.setText("Готово!");
                                SystemClock.sleep(200);
                                Intent intent = new Intent(Splash.this, MainActivity.class);
                                Splash.this.startActivity(intent);
                                Splash.this.finish();
                                execut.shutdown();
                            }
                            switch (perc){
                                case 1:
                                    prompt.setText(R.string.prompt_1);
                                    break;
                                case 28:
                                    prompt.setText(R.string.prompt_3);
                                    break;
                                case 56:
                                    prompt.setText(R.string.prompt_2);
                                    break;
                                case 84:
                                    prompt.setText(R.string.prompt_4);
                                    break;

                            }
                        }
                    });
                }
            }
        };

        Runnable service2 = new Runnable() {
            @Override
            public void run() {
                setDataBase(database);
            }
        };
        execut.submit(service1);
        execut.submit(service2);
        execut.shutdown();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return true;
    }

    @Override
    public boolean isTerminated() {
        return true;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return null;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return null;
    }

    @Override
    public Future<?> submit(Runnable task) {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        return null;
    }

    protected synchronized void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            execut.execute(active);
        }
    }

    @Override
    public void execute(final Runnable command) {
        tasks.add(new Runnable() {
            public void run() {
                try {
                    command.run();
                } finally {
                    scheduleNext();
                }
            }
        });
        if (active == null) {
            scheduleNext();
        }
    }
}