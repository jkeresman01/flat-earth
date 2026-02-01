package com.keresman.flatearth.mapping

import android.database.Cursor
import com.keresman.flatearth.model.Item

fun Cursor.toItems(): List<Item> {
    val items = mutableListOf<Item>()
    use {
        while (moveToNext()) {
            items += Item(
                _id = getLong(getColumnIndexOrThrow(Item::_id.name)),
                title = getString(getColumnIndexOrThrow(Item::title.name)),
                explanation = getString(getColumnIndexOrThrow(Item::explanation.name)),
                picturePath = getString(getColumnIndexOrThrow(Item::picturePath.name)),
                date = getString(getColumnIndexOrThrow(Item::date.name)),
                read = getInt(getColumnIndexOrThrow(Item::read.name)) == 1
            )
        }
    }
    return items
}