package app.globe.com.weatherglobe.data

data class Params(private val latitude: Double, private val longitude: Double, private val time: Long = 0) {

    override fun toString(): String {
        return if(time==0.toLong()) "$latitude,$longitude" else "$latitude,$longitude,$time"
    }

}