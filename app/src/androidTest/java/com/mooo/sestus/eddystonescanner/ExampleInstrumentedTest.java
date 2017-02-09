package com.mooo.sestus.eddystonescanner;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public final ActivityTestRule<MainActivity> main
            = new ActivityTestRule<>(MainActivity.class, true);

    @Test
    public void MainActivity_HasButtton_WithTextScan() throws Exception {
        Button button = (Button) main.getActivity().findViewById(R.id.scan_button);
        assertEquals("scan", button.getText());
    }
}
