package ru.digipeople.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Вертикальный TextView
 *
 * @author Kashonkov Nikita
 */
public class VerticalTextView extends AppCompatTextView
{
    final boolean topDown;
    /**
     * Конструктор
     */
    public VerticalTextView( Context context,
                             AttributeSet attrs )
    {
        super( context, attrs );
        final int gravity = getGravity();
        if ( Gravity.isVertical( gravity )
                && ( gravity & Gravity.VERTICAL_GRAVITY_MASK )
                == Gravity.BOTTOM )
        {
            setGravity(
                    ( gravity & Gravity.HORIZONTAL_GRAVITY_MASK )
                            | Gravity.TOP );
            topDown = false;
        }
        else
        {
            topDown = true;
        }
    }
    /**
     * Получение замера
     */
    @Override
    protected void onMeasure( int widthMeasureSpec,
                              int heightMeasureSpec )
    {
        super.onMeasure( heightMeasureSpec,
                widthMeasureSpec );
        setMeasuredDimension( getMeasuredHeight(),
                getMeasuredWidth() );
    }
    /**
     * Оформление элемента списка
     */
    @Override
    protected void onDraw( Canvas canvas )
    {
        TextPaint textPaint = getPaint();
        textPaint.setColor( getCurrentTextColor() );
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if ( topDown )
        {
            canvas.translate( getWidth(), 0 );
            canvas.rotate( 90 );
        }
        else
        {
            canvas.translate( 0, getHeight() );
            canvas.rotate( -90 );
        }

        canvas.translate( getCompoundPaddingLeft(),
                getExtendedPaddingTop() );

        getLayout().draw( canvas );
        canvas.restore();
    }
}