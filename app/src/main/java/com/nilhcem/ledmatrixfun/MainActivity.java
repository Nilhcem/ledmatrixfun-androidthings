package com.nilhcem.ledmatrixfun;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.contrib.driver.sensehat.LedMatrix;

import java.io.IOException;

class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private LedMatrix ledmatrix;
    private BitmapTextGenerator bitmapGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            ledmatrix = new LedMatrix("I2C1", 0x46);
        } catch (IOException e) {
            Log.e(TAG, "Error initializing LED matrix", e);
        }
        bitmapGenerator = new BitmapTextGenerator(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new TextScroller(new TextScroller.OnTextScrollListener() {
            @Override
            public Bitmap onStart() {
                Bitmap bitmap = bitmapGenerator.textToBitmap("Hello, Android Things!");

                // either set a unique color: e.g.:
                // bitmapGenerator.applyColor(bitmap, Color.RED);

                // or a shader
                int[] rainbow = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
                Shader shader = new LinearGradient(0, 0.5f * bitmap.getHeight(), bitmap.getWidth(), 0.5f * bitmap.getHeight(), rainbow, null, Shader.TileMode.CLAMP);
                bitmapGenerator.applyShader(bitmap, shader);

                return bitmap;
            }

            @Override
            public void onDraw(Bitmap bitmap) throws IOException {
                ledmatrix.draw(bitmap);
            }

            @Override
            public void onStop() {
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            ledmatrix.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing LED matrix", e);
        }
    }
}
