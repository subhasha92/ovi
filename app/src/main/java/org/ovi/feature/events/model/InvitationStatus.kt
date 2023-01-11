package org.ovi.feature.events.model

enum class InvitationStatus(val value:String) {

    ACCEPTED("accepted"),
    PENDING("pending"),
    CANCEL("declined")

}

enum class UserStatus(val value: String){
    REGISTERED("registered")
}