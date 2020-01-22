package com.forecast.weatherapplication.common.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class City : Serializable {

    constructor() {}
    constructor(name: String?) {
        this.name = name
    }

    @SerializedName("name")
    var name: String? = ""
}
