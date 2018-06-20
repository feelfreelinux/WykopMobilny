package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import io.github.feelfreelinux.wykopmobilny.utils.printout


class SuggestionDatabase(context: Context) {
    private val db: SQLiteDatabase by lazy { Helper(context, DB_SUGGESTION, null, 1).writableDatabase }

    fun insertSuggestion(text: String): Long {
        val values = ContentValues()
        values.put(FIELD_SUGGESTION, text)
        return db.insert(TABLE_SUGGESTION, null, values)
    }

    fun getSuggestions(text: String): Cursor {
        return db.query(TABLE_SUGGESTION, arrayOf(FIELD_ID, FIELD_SUGGESTION),
                "$FIELD_SUGGESTION LIKE '$text%'", null, null, null, null, "6")
    }

    private inner class Helper(context: Context, name: String, factory: CursorFactory?,
                               version: Int) : SQLiteOpenHelper(context, name, factory, version) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE " + TABLE_SUGGESTION + " (" +
                    FIELD_ID + " integer primary key autoincrement, " + FIELD_SUGGESTION + " text unique);")
            Log.d("SUGGESTION", "DB CREATED")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        }
    }

    companion object {
        val DB_SUGGESTION = "SUGGESTION_DB"
        val TABLE_SUGGESTION = "SUGGESTION_TB"
        val FIELD_ID = "_id"
        val FIELD_SUGGESTION = "suggestion"
    }

}