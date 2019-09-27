package app.globe.com.weatherglobe.eventbus

import mumayank.com.airlocationlibrary.AirLocation

data class LocationFailureMessageEvent(val locationFailed : AirLocation.LocationFailedEnum)