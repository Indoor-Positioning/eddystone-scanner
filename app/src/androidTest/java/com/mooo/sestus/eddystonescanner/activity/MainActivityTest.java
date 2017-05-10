package com.mooo.sestus.eddystonescanner.activity;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.mooo.sestus.eddystonescanner.MainActivity;
import com.mooo.sestus.eddystonescanner.R;
import com.mooo.sestus.eddystonescanner.ScanBeaconsFragment;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public final IntentsTestRule<MainActivity> main
            = new IntentsTestRule<>(MainActivity.class, true);
    private Matcher<View> button = withId(R.id.scan_button);

    @Test
    public void MainActivity_HasScanButton() throws Exception {
        onView(button).check(matches(withText("scan")));
    }

    @Test
    public void ScanButton_OnClick_StartsScanBeaconsActivity() throws Exception {
        onView(button).perform(click());

        intended(hasComponent(ScanBeaconsFragment.class.getName()));
    }

}
