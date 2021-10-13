package com.example.rssfeedpractice


import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var rv: RecyclerView
    var topSongs = mutableListOf<Qu>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv = findViewById(R.id.rv)
        FetchTopSongs().execute()
    }

    private inner class FetchTopSongs : AsyncTask<Void, Void, MutableList<Qu>>() {
        val parser = XMLParser()
        override fun doInBackground(vararg params: Void?): MutableList<Qu> {
            val url = URL("https://stackoverflow.com/feeds")
            val urlConnection = url.openConnection() as HttpURLConnection
            topSongs =
                urlConnection.getInputStream()?.let {
                    parser.parse(it)
                }
                        as MutableList<Qu>
            return topSongs
        }

        override fun onPostExecute(result: MutableList<Qu>?) {
            super.onPostExecute(result)

            rv.adapter?.notifyDataSetChanged()
            rv.adapter = MyAdapter(topSongs)
            rv.layoutManager = LinearLayoutManager(this@MainActivity)
        }

    }

}