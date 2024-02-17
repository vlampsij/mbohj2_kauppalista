package com.example.kauppalistatodo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kauppalistatodo.notesStuff.CreateNote
import com.example.kauppalistatodo.notesStuff.Note
import com.example.kauppalistatodo.notesStuff.NotesAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*

class Notes : Fragment(), NotesAdapter.OnItemClickListener {
    private lateinit var muistioLista: MutableList<Note>
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Notes"
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAdd: FloatingActionButton = view.findViewById(R.id.btn_add_note)

        btnAdd.setOnClickListener {
            val fragment = CreateNote()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        muistioLista = lueTiedot()

        recyclerView = view.findViewById(R.id.recycler_view_notes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = NotesAdapter(muistioLista, this)
    }

    private fun lueTiedot(): MutableList<Note> {
        val file = File(requireActivity().getExternalFilesDir(null), "notes.txt")
        val notes = mutableListOf<Note>()

        if (file.exists()) {
            BufferedReader(FileReader(file)).use { reader ->
                reader.forEachLine { line ->
                    val osat = line.split("|")
                    if (osat.size == 2) {
                        val id = osat[0].toInt()
                        val description = osat[1]
                        notes.add(Note(id, description))
                    }
                }
            }
        }
        return notes
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClicked(noteId: Int) {
        val noteToDelete = muistioLista.firstOrNull { it.id == noteId }

        noteToDelete?.let {
            muistioLista.remove(it)
            recyclerView.adapter?.notifyDataSetChanged()
            deleteNoteFromFile(it)
        }
    }

    private fun deleteNoteFromFile(note: Note) {
        val file = File(requireActivity().getExternalFilesDir(null), "notes.txt")
        val tempFile = File(requireActivity().getExternalFilesDir(null), "temp_notes.txt")
        try {
            BufferedReader(FileReader(file)).use { reader ->
                BufferedWriter(FileWriter(tempFile)).use { writer ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val currentLine = line!!.split("|")
                        if (currentLine.isNotEmpty() && currentLine[0] != note.id.toString()) {
                            writer.write(line)
                            writer.newLine()
                        }
                    }
                }
            }
            tempFile.renameTo(file)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }
}
