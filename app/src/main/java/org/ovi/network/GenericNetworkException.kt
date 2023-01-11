package org.ovi.network

import android.content.Context
import org.ovi.R

import org.ovi.network.ErrorCodes

open class GenericNetworkException(
    private val context: Context,
    private val code: ErrorCodes,
    private val errorMsg: String? = null
) : Exception() {


    override lateinit var message: String

    init {
        message = getErrorMessage()
    }

    fun errorCode(): ErrorCodes {
        return code
    }

    private fun getErrorMessage(): String {

        return when (code) {
            ErrorCodes.DATA_FORMAT_MISMATCH -> context.getString(R.string.str_json_syntax_mismatch_error)
            ErrorCodes.UN_AUTHORIZED -> context.getString(R.string.str_user_unauthorized_error)
            ErrorCodes.BAD_INPUT -> errorMsg ?: context.getString(R.string.str_bad_input)
            ErrorCodes.API_NOT_FOUND -> errorMsg
                ?: context.getString(R.string.str_api_not_found_error)
            ErrorCodes.INTERNAL_SERVER_ERROR -> context.getString(R.string.str_internal_server_error)
            ErrorCodes.NETWORK_TIMEOUT_ERROR -> context.getString(R.string.str_timeout_error)
            ErrorCodes.NO_NETWORK -> context.getString(R.string.str_internet_connection_error)
            ErrorCodes.RESPONSE_FAILED_ERROR -> context.getString(R.string.str_connect_error)
            ErrorCodes.UNKNOWN_ERROR -> context.getString(R.string.str_unknown_error)
            ErrorCodes.INPUT_OUTPUT_EXCEPTION -> context.getString(R.string.str_io_error)
            ErrorCodes.VALIDATIONS_ERROR -> errorMsg ?: context.getString(R.string.str_bad_input)
            ErrorCodes.CONNECTION_ERROR -> errorMsg
                ?: context.getString(R.string.str_connection_error)
        }
    }
}







