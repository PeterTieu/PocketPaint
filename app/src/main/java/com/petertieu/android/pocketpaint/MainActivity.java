package com.petertieu.android.pocketpaint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;


//Main interface of the app
public class MainActivity extends AppCompatActivity {

    //============= Declare/define instance variables ===========================

    //PaintView for which the paint action takes place
    private PaintView mPaintView;

    //ImageButton for the current color
    private ImageButton mCurrentPaintColor;

    //ImageButtons (i.e. ACTION OPTIONS) in the top pallet
    private ImageButton mNewPaintingImageButton, mBrushImageButton, mEraserImageButton, mSavePaintingImageButton;

    //Brush sizes: EXTRA SMALL, SMALL, SMALL-MEDIUM, MEDIUM, MEDIUM-LARGE, LARGE, EXTRA-LARGE brush sizes
    private float   mExtraSmallBrushSize, mSmallBrushSize, mSmallMediumBrushSize, mMediumBrushSize,
                    mMediumLargeBrushSize, mLargeBrushSize, mExtraLargeBrushSize;





    //Override onCreate(..) activity lifecycle callback method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the view of the layout to activity_main.xml
        setContentView(R.layout.activity_main);

        //Assign the PaintView ref. var. to the associated resource ID in the activity_main.xml file
        mPaintView = (PaintView) findViewById(R.id.paint_view);




        //Assign the layout for PRESET PAINT COLORS (Bottom Pallet)
        LinearLayout presetPaintColors = (LinearLayout) findViewById(R.id.paint_colors);

        mCurrentPaintColor = (ImageButton) presetPaintColors.getChildAt(7);
        mCurrentPaintColor.setImageDrawable(getResources().getDrawable(R.drawable.paint_color_pressed));



        mNewPaintingImageButton = (ImageButton) findViewById(R.id.new_painting_image_button);
        mNewPaintingImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




        mBrushImageButton = (ImageButton) findViewById(R.id.brush_image_button);
        mBrushImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        mEraserImageButton = (ImageButton) findViewById(R.id.eraser_image_button);
        mEraserImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        mSavePaintingImageButton = (ImageButton) findViewById(R.id.save_painting_image_button);
        mSavePaintingImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });





        mExtraSmallBrushSize = 5;
        mSmallBrushSize = 10;
        mSmallMediumBrushSize = 15;
        mMediumBrushSize = 20;
        mMediumLargeBrushSize = 25;
        mLargeBrushSize = 30;
        mExtraSmallBrushSize = 35;




    }
}
