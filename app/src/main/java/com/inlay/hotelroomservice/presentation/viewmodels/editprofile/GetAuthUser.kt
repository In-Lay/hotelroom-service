package com.inlay.hotelroomservice.presentation.viewmodels.editprofile

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GetAuthUser {
    operator fun invoke(): FirebaseUser? {
        return Firebase.auth.currentUser
    }
}