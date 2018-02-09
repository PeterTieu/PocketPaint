package com.petertieu.android.pocketpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
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
    private int mPreviousPaintColor = mCurrentPaintColor;

    //Brush size
    private float mCurrentBrushSize;

    //Previous brush size
    private float mPreviousBrushSize;

    //Enabler/disabler of the "Eraser"
    private boolean mEraserEnabled = false;

    //Tag for Logcat
    private final String TAG = "PaintView";



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





    //Override the View method, onSizeChanged(..) - called when the size of the view has changed.
    //For the app, this is called at the very beginning when the app starts
    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight){
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        //Log method call event to Logcat
        Log.i(TAG, "onSizeChanged(..) caleld");

        //Create MUTABLE Bitmap object, with specified width and height - to be the same as that of the width and height of the PaintView's view
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        //Create canvas object, with the specified MUTABLE Bitmap to draw into (i.e. mBitmap)
        mCanvas = new Canvas(mBitmap);
    }




    //Override the View method, onDraw(..) - called when the view should render its content, i.e. when a path change/paint event is made
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //Log method call event to Logcat
        Log.i(TAG, "onDraw(..) caleld");

        //Draw on the bitmap (mBitmap) using the specified paint (mBitmapPaint).
        // Parameters, 'left', and 'right' are the starting positions of the left and top sides of the bitmap, respectively.
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        //Draw the specified path using the specified paint.
        canvas.drawPath(mPath, mPaint);
    }





    //Override the View method onTouchEvent(..) - 'constantly' called per touch screen motion event
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        //Get the x/y-axis co-ordinates of the CURRENT point of touch
        float touchX = motionEvent.getX(); //x-axis co-ordinate
        float touchY = motionEvent.getY(); //y-axis co-ordinate


        //Detect the type of touch event
        switch (motionEvent.getAction()){

            //If the touch event has STARTED (e.g. finger PRESSED on to touch screen)
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(touchX, touchY); //Set the beginning of the contour to point (touchX, touchY)
                break;

            //If a CHANGE has happened during press gesture (e.g. finger MOVED across touch screen)
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(touchX, touchY); //Add a line from the last point to the specified point (touchX, touchY)
                break;

            //If a press gesture has FINISHED (e.g. finger RELEASED from touch screen)
            case MotionEvent.ACTION_UP:
                //If the eraser-mode is activated
                if (mEraserEnabled){
                    //Set the transfer mode to "CLEAR" (from the PorterDuff.Mode class), so that the SOURCE PIXEL (white pixel from the eraser)
                    // would "eliminate" the destination pixel (pixel that the eraser superimposes on top of)
                    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                }

                //Clear any existing transfer modes (i.e. from eraser)
                //Without this line,
                mPaint.setXfermode(null);


                //Draw the path (mPath) with the paint (mPaint) onto the mutable bitmap (mBitmap)
                mCanvas.drawPath(mPath, mPaint);

                //Restore the path (mPath) to its initial values so that any
                // new path object added to the drawing (i.e. new stroke) would begin with a new paht
                //NOTE: This line is IMPORTANT. If it is omitted, then the path's values are kept - even across new instances of the path (i.e. new strokes).
                // This means that any changes made to the paint would change the entire drawing
                // e.g. if the brush color is changed, then the entire drawing would change to that brush color
                // OR if the eraser is enabled, and used, then the brush color changes, then the erasur's path changes to that particular color
                mPath.reset();

                break;

            default:
                return false;

        }

        //Invalidate the entire PaintView so that the bitmap (mBitmap) is recreated everytime
        // in order to accomodate for this new touch event (e.g. PRESSED, MOVED, or RELEASED).
        // Without this call, the View is retained, thus the bitmap does not get resetted fr the duration of onDraw(..),
        // which means the stroke (color or eraser) does not appear until a new color is changed or the eraser is selected.
        invalidate();

        return true;
    }





    //(Setter) Set the current color of the paint (i.e. brush color)
    public void setCurrentPaintColor(String color){

        //(Safe call) Invalidate the entire PaintView in case it just did not happen to be invalidated previously
        //Probably a good practice to do this to 'start afresh' since a new brush color has been selected
        invalidate();

        //Parse the 'color' parameter from String to color-int, and store it in mCurrentPaintColor
        mCurrentPaintColor = Color.parseColor(color);

        //Set the paint color to the selected color
        mPaint.setColor(mCurrentPaintColor);

        //Reset the previous paint color to the current paint color
        mPreviousPaintColor = mCurrentPaintColor;
    }



}
