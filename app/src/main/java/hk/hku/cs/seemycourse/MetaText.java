package hk.hku.cs.seemycourse;

import android.graphics.RectF;

public class MetaText {
    private String text;
    private RectF frame;

    public MetaText(String text, RectF frame) {
        this.text = text;
        this.frame = frame;
    }

    public String getText() {
        return text;
    }

    public RectF getFrame() {
        return frame;
    }
}
