package com.shaposhnikov.facerecognizer.grabber;

import com.shaposhnikov.facerecognizer.command.Command;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kirill on 26.02.2017.
 */
public abstract class AbstractFrameGrabber implements IFrameGrabber {

    protected final ScheduledExecutorService executor;
    private final Command command;

    public AbstractFrameGrabber(ScheduledExecutorService executor, Command command) {
        this.executor = executor;
        this.command = command;
    }

    public void start() {
        executor.scheduleAtFixedRate(execute(), 0, 40, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        try {
            if (!executor.isShutdown()) {
                executor.shutdown();
                executor.awaitTermination(40, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Couldn't interrupt thread");
        }
    }

    public Runnable execute() {
        return () -> {
            //command.doWork(grab());
        };
    }

}
