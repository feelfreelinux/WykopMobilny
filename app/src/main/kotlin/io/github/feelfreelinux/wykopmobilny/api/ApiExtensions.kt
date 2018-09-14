import okhttp3.MultipartBody
import okhttp3.RequestBody

internal fun Boolean.toRequestBody() = RequestBody.create(MultipartBody.FORM, if (this) "1" else "")!!

internal fun String.toRequestBody() = RequestBody.create(MultipartBody.FORM, this)!!