package dev.emmaguy.fruitninja;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Region;
import android.support.v4.util.SparseArrayCompat;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FruitProjectileManager implements ProjectileManager {

    private final Random random = new Random();
    private final List<Projectile> fruitProjectiles = new ArrayList<Projectile>();
    private final SparseArray<Bitmap> bitmapCache;
    private Region clip;
    private int maxWidth;
    private int maxHeight;

    public FruitProjectileManager(Resources r) {

        bitmapCache = new SparseArray<>(FruitType.values().length);

        for (FruitType t : FruitType.values()) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(r, t.getResourceId(), new Options());

            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = false;
            options.inSampleSize = 2;

            bitmapCache.put(t.getResourceId(), BitmapFactory.decodeResource(r, t.getResourceId(), options));
        }
    }

    public void draw(Canvas canvas) {
        for (Projectile f : fruitProjectiles) {
            f.draw(canvas);
        }
    }

    public void update() {

        if (maxWidth < 0 || maxHeight < 0) {
            return;
        }

        if (random.nextInt(1000) <= 60) {
            fruitProjectiles.add(createNewFruitProjectile());
        }

        for (Iterator<Projectile> iter = fruitProjectiles.iterator(); iter.hasNext(); ) {

            Projectile f = iter.next();
            f.move();

            if (f.hasMovedOffScreen()) {
                iter.remove();
            }
        }
    }

    private FruitProjectile createNewFruitProjectile() {
        int angle = random.nextInt(20) + 70;
        int speed = random.nextInt(30) + 120;
        boolean rightToLeft = random.nextBoolean();

        float gravity = random.nextInt(6) + 14.0f;
        float rotationStartingAngle = random.nextInt(360);
        float rotationIncrement = random.nextInt(100) / 10.0f;

        if (random.nextBoolean()) {
            rotationIncrement *= -1;
        }

        return new FruitProjectile(bitmapCache.get(FruitType.randomFruit().getResourceId()), maxWidth, maxHeight,
                angle, speed, gravity, rightToLeft, rotationIncrement, rotationStartingAngle);
    }

    public void setWidthAndHeight(int width, int height) {
        this.maxWidth = width;
        this.maxHeight = height;
        this.clip = new Region(0, 0, width, height);
        IntersectionUtils.init(width, height);
    }

    @Override
    public int testForCollisions(SparseArrayCompat<TimedPath> allPaths) {

        int score = 0;
        for (int i = 0; i < allPaths.size(); i++) {
            for (Projectile f : fruitProjectiles) {
                if (!f.isAlive()) continue;
                if (IntersectionUtils.intersect(f.getLocation(), allPaths.valueAt(i).get())) {
                    f.kill();
                    score++;
                }
            }
        }
        return score;
    }
}
