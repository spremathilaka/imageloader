package com.samithprem.apa.ui;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.samithprem.apa.R;
import com.samithprem.apa.di.module.NetModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, false);

    private MockWebServer server;

    private Context context;


    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        NetModule.BASE_API_URL = server.url("/").toString();
        context = InstrumentationRegistry.getInstrumentation().getContext();
    }


    @Test
    public void should_show_feed_list_when_api_response_success() throws Exception {
        String fileName = "feeds_200_ok_response.json";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(context, fileName)));

        Intent intent = new Intent();
        activityActivityTestRule.launchActivity(intent);

        onView(withId(R.id.feed_list)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.empty_view)).check(matches(not(isDisplayed())));

    }


    @Test
    public void should_hide_feed_list_when_api_response_failed() throws Exception {
        String fileName = "feeds_200_ok_no_feed_response.json";

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(context, fileName)));

        Intent intent = new Intent();
        activityActivityTestRule.launchActivity(intent);

        onView(withId(R.id.feed_list)).check(matches(not(isDisplayed())));
        onView(withId(R.id.empty_view)).check(matches((isDisplayed())));
        onView(withText(R.string.no_data_available)).check(matches((isDisplayed())));

    }

    @Test
    public void should_show_toast_msg_when_api_response_failed() throws Exception {

        server.enqueue(new MockResponse()
                .setResponseCode(500));

        Intent intent = new Intent();
        activityActivityTestRule.launchActivity(intent);

        onView(withId(R.id.feed_list)).check(matches(not(isDisplayed())));
        onView(withId(R.id.empty_view)).check(matches((not(isDisplayed()))));
    }


    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }


}