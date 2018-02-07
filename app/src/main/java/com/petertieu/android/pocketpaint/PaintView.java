package com.petertieu.android.pocketpaint;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Peter Tieu on 7/02/2018.
 */

//The View for which the paint actions take place

public class PaintView {

    //============== Declare instance variables ==========================

    //The Path to be drawn on the PaintView
    private Path drawingPath;

    //Paint to hold the style and color information of the paint on the PaintView
    private Paint drawingPaint;

    //Paint to hold the style and color information of the canvas (where paint actions will take place)
    private Paint canvasPaint;

    //Initial color of the paint
    private int paintColor = 0xff000000;

    //Previous color of the paint
    private int previousColor = paintColor;

    //Canvas to hold the "draw" calls.
    // To draw something, you need 4 basic components:
    // 1: Bitmap to hold the pixels
    // 2: Canvas to host the draw calls (writing into the bitmap)
    // 3: Drawing primitive (e.g. Rect, Path, text, Bitmap)
    // 4: Paint (to describe the colors and styles for the drawing).
    private Canvas drawCanvas;

    //Bitmap to hold the pixels of the Canvas
    private Bitmap canvasBitmap;

    //Brush size
    private float brushSize;

    //Previous brush size
    private float previousBrushSize;

    //Enabler/disabler of the "Eraser"
    private boolean eraserEnabled = false;









}
