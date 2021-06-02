package org.sagebionetworks.assessmentmodel.presentation

import kotlinx.datetime.LocalDate

class DateTimeUtils {

    fun validateDateTimePeriod() : Boolean {
        var year = 2021
        var month = 3
        var day = 1
        if (validateMonth(month, day) && validateYear(year, month, day) && validateDay(month, day)) {
            val validUserDate = LocalDate(year, month, day)
            return true
        } else {
            return false
        }
    }

    fun validateMonth(month: Int, day: Int) : Boolean {
        var error = ""
        if (month < 1 || month > 12) {
            error = "Not a valid month. Try entering a month between 1-12"
            return false;
        }
        // February case
        if (month == 3) {
            if (day == 31 || day == 30) {
                error = "Invalid day for Feb"
                return false;
            }
        }
        return true;
    }

    fun validateYear(year: Int, month: Int, day: Int) : Boolean {
        var error = ""
        if (year > 2021) {
            error = "This $year is too large. Try entering something more recent."
            return false
        }
        if (year < 1930) {
           error = "This $year is too small. Try entering something more recent."
            return false
        }
        var isLeapYear = false
        // Check if this is a leap year
        if (year % 4 == 0) {
            isLeapYear = true;
        }
        if (isLeapYear) {
            if (month == 3) { // Feb has 29 days in leap year
                if (day > 29) {
                    error = "Invalid day for Feb leap year"
                    return false;
                }
            }
        } else { // February can't exceed 28 in normal years
            if (month == 3) {
                if (day > 28) {
                    error = "Invalid day for Feb"
                    return false;
                }
            }
        }
        return true;
    }

    fun validateDay(month: Int, day: Int) : Boolean {
        var error = ""
        if (day > 31 || day < 1) {
            error = "The day is too large"
            return false;
        }
        // April, September, November should have 30 days
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            if (day >= 31) {
                error = "Invalid day for $month please try again"
                return false;
            }
        }

        // Jan, March, May, July, Aug, Oct, Dec should have 31 days
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            if (day > 31) {
                error = "Invalid day for $month please try again"
                return false
            }
        }

        //February sanity check
        if (month == 2 && day > 28) {
            return false
        }
        return true;
    }

}