package app.globe.com.weatherglobe.ui.fragments.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.db.models.DataItem
import app.globe.com.weatherglobe.utils.DataItemUtil
import app.globe.com.weatherglobe.utils.DateTimeUtil
import java.util.*
import kotlin.collections.ArrayList


class DailyForecastAdapter internal constructor(private val tempInCelsius : Boolean,
    private val daySelectedListener: DaySelectedListener
) : RecyclerView.Adapter<DailyForecastAdapter.WeatherViewHolder>() {
    private var data : List<DataItem> = ArrayList()

    init {
        setHasStableIds(true)
    }

    override fun getItemCount(): Int
    {
        return data.size
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_daily_item, parent, false)
        return WeatherViewHolder(
            view,
            daySelectedListener,
            tempInCelsius
        )
    }

    override fun onBindViewHolder(@NonNull holder: WeatherViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemId(position: Int): Long {
        return data[position].time
    }

    fun setData(data : ArrayList<DataItem>)
    {
        this.data = data
        notifyDataSetChanged()
    }

    class WeatherViewHolder(itemView: View, daySelectedListener: DaySelectedListener,private val tempInCelsius : Boolean) :
        RecyclerView.ViewHolder(itemView) {

        var dayText: TextView = itemView.findViewById(R.id.day_txt)
        var dayImage: ImageView = itemView.findViewById(R.id.day_icon)
        var dayPrecipitationText: TextView = itemView.findViewById(R.id.day_precipitation)
        var dayTemperaturesMaxText : TextView = itemView.findViewById(R.id.day_temperature_max)
        var dayTemperaturesMinText: TextView = itemView.findViewById(R.id.day_temperature_min)


        private var dataItem: DataItem? = null

        init {
            itemView.setOnClickListener {
                if (dataItem != null) {
                    daySelectedListener.onDaySelect(dataItem!!)
                }
            }
        }

        fun bind(dataItem: DataItem) {
            this.dataItem = dataItem

            dayText.text = DateTimeUtil.getDayOfWeekToday(dayText.context,dataItem.time*1000,Calendar.SHORT)
            dayImage.setImageResource(DataItemUtil.getDayIcon(dataItem.icon))
            dayPrecipitationText.text = String.format("%d%%",(dataItem.precipProbability*100).toInt())


            val temperatureMax = if(tempInCelsius) DataItemUtil.toCelsius(dataItem.apparentTemperatureMax) else dataItem.apparentTemperatureMax
            val temperatureMin = if(tempInCelsius) DataItemUtil.toCelsius(dataItem.apparentTemperatureMin) else dataItem.apparentTemperatureMin


            val con = dayTemperaturesMaxText.context
            val tempMax = String.format(con.getString(R.string.temp_holder), temperatureMax.toInt())
            val tempMin = String.format(con.getString(R.string.temp_holder), temperatureMin.toInt())

            dayTemperaturesMaxText.text = tempMax
            dayTemperaturesMinText.text = tempMin

        }
    }
}