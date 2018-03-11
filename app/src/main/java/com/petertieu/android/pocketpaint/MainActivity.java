package com.petertieu.android.pocketpaint;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import yuku.ambilwarna.AmbilWarnaDialog;


//Class that sets the main interface of the app
public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    //=================================Declare/define instance variables ============================================================================

    //-------- ESSENTIALS -------
    //Tag for LogCat
    private final static String TAG = "MainActivity";

    //PaintView (custom view) for which the paint action takes place
    private PaintView mPaintView;

    //Default Brush Color - BLUE
    private String mColorTag = "#0000FF";

    //Pressed Brush Color
    private ImageButton mClickedBrushColor;




    //-------- FEEDBACK - for Brush Size and Color -------
    private Button mSizeFeedbackButton, mColorFeedbackButton;




    //-------- TOP PALLET and BOTTOM PALLET - ACTION BUTTONS -------
    private ImageButton mNewPaintingImageButton, mBrushImageButton, mEraserImageButton, mSizeChooserImageButton, mSavePaintingImageButton, mColorFillerImageButton;




    //---- PRESET COLORS (Below TOP PALLET) ---------------------
    //Layout for Preset Colors
    LinearLayout mPresetPaintColors;

    //Default Brush Color - BLUE
    private ImageButton mInitialPaintColorPressed;

    //Preset Colors
    private ImageButton mRedPaintImageButton, mOrangePaintImageButton, mYellowPaintImageButton, mGreenPaintImageButton, mBluePaintImageButton,
            mIndigoPaintImageButton, mVioletPaintImageButton, mGrayPaintImageButton, mBlackPaintImageButton, mColorPickerImageButton,
            mColorIdentifierImageButton, mPictureAdderImageButton, mShapeAdderImageButton, mTextAdderImageButton;




    //-------- CUSTOM COLORS (Above BOTTOM PALLET) -------
    //Total number of custom colors
    private final int MAX_NUMBER_OF_CUSTOM_COLORS = 6;

    //Number of colors picked
    private int mCustomColorCount = 0;

    //Custom Color Flag - indicates that no Custom Colors have been picked yet
    private boolean mCustomColorAssigned = false;

    //Title for Custom Colors
    private TextView mCustomColorsTextView;

    //Custom Colors
    private ImageButton mCustomColor1, mCustomColor2, mCustomColor3, mCustomColor4, mCustomColor5, mCustomColor6; //Custom color ImageButtons




    //-------- IDENTIFIED COLORS (Above BOTTOM PALLET)-------
    //Identified Color Flag - indicates that no colors have been identified yet
    private int mIdentifiedColorInt;

    //
    private boolean mIsColorIdentifierAssigned = false;


    //Title for Identified Colors
    private TextView mIdentifiedColorTextView;

    //Identified Color
    private ImageButton mIdentifiedColor;




    //-------- IDENTIFIED COLORS (Above BOTTOM PALLET)-------
    //STORAGE PERMISSIONS
    private static final String[] STORAGE_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    //Request code for STORAGE PERMISSIONS
    private static final int REQUEST_CODE_FOR_STORAGE_PERMISSIONS = 1;







    //================================= Declare/define methods ============================================================================

    //Override onCreate(..) activity lifecycle callback method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log in Logcat
        Log.i(TAG, "onCreate(..) called");


        //Request storage permission if necessary
        if (hasWriteExternalStoragePermission() == false){
            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, REQUEST_CODE_FOR_STORAGE_PERMISSIONS);
        }


        //Set the view of the layout to activity_main.xml
        setContentView(R.layout.activity_main);


        //PaintView custom view
        mPaintView = (PaintView) findViewById(R.id.paint_view);

        //Set initial Brush Size
        mPaintView.setBrushSize(mPaintView.mExtraExtraSmallBrushSize);



        //-------- FEEDBACK - for Brush Size and Color -------
            //Size
        mSizeFeedbackButton = (Button) findViewById(R.id.size_feedback);
        mSizeFeedbackButton.setEnabled(false);
        mSizeFeedbackButton.setText("xx-Small"); //Default Size
            //Color
        mColorFeedbackButton = (Button) findViewById(R.id.color_feedback);
        mColorFeedbackButton.setEnabled(false);
        mColorFeedbackButton.setBackgroundColor(getResources().getColor(R.color.blue));
        mColorFeedbackButton.setText("Brush");
        mColorFeedbackButton.setTextSize(15);
        mColorFeedbackButton.setTypeface(Typeface.DEFAULT_BOLD);
        mColorFeedbackButton.setTextColor(Color.WHITE);



        //-------- TOP PALLET and BOTTOM PALLET - ACTION BUTTONS -------
            //Brush
        mBrushImageButton = (ImageButton) findViewById(R.id.brush_image_button);
        mBrushImageButton.setOnClickListener(this);
        mBrushImageButton.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
            //Eraser
        mEraserImageButton = (ImageButton) findViewById(R.id.eraser_image_button);
        mEraserImageButton.setOnClickListener(this);
        mEraserImageButton.setBackgroundResource(android.R.drawable.btn_default);
            //Size Chooser
        mSizeChooserImageButton = (ImageButton) findViewById(R.id.size_chooser_image_button);
        mSizeChooserImageButton.setOnClickListener(this);
        mSizeChooserImageButton.setBackgroundResource(android.R.drawable.btn_default);
            //Save Painting
        mSavePaintingImageButton = (ImageButton) findViewById(R.id.save_painting_image_button);
        mSavePaintingImageButton.setOnClickListener(this);
        mSavePaintingImageButton.setBackgroundResource(android.R.drawable.btn_default);
            //New Painting
        mNewPaintingImageButton = (ImageButton) findViewById(R.id.new_painting_image_button);
        mNewPaintingImageButton.setOnClickListener(this);
        mNewPaintingImageButton.setBackgroundResource(android.R.drawable.btn_default);
            //Color Picker
        mColorPickerImageButton = (ImageButton) findViewById(R.id.color_picker);
        mColorPickerImageButton.setOnClickListener(this);
        mColorPickerImageButton.setBackgroundResource(android.R.drawable.btn_default);
            //Color Identifier
        mColorIdentifierImageButton = (ImageButton) findViewById(R.id.color_identifier);
        mColorIdentifierImageButton.setOnClickListener(this);
        mColorIdentifierImageButton.setBackgroundResource(android.R.drawable.btn_default);
            //Color Filler
        mColorFillerImageButton = (ImageButton) findViewById(R.id.color_filler);
        mColorFillerImageButton.setOnClickListener(this);
        mColorFillerImageButton.setBackgroundResource(android.R.drawable.btn_default);
            //Shape Adder
        mShapeAdderImageButton = (ImageButton) findViewById(R.id.shape_adder);
        mShapeAdderImageButton.setOnClickListener(this);
        mShapeAdderImageButton.setBackgroundResource(android.R.drawable.btn_default);
            //Text Adder
        mTextAdderImageButton = (ImageButton) findViewById(R.id.text_adder);
        mTextAdderImageButton.setOnClickListener(this);
        mTextAdderImageButton.setBackgroundResource(android.R.drawable.btn_default);
            //Picture Adder
        mPictureAdderImageButton = (ImageButton) findViewById(R.id.picture_adder);
        mPictureAdderImageButton.setOnClickListener(this);
        mPictureAdderImageButton.setBackgroundResource(android.R.drawable.btn_default);



        //---- PRESET COLORS (Below TOP PALLET) ---------------------
            //Layout for Preset Colors
        mPresetPaintColors = (LinearLayout) findViewById(R.id.paint_colors_pallet);
            //Default Brush Color - BLUE
        mInitialPaintColorPressed = (ImageButton) mPresetPaintColors.getChildAt(4);
        mInitialPaintColorPressed.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_pressed));
            //Colors
        mRedPaintImageButton = (ImageButton) findViewById(R.id.red_paint_button);
        mOrangePaintImageButton = (ImageButton) findViewById(R.id.orange_paint_button);
        mYellowPaintImageButton = (ImageButton) findViewById(R.id.yellow_paint_button);
        mGreenPaintImageButton = (ImageButton) findViewById(R.id.green_paint_button);
        mBluePaintImageButton = (ImageButton) findViewById(R.id.blue_paint_button);
        mIndigoPaintImageButton = (ImageButton) findViewById(R.id.indigo_paint_button);
        mVioletPaintImageButton = (ImageButton) findViewById(R.id.violet_paint_button);
        mGrayPaintImageButton = (ImageButton) findViewById(R.id.gray_paint_button);
        mBlackPaintImageButton = (ImageButton) findViewById(R.id.black_paint_button);



        //-------- CUSTOM COLORS (Above BOTTOM PALLET) -------
            //Title
        mCustomColorsTextView = (TextView) findViewById(R.id.custom_colors);
        mCustomColorsTextView.setVisibility(View.INVISIBLE);
            //Custom Colors
                //#1
        mCustomColor1 = (ImageButton) findViewById(R.id.custom_color_1);
        mCustomColor1.setTag("#00000000"); //Initialise tag of the custom color to TRANSPARENT
        mCustomColor1.setVisibility(View.INVISIBLE); //Ensures that when no custom color is set for this ImageButton, it could not be pressed on
                //#2
        mCustomColor2 = (ImageButton) findViewById(R.id.custom_color_2);
        mCustomColor2.setTag("#00000000"); //Initialise tag of the custom color to TRANSPARENT
        mCustomColor2.setVisibility(View.INVISIBLE); //Ensures that when no custom color is set for this ImageButton, it could not be pressed on
                //#3
        mCustomColor3 = (ImageButton) findViewById(R.id.custom_color_3);
        mCustomColor3.setTag("#00000000"); //Initialise tag of the custom color to TRANSPARENT
        mCustomColor3.setVisibility(View.INVISIBLE); //Ensures that when no custom color is set for this ImageButton, it could not be pressed on
                //#4
        mCustomColor4 = (ImageButton) findViewById(R.id.custom_color_4);
        mCustomColor4.setTag("#00000000"); //Initialise tag of the custom color to TRANSPARENT
        mCustomColor4.setVisibility(View.INVISIBLE); //Ensures that when no custom color is set for this ImageButton, it could not be pressed on
                //#5
        mCustomColor5 = (ImageButton) findViewById(R.id.custom_color_5);
        mCustomColor5.setTag("#00000000"); //Initialise tag of the custom color to TRANSPARENT
        mCustomColor5.setVisibility(View.INVISIBLE); //Ensures that when no custom color is set for this ImageButton, it could not be pressed on
                //#6
        mCustomColor6 = (ImageButton) findViewById(R.id.custom_color_6);
        mCustomColor6.setTag("#00000000"); //Initialise tag of the custom color to TRANSPARENT
        mCustomColor6.setVisibility(View.INVISIBLE); //Ensures that when no custom color is set for this ImageButton, it could not be pressed on



        //-------- IDENTIFIED COLORS (Above BOTTOM PALLET)-------
            //Title
        mIdentifiedColorTextView = (TextView) findViewById(R.id.identified_color_text);
        mIdentifiedColorTextView.setVisibility(View.INVISIBLE);
            //Idenfied Color
        mIdentifiedColor = (ImageButton) findViewById(R.id.identified_color);
        mIdentifiedColor.setTag("#00000000"); //Initialise tag of the custom color to TRANSPARENT
        mIdentifiedColor.setVisibility(View.INVISIBLE);
    }




    //Check if (dangerous) permissions from the STORAGE permission group have been granted at runtime (by the user)
    private boolean hasWriteExternalStoragePermission(){

        //Check if the permissions have been granted
        //IF granted.. result = PakageManager.PERMISSION_GRANTED, else PackageManager.PERMISSON_DENIED
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //Return the result
        return (result == PackageManager.PERMISSION_GRANTED);
    }




    //Listener for each Brush Color (called by the onClick attribute of each Brush Color's layout in activity_main.xml)
    public void onClickBrushColor(View clickeddBrushColor) {

        //The Brush Color that is pressed
        mClickedBrushColor = (ImageButton) clickeddBrushColor;

        //Get the Tag (color) associated with this View
        mColorTag = mClickedBrushColor.getTag().toString();


        //Set the Brush Color to the associated Tag
        mPaintView.setBrushColor(mColorTag);

        //Update mColorFeedbackButton
        mColorFeedbackButton.setBackgroundColor(Color.parseColor(mColorTag));


        //If the color pressed on is WHITE
        if (Color.parseColor(mColorTag) == Color.WHITE){
            mColorFeedbackButton.setTextColor(Color.BLACK); //Change the text color of th mColorFeedbackButton to BLACK
        }
        else{
            mColorFeedbackButton.setTextColor(Color.WHITE); //Otherwise, keep the text color as WHITE for all other colors
        }


        //Make only the Brush Color pressed appear in the "pressed state" - i.e. all Brush Colors appear in the "unpressed" state
        setBrushColorToPressedState(mClickedBrushColor);

        //Disable Eraser mode
        mPaintView.setEraser(false);

        //Set Brush Size
        mPaintView.setBrushSize(mPaintView.mBrushSize);

        //Set text of Size Feedback to display the (current) Brush Size
        mSizeFeedbackButton.setText(mPaintView.getBrushSizeString());
    }




    //Make only the Brush Color pressed appear in the "pressed state" - i.e. all Brush Colors appear in the "unpressed" state
    private void setBrushColorToPressedState(ImageButton currentColorPressed){

        //Set drawable of all Brush Colors to "unpressed" state
        //Preset Colors
        mRedPaintImageButton.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mOrangePaintImageButton.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mYellowPaintImageButton.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mGreenPaintImageButton.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mBluePaintImageButton.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mIndigoPaintImageButton.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mVioletPaintImageButton.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mGrayPaintImageButton.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mBlackPaintImageButton.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        //Custom Colors
        mCustomColor1.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mCustomColor2.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mCustomColor3.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mCustomColor4.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mCustomColor5.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        mCustomColor6.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));
        //Identified Color
        mIdentifiedColor.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_unpressed));


        //Set drawable of the clicked Brush Color to "pressed" state
        currentColorPressed.setImageDrawable(getResources().getDrawable(R.drawable.preset_paint_color_pressed));
    }




    //Override the onClick(View) method from the implemented View.OnClickListener(..) interface for the Action Buttons of: TOP PALLET and BOTTOM PALLET
    @Override
    public void onClick(View view){

        int resourceId = view.getId();

        switch (resourceId){
            //-----TOP PALLET - Action Buttons--------
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

            case R.id.new_painting_image_button:
                showNewPaintingConfirmationDialog();
                break;


            //-----BOTTOM PALLET - Action Buttons--------
            case R.id.color_picker:
                showColorPickerDialog();
                break;

            case R.id.color_identifier:
                selectColorIdentifier();
                break;

            case R.id.color_filler:
                selectColorFiller();
                break;

            case R.id.shape_adder:
                showShapeAdderDialog();
                break;


            case R.id.text_adder:
                selectTextAdder();
                break;

            case R.id.picture_adder:
                selectPictureAdder();
                break;
        }
    }




    //Brush (Action Button)
    public void selectBrush(){

        //If any touch events exist for mPaintView (in case Color Identifier, Color Filler, Shape Addeer, Text Adder or Picture Adder were pressed), then cancel it
        mPaintView.setOnTouchListener(null);

        //Display the Brush button in the "pressed state", and toggle all other Action Buttons in the "unpressed state"
        setBackgroundButtonPressed(mBrushImageButton);

        //SHOW all Brush Colors
        showAllBrushColors();

        //Disable Eraser Mode
        mPaintView.setEraser(false);


        if (mColorTag != null || !mColorTag.isEmpty()) {
            mColorFeedbackButton.setBackgroundColor(Color.parseColor(mColorTag));
        }


        //-------- FEEDBACK for Size and Color --------------
            //Update Feedback for Size
        mSizeFeedbackButton.setText(mPaintView.getBrushSizeString()); //Set text to Brush Size String
            //Update Feedback for Color
        mColorFeedbackButton.setText("Brush");
        mColorFeedbackButton.setTextColor(Color.WHITE);
        mColorFeedbackButton.setTextSize(15);
    }




    //Eraser (Action Button)
    public void selectEraser(){

        //If any touch events exist for mPaintView (in case Color Identifier, Color Filler, Shape Addeer, Text Adder or Picture Adder were pressed), then cancel it
        mPaintView.setOnTouchListener(null);

        //Display the Eraser button in the "pressed state", and toggle all other Action Buttons in the "unpressed state"
        setBackgroundButtonPressed(mEraserImageButton);

        //HIDE all Brush Colors
        hideAllColors();

        //Enable Eraser Mode
        mPaintView.setEraser(true);


        //-------- FEEDBACK for Size and Color --------------
            //Update Feedback for Size
        mSizeFeedbackButton.setText(mPaintView.getBrushSizeString()); //Set text to Brush Size String
            //Update Feedback for Color
        mColorFeedbackButton.setText("Eraser");
        mColorFeedbackButton.setTextColor(getResources().getColor(R.color.dark_red));
        mColorFeedbackButton.setTextSize(18);
        mColorFeedbackButton.setBackground(getResources().getDrawable(R.drawable.button_background_eraser));
    }




    //Size Chooser (Action Button)
    public void showSizeSelectorChooserDialog(){

        //Create Dialog for Size Chooser
        final Dialog sizeSelectorChooserDialog = new Dialog(this);

        //Set layout for Size Chooser dialog
        sizeSelectorChooserDialog.setContentView(R.layout.dialog_size_chooser);

        //Set title for the Size Chooser dialog
        sizeSelectorChooserDialog.setTitle("Brush and Eraser Size:");


        //-------- Obtain reference variables for each Brush Size button in the Dialog --------------
        ImageButton extraExtraSmallBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.extra_extra_small_brush_size);
        ImageButton extraSmallBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.extra_small_brush_size);
        ImageButton smallBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.small_brush_size);
        ImageButton smallMediumBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.small_medium_brush_size);
        ImageButton mediumBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.medium_brush_size);
        ImageButton mediumLargeBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.medium_large_brush_size);
        ImageButton largeBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.large_brush_size);
        ImageButton extraLargeBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.extra_large_brush_size);
        ImageButton extraExtraLargeBrushSize = (ImageButton) sizeSelectorChooserDialog.findViewById(R.id.extra_extra_large_brush_size);


        //-------- Set up LISTENERS for each Brush Size button in the Dialog --------------
        onBrushSizeChosen(sizeSelectorChooserDialog, extraExtraSmallBrushSize, mPaintView.mExtraExtraSmallBrushSize);
        onBrushSizeChosen(sizeSelectorChooserDialog, extraSmallBrushSize, mPaintView.mExtraSmallBrushSize);
        onBrushSizeChosen(sizeSelectorChooserDialog, smallBrushSize, mPaintView.mSmallBrushSize);
        onBrushSizeChosen(sizeSelectorChooserDialog, smallMediumBrushSize, mPaintView.mSmallMediumBrushSize);
        onBrushSizeChosen(sizeSelectorChooserDialog, mediumBrushSize, mPaintView.mMediumBrushSize);
        onBrushSizeChosen(sizeSelectorChooserDialog, mediumLargeBrushSize, mPaintView.mMediumLargeBrushSize);
        onBrushSizeChosen(sizeSelectorChooserDialog, largeBrushSize, mPaintView.mLargeBrushSize);
        onBrushSizeChosen(sizeSelectorChooserDialog, extraLargeBrushSize, mPaintView.mExtraLargeBrushSize);
        onBrushSizeChosen(sizeSelectorChooserDialog, extraExtraLargeBrushSize, mPaintView.mExtraExtraLargeBrushSize);

        //Show Dialog
        sizeSelectorChooserDialog.show();
    }


    //Helper method - Set up LISTNER for a Brush Size button in the Size Selector Chooser Dialog
    private void onBrushSizeChosen(final Dialog sizeSelectorChooserDialog, ImageButton brushImageButton, final float brushSize){
        brushImageButton.setBackgroundResource(android.R.drawable.btn_default);

        brushImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPaintView.setBrushSize(brushSize); //Set Brush size
                mSizeFeedbackButton.setText( mPaintView.getBrushSizeString()); //Set text of Feedback for Size
                sizeSelectorChooserDialog.dismiss(); //Dismiss the Dialog

                //Display Toast of the Brush Size selected
                Toast sizeToast = Toast.makeText(getApplicationContext(), mPaintView.getBrushSizeString(), Toast.LENGTH_SHORT);
                sizeToast.show();
            }
        });
    }




    //Save Painting (Action Button)
    public void showSavePaintingConfirmationDialog(){

        //Create confirmation Dialog for the Save Button
        final AlertDialog.Builder savePaintingConfirmationDialog = new AlertDialog.Builder(this);

        //If Storage permission is NOT granted
        if (hasWriteExternalStoragePermission() == false){
            savePaintingConfirmationDialog.setTitle("Unable to Save Painting");
            savePaintingConfirmationDialog.setMessage(Html.fromHtml(
                    "Painting could not be saved." +
                            "<br>" + "</br>" + //New line
                            "Allow Storage Permissions in Settings"));

            //Set "Cancel" button
            savePaintingConfirmationDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            //Set "Positive" button
            savePaintingConfirmationDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            hideAllColors(); //Hide all the Brush Colors

                            //Create Intent to open "Permissions Page" in Settings
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });

        }


        //If Storage permission IS granted
        else {
            savePaintingConfirmationDialog.setTitle("Save Painting");
            savePaintingConfirmationDialog.setMessage("Save Painting to Gallery?");

            //Set "Positive" button
            savePaintingConfirmationDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //If any touch events exist for mPaintView (in case Color Identifier, Color Filler, Shape Addeer, Text Adder or Picture Adder were pressed), then cancel it
                            mPaintView.setOnTouchListener(null);

                            //Hide all Brush Colors
                            hideAllColors();

                            //Display the Save button in the "pressed state", and toggle all other Action Buttons in the "unpressed state"
                            setBackgroundButtonPressed(mSavePaintingImageButton);


                            //"Disable" the Paint color (so that when mPaintView is touched, nothing happens)
                            mPaintView.mPaint.setColor(Color.TRANSPARENT);


                            //Enable drawing cache in order to save Bitmap to storage
                            mPaintView.setDrawingCacheEnabled(true);

                            //Save Painting to storage. The returned String stored in "paintingSaved" is the URL of the saved Painting
                            String savedPaintingURL = MediaStore.Images.Media.insertImage(getContentResolver(), mPaintView.getDrawingCache(),
                                                                                    UUID.randomUUID().toString() + ".png", "PocketPaint");

                            //If Painting was NOT saved to Storage
                            if (savedPaintingURL != null) {
                                Toast paintingSavedToast = Toast.makeText(getApplicationContext(), "Painting Saved to Gallery", Toast.LENGTH_LONG);
                                paintingSavedToast.show();
                            }
                            //If Painting was saved to Storage
                            else {
                                Toast paintingNotSavedToast = Toast.makeText(getApplicationContext(), "Painting could not be saved! Drawing Cache is not enabled!", Toast.LENGTH_LONG);
                                paintingNotSavedToast.show();
                            }

                            //Destroy the current drawing cache. Without this line, the previous drawing cache will be used
                            mPaintView.destroyDrawingCache();


                            //-------- FEEDBACK for Size and Color --------------
                                //Update Feedback for Size
                            mSizeFeedbackButton.setText(null);
                                //Update Feedback for Color
                            mColorFeedbackButton.setText(null);
                            mColorFeedbackButton.setBackgroundColor(Color.WHITE);
                            mColorFeedbackButton.setText("No Action To Perform");
                            mColorFeedbackButton.setTextSize(9);
                            mColorFeedbackButton.setTextColor(Color.BLACK);
                            mColorFeedbackButton.setBackground(getResources().getDrawable(R.drawable.button_background_checkers));
                        }
                    });

            //Set "Negative" button
            savePaintingConfirmationDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
        }

        //Show the dialog
        savePaintingConfirmationDialog.show();

    }




    //New Painting (Action Button)
    public void showNewPaintingConfirmationDialog(){

        //Create confirmation Dialog for the New Painting Button
        final AlertDialog.Builder newPaintingConfirmationDialog = new AlertDialog.Builder(this);
        newPaintingConfirmationDialog.setTitle("New Painting");
        newPaintingConfirmationDialog.setMessage("Start a new Painting?");

        //Set "Positive" button
        newPaintingConfirmationDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //If any touch events exist for mPaintView (in case Color Identifier, Color Filler, Shape Addeer, Text Adder or Picture Adder were pressed), then cancel it
                        mPaintView.setOnTouchListener(null);

                        //Start NEW Painting
                        mPaintView.startNewPainting();

                        //Hide all Brush Colors
                        hideAllColors();

                        //Display the Save button in the "pressed state", and toggle all other Action Buttons in the "unpressed state"
                        setBackgroundButtonPressed(mNewPaintingImageButton);

                        //"Disable" the Paint color (so that when mPaintView is touched, nothing happens)
                        mPaintView.mPaint.setColor(Color.TRANSPARENT);

                        //-------- Renew UNDO parameters --------------
                        //Nullify the Bitmap ArrayList from mPaintView (mPaintView) so that the undo-redo history could be cleared
                        mPaintView.mBitmapArrayList = null;

                        //Set the Menu icon "undo" to the "Unabled" drawable
                        sMenu.findItem(R.id.undo_painting).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_undo_unabled));



                        //-------- Renew SHAPE parameters --------------
                        //mLines to null, so that it does not refer to any pre-existing ArrayList of Shape objects
                        mLines = null;

                        //mRectangles to null, so that it does not refer to any pre-existing ArrayList of Shape objects
                        mRectangles = null;

                        //mOvals to null, so that it does not refer to any pre-existing ArrayList of Shape objects
                        mOvals = null;

                        //mCircles to null, so that it does not refer to any pre-existing ArrayList of Shape objects
                        mCircles = null;



                        //-------- FEEDBACK for Size and Color --------------
                            //Update Feedback for Size
                        mSizeFeedbackButton.setText(null);
                            //Update Feedback for Color
                        mColorFeedbackButton.setText(null);
                        mColorFeedbackButton.setText("No Action To Perform");
                        mColorFeedbackButton.setTextSize(9);
                        mColorFeedbackButton.setTextColor(Color.BLACK);
                        mColorFeedbackButton.setBackground(getResources().getDrawable(R.drawable.button_background_checkers));


                        //Show the dialog
                        dialogInterface.dismiss();

                        //Display Toast
                        Toast newPaintingToast = Toast.makeText(getApplicationContext(), "New Painting", Toast.LENGTH_LONG);
                        newPaintingToast.show();
                    }
                }
        );

        //Set "Negative" button
        newPaintingConfirmationDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });

        //Show dialog
        newPaintingConfirmationDialog.show();
    }




    //Color Picker (Action Button)
    public void showColorPickerDialog(){

        //If any touch events exist for mPaintView (in case Color Identifier, Color Filler, Shape Addeer, Text Adder or Picture Adder were pressed), then cancel it
        mPaintView.setOnTouchListener(null);

        //Create AmbiWarna dialog
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(MainActivity.this, mPaintView.getPaintColor(), false, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            //Override "OK" button (i.e. when color picked is confirmed)
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                //Convert the Custom Color flag to "true". This is so that when Brush Colors are shown via showAllBrushColors(), the custom color is also shown
                mCustomColorAssigned = true;

                //Show all the Brush Colors (including the picked color)
                showAllBrushColors();

                //Display the Color Picker button in the "pressed state", and toggle all other Action Buttons in the "unpressed state"
                setBackgroundButtonPressed(mColorPickerImageButton);



                //-------- FETCH Color Picked --------------
                //'color' is the color picked. It is in RGB format (e.g. black color is -16777216, as there are 16,777,216 RGB colors, and black is the last one).
                //Therefore, turn 'color' to a hexadecimal format (e.g. #000000, #FF0000, etc.) so that we could feed it to mPaintView.setBrushColor()
                String colorInHexString = "#" + Integer.toHexString(color);

                //Set the Brush Color to the color picked
                mPaintView.setBrushColor(colorInHexString);



                //-------- FEEDBACK for Size and Color --------------
                    //Update Feedback for Size
                if (mPaintView.getBrushSizeString().isEmpty()){ //If the Brush Size String has NOT been set yet
                    mSizeFeedbackButton.setText("xx-Small"); //Default initialise the Feedback for Size to be "Small"
                }
                else{ //If the Brush Size String HAS been set
                    mSizeFeedbackButton.setText(mPaintView.getBrushSizeString()); //Set text to Brush Size String
                }
                    //Update Feedback for Color
                mColorFeedbackButton.setBackgroundColor(Color.parseColor(colorInHexString));
                mColorFeedbackButton.setText("Brush");
                mColorFeedbackButton.setTextSize(15);
                mColorFeedbackButton.setTextColor(Color.WHITE);



                //-------- Display Color Picked in the 'Custom Color Pane' (Above BOTTOM PALET)--------------
                Log.i(TAG, "mCustomColorCount: " + mCustomColorCount);

                //Number of colors picked
                mCustomColorCount++;

                //"Placing' of the picked color in the 'Custom Color Pane' - Number varies from 0 to 5 (6 numbers)
                mCustomColorCount = mCustomColorCount % MAX_NUMBER_OF_CUSTOM_COLORS;


                //Match the 'placing' of the picked color in the 'Custom Color Pane'
                switch (mCustomColorCount) {

                    //Custom Color #1
                    case 1:
                        newCustomColor(mCustomColor1, colorInHexString); //Display the newly picked custom color in the Custom Color pane to be in "pressed state"
                        break;

                    //Custom Color #2
                    case 2:
                        newCustomColor(mCustomColor2, colorInHexString); //Display the newly picked custom color in the Custom Color pane to be in "pressed state"
                        break;

                    //Custom Color #3
                    case 3:
                        newCustomColor(mCustomColor3, colorInHexString); //Display the newly picked custom color in the Custom Color pane to be in "pressed state"
                        break;

                    //Custom Color #4
                    case 4:
                        newCustomColor(mCustomColor4, colorInHexString); //Display the newly picked custom color in the Custom Color pane to be in "pressed state"
                        break;

                    //Custom Color #5
                    case 5:

                        newCustomColor(mCustomColor5, colorInHexString); //Display the newly picked custom color in the Custom Color pane to be in "pressed state"
                        break;

                    //Custom Color #6
                    case 0:
                        newCustomColor(mCustomColor6, colorInHexString); //Display the newly picked custom color in the Custom Color pane to be in "pressed state"
                        break;
                }


                //If the picked color is WHITE
                if (colorInHexString.equals("#ffffffff")){
                    mColorFeedbackButton.setTextColor(Color.BLACK);
                }
                //If the picked color is a color OTHER THAN white
                else{
                    mColorFeedbackButton.setTextColor(Color.WHITE);
                }
            }


            //Set "Cancel" button
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getApplicationContext(), "No Custom Color Picked", Toast.LENGTH_SHORT).show();
            }
        });

        //Set title of dialog
        dialog.getDialog().setTitle("Assign a Custom Color");

        //Show dialog
        dialog.show();
    }


    //Helper method - Displays the newly picked custom color in the Custom Color pane to be in "pressed state"
    private void newCustomColor(ImageButton customColorButton, String colorInHexString){

        //Set the tag of this View to the Custom Color so that it could later be accessed by onClickBrushColor()
        customColorButton.setTag(colorInHexString);

        //Set the instance variable mColorTag to the Custom Color picked
        mColorTag = colorInHexString;

        //Set the Custom Color button to the picked color
        customColorButton.setBackgroundColor(Color.parseColor(colorInHexString));

        //Make only this View appear in the "pressed state" - i.e. all Brush Colors appear in the "unpressed" state
        setBrushColorToPressedState(customColorButton);

        //Display Toast to notify a custom color has been picked
        Toast toast = Toast.makeText(getApplicationContext(), "New Custom Color", Toast.LENGTH_SHORT);
        toast.show();
    }




    //Color Identifier (Action Button)
    private void selectColorIdentifier(){


        //-------- FEEDBACK for Size and Color --------------
            //Update Feedback for Size
        mSizeFeedbackButton.setText(null);
            //Update Feedback for Color
        mColorFeedbackButton.setText("Color Identifier");
        mColorFeedbackButton.setTextSize(13);
        mColorFeedbackButton.setTextColor(Color.WHITE);
        mColorFeedbackButton.setBackgroundColor(Color.BLACK);


        //Display only the Color Identifier button in the "pressed state", and toggle all other Action Buttons in the "unpressed state"
        setBackgroundButtonPressed(mColorIdentifierImageButton);

        //Hide all Brush Colors
        hideAllColors();

        //'Disable' the Brush Color by making it Transparent, so that pressing onto mPaintView would not cause anything to be drawn)
        mPaintView.mPaint.setColor(Color.TRANSPARENT);

        //Make the Identified Color (on the 'Color Identifier Pane' - above the BOTTOM PANE) visible
        mIdentifiedColor.setVisibility(View.VISIBLE);


        //Set listener for the PaintView custom view
        mPaintView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                //Convert the Color Identified flag to "true". This is so that when Brush Colors are shown via showAllBrushColors(), the identified color is also shown
                mIsColorIdentifierAssigned = true;

                //Set the title of the 'Color Identifier Pane' to VISIBLE
                mIdentifiedColorTextView.setVisibility(View.VISIBLE);

                //Get the x/y-axis co-ordinates of the CURRENT point of touch
                float touchX = motionEvent.getX(); //x-axis co-ordinate
                float touchY = motionEvent.getY(); //y-axis co-ordinate


                //Scan the motion event type
                switch (motionEvent.getAction()) {

                    case (MotionEvent.ACTION_MOVE):

                        //-------- FETCH Color of current co-ordinate pressed on --------------
                        //Get pixel of the co-oridinate pressed on
                        int pixel = mPaintView.getBitmap().getPixel((int) touchX, (int) touchY);

                        //Break the pixel to RGB components
                        int redComponent = Color.red(pixel);
                        int greenComponent = Color.green(pixel);
                        int blueComponent = Color.blue(pixel);

                        //Combine the RGB components to get the identified color in int
                        mIdentifiedColorInt = Color.rgb(redComponent, greenComponent, blueComponent);

                        //Parse the identified color from int to HexString, and add "#" to the start of the Hex String
                        String identifiedColorString = "#" + Integer.toHexString(mIdentifiedColorInt);



                        //--------Process Idenfied Color --------------
                        //Set the Brush Color
                        mPaintView.setBrushColor(identifiedColorString);

                        //Set background of this View to the identified color
                        mIdentifiedColor.setBackgroundColor(Color.parseColor(identifiedColorString));

                        //Set the tag of this View to the identified color so that it could later be accessed by onClickBrushColor()
                        mIdentifiedColor.setTag(identifiedColorString);

                        //Set the instance variable mColorTag to the color identified
                        mColorTag = identifiedColorString;

                        //Make only this View appear in the "pressed state" - i.e. all Brush Colors appear in the "unpressed" state
                        setBrushColorToPressedState(mIdentifiedColor);



                        //-------- FEEDBACK for Color --------------
                            //Update Feedback for Color
                        mColorFeedbackButton.setBackgroundColor(Color.parseColor(identifiedColorString));
                        if (identifiedColorString.equals("#ffffffff")) { //If the identified color is WHITE
                            Log.i(TAG, "color white");
                            mColorFeedbackButton.setTextColor(Color.BLACK); //Set the text color of the mColorFeebackButton to BLACK
                        }
                        else { //If the identified color is any color OTHER THAN white
                            mColorFeedbackButton.setTextColor(Color.WHITE); //Set the text color of the mColorFeebackButton to WHITE
                        }

                        return true;



                    case (MotionEvent.ACTION_UP):
                        //Do nothing
                        return false;
                }

                return true;
            }
        });
    }




    //Color Filler (Action Button)
    private void selectColorFiller(){

        //Show all the Brush Colors
        showAllBrushColors();

        //Display only the Color Filler button in the "pressed state", and toggle all other Action Buttons in the "unpressed state"
        setBackgroundButtonPressed(mColorFillerImageButton);

        //-------- FEEDBACK for Size and Color --------------
            //Update Feedback for Size
        mSizeFeedbackButton.setText(null);
            //Update Feedback for Color
        mColorFeedbackButton.setText("Fill Color");
        mColorFeedbackButton.setTextColor(Color.WHITE);
        mColorFeedbackButton.setBackgroundColor(Color.parseColor(mColorTag));
        mColorFeedbackButton.setTextSize(15);
        if (Color.parseColor(mColorTag) == Color.WHITE){ //If Brush Color is WHITE
            mColorFeedbackButton.setTextColor(Color.BLACK); //Set text of Feedback for Color to BLACK
        }
        else{ //If Brush Color is a color OTHER THAN white
            mColorFeedbackButton.setTextColor(Color.WHITE); //Set text of Feedback for Color to WHITE
        }


        //Set listener for mPaintView (custom view)
        mPaintView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                //Get the x/y-axis co-ordinates of the CURRENT point of touch
                float touchX = motionEvent.getX(); //x-axis co-ordinate
                float touchY = motionEvent.getY(); //y-axis co-ordinate


                //Scan the motion event type
                switch (motionEvent.getAction()) {

                    case (MotionEvent.ACTION_MOVE):

                        //-------- FETCH: INITIAL COLOR - Color of current co-ordinate pressed on --------------
                        //Get pixel of the co-oridinate pressed on
                        int pixel = mPaintView.getBitmap().getPixel((int) touchX, (int) touchY);

                        //Break the pixel to RGB components
                        int redComponent = Color.red(pixel);
                        int greenComponent = Color.green(pixel);
                        int blueComponent = Color.blue(pixel);

                        //Combine the RGB components to get the pixel color in int
                        int initialColor = Color.rgb(redComponent, greenComponent, blueComponent);



                        //-------- GET: DESTINATION COLOR - Color used for filling --------------
                        //Color to fill
                        int destinationColor = Color.parseColor(mColorTag);

                        //GET Bitmap to perform the fill onto
                        Bitmap bitmap = mPaintView.mBitmap;

                        //Create QueueLinearFloodFiller object
                        QueueLinearFloodFiller queueLinearFloodFiller = new QueueLinearFloodFiller(bitmap, initialColor, destinationColor);

                        //Apply Fill onto the co-ordinate pressed onto
                        queueLinearFloodFiller.floodFill((int) touchX, (int) touchY);


                        //Update the mPaintView View
                        mPaintView.invalidate();
                        break;



                    case (MotionEvent.ACTION_UP):

                        //========= SET UP UNDO FUNCTION ===============================================================================
                        //TODO: MAKE METHOD FROM THIS
                        //If the mBitmapArrayList is null. This occurs if a New Painting is created
                        if (mPaintView.mBitmapArrayList == null){
                            //Assign mBitmapArrayList to a new Bitmap ArrayList
                            mPaintView.mBitmapArrayList = new ArrayList<>();
                        }


                        //If the current color is NOT transparent. This occurs if a New Painting is created
                        if (mPaintView.mPaint.getColor() != Color.TRANSPARENT) {




                            //Make a copy (clone) of the mBitmap Bitmap
                            Bitmap cloneOfmBitmap = mPaintView.mBitmap.copy(mPaintView.mBitmap.getConfig(), true);

                            //Add the cloan of mBitmap to the Bitmap ArrayList
                            mPaintView.mBitmapArrayList.add(cloneOfmBitmap);





                            //If there are 1 or more Bitmap objects in the Bitmap ArrayList, set the Undo MenuItem to "enabled" state
                            // NOTE: This menu item was initialised to "unabled" drawable
                            if (mPaintView.mBitmapArrayList != null && mPaintView.mBitmapArrayList.size() > 0) {
                                MainActivity.sMenu.findItem(R.id.undo_painting).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_undo_enabled));
                            }


                            //Log to Logcat - Get size of mBitmapArrayList
                            Log.i("SizeOfmBitmapArrayList", Integer.toString(mPaintView.mBitmapArrayList.size()));
                        }


                        break;


                    default:
                        return true;
                }



                return true;
            }
        });



    }








    private final int REQUEST_CODE_PICTURE_GALLERY =  1;




    private void selectPictureAdder(){

        hideAllColors();

        mSizeFeedbackButton.setText(null);

        mColorFeedbackButton.setText("Add Picture");
        mColorFeedbackButton.setTextColor(Color.BLACK);
        mColorFeedbackButton.setBackgroundResource(R.drawable.button_background_checkers);
        mColorFeedbackButton.setTextSize(15);





        //    //Open gallery activity/app


        //Create implicit intent (to open gallery activity/app)
        Intent choosePictureIntent = new Intent();

        //Set action to open gallery activity/app
        choosePictureIntent.setAction(Intent.ACTION_GET_CONTENT);

        //Set type of media to open (i.e. images, instead of videos or both)
        choosePictureIntent.setType("image/*");



//        //Create PackageManager (which has access to all aps installed in the device)
//        PackageManager packageManager = getPackageManager();
//
//        //Query PackageManager to obtain list of activities/apps that match conditions of choosePictureIntent
//        List<ResolveInfo> galleryActivities = packageManager.queryIntentActivities(
//                choosePictureIntent, //(Intent): The intent to open camera
//                PackageManager.MATCH_DEFAULT_ONLY //Filter the query to only intents of the DEFAULT category
//        );



        //Start the activity, expecting results to be returned (via onActivityResult(..))
        startActivityForResult(Intent.createChooser(choosePictureIntent, "Select Picture"), REQUEST_CODE_PICTURE_GALLERY);




    }






    Paint mBackgroundPaint;

    private Shape mCurrentShape;

    private List<Shape> mLines = new ArrayList<>();
    private  List<Shape> mRectangles = new ArrayList<>();
    private List<Shape> mOvals = new ArrayList<>();
    private List<Shape> mCircles = new ArrayList<>();




    private Paint mCurrentShapePaint;
    private List<Paint> mShapePaints = new ArrayList<>();



    Bitmap mBitmapDrawingPane;
    Canvas mCanvasDrawingPane;



    Paint mShapeFillPaint;




    private void showShapeAdderDialog() {


        //If the touch event exists for mPaintView (in case mColorIdentifier is pressed), then set it to null.
        //If this line is omitted, then IF the touch event is set for mPaintView upon pressing mColorIdentifier, we wouldn't be able to exit it!
        mPaintView.setOnTouchListener(null);




        final Dialog shapeAdderDialog = new Dialog(this);
        shapeAdderDialog.setContentView(R.layout.dialog_shape_chooser);
        shapeAdderDialog.setTitle("Shapes to draw:");






        //Check if the ArrayList of any of these shapes refere to 'null', since all Shape ArrayLists are set to 'null' when the "New Painting" button is pressed
        if (mLines == null){
            mLines = new ArrayList<>();
        }

        if (mRectangles == null){
            mRectangles = new ArrayList<>();
        }

        if (mOvals == null){
            mOvals = new ArrayList<>();
        }

        if (mCircles == null){
            mCircles = new ArrayList<>();
        }



        mBitmapDrawingPane = Bitmap.createBitmap(mPaintView.mBitmap);
        mCanvasDrawingPane = new Canvas(mBitmapDrawingPane);



        ImageButton lineShape = (ImageButton) shapeAdderDialog.findViewById(R.id.line_shape);
        lineShape.setBackgroundResource(android.R.drawable.btn_default);
        lineShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: MAKE METHOD FROM THIS
                showAllBrushColors();
                setBackgroundButtonPressed(mShapeAdderImageButton);
                mSizeFeedbackButton.setText(null);
                mColorFeedbackButton.setText("Draw Line");
                mColorFeedbackButton.setTextColor(Color.WHITE);
                mColorFeedbackButton.setBackgroundColor(Color.parseColor(mColorTag));
                mColorFeedbackButton.setTextSize(12);

                if (Color.parseColor(mColorTag) == Color.WHITE){
                    mColorFeedbackButton.setTextColor(Color.BLACK);
                }
                else{
                    mColorFeedbackButton.setTextColor(Color.WHITE);
                }



                //=================DRAW LINE=======================================================
                mPaintView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        //Create a PointF object, 'current', which holds two float coordinates - x (horizontal) and y (vertical).
                        PointF pointFCurrentShape = new PointF(motionEvent.getX(), motionEvent.getY());



                        Paint currentShapePaint = new Paint(mPaintView.mPaint);
                        currentShapePaint.setColor(Color.parseColor(mColorTag));


                        switch (motionEvent.getAction()) {

                            case (MotionEvent.ACTION_DOWN):


                                mCurrentShape = new Shape(pointFCurrentShape, currentShapePaint);
                                mLines.add(mCurrentShape);

                                break;




                            case (MotionEvent.ACTION_MOVE):

                                if (mCurrentShape != null) {

                                    mCurrentShape.setCurrentPoint(pointFCurrentShape);

                                    mCanvasDrawingPane.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);



                                    //For-each loop to iterate through on Shape object in the mRectangles List of Shape objects at a time.
                                    //Any Shape object being currently worked on is the CURRENT Shape being added to the mRectangles List


                                    for (Shape shape : mLines) {

                                        Paint shapePaint = shape.getShapePaint();



                                        mCanvasDrawingPane.drawLine(shape.getStartingPoint().x, shape.getStartingPoint().y, shape.getCurrentPoint().x, shape.getCurrentPoint().y, shapePaint);
                                    }

//                                    mPaintView.invalidate();
                                }

                                break;






                            case (MotionEvent.ACTION_UP):









                                mPaintView.mCanvas.drawBitmap(mBitmapDrawingPane, 0, 0, null);
                                mCurrentShape = null;




                                //========= SET UP UNDO FUNCTION ===============================================================================
                                //TODO: MAKE METHOD FROM THIS
                                //If the mBitmapArrayList is null. This occurs if a New Painting is created
                                if (mPaintView.mBitmapArrayList == null) {
                                    //Assign mBitmapArrayList to a new Bitmap ArrayList
                                    mPaintView.mBitmapArrayList = new ArrayList<>();
                                }


                                //Make a copy (clone) of the mBitmap Bitmap
                                Bitmap cloneOfmBitmap = mPaintView.mBitmap.copy(mPaintView.mBitmap.getConfig(), true);

                                //Add the cloan of mBitmap to the Bitmap ArrayList
                                mPaintView.mBitmapArrayList.add(cloneOfmBitmap);


                                //If there are 1 or more Bitmap objects in the Bitmap ArrayList, set the Undo MenuItem to "enabled" state
                                // NOTE: This menu item was initialised to "unabled" drawable
                                if (mPaintView.mBitmapArrayList != null && mPaintView.mBitmapArrayList.size() > 0) {
                                    MainActivity.sMenu.findItem(R.id.undo_painting).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_undo_enabled));
                                }


                                //Log to Logcat - Get size of mBitmapArrayList
                                Log.i("SizeOfmBitmapArrayList", Integer.toString(mPaintView.mBitmapArrayList.size()));



                            break;



                        }










                        return true;
                    }
                });







//                mPaintView.invalidate();



                shapeAdderDialog.dismiss();


                Toast shapeModeToast = Toast.makeText(getApplicationContext(), "Circle mode", Toast.LENGTH_SHORT);
                shapeModeToast.show();
            }
        });









        ImageButton rectangleShape = (ImageButton) shapeAdderDialog.findViewById(R.id.rectangle_shape);
        rectangleShape.setBackgroundResource(android.R.drawable.btn_default);
        rectangleShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: MAKE METHOD FROM THIS
                showAllBrushColors();
                setBackgroundButtonPressed(mShapeAdderImageButton);
                mSizeFeedbackButton.setText("");
                mColorFeedbackButton.setText("Draw Rectangle");
                mColorFeedbackButton.setTextColor(Color.WHITE);
                mColorFeedbackButton.setBackgroundColor(Color.parseColor(mColorTag));
                mColorFeedbackButton.setTextSize(12);

                if (Color.parseColor(mColorTag) == Color.WHITE){
                    mColorFeedbackButton.setTextColor(Color.BLACK);
                }
                else{
                    mColorFeedbackButton.setTextColor(Color.WHITE);
                }




                //=================DRAW RECTANGLE=======================================================
                mPaintView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        //Create a PointF object, 'current', which holds two float coordinates - x (horizontal) and y (vertical).
                        PointF pointFCurrentShape = new PointF(motionEvent.getX(), motionEvent.getY());



                        Paint currentShapePaint = new Paint(mPaintView.mPaint);
                        currentShapePaint.setColor(Color.parseColor(mColorTag));


                        switch (motionEvent.getAction()) {

                            case (MotionEvent.ACTION_DOWN):


                                mCurrentShape = new Shape(pointFCurrentShape, currentShapePaint);
                                mRectangles.add(mCurrentShape);

                                break;




                            case (MotionEvent.ACTION_MOVE):

                                if (mCurrentShape != null) {

                                    mCurrentShape.setCurrentPoint(pointFCurrentShape);

                                    mCanvasDrawingPane.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);



                                    //For-each loop to iterate through on Shape object in the mRectangles List of Shape objects at a time.
                                    //Any Shape object being currently worked on is the CURRENT Shape being added to the mRectangles List


                                    for (Shape shape : mRectangles) {

                                        Paint shapePaint = shape.getShapePaint();


                                        float left = Math.min(shape.getStartingPoint().x, shape.getCurrentPoint().x);
                                        float right = Math.max(shape.getStartingPoint().x, shape.getCurrentPoint().x);
                                        float top = Math.min(shape.getStartingPoint().y, shape.getCurrentPoint().y);
                                        float bottom = Math.max(shape.getStartingPoint().y, shape.getCurrentPoint().y);


                                        if (Build.VERSION.SDK_INT >= 21) {
                                            mCanvasDrawingPane.drawRoundRect(left, top, right, bottom, 8, 8, shapePaint);
                                        } else {
                                            mCanvasDrawingPane.drawRect(left, top, right, bottom, shapePaint);
                                        }

                                    }

                                    mPaintView.invalidate();
                                }

                                break;


                            case (MotionEvent.ACTION_UP):





                                mPaintView.mCanvas.drawBitmap(mBitmapDrawingPane, 0, 0, null);
                                mCurrentShape = null;


                                //========= SET UP UNDO FUNCTION ===============================================================================
                                //TODO: MAKE METHOD FROM THIS
                                //If the mBitmapArrayList is null. This occurs if a New Painting is created
                                if (mPaintView.mBitmapArrayList == null) {
                                    //Assign mBitmapArrayList to a new Bitmap ArrayList
                                    mPaintView.mBitmapArrayList = new ArrayList<>();
                                }


                                //Make a copy (clone) of the mBitmap Bitmap
                                Bitmap cloneOfmBitmap = mPaintView.mBitmap.copy(mPaintView.mBitmap.getConfig(), true);

                                //Add the cloan of mBitmap to the Bitmap ArrayList
                                mPaintView.mBitmapArrayList.add(cloneOfmBitmap);


                                //If there are 1 or more Bitmap objects in the Bitmap ArrayList, set the Undo MenuItem to "enabled" state
                                // NOTE: This menu item was initialised to "unabled" drawable
                                if (mPaintView.mBitmapArrayList != null && mPaintView.mBitmapArrayList.size() > 0) {
                                    MainActivity.sMenu.findItem(R.id.undo_painting).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_undo_enabled));
                                }


                                //Log to Logcat - Get size of mBitmapArrayList
                                Log.i("SizeOfmBitmapArrayList", Integer.toString(mPaintView.mBitmapArrayList.size()));











                                break;
                        }
                        return true;
                    }
                });




                shapeAdderDialog.dismiss();


                Toast shapeModeToast = Toast.makeText(getApplicationContext(), "Rectangle mode", Toast.LENGTH_SHORT);
                shapeModeToast.show();


            }
        });









        ImageButton ovalShape = (ImageButton) shapeAdderDialog.findViewById(R.id.oval_shape);
        ovalShape.setBackgroundResource(android.R.drawable.btn_default);
        ovalShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: MAKE METHOD FROM THIS
                showAllBrushColors();
                setBackgroundButtonPressed(mShapeAdderImageButton);
                mSizeFeedbackButton.setText("");
                mColorFeedbackButton.setText("Draw Oval");
                mColorFeedbackButton.setTextColor(Color.WHITE);
                mColorFeedbackButton.setBackgroundColor(Color.parseColor(mColorTag));
                mColorFeedbackButton.setTextSize(12);

                if (Color.parseColor(mColorTag) == Color.WHITE){
                    mColorFeedbackButton.setTextColor(Color.BLACK);
                }
                else{
                    mColorFeedbackButton.setTextColor(Color.WHITE);
                }





                //=================DRAW OVAL=======================================================
                mPaintView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        //Create a PointF object, 'current', which holds two float coordinates - x (horizontal) and y (vertical).
                        PointF pointFCurrentShape = new PointF(motionEvent.getX(), motionEvent.getY());



                        Paint currentShapePaint = new Paint(mPaintView.mPaint);
                        currentShapePaint.setColor(Color.parseColor(mColorTag));


                        switch (motionEvent.getAction()) {

                            case (MotionEvent.ACTION_DOWN):


                                mCurrentShape = new Shape(pointFCurrentShape, currentShapePaint);
                                mOvals.add(mCurrentShape);

                                break;




                            case (MotionEvent.ACTION_MOVE):

                                if (mCurrentShape != null) {

                                    mCurrentShape.setCurrentPoint(pointFCurrentShape);

                                    mCanvasDrawingPane.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);



                                    //For-each loop to iterate through on Shape object in the mRectangles List of Shape objects at a time.
                                    //Any Shape object being currently worked on is the CURRENT Shape being added to the mRectangles List


                                    for (Shape shape : mOvals) {

                                        Paint shapePaint = shape.getShapePaint();


                                        float left = Math.min(shape.getStartingPoint().x, shape.getCurrentPoint().x);
                                        float right = Math.max(shape.getStartingPoint().x, shape.getCurrentPoint().x);
                                        float top = Math.min(shape.getStartingPoint().y, shape.getCurrentPoint().y);
                                        float bottom = Math.max(shape.getStartingPoint().y, shape.getCurrentPoint().y);


                                        if (Build.VERSION.SDK_INT >= 21) {
                                            mCanvasDrawingPane.drawOval(left, top, right, bottom, shapePaint);
                                        }
                                        else{
                                            Toast unableToDrawOvalToast = Toast.makeText(getApplicationContext(), "Oval mode cannot be activated in this device", Toast.LENGTH_SHORT);
                                            unableToDrawOvalToast.show();
                                        }

                                    }

                                    mPaintView.invalidate();
                                }

                                break;


                            case (MotionEvent.ACTION_UP):
                                mPaintView.mCanvas.drawBitmap(mBitmapDrawingPane, 0, 0, null);
                                mCurrentShape = null;












                                //========= SET UP UNDO FUNCTION ===============================================================================
                                //TODO: MAKE METHOD FROM THIS
                                //If the mBitmapArrayList is null. This occurs if a New Painting is created
                                if (mPaintView.mBitmapArrayList == null) {
                                    //Assign mBitmapArrayList to a new Bitmap ArrayList
                                    mPaintView.mBitmapArrayList = new ArrayList<>();
                                }


                                //Make a copy (clone) of the mBitmap Bitmap
                                Bitmap cloneOfmBitmap = mPaintView.mBitmap.copy(mPaintView.mBitmap.getConfig(), true);

                                //Add the cloan of mBitmap to the Bitmap ArrayList
                                mPaintView.mBitmapArrayList.add(cloneOfmBitmap);


                                //If there are 1 or more Bitmap objects in the Bitmap ArrayList, set the Undo MenuItem to "enabled" state
                                // NOTE: This menu item was initialised to "unabled" drawable
                                if (mPaintView.mBitmapArrayList != null && mPaintView.mBitmapArrayList.size() > 0) {
                                    MainActivity.sMenu.findItem(R.id.undo_painting).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_undo_enabled));
                                }


                                //Log to Logcat - Get size of mBitmapArrayList
                                Log.i("SizeOfmBitmapArrayList", Integer.toString(mPaintView.mBitmapArrayList.size()));













                                break;
                        }
                        return true;
                    }
                });




                shapeAdderDialog.dismiss();


                Toast shapeModeToast = Toast.makeText(getApplicationContext(), "Oval mode", Toast.LENGTH_SHORT);
                shapeModeToast.show();
            }
        });



















        ImageButton circleShape = (ImageButton) shapeAdderDialog.findViewById(R.id.circle_shape);
        circleShape.setBackgroundResource(android.R.drawable.btn_default);
        circleShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: MAKE METHOD FROM THIS
                showAllBrushColors();
                setBackgroundButtonPressed(mShapeAdderImageButton);
                mSizeFeedbackButton.setText("");
                mColorFeedbackButton.setText("Draw Circle");
                mColorFeedbackButton.setTextColor(Color.WHITE);
                mColorFeedbackButton.setBackgroundColor(Color.parseColor(mColorTag));
                mColorFeedbackButton.setTextSize(12);

                if (Color.parseColor(mColorTag) == Color.WHITE){
                    mColorFeedbackButton.setTextColor(Color.BLACK);
                }
                else{
                    mColorFeedbackButton.setTextColor(Color.WHITE);
                }



                //=================DRAW CIRCLE=======================================================
                mPaintView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        //Create a PointF object, 'current', which holds two float coordinates - x (horizontal) and y (vertical).
                        PointF pointFCurrentShape = new PointF(motionEvent.getX(), motionEvent.getY());



                        Paint currentShapePaint = new Paint(mPaintView.mPaint);
                        currentShapePaint.setColor(Color.parseColor(mColorTag));


                        switch (motionEvent.getAction()) {

                            case (MotionEvent.ACTION_DOWN):


                                mCurrentShape = new Shape(pointFCurrentShape, currentShapePaint);
                                mCircles.add(mCurrentShape);

                                break;




                            case (MotionEvent.ACTION_MOVE):

                                if (mCurrentShape != null) {

                                    mCurrentShape.setCurrentPoint(pointFCurrentShape);

                                    mCanvasDrawingPane.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);



                                    //For-each loop to iterate through on Shape object in the mRectangles List of Shape objects at a time.
                                    //Any Shape object being currently worked on is the CURRENT Shape being added to the mRectangles List


                                    for (Shape shape : mCircles) {

                                        Paint shapePaint = shape.getShapePaint();

                                        float radiusX = Math.abs(shape.getCurrentPoint().x - shape.getStartingPoint().x);
                                        float radiusY = Math.abs(shape.getCurrentPoint().y - shape.getStartingPoint().y);
                                        float radius = Math.max(radiusX, radiusY);
                                        mCanvasDrawingPane.drawCircle(shape.getStartingPoint().x, shape.getStartingPoint().y, radius, shapePaint);

                                    }

                                    mPaintView.invalidate();
                                }

                                break;


                            case (MotionEvent.ACTION_UP):
                                mPaintView.mCanvas.drawBitmap(mBitmapDrawingPane, 0, 0, null);
                                mCurrentShape = null;












                                //========= SET UP UNDO FUNCTION ===============================================================================
                                //TODO: MAKE METHOD FROM THIS
                                //If the mBitmapArrayList is null. This occurs if a New Painting is created
                                if (mPaintView.mBitmapArrayList == null) {
                                    //Assign mBitmapArrayList to a new Bitmap ArrayList
                                    mPaintView.mBitmapArrayList = new ArrayList<>();
                                }


                                //Make a copy (clone) of the mBitmap Bitmap
                                Bitmap cloneOfmBitmap = mPaintView.mBitmap.copy(mPaintView.mBitmap.getConfig(), true);

                                //Add the cloan of mBitmap to the Bitmap ArrayList
                                mPaintView.mBitmapArrayList.add(cloneOfmBitmap);


                                //If there are 1 or more Bitmap objects in the Bitmap ArrayList, set the Undo MenuItem to "enabled" state
                                // NOTE: This menu item was initialised to "unabled" drawable
                                if (mPaintView.mBitmapArrayList != null && mPaintView.mBitmapArrayList.size() > 0) {
                                    MainActivity.sMenu.findItem(R.id.undo_painting).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_undo_enabled));
                                }


                                //Log to Logcat - Get size of mBitmapArrayList
                                Log.i("SizeOfmBitmapArrayList", Integer.toString(mPaintView.mBitmapArrayList.size()));
























                                break;
                        }
                        return true;
                    }
                });




                shapeAdderDialog.dismiss();


                Toast shapeModeToast = Toast.makeText(getApplicationContext(), "Circle mode", Toast.LENGTH_SHORT);
                shapeModeToast.show();
            }
        });








        shapeAdderDialog.show();

    }















    String mTextToAdd;
    PointF mCoordinatesToAddedText;



    public void selectTextAdder(){




        //TODO: MAKE METHOD FROM THIS
        showAllBrushColors();
        setBackgroundButtonPressed(mTextAdderImageButton);
        mSizeFeedbackButton.setText("");
        mColorFeedbackButton.setText("Add Text");
        mColorFeedbackButton.setTextColor(Color.WHITE);
        mColorFeedbackButton.setBackgroundColor(Color.parseColor(mColorTag));
        mColorFeedbackButton.setTextSize(14);

        if (Color.parseColor(mColorTag) == Color.WHITE){
            mColorFeedbackButton.setTextColor(Color.BLACK);
        }
        else{
            mColorFeedbackButton.setTextColor(Color.WHITE);
        }


        mPaintView.mPaint.setColor(Color.parseColor(mColorTag));






        final AlertDialog.Builder textAdderDialog = new AlertDialog.Builder(MainActivity.this);

        textAdderDialog.setTitle("Add Text");


        final EditText editText = new EditText(MainActivity.this);
        editText.setHint("Text to Add");

        textAdderDialog.setView(editText);




        textAdderDialog.setPositiveButton("Add Text",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        mTextToAdd = editText.getText().toString();





//                        mPaintView.invalidate();


                        Toast toast = Toast.makeText(MainActivity.this, "Tap anywhere to print: " + "\"" + mTextToAdd + "\"", Toast.LENGTH_LONG);
                        toast.show();



                    }
                }
        );

        textAdderDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                }
        );




        textAdderDialog.show();















        mBitmapDrawingPane = Bitmap.createBitmap(mPaintView.mBitmap);
        mCanvasDrawingPane = new Canvas(mBitmapDrawingPane);






        mPaintView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){

                    case MotionEvent.ACTION_DOWN:



                        mCoordinatesToAddedText = new PointF(motionEvent.getX(), motionEvent.getY());



                        mPaintView.mPaint.setTextSize(55);
                        mPaintView.mPaint.setStrokeWidth(3);
                        mPaintView.mPaint.setColor(mPaintView.mPaint.getColor());
//                        mCanvasDrawingPane.drawText("hello", mCoordinatesToAddedText.x, mCoordinatesToAddedText.y, mPaintView.mPaint);
                        mCanvasDrawingPane.drawText(mTextToAdd, mCoordinatesToAddedText.x, mCoordinatesToAddedText.y, mPaintView.mPaint);





//                        mPaintView.invalidate();
                        mPaintView.mCanvas.drawBitmap(mBitmapDrawingPane, 0 , 0, null);

                        mPaintView.invalidate();


                        Toast toast = Toast.makeText(MainActivity.this, "\""  + mTextToAdd + "\"" + " added", Toast.LENGTH_SHORT);
                        toast.show();
















                        //========= SET UP UNDO FUNCTION ===============================================================================
                        //TODO: MAKE METHOD FROM THIS
                        //If the mBitmapArrayList is null. This occurs if a New Painting is created
                        if (mPaintView.mBitmapArrayList == null){
                            //Assign mBitmapArrayList to a new Bitmap ArrayList
                            mPaintView.mBitmapArrayList = new ArrayList<>();
                        }


                        //If the current color is NOT transparent. This occurs if a New Painting is created
                        if (mPaintView.mPaint.getColor() != Color.TRANSPARENT) {




                            //Make a copy (clone) of the mBitmap Bitmap
                            Bitmap cloneOfmBitmap = mPaintView.mBitmap.copy(mPaintView.mBitmap.getConfig(), true);

                            //Add the cloan of mBitmap to the Bitmap ArrayList
                            mPaintView.mBitmapArrayList.add(cloneOfmBitmap);





                            //If there are 1 or more Bitmap objects in the Bitmap ArrayList, set the Undo MenuItem to "enabled" state
                            // NOTE: This menu item was initialised to "unabled" drawable
                            if (mPaintView.mBitmapArrayList != null && mPaintView.mBitmapArrayList.size() > 0) {
                                MainActivity.sMenu.findItem(R.id.undo_painting).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_undo_enabled));
                            }


                            //Log to Logcat - Get size of mBitmapArrayList
                            Log.i("SizeOfmBitmapArrayList", Integer.toString(mPaintView.mBitmapArrayList.size()));
                        }














                        break;

                }

                return true;
            }
        });



    }













    private void drawTriangle(Canvas canvas, Paint paint, int x, int y, float width) {
        float halfWidth = width / 2;

        Path path = new Path();
        path.moveTo(x, y - halfWidth); // Top
        path.lineTo(x - halfWidth, y + halfWidth); // Bottom left
        path.lineTo(x + halfWidth, y + halfWidth); // Bottom right
        path.lineTo(x, y - halfWidth); // Back to Top
        path.close();

        canvas.drawPath(path, paint);
    }








    private void setBackgroundButtonPressed(View view){

        mNewPaintingImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mBrushImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mEraserImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mSizeChooserImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mSavePaintingImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mColorPickerImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mColorIdentifierImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mColorFillerImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mPictureAdderImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mShapeAdderImageButton.setBackgroundResource(android.R.drawable.btn_default);
        mTextAdderImageButton.setBackgroundResource(android.R.drawable.btn_default);


        view.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
    }




    private void showAllBrushColors(){

        mRedPaintImageButton.setVisibility(View.VISIBLE);
        mOrangePaintImageButton.setVisibility(View.VISIBLE);
        mYellowPaintImageButton.setVisibility(View.VISIBLE);
        mGreenPaintImageButton.setVisibility(View.VISIBLE);
        mBluePaintImageButton.setVisibility(View.VISIBLE);
        mIndigoPaintImageButton.setVisibility(View.VISIBLE);
        mVioletPaintImageButton.setVisibility(View.VISIBLE);
        mGrayPaintImageButton.setVisibility(View.VISIBLE);
        mBlackPaintImageButton.setVisibility(View.VISIBLE);



        //Check if the "custom color" has been assigned any colors. If this check is omitted, the TextView "Custom Color" will show up even when no custom colors have been assigned yet
        if (mCustomColorAssigned == true){
            mCustomColorsTextView.setVisibility(View.VISIBLE);
        }





        mCustomColor1.setVisibility(View.VISIBLE);
        mCustomColor2.setVisibility(View.VISIBLE);
        mCustomColor3.setVisibility(View.VISIBLE);
        mCustomColor4.setVisibility(View.VISIBLE);
        mCustomColor5.setVisibility(View.VISIBLE);
        mCustomColor6.setVisibility(View.VISIBLE);







        //Check if the "color identifier" has been assigned a color. If this check is omitted, the TextView "Identified" will show up even when no colors have been identified yet
        if (mIsColorIdentifierAssigned == true) {
            mIdentifiedColorTextView.setVisibility(View.VISIBLE);
        }

        mIdentifiedColor.setVisibility(View.VISIBLE);


    }













    private void hideAllColors(){
        mRedPaintImageButton.setVisibility(View.INVISIBLE);
        mOrangePaintImageButton.setVisibility(View.INVISIBLE);
        mYellowPaintImageButton.setVisibility(View.INVISIBLE);
        mGreenPaintImageButton.setVisibility(View.INVISIBLE);
        mBluePaintImageButton.setVisibility(View.INVISIBLE);
        mIndigoPaintImageButton.setVisibility(View.INVISIBLE);
        mVioletPaintImageButton.setVisibility(View.INVISIBLE);
        mGrayPaintImageButton.setVisibility(View.INVISIBLE);
        mBlackPaintImageButton.setVisibility(View.INVISIBLE);

        mCustomColorsTextView.setVisibility(View.INVISIBLE);
        mCustomColor1.setVisibility(View.INVISIBLE);
        mCustomColor2.setVisibility(View.INVISIBLE);
        mCustomColor3.setVisibility(View.INVISIBLE);
        mCustomColor4.setVisibility(View.INVISIBLE);
        mCustomColor5.setVisibility(View.INVISIBLE);
        mCustomColor6.setVisibility(View.INVISIBLE);

        mIdentifiedColorTextView.setVisibility(View.INVISIBLE);
        mIdentifiedColor.setVisibility(View.INVISIBLE);

    }











    //Declear static Menu reference variable. Static so that it could be accessed by PaintView
    public static Menu sMenu;


    //Override onCreateOptionsMenu(..) activity callback method
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        sMenu = menu;

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


            case (R.id.undo_painting):
                undoPainting();
                return true;




            case (R.id.share_painting):
                sharePainting(mPaintView);
                return true;



            default:
                return super.onOptionsItemSelected(menuItem);

        }


    }










    private void undoPainting(){





        if (mPaintView.mBitmapArrayList == null){
            return;
        }




        //======================= GENERIC UNDO OPERATION ==================================================================
        //If the Bitmap ArrayList (mBitmapArrayList) has size greater than 1 (i.e. it has 1 or more Bitmap objects stored)
        if (mPaintView.mBitmapArrayList.size() > 1) {


            //Remove the LAST Bitmap from the Bitmap ArrayList (mBitmapArrayList)
            mPaintView.mBitmapArrayList.remove(mPaintView.mBitmapArrayList.size() - 1);




            Bitmap lastBitmapInmBitmapArrayList = mPaintView.mBitmapArrayList.get(mPaintView.mBitmapArrayList.size() - 1);
            Bitmap cloneOfLastBitmapInmBitmapArrayList = lastBitmapInmBitmapArrayList.copy(lastBitmapInmBitmapArrayList.getConfig(), true);


            //Set the Bitmap to the (now) last clone of the mBitmap in the Bitmap ArrayList
            mPaintView.mBitmap = cloneOfLastBitmapInmBitmapArrayList;






            //Link the mCanvas (from mPaintView) to the new Bitmap object that mBitmap (from mPaintView) now refers to
            mPaintView.mCanvas.setBitmap(mPaintView.mBitmap);


            mPaintView.invalidate();



        }
        else{
            sMenu.findItem(R.id.undo_painting).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_undo_unabled));

            mPaintView.mBitmapArrayList = new ArrayList<>();
            mPaintView.startNewPainting();


//            mUndoMenuItem.setEnabled(false);



        }










        //======================= UNDO PICTURE OPERATION ==================================================================


















        //======================= UNDO SHAPE OPERATION ==================================================================

        if (mLines != null) {
            if (mLines.size() > 1) {
                mLines.remove(mLines.size() - 1);
            } else {
                mLines = new ArrayList<>();
            }
        }




        if (mRectangles != null) {
            if (mRectangles.size() > 1) {
                mRectangles.remove(mRectangles.size() - 1);
            } else {
                mRectangles = new ArrayList<>();
            }
        }





        if (mOvals != null) {
            if (mOvals.size() > 1) {
                mOvals.remove(mOvals.size() - 1);
            } else {
                mOvals = new ArrayList<>();
            }
        }






        if (mCircles != null) {
            if (mCircles.size() > 1) {
                mCircles.remove(mCircles.size() - 1);
            } else {
                mCircles = new ArrayList<>();
            }
        }























        //Add the cloan of mBitmap to the Bitmap ArrayList
        Log.i("SizeOfmBitmapArrayList", Integer.toString(mPaintView.mBitmapArrayList.size()));









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





















    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){

        Log.i(TAG, "onActivityResult(..) called");


        if (resultCode != Activity.RESULT_OK){
            return;
        }


        if (requestCode == REQUEST_CODE_PICTURE_GALLERY) {


            //If the touch event exists for mPaintView (in case mColorIdentifier is pressed), then set it to null.
            //If this line is omitted, then IF the touch event is set for mPaintView upon pressing mColorIdentifier or mColorFiller, we wouldn't be able to exit it!
            mPaintView.setOnTouchListener(null);

            //Set the paint color to transparent so that even when mPaintView is touched, nothing is drawn
            mPaintView.mPaint.setColor(Color.TRANSPARENT);




            //Set the bitmap version of the picture to refer to ''null'
            Bitmap pictureBitmap = null;


            //If the intent exists
            if (intent != null) {
                try {
                    pictureBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }








            //Get height of window
//            Display display = getWindowManager().getDefaultDisplay();
//            Point size = new Point();
//            display.getSize(size);
//            float width = size.x;
//            float height = size.y;


            //Get width and height of the custom view (mPaintView)
            float paintViewWidth = mPaintView.getWidth();
            float paintViewHeight = mPaintView.getHeight();

            //Get width and height of the picture bitmap
            float pictureBitmapWidth = pictureBitmap.getWidth();
            float pictureBitmapHeight = pictureBitmap.getHeight();


            float scale=1;



            //If picture is PORTRAIT
            if (pictureBitmapHeight >= pictureBitmapWidth) {

                    scale = paintViewHeight / pictureBitmapHeight;

            }

            //If picture is LANDSCAPE (i.e. pictureBitmapWidth > pictureBitmapHeight)
            else{
                    scale = paintViewWidth / pictureBitmapWidth;
            }









            //Get resized Bitmap - based on the NEW height and width
            pictureBitmap = getResizedBitmap(pictureBitmap, pictureBitmapHeight*scale, pictureBitmapWidth*scale);









//            mPaintView.setBitmap(pictureBitmap);

//            Bitmap finalBitmap = overlay(mPaintView.getBitmap(), pictureBitmap);
//
//
//           mPaintView.mCanvas.drawBitmap(finalBitmap, 0, 0, mPaintView.mBitmapPaint);


            float xTranslationToCenter = (paintViewWidth/2) - (pictureBitmap.getWidth()/2);
            float yTranslationToCenter = (paintViewHeight/2) - (pictureBitmap.getHeight()/2);

            mPaintView.mCanvas.drawBitmap(pictureBitmap, xTranslationToCenter, yTranslationToCenter, null);


//            mPaintView.mCanvas.drawPath(mPaintView.mPath, mPaintView.mPaint);


//            mPaintView.invalidate();
//
//
//
//            //Create canvas object, with the specified MUTABLE Bitmap to draw into (i.e. mBitmap)
//            mPaintView.mCanvas = new Canvas(mPaintView.mBitmap);
//
//            //Set the Canvas color to white. Omitting this line means the canvas color would be set to BLACK (by default)
//            mPaintView.mCanvas.drawColor(Color.WHITE);





            setBackgroundButtonPressed(mPictureAdderImageButton);

















            //========= SET UP UNDO FUNCTION ===============================================================================
            //TODO: MAKE METHOD FROM THIS
            //If the mBitmapArrayList is null. This occurs if a New Painting is created
            if (mPaintView.mBitmapArrayList == null){
                //Assign mBitmapArrayList to a new Bitmap ArrayList
                mPaintView.mBitmapArrayList = new ArrayList<>();
            }


//            //If the current color is NOT transparent. This occurs if a New Painting is created
//            if (mPaintView.mPaint.getColor() != Color.TRANSPARENT) {




                //Make a copy (clone) of the mBitmap Bitmap
                Bitmap cloneOfmBitmap = mPaintView.mBitmap.copy(mPaintView.mBitmap.getConfig(), true);

                //Add the cloan of mBitmap to the Bitmap ArrayList
                mPaintView.mBitmapArrayList.add(cloneOfmBitmap);





                //If there are 1 or more Bitmap objects in the Bitmap ArrayList, set the Undo MenuItem to "enabled" state
                // NOTE: This menu item was initialised to "unabled" drawable
                if (mPaintView.mBitmapArrayList != null && mPaintView.mBitmapArrayList.size() > 0) {
                    MainActivity.sMenu.findItem(R.id.undo_painting).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_undo_enabled));
                }


                //Log to Logcat - Get size of mBitmapArrayList
                Log.i("SizeOfmBitmapArrayList", Integer.toString(mPaintView.mBitmapArrayList.size()));
//            }





        }




    }



    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }





    public Bitmap getResizedBitmap(Bitmap bm, float newHeight, float newWidth) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;


        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);



        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

    }












    @Override
    public void onBackPressed(){

        final AlertDialog.Builder backButtonConfirmationDialog = new AlertDialog.Builder(this);
        backButtonConfirmationDialog.setTitle("Eeek!");
        backButtonConfirmationDialog.setMessage(Html.fromHtml("<br>" + "</br>" + "Pressing Back will discard the Painting." +
                                                                    "<br>" + "</br>" + "<br>" + "</br>" + "Do wish to proceed?"));
        backButtonConfirmationDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        MainActivity.super.onBackPressed();
                    }
                }
        );

        backButtonConfirmationDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        backButtonConfirmationDialog.show();

        Log.i(TAG, "onBackPressed() called");
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
