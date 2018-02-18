package com.petertieu.android.pocketpaint;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;


//Main interface of the app
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //============= Declare/define instance variables ===========================

    //Tag for LogCat
    private final static String TAG = "MainActivity";

    //PaintView for which the paint action takes place
    private PaintView mPaintView;


    LinearLayout mPresetPaintColors;


    private ImageButton mRedPaintImageButton, mOrangePaintImageButton, mYellowPaintImageButton, mGreenPaintImageButton, mBluePaintImageButton,
                        mIndigoPaintImageButton, mVioletPaintImageButton, mGrayPaintImageButton, mBlackPaintImageButton;

    //ImageButton for the current color
    private ImageButton mCurrentPaintColor;

    //ImageButtons (i.e. ACTION OPTIONS) in the top pallet
    private ImageButton mNewPaintingImageButton, mBrushImageButton, mEraserImageButton, mSizeChooserImageButton, mSavePaintingImageButton;


    private Button mSizeFeedbackButton, mColorFeedbackButton;

    //Brush sizes: EXTRA SMALL, SMALL, SMALL-MEDIUM, MEDIUM, MEDIUM-LARGE, LARGE, EXTRA-LARGE brush sizes
    private float   mExtraExtraSmallBrushSize, mExtraSmallBrushSize, mSmallBrushSize, mSmallMediumBrushSize, mMediumBrushSize,
                    mMediumLargeBrushSize, mLargeBrushSize, mExtraLargeBrushSize, mExtraExtraLargeBrushSize;


    private static final String[] STORAGE_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


    private static final int REQUEST_CODE_FOR_STORAGE_PERMISSIONS = 1;


    PaintView mViewToShare;


    //Override onCreate(..) activity lifecycle callback method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log in Logcat
        Log.i(TAG, "onCreate(..) called");


        if (hasWriteExternalStoragePermission() == false){
            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, REQUEST_CODE_FOR_STORAGE_PERMISSIONS);
        }

        //Set the view of the layout to activity_main.xml
        setContentView(R.layout.activity_main);

        //Assign the PaintView ref. var. to the associated resource ID in the activity_main.xml file
        mPaintView = (PaintView) findViewById(R.id.paint_view);




        //Assign the layout for PRESET PAINT COLORS (Bottom Pallet)
        mPresetPaintColors = (LinearLayout) findViewById(R.id.paint_colors_pallet);

        mRedPaintImageButton = (ImageButton) findViewById(R.id.red_paint_button);
        mOrangePaintImageButton = (ImageButton) findViewById(R.id.orange_paint_button);
        mYellowPaintImageButton = (ImageButton) findViewById(R.id.yellow_paint_button);
        mGreenPaintImageButton = (ImageButton) findViewById(R.id.green_paint_button);
        mBluePaintImageButton = (ImageButton) findViewById(R.id.blue_paint_button);
        mIndigoPaintImageButton = (ImageButton) findViewById(R.id.indigo_paint_button);
        mVioletPaintImageButton = (ImageButton) findViewById(R.id.violet_paint_button);
        mGrayPaintImageButton = (ImageButton) findViewById(R.id.gray_paint_button);
        mBlackPaintImageButton = (ImageButton) findViewById(R.id.black_paint_button);






        //Set default color
        mCurrentPaintColor = (ImageButton) mPresetPaintColors.getChildAt(7);
        mCurrentPaintColor.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_pressed));



        mNewPaintingImageButton = (ImageButton) findViewById(R.id.new_painting_image_button);
        mNewPaintingImageButton.setOnClickListener(this);
        mNewPaintingImageButton.setBackgroundResource(android.R.drawable.btn_default);



        mBrushImageButton = (ImageButton) findViewById(R.id.brush_image_button);
        mBrushImageButton.setOnClickListener(this);
        mBrushImageButton.setBackgroundResource(android.R.drawable.btn_default);



        mEraserImageButton = (ImageButton) findViewById(R.id.eraser_image_button);
        mEraserImageButton.setOnClickListener(this);
        mEraserImageButton.setBackgroundResource(android.R.drawable.btn_default);


        mSizeChooserImageButton = (ImageButton) findViewById(R.id.size_chooser_image_button);
        mSizeChooserImageButton.setOnClickListener(this);
        mSizeChooserImageButton.setBackgroundResource(android.R.drawable.btn_default);



        mSizeFeedbackButton = (Button) findViewById(R.id.size_feedback);
        mSizeFeedbackButton.setEnabled(false);
        mSizeFeedbackButton.setText("Size: " + mPaintView.getCurrentSize());

        mColorFeedbackButton = (Button) findViewById(R.id.color_feedback);
        mColorFeedbackButton.setEnabled(false);
        mColorFeedbackButton.setBackgroundColor(getResources().getColor(R.color.gray));





        mSavePaintingImageButton = (ImageButton) findViewById(R.id.save_painting_image_button);
        mSavePaintingImageButton.setOnClickListener(this);
        mSavePaintingImageButton.setBackgroundResource(android.R.drawable.btn_default);









//        mExtraExtraSmallBrushSize = (int) getResources().getDimension(R.dimen.extra_extra_small_brush_size);
//        mExtraSmallBrushSize    =   (int) getResources().getDimension(R.dimen.extra_small_brush_size);
//        mSmallBrushSize         =   (int) getResources().getDimension(R.dimen.small_brush_size);
//        mSmallMediumBrushSize   =   (int) getResources().getDimension(R.dimen.small_medium_brush_size);
//        mMediumBrushSize        =   (int) getResources().getDimension(R.dimen.medium_brush_size);
//        mMediumLargeBrushSize   =   (int) getResources().getDimension(R.dimen.medium_large_brush_size);
//        mLargeBrushSize         =   (int) getResources().getDimension(R.dimen.large_brush_size);
//        mExtraLargeBrushSize    =   (int) getResources().getDimension(R.dimen.extra_large_brush_size);



        mExtraExtraSmallBrushSize   = 2;
        mExtraSmallBrushSize        = 5;
        mSmallBrushSize             = 10;
        mSmallMediumBrushSize       = 15;
        mMediumBrushSize            = 20;
        mMediumLargeBrushSize       = 25;
        mLargeBrushSize             = 30;
        mExtraLargeBrushSize        = 35;
        mExtraExtraLargeBrushSize   = 40;








        //================ SET UP mViewToShare ==================================================================
        //Define the View to capture in order to share the Painting.
        // In this case, it is the PaintView View
        mViewToShare = (PaintView) mPaintView.findViewById(R.id.paint_view);


    }



    //Check if (dangerous) permissions from the STORAGE permission group have been granted at runtime (by the user)
    private boolean hasWriteExternalStoragePermission(){

        //Check if the permissions have been granted
        //IF granted.. result = PakageManager.PERMISSION_GRANTED, else PackageManager.PERMISSON_DENIED
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //Return the result
        return (result == PackageManager.PERMISSION_GRANTED);
    }



    @Override
    public void onClick(View view){

        int resourceId = view.getId();

        switch (resourceId){

            case R.id.new_painting_image_button:
                showNewPaintingConfirmationDialog();
                break;

            case R.id.brush_image_button:
                selectBrush();
                break;

            case R.id.eraser_image_button:
                selectEraser();
                break;

            case R.id.size_chooser_image_button:
                showSizeSelectorChooserDialog();
                break;

            case R.id.save_painting_image_button:
                showSavePaintingConfirmationDialog();
                break;
        }

    }




    //Listener for each PRESET PAINT COLOR ImageButton (called by the onClick attribute of each PRESET PAINT COLOR ImageButton in activity_main.xml)
    public void presetPaintColorPressed(View view){

        if (view != getCurrentFocus()){
            ImageButton imageButton = (ImageButton) view;

            String colorTag = imageButton.getTag().toString();

            mPaintView.setCurrentPaintColor(colorTag);

            mColorFeedbackButton.setBackgroundColor(Color.parseColor(colorTag));

            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_pressed));

            mCurrentPaintColor.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));

            mCurrentPaintColor = (ImageButton) view;

            mPaintView.setEraser(false);

            mPaintView.setCurrentSize(mPaintView.getPreviousSize());

            mSizeFeedbackButton.setText("Size: " + mPaintView.getCurrentSize());

        }


    }









    public void showNewPaintingConfirmationDialog(){
        final AlertDialog.Builder newPaintingConfirmationDialog = new AlertDialog.Builder(this);
        newPaintingConfirmationDialog.setTitle("New Painting");
        newPaintingConfirmationDialog.setMessage("Start a new Painting?");
        newPaintingConfirmationDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeAllColorImageButtons();
                        setBackgroundButtonPressed(mNewPaintingImageButton);
                        mPaintView.startNewPainting();
                        dialogInterface.dismiss();

                        Toast newPaintingToast = Toast.makeText(getApplicationContext(), "New Painting", Toast.LENGTH_LONG);
                        newPaintingToast.show();
                    }
                }
        );

        newPaintingConfirmationDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });

        newPaintingConfirmationDialog.show();


    }





    public void selectBrush(){

        setBackgroundButtonPressed(mBrushImageButton);

        showAllColorImageButtons();

        mPaintView.setEraser(false);



    }












    public void selectEraser(){

        mPaintView.setEraser(true);
        setBackgroundButtonPressed(mEraserImageButton);
        showAllColorImageButtons();
        removeAllColorImageButtons();

    }











    public void showSizeSelectorChooserDialog(){
        final Dialog sizeSelectorChooserDialog = new Dialog(this);

        sizeSelectorChooserDialog.setContentView(R.layout.dialog_size_chooser);

        sizeSelectorChooserDialog.setTitle("Brush and Eraser Size:");


        ImageButton extraExtraSmallBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.extra_extra_small_brush_size);
        extraExtraSmallBrushSize.setBackgroundResource(android.R.drawable.btn_default);
        extraExtraSmallBrushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPresetPaintColors.setVisibility(View.GONE);
                mPaintView.setCurrentSize(mExtraExtraSmallBrushSize);
                mPaintView.setPreviousSize(mExtraExtraSmallBrushSize);
                mSizeFeedbackButton.setText("Size: " + mPaintView.getCurrentSize());
                sizeSelectorChooserDialog.dismiss();
                Toast sizeToast = Toast.makeText(getApplicationContext(), "Size: " + mPaintView.getCurrentSize(), Toast.LENGTH_SHORT);
                sizeToast.show();
            }
        });

        ImageButton extraSmallBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.extra_small_brush_size);
        extraSmallBrushSize.setBackgroundResource(android.R.drawable.btn_default);
        extraSmallBrushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPresetPaintColors.setVisibility(View.GONE);
                mPaintView.setCurrentSize(mExtraSmallBrushSize);
                mPaintView.setPreviousSize(mExtraSmallBrushSize);
                mSizeFeedbackButton.setText("Size: " + mPaintView.getCurrentSize());
                sizeSelectorChooserDialog.dismiss();
                Toast sizeToast = Toast.makeText(getApplicationContext(), "Size: " + mPaintView.getCurrentSize(), Toast.LENGTH_SHORT);
                sizeToast.show();
            }
        });

        ImageButton smallBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.small_brush_size);
        smallBrushSize.setBackgroundResource(android.R.drawable.btn_default);
        smallBrushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPresetPaintColors.setVisibility(View.GONE);
                mPaintView.setCurrentSize(mSmallBrushSize);
                mPaintView.setPreviousSize(mSmallBrushSize);
                mSizeFeedbackButton.setText("Size: " + mPaintView.getCurrentSize());
                sizeSelectorChooserDialog.dismiss();
                Toast sizeToast = Toast.makeText(getApplicationContext(), "Size: " + mPaintView.getCurrentSize(), Toast.LENGTH_SHORT);
                sizeToast.show();
            }
        });


        ImageButton smallMediumBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.small_medium_brush_size);
        smallMediumBrushSize.setBackgroundResource(android.R.drawable.btn_default);
        smallMediumBrushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPresetPaintColors.setVisibility(View.GONE);
                mPaintView.setCurrentSize(mSmallMediumBrushSize);
                mPaintView.setPreviousSize(mSmallMediumBrushSize);
                mSizeFeedbackButton.setText("Size: " + mPaintView.getCurrentSize());
                sizeSelectorChooserDialog.dismiss();
                Toast sizeToast = Toast.makeText(getApplicationContext(), "Size: " + mPaintView.getCurrentSize(), Toast.LENGTH_SHORT);
                sizeToast.show();
            }
        });


        ImageButton mediumBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.medium_brush_size);
        mediumBrushSize.setBackgroundResource(android.R.drawable.btn_default);
        mediumBrushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPresetPaintColors.setVisibility(View.GONE);
                mPaintView.setCurrentSize(mMediumBrushSize);
                mPaintView.setPreviousSize(mMediumBrushSize);
                mSizeFeedbackButton.setText("Size: " + mPaintView.getCurrentSize());
                sizeSelectorChooserDialog.dismiss();
                Toast sizeToast = Toast.makeText(getApplicationContext(), "Size: " + mPaintView.getCurrentSize(), Toast.LENGTH_SHORT);
                sizeToast.show();
            }
        });


        ImageButton mediumLargeBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.medium_large_brush_size);
        mediumLargeBrushSize.setBackgroundResource(android.R.drawable.btn_default);
        mediumLargeBrushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPresetPaintColors.setVisibility(View.GONE);
                mPaintView.setCurrentSize(mMediumLargeBrushSize);
                mPaintView.setPreviousSize(mMediumLargeBrushSize);
                mSizeFeedbackButton.setText("Size: " + mPaintView.getCurrentSize());
                sizeSelectorChooserDialog.dismiss();
                Toast sizeToast = Toast.makeText(getApplicationContext(), "Size: " + mPaintView.getCurrentSize(), Toast.LENGTH_SHORT);
                sizeToast.show();
            }
        });


        ImageButton largeBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.large_brush_size);
        largeBrushSize.setBackgroundResource(android.R.drawable.btn_default);
        largeBrushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPresetPaintColors.setVisibility(View.GONE);
                mPaintView.setCurrentSize(mLargeBrushSize);
                mPaintView.setPreviousSize(mLargeBrushSize);
                mSizeFeedbackButton.setText("Size: " + mPaintView.getCurrentSize());
                sizeSelectorChooserDialog.dismiss();
                Toast sizeToast = Toast.makeText(getApplicationContext(), "Size: " + mPaintView.getCurrentSize(), Toast.LENGTH_SHORT);
                sizeToast.show();
            }
        });

        ImageButton extraLargeBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.extra_large_brush_size);
        extraLargeBrushSize.setBackgroundResource(android.R.drawable.btn_default);
        extraLargeBrushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPresetPaintColors.setVisibility(View.GONE);
                mPaintView.setCurrentSize(mExtraLargeBrushSize);
                mPaintView.setPreviousSize(mExtraLargeBrushSize);
                mSizeFeedbackButton.setText("Size: " + mPaintView.getCurrentSize());
                sizeSelectorChooserDialog.dismiss();
                Toast sizeToast = Toast.makeText(getApplicationContext(), "Size: " + mPaintView.getCurrentSize(), Toast.LENGTH_SHORT);
                sizeToast.show();
            }
        });

        ImageButton extraExtraLargeBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.extra_extra_large_brush_size);
        extraExtraLargeBrushSize.setBackgroundResource(android.R.drawable.btn_default);
        extraExtraLargeBrushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mPresetPaintColors.setVisibility(View.GONE);
                mPaintView.setCurrentSize(mExtraExtraLargeBrushSize);
                mPaintView.setPreviousSize(mExtraExtraLargeBrushSize);
                mSizeFeedbackButton.setText("Size: " + mPaintView.getCurrentSize());
                sizeSelectorChooserDialog.dismiss();
                Toast sizeToast = Toast.makeText(getApplicationContext(), "Size: " + mPaintView.getCurrentSize(), Toast.LENGTH_SHORT);
                sizeToast.show();
            }
        });


        sizeSelectorChooserDialog.show();

    }





















    public void showSavePaintingConfirmationDialog(){
        final AlertDialog.Builder savePaintingConfirmationDialog = new AlertDialog.Builder(this);



        if (hasWriteExternalStoragePermission() == false){
            savePaintingConfirmationDialog.setTitle("Unable to Save Painting");
            savePaintingConfirmationDialog.setMessage(Html.fromHtml(
                                                    "Painting could not be saved." +
                                                            "<br>" + "</br>" + //New line
                                                            "Allow Storage Permissions in Settings"));

            savePaintingConfirmationDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            savePaintingConfirmationDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removeAllColorImageButtons();
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });

        }



        else {
            savePaintingConfirmationDialog.setTitle("Save Painting");
            savePaintingConfirmationDialog.setMessage("Save Painting to Gallery?");
            savePaintingConfirmationDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removeAllColorImageButtons();
                            setBackgroundButtonPressed(mSavePaintingImageButton);
                            mPaintView.setDrawingCacheEnabled(true);

                            String paintingSaved = MediaStore.Images.Media.insertImage(getContentResolver(), mPaintView.getDrawingCache(), UUID.randomUUID().toString() + ".png", "PocketPaint");

                            if (paintingSaved != null) {
                                Toast paintingSavedToast = Toast.makeText(getApplicationContext(), "Painting Saved to Gallery", Toast.LENGTH_LONG);
                                paintingSavedToast.show();
                            } else {
                                Toast paintingNotSavedToast = Toast.makeText(getApplicationContext(), "Painting could not be saved!", Toast.LENGTH_LONG);
                                paintingNotSavedToast.show();
                            }

                            //Important line.. without it, the previous drawing cache will be used
                            mPaintView.destroyDrawingCache();
                        }
                    });

            savePaintingConfirmationDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
        }

        savePaintingConfirmationDialog.show();

    }




    private void setBackgroundButtonPressed(View view){

        mNewPaintingImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mBrushImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mEraserImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mSizeChooserImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mSavePaintingImageButton.setBackgroundResource(android.R.drawable.btn_default);

        view.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
    }




    private void showAllColorImageButtons(){
        mRedPaintImageButton.setVisibility(View.VISIBLE);
        mOrangePaintImageButton.setVisibility(View.VISIBLE);
        mYellowPaintImageButton.setVisibility(View.VISIBLE);
        mGreenPaintImageButton.setVisibility(View.VISIBLE);
        mBluePaintImageButton.setVisibility(View.VISIBLE);
        mIndigoPaintImageButton.setVisibility(View.VISIBLE);
        mVioletPaintImageButton.setVisibility(View.VISIBLE);
        mGrayPaintImageButton.setVisibility(View.VISIBLE);
        mBlackPaintImageButton.setVisibility(View.VISIBLE);
    }





    private void removeAllColorImageButtons(){
        mRedPaintImageButton.setVisibility(View.GONE);
        mOrangePaintImageButton.setVisibility(View.GONE);
        mYellowPaintImageButton.setVisibility(View.GONE);
        mGreenPaintImageButton.setVisibility(View.GONE);
        mBluePaintImageButton.setVisibility(View.GONE);
        mIndigoPaintImageButton.setVisibility(View.GONE);
        mVioletPaintImageButton.setVisibility(View.GONE);
        mGrayPaintImageButton.setVisibility(View.GONE);
        mBlackPaintImageButton.setVisibility(View.GONE);
    }












    //Override onCreateOptionsMenu(..) activity callback method
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        //Log lifecycle callback
        Log.i(TAG, "onCreateOptionsMenu(..) called");

        MenuInflater menuInflater = getMenuInflater();

        //Inflate a menu hierarchy from specified resource
        menuInflater.inflate(R.menu.activity_main, menu);

        return true;
    }



    //Override onOptionsItemSelected(..) activity callback method
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        Log.i(TAG, "onOptionsItemSelected(..) called");

        switch (menuItem.getItemId()){

            case (R.id.share_painting):
                sharePainting(mViewToShare);
                return true;



            default:
                return super.onOptionsItemSelected(menuItem);

        }


    }



    //Share the Painting
    private void sharePainting(View viewToShare){


        //'Screenshot' the View to share, and store it in a Bitmap object
        Bitmap viewToShareBitmap = getBitmapFromView(viewToShare);




        //Try 'risky' task - getExternalCatcheDir() can throw an Exception
        try {
            //Get absolute path to application-specific directory on the primary shared/external storage device to store cache files
            //NOTE: These files are internal to the application, and not typically visible to the user as media
            File file = new File(this.getExternalCacheDir(), "paintingViewToShare.png");

            //Create the FileOutputStream to write data to the File
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            //Compress the bitmap into the FileOutputStream
            viewToShareBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

            //Force the buffered output bytes to be written out
            fileOutputStream.flush();

            //Closes the output stream and releases any system resources associated with the stream
            fileOutputStream.close();

            //Set the owner's read permission for the abstract pathname
            file.setReadable(true, false);

            //Set Intent to send file
            final Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);

            //Set the special flag controlling how this intent is handled
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Add the file to URI of the file as extra in the intent
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

            //Set image type as png
            sendIntent.setType("image/png");

            //Start chooser activity
            startActivity(Intent.createChooser(sendIntent, "Share Painting via"));
        }
        //Catch potential Exception thrown by getExternalCacheDir()
        catch (Exception e) {
            //Prince the stacktrace containing the exception
            e.printStackTrace();
        }

    }





    //'Screenshot' the View to share, and store it in a Bitmap object
    private Bitmap getBitmapFromView(View view) {

        //Create drawable
        Bitmap viewToShareBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        //Create a Canvas object, passing in the Bitmap version of the view to share
        Canvas canvas = new Canvas(viewToShareBitmap);

        //Create background drawable object
        Drawable backgroundDrawable = view.getBackground();

        //If background drawable EXISTS
        if (backgroundDrawable != null) {
            //Draw the background drawable on the draw on the canvas
            backgroundDrawable.draw(canvas);
        }
        //If background drawable does NOT exist
        else {
            //Draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }

        //Draw the canvas on the View
        view.draw(canvas);

        //Return view to share as a Bitmap
        return viewToShareBitmap;
    }





    //onStart() activity lifecycle callback method
    @Override
    public void onStart(){
        super.onStart();

        //Log in Logcat
        Log.i(TAG, "onStart() called");
    }





    //onResume() activity lifecycle callback method
    @Override
    public void onResume(){
        super.onResume();

        //Log in Logcat
        Log.i(TAG, "onResume() called");
    }





    //onPause() activity lifecycle callback method
    @Override
    public void onPause(){
        super.onPause();

        //Log in Logcat
        Log.i(TAG, "onPause() called");
    }





    //onStop() activity lifecycle callback method
    @Override
    public void onStop(){
        super.onStop();

        //Log in Logcat
        Log.i(TAG, "onStop() called");
    }





    //onDestroy() activity lifecycle callback method
    @Override
    public void onDestroy(){
        super.onDestroy();

        //Log in Logcat
        Log.i(TAG, "onDestroy() called");
    }




}
