package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R

@AndroidEntryPoint
class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            googleMap.setOnMapClickListener { latLng ->
                parentFragmentManager.setFragmentResult("location", Bundle().apply {
                    putDouble("lat", latLng.latitude)
                    putDouble("lng", latLng.longitude)
                })
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        return view
    }
}
