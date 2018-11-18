package hk.hku.cs.seemycourse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Util {

    public static final String IMAGE_DIRECTORY = "seemycourse";

    public static File GenerateFilePath(String fileName) {
        File dir = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY);
        if(!dir.exists()) { dir.mkdirs(); }
        return new File(dir, fileName);
    }

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
}
