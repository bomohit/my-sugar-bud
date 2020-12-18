package com.bit.mysugarbud.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.bit.mysugarbud.MainActivity
import com.bit.mysugarbud.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class LogsFragment : Fragment() {

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_logs, container, false)
        val bloodGlucose = root.findViewById<TextView>(R.id.input_bloodGlucose).text
        val weight = root.findViewById<TextView>(R.id.input_weight).text
        val bloodPressure = root.findViewById<TextView>(R.id.input_bloodPressure).text
        val additionalNotes = root.findViewById<TextView>(R.id.input_additionalNotes).text
        val buttonSave = root.findViewById<Button>(R.id.buttonSave)
        val uid = (activity as MainActivity).uid

        buttonSave.setOnClickListener {
            if (bloodGlucose.isNotEmpty() and weight.isNotEmpty() and bloodPressure.isNotEmpty()) {
                // update to db
                val data = hashMapOf(
                    "blood glucose" to bloodGlucose.toString(),
                    "weight" to weight.toString(),
                    "blood pressure" to bloodPressure.toString(),
                    "additional notes" to additionalNotes.toString()
                )
                db.collection("log").document(uid)
                    .set(data)
                    .addOnSuccessListener {
                        Snackbar.make(requireView(), "SAVED", Snackbar.LENGTH_SHORT).show()
                        root.findNavController().popBackStack()
                    }

            } else {
                Toast.makeText(context, "Fill all the form", Toast.LENGTH_SHORT).show()
            }
        }


        return root
    }

}