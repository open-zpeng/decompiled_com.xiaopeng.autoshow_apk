package com.xiaopeng.autoshow.dialog;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;
/* loaded from: classes.dex */
public class VerticalAlignTextSpan extends ReplacementSpan {
    private int fontSizeSp;

    public VerticalAlignTextSpan(int i) {
        this.fontSizeSp = -1;
        this.fontSizeSp = i;
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
        return (int) getCustomTextPaint(paint).measureText(charSequence, i, i2);
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
        TextPaint customTextPaint = getCustomTextPaint(paint);
        Paint.FontMetricsInt fontMetricsInt = customTextPaint.getFontMetricsInt();
        canvas.drawText(charSequence, i, i2, f, i4 - (((((fontMetricsInt.ascent + i4) + i4) + fontMetricsInt.descent) / 2) - ((i3 + i5) / 2)), customTextPaint);
    }

    private TextPaint getCustomTextPaint(Paint paint) {
        TextPaint textPaint = new TextPaint(paint);
        int i = this.fontSizeSp;
        if (i != -1) {
            textPaint.setTextSize(i * textPaint.density);
        }
        return textPaint;
    }
}
