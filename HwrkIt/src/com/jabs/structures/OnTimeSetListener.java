package com.jabs.structures;

import com.jabs.time.RadialPickerLayout;

public interface OnTimeSetListener {
    /**
     * @param view The view associated with this listener.
     * @param hourOfDay The hour that was set.
     * @param minute The minute that was set.
     */
    void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute);
}
