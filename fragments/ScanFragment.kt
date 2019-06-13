package com.example.bookreader.fragments


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.bookreader.R
import com.example.bookreader.toast
import com.squareup.picasso.Picasso

class ScanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }
    //Implementacion del qr

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {
            var intent = Intent("com.google.zxing.client.android.SCAN")
            intent.putExtra("SCAN_MODE","QR_CODE_MODE")
            startActivityForResult(intent,0)
        }catch (e : Exception)
        {
            val marketUri = Uri.parse("market://details?id=com.google.zxing.client.android")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode)
        {
            0 -> {
                if (resultCode == Activity.RESULT_OK)
                {
                    var contents : String = data!!.getStringExtra("SCAN_RESULT")
                    activity?.toast(contents)
                    //Donde Pasamos a abrir el libro en otro lado por que va a ser un pdf
                }
                else
                {
                    if (requestCode == Activity.RESULT_CANCELED)
                    {
                        activity?.toast("ERROR EN EL SCANEO")
                    }
                }
            }
        }
    }

}
