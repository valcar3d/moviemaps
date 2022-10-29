package com.example.moviemapsapp.views

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moviemapsapp.R
import com.example.moviemapsapp.api.MapsApi
import com.example.moviemapsapp.databinding.ActivityMapsBinding
import com.example.moviemapsapp.di.AppComponent
import com.example.moviemapsapp.models.MapDataModel
import com.example.moviemapsapp.utilities.Utilities
import com.example.moviemapsapp.viewmodels.CoordinatesViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Maps : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = Maps::class.java.simpleName
    private var requestcode = 100
    private lateinit var binding: ActivityMapsBinding
    private lateinit var map: GoogleMap
    private lateinit var currentCoordinatesViewModel: CoordinatesViewModel
    private lateinit var mapDataObject: MapDataModel

    @Inject
    lateinit var mGoogleMapsService: MapsApi

    //region location fields
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    //endregion

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init dagger
        (application as AppComponent).getMapsComponent().inject(this)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mapDataObject = MapDataModel()

        //get the viewmodel class
        currentCoordinatesViewModel = ViewModelProvider(this).get(CoordinatesViewModel::class.java)


        binding.btnEnviarDatos.setOnClickListener {

            currentCoordinatesViewModel.mapData.observe(this) {
                it.let { mapDataObject ->
                    val intent = Intent(this, TravelDataView::class.java)
                    intent.putExtra("timeToArrive", it.timeToArrive)
                    intent.putExtra("originCoordinates", it.originCoordenates)
                    intent.putExtra("totalDistance", it.distance)
                    startActivity(intent)
                }
            }
        }

        //init fused location client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(60))
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(30))
                .setMaxUpdateDelayMillis(TimeUnit.MINUTES.toMillis(2))
                .build()

        //use it if location is needed at a specific interval
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    // Update UI with location data
                    //Log.d(TAG, "Latitude: ${location.latitude}, longitude: ${location.longitude}")
                    val markerLocation = LatLng(location.latitude, location.longitude)

                    mapDataObject.originCoordenates =
                        location.latitude.toString() + "," + location.longitude.toString()
                    currentCoordinatesViewModel.mapData.value = mapDataObject

                    map.addMarker(
                        MarkerOptions()
                            .position(markerLocation)
                            .title("Tu Ubicacion Actual")
                    )
                    map.moveCamera(CameraUpdateFactory.newLatLng(markerLocation))
                }
            }

        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        if (Utilities.isLocationPermissionGranted(this, requestcode)) {
            createMapFragment()
        } else {
            Utilities.isLocationPermissionGranted(this, requestcode)
        }

    }

    //region Google maps
    private fun createMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.isMyLocationEnabled = true

        map.setOnMapClickListener { location ->
            map.clear()
            val marker = MarkerOptions().position(LatLng(location.latitude, location.longitude))
                .title("Destino")
            map.addMarker(marker)
            var currentCoordinates = currentCoordinatesViewModel.mapData.value?.originCoordenates
            var destinationCoordinates =
                location.latitude.toString() + "," + location.longitude.toString()

            Log.d(TAG, "currentCoordinates: $currentCoordinates")

            requestLocationCalculation(
                currentCoordinates!!,
                destinationCoordinates
            )
        }

    }

    //endregion

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                finish()
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Se deben aceptar los permisos para usar el mapa",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun drawRoute(stepsFromRequest: List<String>, colorLine: Int = Color.RED) {
        val options = PolylineOptions()
        options.color(colorLine)
        options.width(5f)
        val latLongB = LatLngBounds.Builder()
        val listOfCoordinates: MutableList<LatLng> = mutableListOf()

        //add all latLong values decoded to the global list
        for (element in stepsFromRequest) {
            listOfCoordinates.addAll(PolyUtil.decode(element.replace("\\\\", "\\")))
        }

        //add all the extracted coordinates to the polyline
        for (coordinates in listOfCoordinates) {
            options.add(coordinates)
            latLongB.include(coordinates)
        }

        val bounds = latLongB.build()
        // add polyline to the map
        map.addPolyline(options)
        // show map with route centered
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

        binding.btnEnviarDatos.isEnabled = true

    }

    private fun requestLocationCalculation(originCoordinates: String, destination: String) {

        var stepsFromRequest: MutableList<String> = mutableListOf()
        val getRouteService =
            mGoogleMapsService.getRouteCalculation(originCoordinates, destination)
        getRouteService.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //Log.d(TAG, "onResponse: ${response.body()}")

                var responseRaw = response.body()!!.string()
                //creating json object
                val rootResponse = JSONObject(responseRaw)
                val jsonRoutesArray = rootResponse.getJSONArray("routes")
                val jsonRoutesObject = jsonRoutesArray.getJSONObject(0)
                val jsonLegsArray = jsonRoutesObject.getJSONArray("legs")
                val jsonLegsObject = jsonLegsArray.getJSONObject(0)
                val jsonStepsArray = jsonLegsObject.getJSONArray("steps")

                val distanceToDestination: JSONObject =
                    jsonLegsArray.getJSONObject(0).get("distance") as JSONObject
                val timeToDestination: JSONObject =
                    jsonLegsArray.getJSONObject(0).get("duration") as JSONObject

                currentCoordinatesViewModel.mapData.value?.timeToArrive =
                    timeToDestination.get("text").toString()

                currentCoordinatesViewModel.mapData.value?.distance =
                    distanceToDestination.get("text").toString()

                for (i in 0..jsonStepsArray.length() - 1) {
                    stepsFromRequest.add(
                        jsonStepsArray.getJSONObject(i)
                            .getJSONObject("polyline").get("points").toString().replace(" ", "")
                    )
                }
                //Log.d(TAG, "stepsFromRequest: ${stepsFromRequest}")
                drawRoute(stepsFromRequest)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }
}