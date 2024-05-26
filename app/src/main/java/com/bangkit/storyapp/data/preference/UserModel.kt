package com.bangkit.storyapp.data.preference

class UserModel (
    val token: String,
    val email: String,
    val name: String,
    val isLogin: Boolean = false
)