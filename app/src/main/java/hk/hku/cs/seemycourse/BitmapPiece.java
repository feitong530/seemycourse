package hk.hku.cs.seemycourse;

import android.graphics.Bitmap;

public class BitmapPiece {
    private String index;
    private Bitmap bitmap;

    public BitmapPiece(String i, Bitmap b) {
        index = i;
        bitmap = b;
    }

    public String getIndex() {
        return index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}


