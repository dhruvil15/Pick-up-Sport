package com.example.pickupsports.ui.loginAndRegister

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserData(
    val phoneNumber: String? = null, val firstName: String? = null, val lastName: String? = null,
    val dob: String? = null
) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
