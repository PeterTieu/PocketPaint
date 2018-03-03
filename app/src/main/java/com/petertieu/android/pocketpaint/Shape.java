package com.petertieu.android.pocketpaint;

/**
 * Created by Peter Tieu on 2/03/2018.
 */

import android.graphics.Paint;
import android.graphics.PointF;

//Shape is called by the method: selectShapeAdder() of MainActivity - to contain information of each shape template.
//Shape keeps track of two points of a single Shape when it is drawn, as well as the current Paint color.
    //Starting point: Initial co-ordinates from where the Shape was created
    //Current point: Current co-ordinates of the Shape
public class Shape {

    //Declare instance variables

    //INITIAL x and y co-ordinates (of type float)
    private PointF mStartingPoint;

    //CURRENT x and y co-ordinates (of type float)
    private PointF mCurrentPoint;

    //Shape's Paint (includes these properties: color and stroke size)
    private Paint mShapePaint;



    //Build constructor
    public Shape(PointF origin, Paint paint){
        mStartingPoint = origin;
        mCurrentPoint = origin;
        mShapePaint = paint;
    }


    //Get the current point
    public PointF getCurrentPoint(){
        return mCurrentPoint;
    }

    //Set the current point
    public void setCurrentPoint(PointF current){
        mCurrentPoint = current;
    }


    //Get the origin point
    public PointF getStartingPoint(){
        return mStartingPoint;
    }


    //Set the current shape
    public Paint getShapePaint(){
        return mShapePaint;
    }

}