package org.ovi.data.local

import org.ovi.common.OviApp

object DataUtil {

    private val context by lazy {
        OviApp.getAppContext()
    }

    const val NOTIFICATION_ID = 1

    fun getChooseRoles() = arrayListOf(
        SelectionModel("Youth or Young Adult", false),
        SelectionModel("Child Welfare Professional", false),
        SelectionModel("Supportive Adult", false)
    )

    fun getRace() = arrayListOf(
        SelectionModel("American Indian or Alaska Native", false),
        SelectionModel("Asian", false),
        SelectionModel("Black or African American", false),
        SelectionModel("Native Hawaiian or Other Pacific Islander", false),
        SelectionModel("White", false)
    )

    fun getEthnicity() = arrayListOf(
        SelectionModel("American Indian and Alaska Native alone, non-Hispanic", false),
        SelectionModel("Asian alone, non-Hispanic", false),
        SelectionModel("Black or African American alone, non-Hispanic", false),
        SelectionModel("Hispanic", false),
        SelectionModel("Multiracial, non-Hispanic", false),
        SelectionModel("Native Hawaiian and Other Pacific Islander alone, non-Hispanic", false),
        SelectionModel("Some Other Race alone, non-Hispanic", false),
        SelectionModel("White alone, non-Hispanic", false)
    )

    fun getGender() = arrayListOf(
        SelectionModel("Male", false),
        SelectionModel("Female", false),
        SelectionModel("Nonbinary", false),
        SelectionModel("Other", false)
    )

    fun getYesNo() = arrayListOf(
        SelectionModel("Yes", false),
        SelectionModel("No", false)
    )

    fun getEducation() = arrayListOf(
        SelectionModel("Grades 1 through 11", false),
        SelectionModel("12th grade - no diploma", false),
        SelectionModel("Regular high school diploma", false),
        SelectionModel("GED or alternative credential", false),
        SelectionModel("Some college credit, but less than one year of college", false),
        SelectionModel("1 or more years of college credit, no degree", false),
        SelectionModel("Associates degree(For example, AA, AS)", false),
        SelectionModel("Bachelor's degree(For example BA, BS)", false),
        SelectionModel("Master's degree(For example MA, MS, MEng, MEd, MSW, MBA)", false),
        SelectionModel(
            "Professional degree beyond Bachelors degree(For example MD, DDS, DVM, LLB, JD)",
            false
        ),
        SelectionModel("Doctorate degree(For example PhD, EdD)", false)
    )

    fun getAgency() = arrayListOf(
        SelectionModel("lorem ipsum", false),
        SelectionModel("African American", false),
        SelectionModel("American Indian", false),
        SelectionModel("lorem ipsum", false),
        SelectionModel("Asian", false)
    )

    fun getShirtSize() = arrayListOf(
        SelectionModel("XS", false),
        SelectionModel("S", false),
        SelectionModel("M", false),
        SelectionModel("L", false),
        SelectionModel("XL", false),
        SelectionModel("XXL", false),
        SelectionModel("Other", false)
    )

    fun getOptionMenu() = arrayListOf(
        "Home",
        "Profile",
        "My Events",
        "Upcoming Events",
        "Notification",
        "About Us",
        "Change Password",
        "Sign out"
    )


    fun getTypeOfRate()= arrayListOf(
        SelectionModel("Very good",false),
        SelectionModel("Good",false),
        SelectionModel("Acceptable",false),
        SelectionModel("Poor",false),
        SelectionModel("Very poor",false),
        SelectionModel("Other Specify",false),
    )
}