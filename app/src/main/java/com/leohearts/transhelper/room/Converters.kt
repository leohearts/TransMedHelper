package com.leohearts.transhelper.room

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun remindTimeToString(remindTime: ArrayList<ArrayList<Int>> ): String {
        var res = ""
        remindTime.forEach{e->
            res += e[0]
            res += ":"
            res += e[1]
            res += "/"
            res += e[2]
            res += ","
        }
        return res
    }

    @TypeConverter
    fun stringToRemindTime(rawdata: String): ArrayList<ArrayList<Int>> {
        var res = ArrayList<ArrayList<Int>>()
        rawdata.split(",").forEach{ line ->
            try {
                val t = line.split("/")[0]
                val fst = t.split(":")[0].toIntOrNull()
                val second = t.split(":")[1].toIntOrNull()
                val freq = line.split("/")[1].toIntOrNull()
                if (second != null && fst != null && freq != null) {
                    res.add(arrayListOf(fst, second, freq))
                }
            } catch(e: java.lang.IndexOutOfBoundsException){}
        }
        return res
    }
}
