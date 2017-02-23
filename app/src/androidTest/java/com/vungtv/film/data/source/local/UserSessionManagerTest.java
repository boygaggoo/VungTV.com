package com.vungtv.film.data.source.local;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by pc on 2/23/2017.
 */
public class UserSessionManagerTest {
    Context context;
    @Before
    public void setUp() throws Exception {
        // Context of the app under test.
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void isVIP() throws Exception {
        System.out.print("is VIP : " + UserSessionManager.isVIP(context));
    }

}