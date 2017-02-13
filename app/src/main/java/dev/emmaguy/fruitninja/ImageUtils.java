package dev.emmaguy.fruitninja;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class ImageUtils {

    public static Bitmap createDeadBitmap(Bitmap aliveBitmap) {
        Bitmap deadBitmap = aliveBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(deadBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        int transparentWidth = Math.max(1, deadBitmap.getWidth() / 20);
        canvas.drawRect(deadBitmap.getWidth() / 2 - transparentWidth, 0, deadBitmap.getWidth() / 2 + transparentWidth, deadBitmap.getHeight(), paint);
        return deadBitmap;
    }
}
