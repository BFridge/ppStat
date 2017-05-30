package fridge.com.hilistview.utils;

/**
 * Created by Fridge on 17/5/23.
 */

public final class FloatUtils {
    private static final float EPSILON = 1.0E-5F;

    private FloatUtils() {
    }

    public static boolean floatsEqual(float f1, float f2) {
        return !Float.isNaN(f1) && !Float.isNaN(f2)?Math.abs(f2 - f1) < 1.0E-5F:Float.isNaN(f1) && Float.isNaN(f2);
    }
}
