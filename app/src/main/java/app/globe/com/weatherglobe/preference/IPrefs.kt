package app.globe.com.weatherglobe.preference

interface IPrefs {

    fun setDistanceUnit(unit : Int)
    fun getDistanceUnit() : Int

    fun setTempUnit(unit : Int)
    fun getTempUnit() : Int
}