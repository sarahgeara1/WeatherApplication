package com.forecast.weatherapplication.common.utils

import android.app.Activity
import android.app.AlertDialog
import android.os.Handler
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


class GlobalFunctions {

    companion object {
        fun showToast(activity: Activity?, Message: String?) {
            if (activity != null) {
                Toast.makeText(activity, Message, Toast.LENGTH_LONG).show()
            }
        }

        fun formatDate(date: String?, inputFromat: String?, desiredFormat: String?): String? {
            return try {
                var oneWayTripDate: Date? = null
                val input =
                    SimpleDateFormat(inputFromat, Locale.US)
                val output =
                    SimpleDateFormat(desiredFormat, Locale.US)
                oneWayTripDate = input.parse(date)
                output.format(oneWayTripDate)
            } catch (e: Exception) {
                e.printStackTrace()
                date
            }
        }

        fun showDialog(
            activity: Activity,
            message: String?,
            positiveBtn: String?,
            negativeBtn: String?,
            positiveRunnable: Runnable?,
            negativeRunnable: Runnable?
        ): AlertDialog? {
            try {
                val diag = AlertDialog.Builder(activity)
                diag.setMessage(message)
                diag.setCancelable(false)
                diag.setPositiveButton(
                    positiveBtn
                ) { dialog, which ->
                    dialog.dismiss()
                    if (positiveRunnable != null) {
                        val handler = Handler()
                        handler.post(positiveRunnable)
                    }
                }
                diag.setNegativeButton(
                    negativeBtn
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                    if (negativeRunnable != null) {
                        val handler = Handler()
                        handler.post(negativeRunnable)
                    }
                }
                if (!activity.isFinishing) {
                    return diag.show()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}