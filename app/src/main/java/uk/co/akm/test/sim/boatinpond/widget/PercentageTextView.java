package uk.co.akm.test.sim.boatinpond.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * {@link #TextView} designed to display integer percentage values. A special method to
 * set the display text by supplying the integer value of the percentage, is available.
 *
 * Created by Thanos Mavroidis on 28/03/2018.
 */
public final class PercentageTextView extends AppCompatTextView {
    private static final int DISPLAY_MAX_LEN = 4;

    private final String[] displayValues = new String[101];

    public PercentageTextView(Context context) {
        super(context);
        init();
    }

    public PercentageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PercentageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(Typeface.MONOSPACE);
        initDisplayValues();
    }

    // Hard code the 100 possible display values (i.e. from 0% to 100%) so we can select one such
    // display value for any setting, rather than having to build a brand new one for every current
    // setting.
    private void initDisplayValues() {
        final String[] paddings = {null, "\u0020", "\u0020\u0020"};
        for (int i=0 ; i<displayValues.length ; i++) {
            final String value = (Integer.toString(i) + "%");
            final String padding = paddings[DISPLAY_MAX_LEN - value.length()];
            displayValues[i] = (padding == null ? value : padding + value);
        }
    }

    /**
     * Set the text for the input percentage value. Please note that this method will do nothing if
     * the input value is not between 0 and 100, inclusive.
     *
     * @param percentage must be between 0 and 100, inclusive.
     */
    public void setTextForValue(int percentage) {
        if (percentage >= 0 && percentage < displayValues.length) {
            setText(displayValues[percentage]);
        }
    }
}
