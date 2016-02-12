package com.makingiants.today.api.error_handling

import android.support.annotation.VisibleForTesting
import com.makingiants.today.api.utils.TextUtils

/**
 * Api Error representation.
 * {
 * "message": "UnAuthorized Order",
 * "name": "UnAuthorized",
 * "value": "401",
 * }
 */

open class ApiException : Throwable {
    var value: String? = null
        private set
    val text: String
    val name: String

    /**
     * @param aMessage if null will have R.string.error_basic as message
     * *
     * @param aName if null will have R.string.error as name
     */
    constructor(aMessage: String?, aName: String?, cause: Throwable?) : super(aMessage, cause) {
        this.text = if (TextUtils.isEmpty(aMessage)) DEFAULT_MESSAGE else aMessage!!
        this.name = if (TextUtils.isEmpty(aName)) DEFAULT_NAME else aName!!
    }

    private constructor(cause: Throwable) : super("Error", cause) {
        this.text = DEFAULT_MESSAGE
        this.name = DEFAULT_NAME
    }

    companion object {
        @VisibleForTesting val DEFAULT_NAME = "Error"
        @VisibleForTesting val DEFAULT_MESSAGE = "There was an error, please try again."

        /**
         * Helper function to transform a throwable into ApiException
         */
        fun from(throwable: Throwable): ApiException {
            return if (throwable is ApiException) throwable
            else ApiException(throwable)
        }
    }

    //</editor-fold>
}
