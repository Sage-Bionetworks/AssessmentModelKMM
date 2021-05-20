package org.sagebionetworks.assessmentmodel.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.datetime.LocalDate
import kotlinx.serialization.descriptors.PrimitiveKind
import org.sagebionetworks.assessmentmodel.presentation.databinding.FragmentDateTimeInputBinding
import org.sagebionetworks.assessmentmodel.survey.QuestionState
import org.sagebionetworks.assessmentmodel.survey.SimpleQuestion
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 * Use the [DateTimeInputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DateTimeInputFragment : StepFragment() {
    private var _binding: FragmentDateTimeInputBinding? = null
    val binding get() = _binding!!

    lateinit var questionStep: SimpleQuestion
    lateinit var questionState: QuestionState
    private var month: Int = 0
    private var day: Int = 0
    private var year: Int = 0
    private var isValidDate: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionState = nodeState as QuestionState
        questionStep = questionState.node as SimpleQuestion
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDateTimeInputBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navBar.setForwardOnClickListener {
            assessmentViewModel.goForward()
        }
        binding.questionHeader.questionTitle.text = questionStep.title
        binding.questionHeader.questionSubtitle.text = questionStep.subtitle
        binding.navBar.setBackwardOnClickListener { assessmentViewModel.goBackward() }
        binding.navBar.setSkipOnClickListener { assessmentViewModel.goForward() }
    }


    fun validateDateTimePeriod() {
        // Step 1: use LocalDate to construct date from year, month, day
        if (validateMonth() && validateYear() && validateDay()) {
            val userDate = LocalDate(year, month, day)
        }
    }

    fun validateMonth() : Boolean {
        month = binding.inputMonth.toString().toInt()
        if (month < 1 || month > 12) {
            return false;
        }
        // Only March, April, September, November have 30 days
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            if (day == 31) {
                return false;
            }
        }
        // February case
        if (month == 3) {
            if (day == 31 || day == 30) {
                return false;
            }
            return validateYear()
        }
        return true;
    }

    fun validateYear() : Boolean {
        year = binding.inputYear.toString().toInt()
        var isLeapYear : Boolean = false
        // Check if leap year
        if (year % 4 == 0) {
            if ( year % 100 == 0) {
                if (year % 400 == 0) {
                    isLeapYear = true;
                }
            }
        }
        if (isLeapYear) {
            if (month == 3) { // Feb has 29 days in leap year
                if (day > 29) {
                    return false;
                }
            }
        } else { // February can't exceed 28 in normal years
            if (month == 3) {
                if (day > 28) {
                    return false;
                }
            }
        }
        return true;
    }

    fun validateDay() : Boolean {
        day = binding.inputDay.toString().toInt()
        if (day > 31) {
            return false;
        }
        return true;
    }

/*
    fun validateDate(date: String): Boolean {
        val regex = "^(0[0-9]||1[0-2])/([0-2][0-9]||3[0-1])/([0-9][0-9])?[0-9][0-9]$"
        val matcher = Pattern.compile(regex).matcher(date)
        return if (matcher.matches()) {
            matcher.reset()
            if (matcher.find()) {
                val dateDetails = date.split("/")
                val day: String = dateDetails[1]
                val month: String = dateDetails[0]
                val year: String = dateDetails[2]
                if (validateMonthWithMaxDate(day, month)) {
                    return false
                } else if (isFebruaryMonth(month)) {
                    if (isLeapYear(year)) {
                        leapYearWith29Date(day)
                    } else {
                        notLeapYearFebruary(day)
                    }
                } else {
                    return true
                }
            } else {
                return false
            }
        } else {
            return false
        }
    }

    private fun validateMonthWithMaxDate(day: String, month: String): Boolean = day == "31" && (month == "4" || month == "6" || month == "9" || month == "11" || month == "04" || month == "06" || month == "09")
    private fun isFebruaryMonth(month: String): Boolean = month == "2" || month == "02"
    private fun isLeapYear(year: String): Boolean = year.toInt() % 4 == 0
    private fun leapYearWith29Date(day: String): Boolean = !(day == "30" || day == "31")
    private fun notLeapYearFebruary(day: String): Boolean = !(day == "29" || day == "30" || day == "31") */
}