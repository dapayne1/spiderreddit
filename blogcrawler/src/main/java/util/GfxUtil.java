package util;

import android.content.res.Resources;

/**
 * Helper utility with lightweight methods for pixel to density-pixel (px to dp, respectively)
 * conversion and vice vera.
 */
public class GfxUtil {
    /**
     *
     * @param dp input density pixels.
     * @return returns conversion in pixels.
     */
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     *
     * @param px input pixels.
     * @return returns conversion in density pixels.
     */
    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
