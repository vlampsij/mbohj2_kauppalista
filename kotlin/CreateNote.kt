package com.example.kauppalistatodo.notesStuff

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kauppalistatodo.R
import java.io.File

class CreateNote : Fragment() {
    private var noteId = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Note"
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imgBack: ImageView = view.findViewById(R.id.imgBack)
        val imgDone: ImageView = view.findViewById(R.id.imgDone)

        imgBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        imgDone.setOnClickListener {
            val description = view.findViewById<EditText>(R.id.etNoteDesc).text.toString()

            val file = File(requireActivity().getExternalFilesDir(null), "notes.txt")

            noteId = getLatestNoteId(file)// hakee viimisimmän idn notesta

            file.appendText("$noteId|$description\n")

            noteId++ // uus id seuraavalle notelle
            parentFragmentManager.popBackStack()
        }
    }

    private fun getLatestNoteId(file: File): Int {
        if (!file.exists()) return 1 // jos tiedostoa ei ole aloita id=1
        val lines = file.readLines()
        if (lines.isEmpty()) return 1 // jos tiedosto löytyy mutta se on tyhjä id=1
        val lastLine = lines.lastOrNull() ?: return 1 // jos ei ole rivejä  id=1
        val parts = lastLine.split("|")
        if (parts.size < 2) return 1 // jos rivit on väärin id=1
        return parts[0].toIntOrNull()?.plus(1) ?: 1
    }
}
