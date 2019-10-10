package app.globe.com.weatherglobe.ui.fragments.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.db.models.HourlyItem
import app.globe.com.weatherglobe.utils.DataItemUtil
import app.globe.com.weatherglobe.utils.DateTimeUtil

class HourlyForecastAdapter(private var tempInCelsius : Boolean, private var timeFormatIs24 : Boolean) : RecyclerView.Adapter<HourlyForecastAdapter.HourlyViewHolder>() {
    private var data: List<HourlyItem> = ArrayList()

    init {
        setHasStableIds(true)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.hourly_forecast_item, parent, false)
        return HourlyViewHolder(view,tempInCelsius,timeFormatIs24)
    }

    override fun onBindViewHolder(@NonNull holder: HourlyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemId(position: Int): Long {
        return data[position].time
    }

    fun setData(data: List<HourlyItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    class HourlyViewHolder(itemView: View, private val tempInCelsius : Boolean, private val timeFormatIn24 : Boolean) :
        RecyclerView.ViewHolder(itemView) {

        var hourTime: TextView = itemView.findViewById(R.id.hourly_time)
        var hourlyTemperatureIcon : ImageView = itemView.findViewById(R.id.hourly_forecast_icon)
        var hourlyTemperature : TextView = itemView.findViewById(R.id.hourly_temperature)

        fun bind(item: HourlyItem) {

            hourTime.text = DateTimeUtil.getHourOfDay(item.time * 1000, timeFormatIn24)
            hourlyTemperatureIcon.setImageResource(DataItemUtil.getDayIcon(item.icon))

            val temperature = if(tempInCelsius) DataItemUtil.toCelsius(item.temperature) else item.temperature

            hourlyTemperature.text = String.format(hourTime.context.getString(R.string.temp_holder), temperature.toInt())

        }
    }
}