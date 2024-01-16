package com.aditya.appstoryaditya.utils

import com.aditya.appstoryaditya.models.Story

object DataDummy {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..5) {
            val story = Story(
                i.toString(),
                "name + $i",
                "description $i",
                "photoUrl $i",
                "createdAt $i",
                0.0,
                0.0,


                )
            items.add(story)
        }
        return items
    }
}