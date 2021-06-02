package org.sagebionetworks.assessmentmodel.presentation

import junit.framework.TestCase


class DateTimeUtilsTest : TestCase() {

    fun testValidateDateTimePeriod() {
        // test leap years
    }

    fun testValidateMonth() {
        var d = DateTimeUtils()
        for (i in 1..12) {
            assertTrue(d.validateMonth(i, 1))
        }
        var month = 2
        for (i in 1..28) {
            assertTrue(d.validateMonth(month, i))
        }
    }
    fun testValidateYear() {
        var d = DateTimeUtils()
        var day = 1;
        for (i in 1..12) {
            for (j in 2000..2021) {
                assertTrue(d.validateYear(j, i, day))
            }
        }
        // assert false for years over 2021
        for (i in 1..12) {
            for (j in 1..31) {
                for (k in 2022..2030) {
                    assertFalse(d.validateYear(k, j, i))
                }
            }
        }
        //leap years
        var leapYears = listOf(1988, 1992, 1996)
        for (i in leapYears) {
            assertTrue(i % 4 == 0)
        }
        for (i in leapYears) {
            for (j in 30..31) {
                assertFalse(d.validateYear(i, 2, j))
            }
        }
        for (i in leapYears) {
            for (j in 1..29) {
                assertTrue(d.validateYear(i, 2, j))
            }
        }
    }
    fun testValidateDay() {
        // test months with 31 days
        var d = DateTimeUtils()
        var monthsWith31 = listOf(1, 3, 5, 7, 8, 10, 12)
        var monthsWith30 = listOf(4, 6, 9, 11)
        for (i in 1..31) {
            for (j in monthsWith31) {
                assertTrue(d.validateDay(j, i))
            }
        }
        for (i in 1..30) {
            for (j in monthsWith30) {
                assertTrue(d.validateDay(j, i))
            }
        }
        //february
        for (i in 29..31) {
            assertFalse(d.validateDay(2, i))
        }

        for (i in 32..Int.MAX_VALUE) {
            for (j in monthsWith30) {
                assertFalse(d.validateDay(j, i))
            }
        }
        for (i in 32..Int.MAX_VALUE) {
            for (j in monthsWith31) {
                assertFalse(d.validateDay(j, i))
            }
        }

        for (i in Int.MIN_VALUE..0) {
            for (j in monthsWith30) {
                assertFalse((d.validateDay(j, i)))
            }
        }
        for (i in Int.MIN_VALUE..0) {
            for (j in monthsWith31) {
                assertFalse((d.validateDay(j, i)))
            }
        }
    }

}