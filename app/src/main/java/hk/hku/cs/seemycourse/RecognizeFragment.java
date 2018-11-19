package hk.hku.cs.seemycourse;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionText.TextBlock;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class RecognizeFragment extends Fragment {

    public static final int REQUEST_PERMISSION_REQUEST = 0b00;
    public static final int REQUEST_IMAGE_CAPTURE      = 0b01;

    /* Image Uri from Cropping or Photo Taking */
    private Uri imageUri = null;
    /* Activity Context */
    private Context ctx = null;

    /* Input Image Size */
    private float currentHeight = 0;
    private float currentWidth  = 0;

    @BindView(R.id.btn_select_image) Button btn_select_image;
    @BindView(R.id.imageView) ImageView iv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recognition, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    /**
     * Initialize
     */
    private void init() {
        ctx = getContext();

        // request Permission to get images
        requestPermissions(
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                REQUEST_PERMISSION_REQUEST
        );
    }

    /**
     * Start An Activity to Take Photo
     */
    @OnClick(R.id.btn_take_photo)
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(ctx.getPackageManager()) != null) {
            imageUri = FileProvider.getUriForFile(
                    ctx,
                    ctx
                            .getApplicationContext()
                            .getPackageName() + ".hk.hku.cs.seemycourse.provider",
                    Util.GenerateFilePath("temp.jpg")
            );
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Select an image from device and crop it
     */
    @OnClick(R.id.btn_select_image)
    public void selectImage() {
        CropImage.activity().start(ctx, this);
    }

    /**
     * Using Google ML Kit API to Recognize Text Information
     */
    @OnClick(R.id.btn_recognize)
    public void recognizeImage() {
        if (imageUri == null) return;
        Util.recognizeText(ctx, imageUri, new Util.Callback<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText result) {
                List<TextBlock> textBlocks = result.getTextBlocks();
                ArrayList<MetaText> blocks = new ArrayList<>();
                for (TextBlock block : textBlocks) {
//                    String blockText = block.getText();
//                    TetrisPoint[] blockCornerPoints = block.getCornerPoints();
//                    Rect blockFrame = block.getBoundingBox();
                    for (FirebaseVisionText.Line line: block.getLines()) {
//                        String lineText = line.getText();
//                        Float lineConfidence = line.getConfidence();
//                        TetrisPoint[] lineCornerPoints = line.getCornerPoints();
//                        Rect lineFrame = line.getBoundingBox();
                        for (FirebaseVisionText.Element element: line.getElements()) {
                            String elementText = element.getText();
                            Rect elementFrame = element.getBoundingBox();
//                            Float elementConfidence = element.getConfidence();
//                            TetrisPoint[] elementCornerPoints = element.getCornerPoints();

                            blocks.add(new MetaText(elementText, new RectF(elementFrame)));
                        }
                    }
                }
                drawCanvas(blocks);
            }

            @Override
            public void onFailure(String message) { makeSnackbar(message); }
        });
    }

    /**
     * Draw Text to Bitmap
     * @param textBlocks text blocks that recognize from image
     */
    private void drawCanvas(ArrayList<MetaText> textBlocks) {
        // Background Template
        Bitmap template = BitmapFactory
                .decodeResource(getResources(), R.mipmap.night);

        // Create a Empty Bitmap
        Bitmap cover = Bitmap.createBitmap(
                template.getWidth(),
                template.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        // Create Canvas base on the Empty Bitmap
        Canvas canvas = new Canvas(cover);
        // Draw the template as the background
        canvas.drawBitmap(template, 0, 0, null);

        // Draw Masking
        canvas.drawARGB(80, 102, 204, 255);

        // Create a Paint Config of the Text
        Paint textPaintMesh = new Paint();
        textPaintMesh.setTextSize(32);
        textPaintMesh.setColor(Color.WHITE);
        textPaintMesh.setAntiAlias(true);
        textPaintMesh.setFakeBoldText(true);

        for (MetaText block : textBlocks) {
            RectF rect = block.getFrame();
            float left = rect.left / currentWidth * template.getWidth();
            float bottom = rect.bottom / currentHeight * template.getHeight();
            canvas.drawText(block.getText(), left, bottom, textPaintMesh);
        }

        iv.setImageBitmap(cover);
    }

    /**
     * Crop Image By Image Uri
     * @param imageUri uri of the image
     */
    private void cropImage(@NonNull Uri imageUri) {
        CropImage.activity(imageUri).start(ctx, this);
    }

    /**
     * Action after invoking activity
     * @param requestCode Request Code Of Activity
     * @param resultCode Result Status
     * @param data Information Need
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Take Photo Result
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode != RESULT_OK) {
                    makeSnackbar("No Photo Captured");
                    return;
                }

                cropImage(imageUri);
                break;
            // Crop Image Result
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (result == null) return;
                if (resultCode == RESULT_OK) {
                    imageUri = result.getUri();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), imageUri);
                        currentWidth = bitmap.getWidth();
                        currentHeight = bitmap.getHeight();
                        iv.setImageBitmap(Util.getResizedBitmap(bitmap, 1000));
                    } catch (IOException e) {
                        e.printStackTrace();
                        iv.setImageURI(imageUri);
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    makeSnackbar(error.getMessage());
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_REQUEST) {
            if (grantResults.length <= 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED
                    || grantResults[1] != PackageManager.PERMISSION_GRANTED
                    ) {
                makeSnackbar("Permission is denied.");
            }
        }
    }

    /**
     * Prompt a snackbar
     * @param message message to tell
     */
    private void makeSnackbar(String message) {
        Snackbar.make(btn_select_image, message, Snackbar.LENGTH_SHORT).show();
    }
}
