package dev.emmaguy.fruitninja;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

public class FruitProjectile implements Projectile {

    private final Paint paint = new Paint();
    private final Matrix m = new Matrix();

    private final Bitmap aliveBitmap;
    private final Bitmap deadBitmap;
    private final float rotationIncrement;

    private float gravity;
    private final int maxWidth;
    private final int maxHeight;
    private final double angleCos;
    private final double angleSin;
    private final int initialSpeed;

    private int xLocation;
    private int yLocation;
    private int absYLocation;
    private float rotationAngle;
    private double time = 0;
    private long lastTimeStamp;
    private float fallingVelocity = 1.0f;
    private boolean rightToLeft;
    private boolean isAlive = true;
    private int xShift;

    public FruitProjectile(Bitmap aliveBitmap, Bitmap deadBitmap, int maxWidth, int maxHeight, int angle, int initialSpeed, float gravity,
                           boolean rightToLeft, float rotationIncrement, float rotationStartingAngle, int xShift) {
        this.aliveBitmap = aliveBitmap;
        this.deadBitmap = deadBitmap;
        this.maxHeight = maxHeight;
        this.angleCos = Math.cos(Math.toRadians(angle));
        this.angleSin = Math.sin(Math.toRadians(angle));
        this.initialSpeed = initialSpeed;
        this.gravity = gravity;
        this.maxWidth = maxWidth;
        this.rightToLeft = rightToLeft;
        this.rotationIncrement = rotationIncrement;
        this.rotationAngle = rotationStartingAngle;
        this.xShift = xShift;

        lastTimeStamp = System.currentTimeMillis();
        paint.setAntiAlias(true);
    }

    @Override
    public boolean hasMovedOffScreen() {
        return yLocation < 0 || xLocation + aliveBitmap.getWidth() < 0 || xLocation > maxWidth;
    }

    @Override
    public void move() {

        if (isAlive) {
            xLocation = (int) (initialSpeed * angleCos * time);
            yLocation = (int) (initialSpeed * angleSin * time - 0.5 * gravity * time * time);

            if (rightToLeft) {
                xLocation = maxWidth - xShift - aliveBitmap.getWidth() - xLocation;
            } else {
                xLocation += xShift;
            }
            rotationAngle += rotationIncrement;
        } else {
            yLocation -= time * (fallingVelocity + time * gravity / 2);
            fallingVelocity += time * gravity;
        }

        // 0,0 is top left, we want the parabola to go the other way up
        absYLocation = (yLocation * -1) + maxHeight;

        time += (System.currentTimeMillis() - lastTimeStamp) * 1e-2;
        lastTimeStamp = System.currentTimeMillis();
    }

    @Override
    public void draw(Canvas canvas) {
        m.reset();
        m.postTranslate(-aliveBitmap.getWidth() / 2, -aliveBitmap.getHeight() / 2);
        m.postRotate(rotationAngle);
        m.postTranslate(xLocation + (aliveBitmap.getWidth() / 2), absYLocation + (aliveBitmap.getHeight() / 2));

        canvas.drawBitmap(isAlive ? aliveBitmap : deadBitmap, m, paint);
    }

    private Rect reuseRect = new Rect();

    @Override
    public Rect getLocation() {
        reuseRect.set(xLocation, absYLocation, xLocation + aliveBitmap.getWidth(), absYLocation + aliveBitmap.getHeight());
        return reuseRect;
    }

    @Override
    public void kill() {
        this.gravity /= 12.0f;
        this.time = 0.0f;
        this.isAlive = false;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }
}