package com.vungtv.film.util;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * Created by pc on 2/23/2017.
 */
public class TimeUtilsTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void formatTime() throws Exception {

    }

    @Test
    public void getCurrentTimeMillis() throws Exception {
        System.out.print("time milis: " + TimeUtils.getCurrentTimeMillis());
    }

    @Test
    public void getCurrentDateTime() throws Exception {

    }

    @Test
    public void formatMs() throws Exception {
        System.out.print(TimeUtils.formatMs(7000000));
    }

    @Test
    public void convertTimeStampToDate() throws Exception {

    }

    @Test
    public void convertTimeStampToDateTime() throws Exception {
        System.out.print(TimeUtils.convertTimeStampToDateTime(1487847002));
    }

}