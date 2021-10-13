package com.example.rssfeedpractice

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

data class Qu(val title: String?,val rank: String?) {
    override fun toString(): String = title!!
}

class XMLParser {
    private val ns: String? = null
    val parser: XmlPullParser = Xml.newPullParser()
    var title: String? = null
    var rank: String? = null


    fun parse(inputStream: InputStream): List<Qu> {
        inputStream.use { inputStream ->
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readSongsRssFeed(parser)
        }
    }

    private fun readSongsRssFeed(parser: XmlPullParser): List<Qu> {

        val songs = mutableListOf<Qu>()

        parser.require(XmlPullParser.START_TAG, ns, "feed")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "entry") {

                parser.require(XmlPullParser.START_TAG, ns, "entry")

                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "title" -> title = readTitle(parser)
                        "re:rank" -> rank = readName(parser)
                        else -> skip(parser)
                    }
                }
                songs.add(Qu(title,rank))
            } else {
                skip(parser)
            }
        }
        return songs
    }



    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    private fun readName(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "re:rank")

        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "re:rank")
        return summary
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}