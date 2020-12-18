package com.bit.mysugarbud.ui.diets

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.mysugarbud.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class DietFragment : Fragment() {

    private lateinit var dietsViewModel: DietsViewModel
    val db = Firebase.firestore
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dietsViewModel =
                ViewModelProvider(this).get(DietsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_diet, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.dietsRecyclerView)
        val search = root.findViewById<TextView>(R.id.diet_search)

        val foodChoice = mutableListOf<FoodChoice>()

//        for ( i in 0..10) {
//            foodChoice.add(FoodChoice("name $i", "info", "protein"))
//        }

        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@DietFragment.context)
                adapter = FoodChoices(foodChoice)
            }
        }

        fun getItem(text: String) {
            db.collection("food")
                .get()
                .addOnSuccessListener {
                    foodChoice.clear()
                    for (result in it) {
                        val fName = result.getField<String>("name").toString()
                        if (fName.contains(text)) {
                            val info = result.getField<String>("info").toString()
                            val protein = result.getField<String>("protein").toString()

                            foodChoice.add(FoodChoice(fName, info, protein))
                        }
                        rv()
                    }
                }
        }

        getItem("")

        search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                d("bomoh", "Text is : $s")
                getItem(s.toString())

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


//        val textView: TextView = root.findViewById(R.id.text_gallery)
//        dietsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }
}

data class FoodChoice (
    val name : String,
    val info : String,
    val protein : String
)
