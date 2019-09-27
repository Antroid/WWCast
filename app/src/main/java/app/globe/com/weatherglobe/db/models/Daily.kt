package app.globe.com.weatherglobe.db.models

import com.squareup.moshi.Json

data class Daily(

	@Json(name="data")
	var data: ArrayList<DataItem>? = null

)