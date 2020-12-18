package com.bit.mysugarbud.ui.home

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bit.mysugarbud.MainActivity
import com.bit.mysugarbud.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.ramijemli.percentagechartview.PercentageChartView

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val db = Firebase.firestore
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val displayGlucose: TextView = root.findViewById(R.id.homeGlucose)
        val displayWeight: TextView = root.findViewById(R.id.homeWeight)
        val displayBloodPressure: TextView = root.findViewById(R.id.homeBlood)
        val progressWeight: PercentageChartView = root.findViewById(R.id.weight_id)
        val progressGlucose: PercentageChartView = root.findViewById(R.id.glucose_id)
        val progressBlood: PercentageChartView = root.findViewById(R.id.blood_id)

        val uid = (activity as MainActivity).uid
        db.collection("log").document(uid)
                .get()
                .addOnSuccessListener {
                    val bloodGlucose = it.getField<String>("blood glucose")
                    val weight = it.getField<String>("weight")
                    val bloodPressure = it.getField<String>("blood pressure")
                    val additionalNote = it.getField<String>("additional notes")

                    if (bloodGlucose.isNullOrEmpty()) {
                        displayGlucose.text = "0"
                        displayWeight.text = "0kg"
                        displayBloodPressure.text = "0Hg"
                        progressWeight.setProgress(0.00.toFloat(), true)
                        progressGlucose.setProgress(0.00.toFloat(), true)
                        progressBlood.setProgress(0.00.toFloat(), true)
                    } else {
                        displayGlucose.text = bloodGlucose.toString()
                        displayWeight.text = "${weight.toString()}kg"
                        displayBloodPressure.text = "${bloodPressure.toString()}Hg"
                        progressWeight.setProgress(weight!!.toFloat(), true)
                        progressGlucose.setProgress(bloodGlucose!!.toFloat()/(200/100), true)
                        progressBlood.setProgress(bloodGlucose!!.toFloat()/(200/100), true)
                    }

                }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button).setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_logs_Fragment)
        }
    }
}