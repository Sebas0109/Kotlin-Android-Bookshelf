package com.example.bookreader.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bookreader.R
import java.io.ByteArrayOutputStream

class ReadQrActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_qr)


        ReadQr()
    }

    private fun ReadQr()
    {
        try{
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
        //TAMBIEN TENDREMOS QUE PONER UN WHEN PARA QUE VEA EL CODIGO DE ACEPTACION
        when(requestCode)
        {
            0 -> {
                if (resultCode == Activity.RESULT_OK)
                {
                    var contents : String = data!!.getStringExtra("SCAN_RESULT")
                    Toast.makeText(this,contents, Toast.LENGTH_SHORT).show()
                    //Picasso.with(context).load(contents).fit().into(imgView)
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(contents))
                    startActivity(browserIntent)
                }
                else
                {
                    if (requestCode == Activity.RESULT_CANCELED)
                    {
                        Toast.makeText(this,"ERROR EN EL SCANEO", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
