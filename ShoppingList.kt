package com.example.kauppalistatodo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kauppalistatodo.shoppingStuff.ShoppingAdapter
import com.example.kauppalistatodo.shoppingStuff.ShoppingItem
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ShoppingList : Fragment(), ShoppingAdapter.OnItemClickListener  {

    private var itemId = 1
    private lateinit var ostosLista: MutableList<ShoppingItem>
    private lateinit var recyclerView: RecyclerView

    private lateinit var shoppingAdapter: ShoppingAdapter

    private lateinit var paivays : TextView
    private lateinit var viimeisinPaivays: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Shopping List"
        return inflater.inflate(R.layout.fragment_shoppinglist, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paivays = view.findViewById(R.id.paivays)
        val btnAdd: ImageView = view.findViewById(R.id.btn_itemAdd)

        viimeisinPaivays = File(requireActivity().getExternalFilesDir(null), "paivays.txt")
        naytaViimeisinPaivays()

        recyclerView = view.findViewById(R.id.recycler_view_shopping)
        lueTiedot()

        ostosLista = lueTiedot()

        shoppingAdapter = ShoppingAdapter(ostosLista, this)
        recyclerView.adapter = shoppingAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        btnAdd.setOnClickListener {
            val newItemText = view.findViewById<EditText>(R.id.etUusiItem).text.toString()
            shoppingAdapter.notifyDataSetChanged()
            if (newItemText.isBlank()) {
                Toast.makeText(context, "Teksti on tyhjä!", Toast.LENGTH_SHORT).show()
            } else {
                lisaaListaan(newItemText)
                shoppingAdapter.notifyDataSetChanged()

                paivitaPaivays()
            }
        }

    }

    private fun lueTiedot(): MutableList<ShoppingItem> {
        val file = File(requireActivity().getExternalFilesDir(null), "shopping_items.txt")
        val items = mutableListOf<ShoppingItem>()

        if (file.exists()) {
            BufferedReader(FileReader(file)).use { reader ->
                reader.forEachLine { line ->
                    val osat = line.split("|")
                    if (osat.size == 2) {
                        val id = osat[0].toInt()
                        val item = osat[1]
                        items.add(ShoppingItem(id, item))
                    }
                }
            }
        }
        return items
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun lisaaListaan(itemText: String) {
        val file = File(requireActivity().getExternalFilesDir(null), "shopping_items.txt")

        itemId = haeViimeisinId(file)

        file.appendText("$itemId|• $itemText\n")

        ostosLista.add(ShoppingItem(itemId, "• $itemText"))

        itemId++

        shoppingAdapter.notifyDataSetChanged() // Notify the adapter that the data set has changed
        view?.findViewById<EditText>(R.id.etUusiItem)!!.text.clear()
    }



        private fun haeViimeisinId(file: File): Int {
            if (!file.exists()) return 1 // jos tiedostoa ei ole aloita id=1
            val lines = file.readLines()
            if (lines.isEmpty()) return 1 // jos tiedosto löytyy mutta se on tyhjä id=1
            val lastLine = lines.lastOrNull() ?: return 1 // jos ei ole rivejä  id=1
            val parts = lastLine.split("|")
            if (parts.size < 2) return 1 // jos rivit on väärin id=1
            return parts[0].toIntOrNull()?.plus(1) ?: 1
        }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClicked(noteId: Int) {
        val itemToDelete = ostosLista.firstOrNull { it.id == noteId }

        itemToDelete?.let {
            ostosLista.remove(it)
            recyclerView.adapter?.notifyDataSetChanged()
            poistaTavaraTiedostosta(it)
        }

        paivitaPaivays()
    }
    private fun poistaTavaraTiedostosta(item: ShoppingItem) {
        val file = File(requireActivity().getExternalFilesDir(null), "shopping_items.txt")
        val tempFile = File(requireActivity().getExternalFilesDir(null), "temp_items.txt")
        try {
            BufferedReader(FileReader(file)).use { reader ->
                BufferedWriter(FileWriter(tempFile)).use { writer ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        val currentLine = line!!.split("|")
                        if (currentLine.isNotEmpty() && currentLine[0] != item.id.toString()) {
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

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
    private fun haePaivaysNyt(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 2) // Add 2 hours to adjust for the time difference
        return calendar.time
    }
    private fun naytaViimeisinPaivays() {
        if (viimeisinPaivays.exists()) {
            val lastModifiedDate = viimeisinPaivays.readText()
            paivays.text = "Viimeksi muokattu: $lastModifiedDate"
        }
    }
    private fun paivitaPaivays() {
        val currentDate = haePaivaysNyt()
        viimeisinPaivays.writeText(currentDate.toString("yyyy/MM/dd HH:mm:ss"))
        paivays.text = "Viimeksi muokattu: ${currentDate.toString("yyyy/MM/dd HH:mm:ss")}"
    }

}