package org.ovi.feature.survey.model


enum class QuestionType(val type: String) {

    YESORNO("yes_no"),
    YES_OR_NO("yes_or_no"),
    DROPDOWN("dropdown"),
    MULTIPLECHOICE("multiple_choice"),
    PICTURECHOICE("picture_choice"),
    TIME("time"),
    NUMBER("number"),
    PHONENUMBER("phone_number"),
    CHECKBOX("checkbox"),
    EMAIL("email"),
    DATETIME("date_time"),
    STATEMENT("statement"),
    WELCOME("welcome_screen"),
    RATING("rating"),
    THANKYOU("thankyou_screen"),
    SHORTTEXT("short_text"),
    LONGTEXT("long_text"),
    OPINIONSCALE("likert_scale");

}
