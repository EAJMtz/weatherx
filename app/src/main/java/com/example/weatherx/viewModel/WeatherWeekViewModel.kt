import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherx.model.Weather.Forecast
import com.example.weatherx.network.WeatherRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class WeatherWeekViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val _weatherForecast = MutableLiveData<Forecast>()
    val weatherForecast: LiveData<Forecast>
        get() = _weatherForecast

    fun getWeatherWeekDetail(coordinates: String) {
        _loaderState.value = true
        viewModelScope.launch {
            try {
                val response = repository.getWeatherDetail(coordinates)
                _loaderState.value = false

                response?.forecast?.forecastDays?.let { days ->
                    Log.d("WeatherDebug", "Datos crudos antes de procesamiento: ${days.map { it.date }}") //Ver datos originales

                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                    val orderedDays = days.sortedBy {
                        try {
                            formatter.parse(it.date)
                        } catch (e: Exception) {
                            Log.e("WeatherDebug", "Error en parseo: ${it.date}")
                            formatter.parse(SimpleDateFormat("yyyy-MM-dd").format(Date()))
                        }
                    }

                    val calendar = Calendar.getInstance()
                    val firstMondayIndex = orderedDays.indexOfFirst {
                        calendar.time = formatter.parse(it.date) ?: Date()
                        calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
                    }

                    val adjustedDays = if (firstMondayIndex != -1) {
                        orderedDays.subList(firstMondayIndex, orderedDays.size)
                    } else {
                        orderedDays
                    }

                    Log.d("WeatherDebug", "Días finales procesados: ${adjustedDays.map { it.date }}")
                    _weatherForecast.value = Forecast(adjustedDays)
                } ?: Log.e("WeatherDebug", "forecastDays está vacío o nulo")
            } catch (e: Exception) {
                Log.e("WeatherDebug", "Error crítico en ViewModel: ${e.message}")
            }
        }
    }
}
