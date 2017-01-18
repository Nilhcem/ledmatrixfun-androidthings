package com.nilhcem.ledmatrixfun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;

public class BitmapTextGenerator {

    private static final String CHARS = " +-*/!\"#$><0123456789.=)(ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz?,;:|@%[&_']\\~";
    private static final int CHAR_WIDTH = 5;
    private static final int CHAR_HEIGHT = 8;
    private static final int SPACE_BETWEEN_CHARS = 1;

    private Bitmap charsTable;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BitmapTextGenerator(Context context) {
        charsTable = BitmapFactory.decodeResource(context.getResources(), R.drawable.sense_hat_text);
    }

    public Bitmap textToBitmap(String message) {
        int messageLength = message.length();
        Bitmap bitmap = Bitmap.createBitmap(messageLength * CHAR_WIDTH + (messageLength - 1) * SPACE_BETWEEN_CHARS, CHAR_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int i = 0; i < messageLength; i++) {
            char c = message.charAt(i);
            Bitmap b = getBitmapFor(c);
            canvas.drawBitmap(b, i * CHAR_WIDTH + (i == 0 ? 0 : i * SPACE_BETWEEN_CHARS), 0, paint);
        }

        return bitmap;
    }

    public void applyColor(Bitmap bitmap, int color) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
    }

    public void applyShader(Bitmap bitmap, Shader shader) {
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
    }

    private Bitmap getBitmapFor(char ch) {
        int index = CHARS.indexOf(ch);

        Bitmap bitmap = Bitmap.createBitmap(CHAR_WIDTH, CHAR_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(charsTable, new Rect(index * CHAR_WIDTH, 0, (index + 1) * CHAR_WIDTH, CHAR_HEIGHT), new Rect(0, 0, CHAR_WIDTH, CHAR_HEIGHT), paint);
        return bitmap;
    }
}
