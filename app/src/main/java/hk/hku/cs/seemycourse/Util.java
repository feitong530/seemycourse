package hk.hku.cs.seemycourse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Util {

    /**
     * @description temp directory in device
     */
    public static final String IMAGE_DIRECTORY = "seemycourse";

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
