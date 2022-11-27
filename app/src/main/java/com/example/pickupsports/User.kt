package com.example.pickupsports

class User {
    var firstName: String? = null
    var lastName: String? = null
    var dob: String? = null
    var phoneNumber: String? = null

    constructor(){}

    constructor(dob: String?, firstName: String?, lastName: String?, phoneNumber: String?){
        this.dob = dob
        this.firstName = firstName
        this.lastName = lastName
        this.phoneNumber = phoneNumber
    }

}