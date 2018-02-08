package com.petertieu.android.pocketpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Peter Tieu on 7/02/2018.
 */

//CUSTOM VIEW
//The View for which the paint actions take place
    //Keywords:
        //bitmap: Holds the Pixels. It si the base of the PaintView, for which paint will be superimposed on top.
        //canvas: Holds the draw calls. Writes into the Bitmap
        //paint: The actual paint that will be made on the Bitmap. Holds properties such as colors and styles for the drawing

public class PaintView extends View{

    //============== Declare/define instance variables ==========================

    //Bitmap to hold the pixels
    private Bitmap mBitmap;

    //Path describing the path of the paint
    private Path mPath;

    //Canvas to hold the draw calls
    private Canvas mCanvas;

    //Paint to hold the style and color information of the paint to be made on the Bitmap
    private Paint mPaint;

    //Paint ref. var. to hold the style and color information of the canvas
    private Paint mBitmapPaint;

    //Initialize first color of the paint (black)
    private int mCurrentPaintColor = 0xff000000;

    //Previous color of the pain
    private int mPreviousColor = mCurrentPaintColor;

    //Brush size
    private float mCurrentBrushSize;

    //Previous brush size
    private float mPreviousBrushSize;

    //Enabler/disabler of the "Eraser"
    private boolean mEraserEnabled = false;




    //============== Declare/define methods ==========================

    //Build constructor - to be called by MaintActivity
    public PaintView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        //Begin the drawing
        setUpPaint();
    }




    //Set up the Paint and the Path
    private void setUpPaint(){

        //Create Path object
        mPath = new Path();

        //Create Paint object for the paint
        mPaint = new Paint();
        mPaint.setColor(mCurrentPaintColor);    //Set color of Paint
        mPaint.setAntiAlias(true);              //Set anti-aliasing. Anti-aliasing smoths out the edges of the paint, but does not affect its interior
        mPaint.setStyle(Paint.Style.STROKE);    //Set style to a Stroke. Other options: FILL, FILL_AND_STROKE
        mPaint.setStrokeJoin(Paint.Join.ROUND); //Set the joints/bends/corners. Other options: BEVEL, MITER
        mPaint.setStrokeCap(Paint.Cap.ROUND);   //Set the cap, i.e. the very start of the paint. Other options: BUTT, ROUND

        //Create Paint object for the canvas, enabling dithering when blitting.
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        //Define current brush size
        mCurrentBrushSize = 20;

        //Define the previous brush size - let it equal the current brush size
        mPreviousBrushSize = mCurrentBrushSize;

        //Set stroke width of the paint to current brush size
        mPaint.setStrokeWidth(mCurrentBrushSize);
    }





    //Override the View method (onSizeChanged(..)) - called when the size of the view has changed.
    //For the app, this is called at the very beginning when the app starts
    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight){
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        //Create MUTABLE Bitmap object, with specified width and height - to be the same as that of the width and height of the PaintView's view
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        //Create canvas object, with the specified MUTABLE Bitmap to draw into (i.e. mBitmap)
        mCanvas = new Canvas(mBitmap);
    }




    //Override the View method (onDraw(..)) - called when the view should render its content, i.e. when a path change/paint event is made
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //Draw on the bitmap (mBitmap) using the specified paint (mBitmapPaint).
        // Parameters, 'left', and 'right' are the starting positions of the left and top sides of the bitmap, respectively.
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        //Draw the specified path using the specified paint.
        canvas.drawPath(mPath, mPaint);
    }





//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//
//    }











}
