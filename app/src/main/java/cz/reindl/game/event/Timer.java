package cz.reindl.game.event;

import android.os.Handler;

public class Timer {
    private Handler handler;
    private boolean paused;

    private int interval;

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (!paused) {
                runnable.run();
                Timer.this.handler.postDelayed(this, interval);
            }
        }
    };

    private Runnable runnable;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void startTimer() {
        paused = false;
        handler.postDelayed(task, interval);
    }

    public void stopTimer() {
        paused = true;
    }

    public Timer(Runnable runnable, int interval, boolean started) {
        handler = new Handler();
        this.runnable = runnable;
        this.interval = interval;
        if (started)
            startTimer();
    }
}