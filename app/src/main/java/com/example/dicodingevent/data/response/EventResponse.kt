package com.example.dicodingevent.data.response

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("error") val error: Boolean? = false,
    @SerializedName("message") val message: String? = "",
    @SerializedName("listEvents") val event: List<EventData>? = emptyList()
) {
    data class EventData(
        @SerializedName("id") val id: Int? = 0,
        @SerializedName("name") val name: String? = "",
        @SerializedName("summary") val summary: String? = "",
        @SerializedName("description") val description: String? = "",
        @SerializedName("imageLogo") val imageLogo: String? = "",
        @SerializedName("mediaCover") val mediaCover: String? = "",
        @SerializedName("category") val category: String? = "",
        @SerializedName("ownerName") val ownerName: String? = "",
        @SerializedName("cityName") val cityName: String? = "",
        @SerializedName("quota") val quota: String? = "",
        @SerializedName("registrants") val registrants: String? = "",
        @SerializedName("beginTime") val beginTime: String? = "",
        @SerializedName("endTime") val endTime: String? = "",
        @SerializedName("link") val link: String? = ""
    )
}
