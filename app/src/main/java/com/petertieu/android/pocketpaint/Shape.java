package com.petertieu.android.pocketpaint;

import android.graphics.Paint;
import android.graphics.PointF;


//Shape is called by the method: selectShapeAdder() of MainActivity to add shapes to the Painting (i.e. Line, Rectangle, Oval, Circle).
//Shape keeps track of:
    //1: Paint properties (i.e. color and stroke size)
    //2: Co-ordinates of a single Shape when it is drawn
        //a: Starting point: Initial co-ordinates from where the Shape was created
        //b: Current point: Current co-ordinates of the Shape

public class Shape{

    //========= Declare instance variables =================================

    //Shape's Paint (includes these properties: color and stroke size)
    private Paint mShapePaint;

    //STARTING x and y co-ordinates (of type float)
    private PointF mStartingPoint;

    //CURRENT x and y co-ordinates (of type float)
    private PointF mCurrentPoint;




    //========= Define methods =================================

    //Build constructor
    public Shape(PointF origin, Paint paint){
        mStartingPoint = origin;
        mCurrentPoint = origin;
        mShapePaint = paint;
    }




    //Get the STARTING point
    public PointF getStartingPoint(){
        return mStartingPoint;
    }




    //Get the CURRENT point
    public PointF getCurrentPoint(){
        return mCurrentPoint;
    }




    //Set the CURRENT point
    public void setCurrentPoint(PointF current){
        mCurrentPoint = current;
    }




    //Set the current shape
    public Paint getShapePaint(){
        return mShapePaint;
    }

}