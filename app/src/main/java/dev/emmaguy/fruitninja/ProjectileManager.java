package dev.emmaguy.fruitninja;

import android.graphics.Canvas;
import android.support.v4.util.SparseArrayCompat;

public interface ProjectileManager {
    void draw(Canvas c);

    void update();

    void setWidthAndHeight(int width, int height);

    int testForCollisions(SparseArrayCompat<TimedPath> path);
}
