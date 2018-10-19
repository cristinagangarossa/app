package com.neperiagroup.happysalus.Renderers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.Range;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class VerticalBarChartRendererStar extends BarChartRenderer {


    private final Bitmap barImage;

    public VerticalBarChartRendererStar(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, Bitmap barImage) {
        super(chart, animator, viewPortHandler);
        this.barImage = barImage;
    }

    @Override
    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()));

        final boolean drawBorder = dataSet.getBarBorderWidth() > 0.f;

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        // initialize the buffer
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(mChart.getBarData().getBarWidth());

        buffer.feed(dataSet);

        trans.pointValuesToPixel(buffer.buffer);

        final boolean isSingleColor = dataSet.getColors().size() == 1;

        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }

        for (int j = 0; j < buffer.size(); j += 4) {

            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;

            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;

            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint.setColor(dataSet.getColor(j / 4));
            }

            //c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
            //      buffer.buffer[j + 3], mRenderPaint);
            c.drawRoundRect(new RectF(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3]),25f , 25f , mRenderPaint);

            if (drawBorder) {
                c.drawRoundRect(new RectF(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3]),25f , 25f , mBarBorderPaint);
            }


        }
        //drawBarImages(c, dataSet, index);
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        BarData barData = mChart.getBarData();
        for (Highlight high : indices) {

            IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            BarEntry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            Transformer trans = mChart.getTransformer(set.getAxisDependency());

            mHighlightPaint.setColor(set.getHighLightColor());
            mHighlightPaint.setAlpha(set.getHighLightAlpha());

            boolean isStack = (high.getStackIndex() >= 0  && e.isStacked()) ? true : false;

            final float y1;
            final float y2;

            if (isStack) {

                if(mChart.isHighlightFullBarEnabled()) {

                    y1 = e.getPositiveSum();
                    y2 = -e.getNegativeSum();

                } else {

                    Range range = e.getRanges()[high.getStackIndex()];

                    y1 = range.from;
                    y2 = range.to;
                }

            } else {
                y1 = e.getY();
                y2 = 0.f;
            }

            prepareBarHighlight(e.getX(), y1, y2, barData.getBarWidth() / 2f, trans);

            setHighlightDrawPos(high, mBarRect);

            c.drawRoundRect(mBarRect, 25, 25, mHighlightPaint);

            BarBuffer buffer = mBarBuffers[(int) set.getXMin()];
            final Bitmap scaledBarImage = scaleBarImage(buffer);
            int starWidth = scaledBarImage.getWidth();
            int starOffset = starWidth / 2;
            int xStart = ((int)e.getX())*4;
            float left = buffer.buffer[xStart];
            float right = buffer.buffer[(xStart + 2)];
            float top = buffer.buffer[(xStart + 1)];
            float x = (left + right) / 2f;
            float val = y1;
            if (val > 7000) {
                drawImage(c, scaledBarImage, x - starOffset, top);
            }
        }
    }

    @Override
    public void drawData(Canvas c) {
        super.drawData(c);
    }

    protected void drawBarImages(Canvas c, IBarDataSet dataSet, int index) {
        BarBuffer buffer = mBarBuffers[index];

        float left; //avoid allocation inside loop
        float right;
        float top;
        float bottom;

        final Bitmap scaledBarImage = scaleBarImage(buffer);

        int starWidth = scaledBarImage.getWidth();
        int starOffset = starWidth / 2;

        for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {
            left = buffer.buffer[j];
            right = buffer.buffer[j + 2];
            top = buffer.buffer[j + 1];
            bottom = buffer.buffer[j + 3];

            float x = (left + right) / 2f;

            if (!mViewPortHandler.isInBoundsRight(x))
                break;

            if (!mViewPortHandler.isInBoundsY(top)
                    || !mViewPortHandler.isInBoundsLeft(x))
                continue;

            BarEntry entry = dataSet.getEntryForIndex(j / 4);
            float val = entry.getY();

            if (val > 7000) {
                drawImage(c, scaledBarImage, x - starOffset, top);
            }
        }
    }

    private Bitmap scaleBarImage(BarBuffer buffer) {
        float firstLeft = buffer.buffer[0];
        float firstRight = buffer.buffer[2];
        int firstWidth = (int) Math.ceil(firstRight - firstLeft);
        return Bitmap.createScaledBitmap(barImage, firstWidth, firstWidth, false);
    }

    protected void drawImage(Canvas c, Bitmap image, float x, float y) {
        if (image != null) {
            c.drawBitmap(image, x, y, null);
        }
    }
}
