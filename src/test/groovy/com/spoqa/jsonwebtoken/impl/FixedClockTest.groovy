package com.spoqa.jsonwebtoken.impl

import org.junit.Test

class FixedClockTest {

    @Test
    void testFixedClockDefaultConstructor() {

        def clock = new FixedClock()

        def date1 = clock.now()
        Thread.sleep(100)
        def date2 = clock.now()

        assertSame date1, date2
    }
}
