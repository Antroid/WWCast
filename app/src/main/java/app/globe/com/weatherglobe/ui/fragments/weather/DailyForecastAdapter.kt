package app.globe.com.weatherglobe.ui.fragments.weather

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import app.globe.com.weatherglobe.R
import app.globe.com.weatherglobe.db.models.DataItem
import app.globe.com.weatherglobe.utils.DataItemUtil
import butterknife.BindView
import butterknife.ButterKnife
import java.util.*
import kotlin.collections.ArrayList


class DailyForecastAdapter internal constructor(
    private val daySelectedListener: DaySelectedListener
) : RecyclerView.Adapter<DailyForecastAdapter.WeatherViewHolder>() {
    private var data = ArrayList<DataItem>()

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
            LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(
            view,
            daySelectedListener
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

    class WeatherViewHolder(itemView: View, daySelectedListener: DaySelectedListener) :
        RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.day_txt)
        lateinit var dayText: TextView

        @BindView(R.id.day_icon)
        lateinit var dayImage: ImageView

        @BindView(R.id.day_precipitation)
        lateinit var dayPrecipitationText: TextView

        @BindView(R.id.day_temperatures)
        lateinit var dayTemperaturesText: TextView

        private var dataItem: DataItem? = null

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener {
                if (dataItem != null) {
                    daySelectedListener.onDaySelect(dataItem!!)
                }
            }
        }

        @SuppressLint("WrongConstant", "InlinedApi")
        fun bind(dataItem: DataItem) {
            this.dataItem = dataItem

            dayText.text = DataItemUtil.getDayOfWeek(dataItem.time,Calendar.SHORT)
            dayImage.setImageResource(DataItemUtil.getDayIcon(dataItem.icon))
            dayPrecipitationText.text = String.format("%d%%",(dataItem.precipProbability*100).toInt())
            val con = dayTemperaturesText.context
            val tempHolder = String.format(con.getString(R.string.temps_holder),
                dataItem.apparentTemperatureMax.toInt(), dataItem.apparentTemperatureMin.toInt())
            dayTemperaturesText.text = HtmlCompat.fromHtml(tempHolder, Html.FROM_HTML_MODE_LEGACY)
        }
    }
}