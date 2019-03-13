package com.example.BillReceipt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import static android.content.ContentValues.TAG;

public class GalleryActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    Bitmap img;
    GraphicOverlay<OcrGraphic> mGraphicOverlay;
    ImageView imgView;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gestureDetector = new GestureDetector(this, new CaptureGestureListener());

        SpannableString s = new SpannableString("$napney");
        s.setSpan(new TypefaceSpan(this, "Playball.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);


        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        imgView = (ImageView) findViewById(R.id.imageView);

        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {

        return  gestureDetector.onTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {

        return gestureDetector.onTouchEvent(ev);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = this.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);

                cursor.close();

                img = BitmapFactory.decodeFile(imgDecodableString);

                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(img);


                getTextFromImage();

            } else {
                Toast.makeText(this, R.string.no_picture_chosen,
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void getTextFromImage() {

        mGraphicOverlay = (GraphicOverlay<OcrGraphic>) findViewById(R.id.graphicOverlay2);
        Context context = this;
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();

        if (!textRecognizer.isOperational()) {
            Toast.makeText(this,R.string.detector,Toast.LENGTH_SHORT );
        }


        Frame frame = new Frame.Builder().setBitmap(BitmapFactory.decodeFile(imgDecodableString)).build();
        SparseArray<TextBlock> text = textRecognizer.detect(frame);

        for (int i = 0; i < text.size(); ++i) {
            TextBlock item = text.valueAt(i);
            if (item != null && item.getValue() != null) {
                Log.d("Processor", "Text detected! " + item.getValue());
            }
            OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);
        }


    }

    private boolean onTap(float rawX, float rawY) {
        OcrGraphic graphic = mGraphicOverlay.getGraphicAtLocation(rawX, rawY);
        TextBlock text = null;
        if (graphic != null) {
            text = graphic.getTextBlock();
            if (text != null && text.getValue() != null) {
                Log.d(TAG, "Textdaten werden übernommen! " + text.getValue());
                final EditText txtUrl = new EditText(GalleryActivity.this);

                // Sets the hint to a randomly generated example
                txtUrl.setText(text.getValue());

                new android.support.v7.app.AlertDialog.Builder(GalleryActivity.this)
                        .setTitle(R.string.found)
                        .setMessage(R.string.change_value)
                        .setView(txtUrl)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String sum = txtUrl.getText().toString();
                                saveReceipt(sum);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
            }
            else {
                Log.d(TAG, "text == null");
            }
        }
        else {
            Log.d(TAG,"Kein Text gefunden");
        }
        return text != null;
    }

    private class CaptureGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onTap(e.getRawX(), e.getRawY()) || super.onSingleTapConfirmed(e);
        }
    }
    public void saveReceipt(String sum) {


        final String PREFS_NAME = "SN_PREFS";
        final String PREFS_KEY = "SN_PREFS_MONEY";

        float currentSum = -123;
        SharedPreferences settings;
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();

        sum = sum.replace(",",".");
        sum = sum.replace("€","");
        sum = sum.replace("$","");
        currentSum = Float.parseFloat(sum);


        float saved = settings.getFloat(PREFS_KEY, 0);

        if (currentSum != -123) {
            saved += currentSum;
            editor.putFloat(PREFS_KEY, saved);
            editor.commit();
        } else {
            Snackbar.make(mGraphicOverlay, R.string.addition_error,
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }


}
