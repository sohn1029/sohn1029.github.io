package com.sohn.weatherforecast

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result;
import org.json.JSONObject
import kotlinx.coroutines.*
import org.json.JSONArray

const val API_KEY = ""
const val BASE_URL = "http://api.openweathermap.org/data/2.5/weather"
val city = listOf(
    "Seoul",
    "Daejeon",
    "Taegu",
    "Busan"
)

class ForecastFragment: Fragment(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val url = "$BASE_URL?q=Seoul,kr&APPID=$API_KEY"
        val view = inflater.inflate(R.layout.forecast_listview,null)
        var weatherlist = mutableListOf<String>()
        //weatherlist.add("aaa")

//        val (request, response, result) = url
//            .httpGet()
//            .responseString()

//        GlobalScope.launch{
//
//
            var result = runBlocking {
                val (request, response, result) = url
                    .httpGet()
                    .response()
                result
            }


            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    println(ex)
                }
                is Result.Success -> {
                    val data = result.get()
                    val json = JSONObject(data.toString())
                    //weather array
                    val weatherArray = json.getJSONArray("weather")
                    val weatherObject = weatherArray.getJSONObject(0)
                    //main
                    val main = (json["main"] as JSONObject)

                    val weathermain = weatherObject.getJSONObject("main").toString()
                    val mainmintemp = main.getJSONObject("temp_min").toString()
                    val mainmaxtemp = main.getJSONObject("temp_max").toString()
                    val listtitle = "$weathermain-$mainmintemp/$mainmaxtemp"
                    weatherlist.add(listtitle)
                    println(weatherlist)
                }
            }
                //adapter.notifyDataSetChanged()
//        }
        //weatherlist.add("bbb")
        val adapter = ArrayAdapter(this.context!!,android.R.layout.simple_list_item_1,weatherlist)
        var listview = view!!.findViewById<ListView>(R.id.forecast_list)
        listview.adapter = adapter




        listview.setOnItemClickListener { parent, view, position, id ->
            val element = adapter.getItem(position) // The item that was clicked
            val intent = Intent(this.context, WeatherDetail::class.java)
            startActivity(intent)

        }

        return view
    }


}



