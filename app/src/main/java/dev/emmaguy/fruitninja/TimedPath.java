package dev.emmaguy.fruitninja;

import android.graphics.Path;

public class TimedPath {

    private static final int MAX_SIZE = 5;

    private long timeDrawn;
    private int size;

    private float[] x;
    private float[] y;
    private int pointer;
    private int head;

    private Path reuse = new Path();
    private boolean pathChanged;

    public TimedPath() {
        x = new float[MAX_SIZE];
        y = new float[MAX_SIZE];
    }

    public long getTimeDrawn() {
        return timeDrawn;
    }

    public void updateTimeDrawn(long timeDrawn) {
        this.timeDrawn = timeDrawn;
    }

    public void moveTo(float x, float y) {
        pointer = head = 0;
        add(x, y);
    }

    public void lineTo(float x, float y) {
        add(x, y);
    }

    private void add(float xCord, float yCord) {
        pathChanged = true;
        x[pointer] = xCord;
        y[pointer] = yCord;
        if (size == MAX_SIZE) {
            head = next(head);
        } else {
            size++;
        }
        pointer = next(pointer);
    }

    public Path get() {
        if (!pathChanged) {
            return reuse;
        }
        reuse.reset();
        reuse.moveTo(x[head], y[head]);
        int cur = next(head);
        for (int i = 1; i < size; i++) {
            reuse.lineTo(x[cur], y[cur]);
            cur = next(cur);
        }
        return reuse;
    }

    private int next(int index) {
        index++;
        if (index == MAX_SIZE) index = 0;
        return index;
    }
}
