package com.fifahkhirnnsa.dicodingstory

import com.fifahkhirnnsa.dicodingstory.data.database.ListItemStory
import java.util.UUID

object DataDummy {
    fun generateDummyStoryResponse(): List<ListItemStory> {
        val items: MutableList<ListItemStory> = arrayListOf()
        for (i in 0..100) {
            val story = ListItemStory(
                photoUrl = "https://unsplash.com/photos/open-book-lot-Oaqk7qqNh_c$i",
                createdAt = "2024-10-${i % 30 + 1}T12:00:00Z",
                name = "Story Author $i",
                description = "Description Story $i",
                lon =  100.0 + i * 0.01,
                id = UUID.randomUUID().toString(),
                lat = -7.0 - i * 0.01
            )
            items.add(story)
        }
        return items
    }
}