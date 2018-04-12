package com.bignerdranch.android.draganddraw;

import android.graphics.PointF;

public class Box {
    private PointF mOrigin;
    private PointF mCurrent;
    private double mAngle;											//Angle between mCurrent and the secondary pointer
    private double mDiffAngle;

    public Box(PointF origin) {
        mOrigin = mCurrent = origin;								//Starting coord of the box
        mAngle  = mDiffAngle = 0.0;
    }

    public void setCurrent(PointF current) {						//Change the current coord
        mCurrent = current;
    }

    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setAngle(double x, double y) {
        mAngle = Math.atan2(y - mCurrent.y, x - mCurrent.x) * 180/Math.PI;
        mAngle = clipTo0_360(mAngle);
    }

    public double getAngle() {
        return mAngle;
    }
    public double getDiffAngle() {
        return mDiffAngle;
    }

    public void reset() {
        mAngle = 0.0;
    }

    public void resetOrigin(double x, double y) {
        double newAngle = Math.atan2(y - mCurrent.y, x - mCurrent.x) * 180/Math.PI;
        newAngle = clipTo0_360(newAngle);
        mDiffAngle = newAngle - mAngle;
        mDiffAngle = clipTo0_360(mDiffAngle);
//		float[] pts = new float[2];														//Using matrix class
//		pts[0] = mOrigin.x; pts[1] = mOrigin.y;
//		Matrix transform = new Matrix();												//Matrix class to help with transformation
//		transform.setRotate((float) diffAngle, (float) mCurrent.x, (float) mCurrent.y);	//Rotate about the mCurrent
//		transform.mapPoints(pts);
//		mOrigin.x = pts[0]; mOrigin.y = pts[1];
    }

    public double clipTo0_360(double angle) {
        if (angle < 0) angle += 360.0;
        else if (angle >= 360.0) angle -= 360.0;
        return angle;
    }
}
