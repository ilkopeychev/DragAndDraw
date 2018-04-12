package com.bignerdranch.android.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    public static final String TAG = "BoxDrawingView";
    private Box mCurrentBox;
    private ArrayList<Box> mBoxes = new ArrayList<Box>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Paint the boxes a nice semi-transparent red (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        // Paint the background off-white
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    //Called every time a touch event happens
    @Override
    public boolean onTouchEvent(MotionEvent event) {					//MotionEvent is a class that describes the touch event
        PointF curr = new PointF(event.getX(0), event.getY(0));			//PointF is android graphics

        switch (event.getActionMasked()) {								//getActionMasked() is for multi-touch
            case MotionEvent.ACTION_DOWN:
                mCurrentBox = new Box(curr);								//Reset drawing state
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentBox != null && mCurrentBox.getAngle() == 0) {
                    mCurrentBox.setCurrent(curr);							//Updates as the fingers move around the screen
                    invalidate();											//Forces BoxDrawingView to redraw itself so the users can see
                } else {
                    mCurrentBox.resetOrigin(event.getX(1), event.getY(1));
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurrentBox = null;											//User's fingers leave the screen
                break;
            case MotionEvent.ACTION_POINTER_DOWN:							//Secondary pointer
                mCurrentBox.setAngle(event.getX(1), event.getY(1));
                break;
            case MotionEvent.ACTION_POINTER_UP:								//Secondary pointer
                mCurrentBox.reset();
                break;
            case MotionEvent.ACTION_CANCEL:
                mCurrentBox = null;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Fill the background
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxes) {												//Draw every box
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.rotate((float) box.getDiffAngle(), box.getCurrent().x, box.getCurrent().y);	//Change matrix
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
            canvas.restore();													//Restore original matrix config before save()
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {									//Saving state of custom view
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putSerializable("ArrayListBox", mBoxes);
        return bundle;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mBoxes = (ArrayList<Box>) bundle.getSerializable("ArrayListBox");
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }
}
