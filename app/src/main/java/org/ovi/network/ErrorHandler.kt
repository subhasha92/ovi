package org.ovi.network

import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorHandler {

    fun handleError(error: Throwable): ErrorDTO {

        return when (error) {
            is HttpException -> if (error.response()?.errorBody() != null)
                ErrorDTO(
                    getErrorMessage(error.response()?.errorBody()!!),
                    ErrorCodes.VALIDATIONS_ERROR
                ) else ErrorDTO(error.message.toString(), ErrorCodes.VALIDATIONS_ERROR)
            is IOException -> ErrorDTO(error.message.toString(), ErrorCodes.INPUT_OUTPUT_EXCEPTION)
            is UnknownError -> ErrorDTO(error.message.toString(), ErrorCodes.UNKNOWN_ERROR)
            is GenericNetworkException -> {
                if (error.message.isNotEmpty()) ErrorDTO(error.message, ErrorCodes.UNKNOWN_ERROR)
                else ErrorDTO(error.localizedMessage, ErrorCodes.VALIDATIONS_ERROR)
            }
            is UnknownHostException -> ErrorDTO(error.message, ErrorCodes.CONNECTION_ERROR)
            is SocketTimeoutException -> ErrorDTO(error.message, ErrorCodes.CONNECTION_ERROR)
            else -> ErrorDTO(error.message.toString(), ErrorCodes.UNKNOWN_ERROR)
        }
    }

    fun getErrorMessage(error: Throwable): String? {

        return when (error) {
            is HttpException -> if (error.response()?.errorBody() != null)
                getErrorMessage(error.response()?.errorBody()!!)
            else error.toString()
            is SocketTimeoutException -> "No Internet Connection"
            is UnknownHostException -> "No Internet Connection"
            is IOException -> error.message.toString()
            is UnknownError -> "No Internet Connection"
            is GenericNetworkException -> {
                if (error.message.isEmpty()) error.localizedMessage else error.message
            }
            else -> error.message.toString()
        }
    }

    fun handleValidationErrors(error: ResponseBody?): ErrorDTO {
        return if (error == null) ErrorDTO(error.toString(), ErrorCodes.UNKNOWN_ERROR)
        else ErrorDTO(getErrorMessage(error), ErrorCodes.UNKNOWN_ERROR)
    }
}

fun getErrorMessage(error : Array<String>?): String {
    if(error.isNullOrEmpty())
        return "Something went wrong! Try again"
    val sb = java.lang.StringBuilder()
    error.forEach {
        sb.append(it)
        sb.append("\n")
    }
    return sb.toString()
}

fun getErrorMessage(responseBody: ResponseBody): String {

    var errMsg = try {
        val jsonObject = JSONObject(responseBody.string())
        var tmp : String?=null
        when {
            (jsonObject.has("errors") && jsonObject.opt("errors") != null) -> {
                runCatching {
                    val jsonArr = jsonObject["errors"] as JSONArray
                    if(jsonArr.length() > 0)
                        tmp = jsonArr.get(0) as String
                    else jsonObject["errors"].toString()
                }.onFailure {
                    tmp = jsonObject["errors"].toString()
                }
                tmp ?: "Unable to process the request"
            }
            jsonObject.has("message") -> {
                jsonObject.getString("message").let {
                    if(it.isNullOrEmpty()) return "Unable to process the request"
                    else it
                }
            }
            else -> "Unable to process the request"
        }

    } catch (e: Exception) {
        null
    }

    if (errMsg == null) {
        val err = getStringFromByte(responseBody.byteStream())

        err?.let {
            if (it.isNotEmpty()) {
                val jsonObject = JSONObject(it)
                if (jsonObject.has("detail")) {
                    errMsg = jsonObject.getString("detail")
                }
            } else
                return "Internal Server Error"
        }
    }

    return errMsg ?: "Please try again after sometime!"
}

private fun getStringFromByte(paramInputStream: InputStream): String? {
    return try {
        val localStringBuilder = StringBuilder()
        val localBufferedReader = BufferedReader(InputStreamReader(paramInputStream))
        try {
            while (true) {
                val str: String = localBufferedReader.readLine() ?: break
                localStringBuilder.append(str)
            }
        } catch (localIOException: IOException) {
            localIOException.printStackTrace()
        }
        localStringBuilder.toString()
    } catch (ex: java.lang.Exception) {
        null
    }
}




