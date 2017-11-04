package io.github.feelfreelinux.wykopmobilny.api

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.github.feelfreelinux.wykopmobilny.utils.printout
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class WykopRequestBodyConverterFactory(private val moshi : Moshi) : Converter.Factory() {
    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *> {
        val adapter = moshi.adapter<Any>(type)
        return WykopBodyConverter(adapter)
    }

    class ApiException(override val message : String, val code : Int) : IOException()

    private class WykopBodyConverter<T : Any>(val adapter : JsonAdapter<T>) : Converter<ResponseBody, T> {
        @Throws(IOException::class)
        override fun convert(response: ResponseBody): T {
            response.use { value ->

                return adapter.fromJson(value.string())
            }
        }
    }
}