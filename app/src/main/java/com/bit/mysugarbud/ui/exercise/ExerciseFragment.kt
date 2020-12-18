package com.bit.mysugarbud.ui.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.mysugarbud.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class ExerciseFragment : Fragment() {

    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_exercise, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.exerciseRecyclerView)

        val exerciseVideos = mutableListOf<ExerciseVideos>()
//        for (i in 0..0) {
//            exerciseVideos.add(ExerciseVideos("JnfdU2urcUw"))
//        }
        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ExerciseFragment.context)
                adapter = Exercise(exerciseVideos)
            }
        }

        db.collection("video")
            .get()
            .addOnSuccessListener {
                for (result in it) {
                    val link = result.getField<String>("link").toString()
                    exerciseVideos.add(ExerciseVideos(link))
                }
                rv()
            }

        return root
    }


}

data class ExerciseVideos (
    val link: String

    )
