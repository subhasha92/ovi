package org.ovi.data.pref

open class OVIPreferences : Preferences(){

    var isLoggedIn by booleanPref(OVIPreferenceEnum.IS_LOGGED_IN.value, false)
    var token by stringPref(OVIPreferenceEnum.TOKEN.value, "")
    var email by stringPref(OVIPreferenceEnum.USER_EMAIL.value, "")
    var id by stringPref(OVIPreferenceEnum.ID.value, "")
    var fireToken by stringPref(OVIPreferenceEnum.FIRETOKEN.value, null)
    var memberUserID by stringPref(OVIPreferenceEnum.MEMBERUSERID.value, "")
    var showOnBoard by booleanPref(OVIPreferenceEnum.SHOWONBOARD.value,false)
    var zipcode by stringPref(OVIPreferenceEnum.ZIPCODE.value)
    var imageUrl by stringPref(OVIPreferenceEnum.IMAGEURL.value)

}