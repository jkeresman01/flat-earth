package com.keresman.flatearth.api.model

import com.google.gson.annotations.SerializedName

data class NasaItemRecord(
    @SerializedName("record") val record : List<NasaItem>
)
