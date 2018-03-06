package com.petertieu.android.pocketpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Tieu on 7/02/2018.
 */

//CUSTOM VIEW
//The View for which the paint actions take place
    //Keywords:
        //bitmap: Holds the Pixels. It si the base of the PaintView, for which paint will be superimposed on top.
        //path: Describes the outline of a stroke
        //canvas: Holds the draw calls. Writes into the Bitmap
        //paint: The actual paint that will be made on the Bitmap. Holds properties such as colors and styles for the drawing

public class PaintView extends View{


    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.f;





    //============== Declare/define instance variables ==========================

    //Bitmap to hold the pixels
    public Bitmap mBitmap;

    //Path describing the path of the paint
    public Path mPath;

    //Canvas to hold the draw calls
    public Canvas mCanvas;

    //Paint to hold the style and color information of the paint to be made on the Bitmap
    public Paint mPaint;

    //Paint ref. var. to hold the style and color information of the canvas
    public Paint mBitmapPaint;

    //Initialize first color of the paint (black)
    private int mCurrentPaintColor = getResources().getColor(R.color.blue);

    //Previous color of the pain
    private int mPreviousPaintColor = mCurrentPaintColor;

    //Brush size
    private float mCurrentBrushSize;

    //Previous brush size
    private float mPreviousBrushSize;






    //NOTE: Each of the primitives correspond to the valeus in the resource file: dimens.xml
    public float    mExtraExtraSmallBrushSize   = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()), //gives: float 5
                    mExtraSmallBrushSize        = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()), //gives: float 13
                    mSmallBrushSize             = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()), //gives: float 26
                    mSmallMediumBrushSize       = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()), //gives: float 39
                    mMediumBrushSize            = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()), //gives: float 52
                    mMediumLargeBrushSize       = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics()), //gives: float 65
                    mLargeBrushSize             = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()), //gives: float 78
                    mExtraLargeBrushSize        = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics()), //gives: float 91
                    mExtraExtraLargeBrushSize   = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()); //gives: float 105




    //Enabler/disabler of the "Eraser"
    public boolean mEraserEnabled = false;


    //Tag for Logcat
    private final String TAG = "PaintView";



    //============== Declare/define methods ==========================

    //Build constructor - to be called by
    // The element: com.petertieu.android.pocketpaint.PaintView
    // In: activity_main.xml
    public PaintView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        //Log to Logcat
        Log.i(TAG, "PaintView() called");

        //Begin the drawing
        setUpPaint();
    }





    //Set up the Paint and the Path
    private void setUpPaint(){

        //Log to Logcat
        Log.i(TAG, "setUpPaint() called");

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

        //Define current brush size - this would be the initial (default) brush size
        mCurrentBrushSize = 5;

        //Define the previous brush size - let it equal the current brush size
        mPreviousBrushSize = mCurrentBrushSize;

        //Set stroke width of the paint to current brush size
        mPaint.setStrokeWidth(mCurrentBrushSize);
    }





    //Override the View method, onSizeChanged(..) - called when the size of the view has changed.
    //For the app, this is called at the very beginning when the app starts - when PaintView begins
    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight){
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        //Log method call event to Logcat
        Log.i(TAG, "onSizeChanged(..) called");

        //Create MUTABLE Bitmap object, with specified width and height - to be the same as that of the width and height of the PaintView's view
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        //Create canvas object, with the specified MUTABLE Bitmap to draw into (i.e. mBitmap)
        mCanvas = new Canvas(mBitmap);

        //Set the Canvas color to white. Omitting this line means the canvas color would be set to BLACK (by default)
        mCanvas.drawColor(Color.WHITE);
    }




    //Override the View method, onDraw(..) - called when the view should render its content, i.e. when a path change/paint event is made
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);


        //Log method call event to Logcat
        Log.i(TAG, "onDraw(..) called");

        //Draw on the bitmap (mBitmap) using the specified paint (mBitmapPaint).
        // Parameters, 'left', and 'right' are the starting positions of the left and top sides of the bitmap, respectively.
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);


        //Draw the specified path using the specified paint.
        canvas.drawPath(mPath, mPaint);
    }






    public List<Bitmap> mBitmapArrayList = new ArrayList<>();
    public static int sUndoRedoTracker;


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
                    //(Safe call) Set the transfer mode to "CLEAR" (from the PorterDuff.Mode class), so that the SOURCE PIXEL (white pixel from the eraser)
                    // would "eliminate" the destination pixel (pixel that the eraser superimposes on top of)
                    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                }

                //Clear any existing transfer modes (i.e. from eraser)
                //Without this line, IF the eraser had been enabled, then there would be a black line across the page if it is swiped
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





















                //========= SET UP UNDO FUNCTION ===============================================================================

                //If the mBitmapArrayList is null. This occurs if a New Painting is created
                if (mBitmapArrayList == null){
                    //Assign mBitmapArrayList to a new Bitmap ArrayList
                    mBitmapArrayList = new ArrayList<>();
                }


                //If the current color is NOT transparent. This occurs if a New Painting is created
                if (mPaint.getColor() != Color.TRANSPARENT) {




                    //Make a copy (clone) of the mBitmap Bitmap
                    Bitmap cloneOfmBitmap = mBitmap.copy(mBitmap.getConfig(), true);

                    //Add the cloan of mBitmap to the Bitmap ArrayList
                    mBitmapArrayList.add(cloneOfmBitmap);





                    //If there are 1 or more Bitmap objects in the Bitmap ArrayList, set the Undo MenuItem to "enabled" state
                    // NOTE: This menu item was initialised to "unabled" drawable
                    if (mBitmapArrayList != null && mBitmapArrayList.size() > 0) {
                        MainActivity.sMenu.findItem(R.id.undo_painting).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_undo_enabled));
                    }


                    //Log to Logcat - Get size of mBitmapArrayList
                    Log.i("SizeOfmBitmapArrayList", Integer.toString(mBitmapArrayList.size()));
                }















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





    //(Setter) Set the CURRENT color of the paint (i.e. brush color)
    public void setCurrentPaintColor(String colorTag){

        //(Safe call) Invalidate the entire PaintView in case it just did not happen to be invalidated previously
        //Probably a good practice to do this to 'start afresh' since a new brush color has been selected
        invalidate();

        //Parse the 'color' parameter from String to color-int, and store it in mCurrentPaintColor
        mCurrentPaintColor = Color.parseColor(colorTag);

        //Set the paint color to the selected color
        mPaint.setColor(mCurrentPaintColor);

        //Reset the previous paint color to the current paint color
        mPreviousPaintColor = mCurrentPaintColor;
    }


    public int getCurrentPaintColor(){
        return mCurrentPaintColor;
    }





    //(Setter) Set the CURRENT size of the brush
    public void setCurrentSize(float currentBrushSize){

        mCurrentBrushSize = currentBrushSize;

        //Set the width of the paint for stroking to the current set brush size
        mPaint.setStrokeWidth(mCurrentBrushSize);
    }




    public String getCurrentSizeString(){


        //CANNOT do switch-case, as we are dealing with non-constant (i.e. non-primitive) variables

        if (mCurrentBrushSize == mExtraExtraSmallBrushSize){
            return "xx-Small";
        }

        else if (mCurrentBrushSize == mExtraSmallBrushSize){
            return "x-Small";
        }

        else if (mCurrentBrushSize == mSmallBrushSize){
            return "Small";
        }

        else if (mCurrentBrushSize == mSmallMediumBrushSize){
            return "Sml-Med";
        }

        else if (mCurrentBrushSize == mMediumBrushSize){
            return "Medium";
        }

        else if (mCurrentBrushSize == mMediumLargeBrushSize){
            return "Med-Lrg";
        }

        else if (mCurrentBrushSize == mLargeBrushSize){
            return "Large";
        }

        else if (mCurrentBrushSize == mExtraLargeBrushSize){
            return "x-Large";
        }

        else if (mCurrentBrushSize == mExtraExtraLargeBrushSize){
            return "xx-Large";
        }

        else{
            return "";
        }









//        switch (mCurrentBrushSize){
//
//            case EXTRA_EXTRA_SMALL_BRUSH_SIZE:
//                return "xx-Small";
//
//            case EXTRA_SMALL_BRUSH_SIZE:
//                return "x-Small";
//
//            case SMALL_BRUSH_SIZE:
//                return "Small";
//
//            case SMALL_MEDIUM_BRUSH_SIZE:
//                return "Sml-Med";
//
//            case MEDIUM_BRUSH_SIZE:
//                return "Medium";
//
//            case MEDIUM_LARGE_BRUSH_SIZE:
//                return "Med-Lrg";
//
//            case LARGE_BRUSH_SIZE:
//                return "Large";
//
//            case EXTRA_LARGE_BRUSH_SIZE:
//                return "x-Large";
//
//            case EXTRA_EXTRA_LARGE_BRUSH_SIZE:
//                return "xx-Large";
//
//            default:
//                return "";
//        }


    }





    //(Setter) Set the PREVIOUS size of the brush
    public void setPreviousSize(float previousBrushSize){

        mPreviousBrushSize = previousBrushSize;
    }




    //(Getter) Get the PREVIOUS size of the brush
    public float getPreviousSize(){
        return mPreviousBrushSize;
    }





    //(Setter) Enable/Disable the eraser
    public void setEraser(boolean isEraserEnabled){

        //Enable/Disable eraser from the parameter
        mEraserEnabled = isEraserEnabled;

        //If eraser is ENABLED
        if (mEraserEnabled) {
            //Set the color of the paint to the background color
            mPaint.setColor(Color.WHITE);
        }
        //If eraser is DISABLED
        else{
            //Set the color of the paint to the previous color (i.e. away from White)
            mPaint.setColor(mPreviousPaintColor);

            //(Safe call) Clear any existing transfer modes (i.e. from eraser)
            mPaint.setXfermode(null);
        }
    }



    public Bitmap getBitmap(){
        return mBitmap;
    }


    public void setBitmap(Bitmap bitmap){
        mBitmap = bitmap;
    }





    //Start a new paint by 'wiping' out the entire mutable Bitmap (mBitmap) and turning the color white
    public void startNewPainting(){

        //Fill the entire mutable bitmap (mBitmap) of the canvas (mCanvas) with the specified color and porter-duff xfermode
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        //Invalidate the entire PaintView so that the bitmap (mBitmap) is recreated
        invalidate();
    }


}
