package com.nilhcem.ledmatrixfun;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.io.IOException;

public class TextScroller {

    private static final int MATRIX_SIZE = 8;
    private static final int REFRESH_RATE_MS = 33;

    interface OnTextScrollListener {
        Bitmap onStart();

        void onDraw(Bitmap bitmap) throws IOException;

        void onStop();
    }

    private OnTextScrollListener listener;

    public TextScroller(OnTextScrollListener listener) {
        this.listener = listener;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap fullText = listener.onStart();

                try {
                    Bitmap part = Bitmap.createBitmap(MATRIX_SIZE, MATRIX_SIZE, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(part);

                    for (int i = 0; i < fullText.getWidth() - MATRIX_SIZE; i++) {
                        part.eraseColor(Color.TRANSPARENT);
                        canvas.drawBitmap(fullText, new Rect(i, 0, i + MATRIX_SIZE, MATRIX_SIZE), new Rect(0, 0, MATRIX_SIZE, MATRIX_SIZE), null);
                        listener.onDraw(part);
                        Thread.sleep(REFRESH_RATE_MS);
                    }

                    listener.onStop();
                } catch (IOException e) {
                    Log.e(TextScroller.class.getSimpleName(), "Error drawing bitmap", e);
                } catch (InterruptedException e) {
                    Log.e(TextScroller.class.getSimpleName(), "Error invoking Thread.sleep", e);
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}
