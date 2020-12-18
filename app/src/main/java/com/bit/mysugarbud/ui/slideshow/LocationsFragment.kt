package com.bit.mysugarbud.ui.slideshow

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.mysugarbud.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class LocationsFragment : Fragment() {

    private lateinit var locationsViewModel: LocationsViewModel
    private val db = Firebase.firestore
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        locationsViewModel =
                ViewModelProvider(this).get(LocationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_locations, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.locationsRecyclerView)
        val search = root.findViewById<TextView>(R.id.searchLocation)

        val locations = mutableListOf<Locations>()

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@LocationsFragment.context)
                adapter = CentreLocation(locations)
            }
        }

        fun getItem(text: String) {
            locations.clear()
            db.collection("location")
                .get()
                .addOnSuccessListener {
                    for (result in it) {
                        val name = result.getField<String>("name").toString()
                        val open = result.getField<String>("open").toString()
                        val close = result.getField<String>("close").toString()
                        val location = result.getField<String>("location").toString()

                        if (name.contains(text)) {
                            locations.add(Locations(name, location, open, close))
                        }
                    }
                    rv()
                }
        }

        getItem("")

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getItem(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })




        return root
    }
}

data class Locations (
    val name: String,
    val location: String,
    val open: String,
    val close: String
)
