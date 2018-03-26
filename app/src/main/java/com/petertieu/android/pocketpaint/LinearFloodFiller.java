package com.petertieu.android.pocketpaint;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.LinkedList;
import java.util.Queue;

//Class that takes a Bitmap and FILLS a part of it with a color, using the FLOOD FILL algorithm
public class LinearFloodFiller {

    //=================================Declare/define instance variables ============================================================================
    //-------- BITMAP -------
    protected Bitmap mBitmap = null;                        //Bitmap to write onto
    protected int[] mTolerance = new int[] { 0, 0, 0 };     //Tolerance of the Bitmap


    //-------- HEIGHT AND WIDTH of Bitmap -------
    protected int mHeight = 0;  //Height
    protected int mWidth = 0;   //Width


    //-------- PIXELS -------
    protected int[] mPixels = null;             //Pixels
    protected boolean[] mPixelsChecked;         //Pixels Checked
    protected Queue<FloodFillRange> mRanges;    //Ranges


    //-------- PIXELS -------
    protected int[] mInitialColor = new int[] { 0, 0, 0 };  //Initial/BEFORE Color
    protected int mFillColor = 0;                           //Fill Color






    //================================= Define methods ============================================================================
    //Construct with BufferedImage and flood fill will write directly to provided BufferedImage
    public LinearFloodFiller(Bitmap img, int targetColor, int newColor) {

        //Get the Bitmap to write onto
        useImage(img);

        //Set the target color (i.e. the initial/BEFORE color)
        setTargetColor(targetColor);

        //Set the fil color (i.e. the final/AFTER color)
        setFillColor(newColor);
    }




    //Helper method - Get the Bitmap to write onto
    public void useImage(Bitmap img) {

        mWidth = img.getWidth();
        mHeight = img.getHeight();
        mBitmap = img;

        mPixels = new int[mWidth * mHeight];

        mBitmap.getPixels(mPixels, 0, mWidth, 1, 1, mWidth - 1, mHeight - 1);
    }




    //Helper method - Set the target color (i.e. the initial color)
    public void setTargetColor(int targetColor) {
        mInitialColor[0] = Color.red(targetColor);
        mInitialColor[1] = Color.green(targetColor);
        mInitialColor[2] = Color.blue(targetColor);
    }




    //Helper method - Set the fil color (i.e. the final/AFTER color)
    public void setFillColor(int value){
        mFillColor = value;
    }




    // Fill the specified point on the bitmap with the currently selected fill color. int x, int y: The starting coords for the fill
    public void floodFill(int x, int y) {

        //Set up the Pixels and Ranges to prepareFloodFill for the Flood Fill
        prepareFloodFill();


        if (mInitialColor[0] == 0) {
            //Get starting color.
            int startPixel = mPixels[(mWidth * y) + x];
            mInitialColor[0] = (startPixel >> 16) & 0xff;
            mInitialColor[1] = (startPixel >> 8) & 0xff;
            mInitialColor[2] = startPixel & 0xff;
        }


        //Do first call to floodfill.
        LinearFill(x, y);


        //Call floodfill routine while floodfill mRanges still exist on the queue
        FloodFillRange range;

        while (mRanges.size() > 0) {
            //Get Next Range Off the Queue
            range = mRanges.remove();

            //Check Above and Below Each Pixel in the Floodfill Range
            int downPxIdx = (mWidth * (range.Y + 1)) + range.startX;
            int upPxIdx = (mWidth * (range.Y - 1)) + range.startX;
            int upY = range.Y - 1;// so we can pass the y coord by ref
            int downY = range.Y + 1;

            for (int i = range.startX; i <= range.endX; i++) {
                //Start Fill Upwards if we're not above the top of the bitmap and the pixel above this one is within the color mTolerance
                if (range.Y > 0 && (!mPixelsChecked[upPxIdx])
                        && CheckPixel(upPxIdx))
                    LinearFill(i, upY);

                //Start Fill Downwards if we're not below the bottom of the bitmap and the pixel below this one is within the color mTolerance
                if (range.Y < (mHeight - 1) && (!mPixelsChecked[downPxIdx])
                        && CheckPixel(downPxIdx))
                    LinearFill(i, downY);

                downPxIdx++;
                upPxIdx++;
            }
        }

        mBitmap.setPixels(mPixels, 0, mWidth, 1, 1, mWidth - 1, mHeight - 1);
    }


    //Helper method - Set up the Pixels and Ranges to prepareFloodFill for the Flood Fill
    protected void prepareFloodFill() {
        // Called before starting flood-fill
        mPixelsChecked = new boolean[mPixels.length];
        mRanges = new LinkedList<FloodFillRange>();
    }


    //Helper method - Finds the furthermost left and right boundaries of the fill area on a given y coordinate, starting from a given x coordinate, filling as it goes.
    // Adds the resulting horizontal range to the queue of floodfill mRanges, to be processed in the main loop.
    protected void LinearFill(int xStartingCoordinate, int yStartingCoordinate) {

        //Find Left Edge of Color Area
        int lFillLoc = xStartingCoordinate; //the location to check/fill on the left
        int pxIdx = (mWidth * yStartingCoordinate) + xStartingCoordinate;

        while (true) {
            //Fill with the color
            mPixels[pxIdx] = mFillColor;

            //Indicate that this pixel has already been checked and filled
            mPixelsChecked[pxIdx] = true;

            //De-increment
            lFillLoc--; // de-increment counter
            pxIdx--; // de-increment pixel index

            //Exit loop if we're at edge of bitmap or color area
            if (lFillLoc < 0 || (mPixelsChecked[pxIdx]) || !CheckPixel(pxIdx)) {
                break;
            }
        }


        lFillLoc++;

        //Find Right Edge of Color Area
        int rFillLoc = xStartingCoordinate; // the location to check/fill on the left

        pxIdx = (mWidth * yStartingCoordinate) + xStartingCoordinate;

        while (true) {
            //Fill with the color
            mPixels[pxIdx] = mFillColor;

            //Indicate that this pixel has already been checked and filled
            mPixelsChecked[pxIdx] = true;

            //Increment
            rFillLoc++; //increment counter
            pxIdx++;    //increment pixel index

            //Exit loop if we're at edge of bitmap or color area
            if (rFillLoc >= mWidth || mPixelsChecked[pxIdx] || !CheckPixel(pxIdx)) {
                break;
            }
        }

        rFillLoc--;

        //Add range to queue
        FloodFillRange r = new FloodFillRange(lFillLoc, rFillLoc, yStartingCoordinate);

        mRanges.offer(r);
    }


    //Helper method of helper method - Sees if a pixel is within the color mTolerance range.
    protected boolean CheckPixel(int px) {
        int red = (mPixels[px] >>> 16) & 0xff;
        int green = (mPixels[px] >>> 8) & 0xff;
        int blue = mPixels[px] & 0xff;

        return (red >= (mInitialColor[0] - mTolerance[0])
                && red <= (mInitialColor[0] + mTolerance[0])
                && green >= (mInitialColor[1] - mTolerance[1])
                && green <= (mInitialColor[1] + mTolerance[1])
                && blue >= (mInitialColor[2] - mTolerance[2]) && blue <= (mInitialColor[2] + mTolerance[2]));
    }


    //Utility class of Helper method - Represents a linear range to be filled and branched from
    protected class FloodFillRange {
        public int startX;
        public int endX;
        public int Y;

        public FloodFillRange(int startX, int endX, int y) {
            this.startX = startX;
            this.endX = endX;
            this.Y = y;
        }
    }
}