import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

internal fun Boolean.toRequestBody() = (if (this) "1" else "").toRequestBody(MultipartBody.FORM)

internal fun String.toRequestBody() = this.toRequestBody(MultipartBody.FORM)
