package com.bangkit.storyapp.data.api.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class StoryUploadResponse(
	@field:SerializedName("error")
	val error: Boolean? = null,
	@field:SerializedName("message")
	val message: String? = null
) : Parcelable
