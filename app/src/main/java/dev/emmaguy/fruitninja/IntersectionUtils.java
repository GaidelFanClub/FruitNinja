package dev.emmaguy.fruitninja;


import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;

public class IntersectionUtils {

    private static final Region region = new Region();
    private static final Region path = new Region();
    private static final Region clip = new Region();

    public static final void init(int w, int h) {
        clip.set(0, 0, w, h);
    }

    public static boolean intersect(Rect location, Path linePath) {
        region.set(location);
        path.setPath(linePath, clip);
        return !region.quickReject(path) && region.op(path, Region.Op.INTERSECT);
    }

}
