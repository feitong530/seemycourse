package hk.hku.cs.seemycourse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.JsonReader;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

public class Util {

    /* temp directory in device */
    private static final String IMAGE_DIRECTORY = "seemycourse";

    private static final String PACKAGE_NAME = "seemycourse";
    private static final String SCHEDULE_STORAGE_KEY = "schedule";

    /* Bitmaps to be combine */
    public static ArrayList<BitmapPiece> puzzleList = null;

    /**
     * Generate A File in Device
     * @param fileName file name
     * @return {File}
     */
    public static File GenerateFilePath(String fileName) {
        File dir = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY);
        if(!dir.exists()) { dir.mkdirs(); }
        return new File(dir, fileName);
    }

    /**
     * Resize a bitmap
     * @param image Original Bitmap
     * @param maxSize max size
     * @return {Bitmap}
     */
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1.0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /**
     * Split a Bitmap into several Bitmaps
     * @param original original bitmap
     * @param columns columns to be split
     * @param rows rows to be split
     * @return array of bitmap and it's original index
     */
    public static ArrayList<BitmapPiece> splitBitmap(Bitmap original, int columns, int rows) {
        ArrayList<BitmapPiece> list = new ArrayList<>(rows * columns);
        int width = original.getWidth();
        int height = original.getHeight();
        int pieceWidth = width / rows;
        int pieceHeight = height / columns;
        for (int y = 0; y < rows; ++y) {
            for (int x = 0; x < columns; ++x) {
                list.add(new BitmapPiece(
                        String.valueOf(y * columns + x),
                        Bitmap.createBitmap(
                                original,
                                x * pieceWidth,
                                y * pieceHeight,
                                pieceWidth,
                                pieceHeight
                        )
                ));
            }
        }
        return list;
    }

    /**
     * Load Schedule from storage
     * @param ctx Context of activity
     * @return schedule strings
     */
    public static ArrayList<String> loadSchedule(Context ctx) {
        ArrayList<String> schedule = new ArrayList<>();
        SharedPreferences pref = ctx.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        String scheduleJSONString = pref.getString(SCHEDULE_STORAGE_KEY, "[]");
        if (scheduleJSONString.equals("[]")) {
            scheduleJSONString = "[\"Manage Your Course !\"]";
        }
        JsonReader reader = new JsonReader(new StringReader(scheduleJSONString));
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                schedule.add(reader.nextString());
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    /**
     * Save schedule to storage
     * @param ctx Context of activity
     * @param schedule schedule strings
     */
    public static void saveSchedule(Context ctx, ArrayList<String> schedule) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).edit();

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < schedule.size(); ++i) {
            builder.append(String.format("\"%s\"", schedule.get(i)));
            if (i < schedule.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        editor.putString(SCHEDULE_STORAGE_KEY, builder.toString());
        Log.e("HELLO", builder.toString());
        editor.apply();
    }

    /**
     * Save an image to Device and return A Uri of the image
     * @param ctx Context of Activity or Fragment
     * @param fileNamePrefix prefix of filename
     * @param output Image Bitmap
     * @return {Uri}
     */
    public static Uri saveImageToDevice(@NonNull Context ctx, String fileNamePrefix, @NonNull Bitmap output) {
        String fileName = fileNamePrefix + "_" + new Date().getTime() + ".jpg";
        File file = GenerateFilePath(fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            output.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Notify the image album
        try {
            MediaStore.Images.Media.insertImage(
                    ctx.getContentResolver(),
                    file.getAbsolutePath(),
                    fileName,
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri imageUri = Uri.fromFile(file);
        ctx.sendBroadcast(new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                imageUri
        ));
        return imageUri;
    }

    /**
     * Recognize Text Using Google ML Kit API
     * @param ctx Context of Activity or Fragment
     * @param uri Uri of the image
     * @param callback Callback Function
     */
    public static void recognizeText(
            @NonNull Context ctx,
            @NonNull Uri uri,
            @NonNull final Callback<FirebaseVisionText> callback
        ) {
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        try {
            FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(ctx, uri);
            recognizer.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            callback.onSuccess(firebaseVisionText);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.onFailure(e.getMessage());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailure(e.getMessage());
        }
    }

    /**
     * Callback Function Interface
     * @param <T> type of returned object
     */
    public interface Callback<T> {
        void onSuccess(T result);
        void onFailure(String message);
    }
}
