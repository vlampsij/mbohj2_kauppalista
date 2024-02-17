package com.example.kauppalistatodo.notesStuff

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.kauppalistatodo.R

class NotesAdapter(private val notes: List<Note>,
                   var listener: OnItemClickListener? = null) :
                    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val varit = listOf("#957DAD", "#d895df", "#FEC8D8", "#F8FFEB", "#CBC7DD", "#CBF2B8", "#BAEEE5", "#EEBAB2", "#5f50df", "#B7D3DF")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)

        //vaihetaan randomisti värejä muisti-korteille listasta varit :)
        val colorHex = varit.random()
        val colorInt = Color.parseColor(colorHex)
        view.findViewById<CardView>(R.id.cardViewNotes).setCardBackgroundColor(colorInt)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDesc: TextView = itemView.findViewById(R.id.tvDesc) //muistion teksti
        private val id: TextView = itemView.findViewById(R.id.tvId) //muistion id
        private val btnDelete: ImageView = itemView.findViewById(R.id.imgDelete) //poista nappula

        fun bind(note: Note) {
            textViewDesc.text = note.description
            id.text = note.id.toString()

            // Set click listener for delete button
            btnDelete.setOnClickListener {
                listener?.onClicked(note.id)
            }
        }
    }

    interface OnItemClickListener {
        fun onClicked(noteId: Int)
    }
}

