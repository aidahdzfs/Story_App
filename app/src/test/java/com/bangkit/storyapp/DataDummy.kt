package com.bangkit.storyapp

import com.bangkit.storyapp.data.api.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "created at $i",
                "name $i",
                "deskripsi $i",
                i.toDouble(),
                "id $i",
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}