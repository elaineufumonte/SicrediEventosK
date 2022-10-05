package com.example.sicredieventos_.sicredieventosk.data

import android.app.Activity
import android.app.AlertDialog
import com.example.sicredieventos_.sicredieventosk.R

class LoadingDialog (val mActivity: Activity){
    private lateinit var isdialog:AlertDialog
    fun starLoading(){
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.loanding, null)
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isdialog = builder.create()
        isdialog.show()
    }
    fun isDismiss(){
        isdialog.dismiss()
    }
}