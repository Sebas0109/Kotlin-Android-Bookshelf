package com.example.bookreader.activities

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.bookreader.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.microsoft.azure.storage.StorageException
import kotlinx.android.synthetic.main.activity_share_book_qr.*

class ShareBookQR : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_book_qr)

        var urlSent : String = intent.getStringExtra("urlPdf")

        var multiFormatW : MultiFormatWriter = MultiFormatWriter()
        try {
            var bitMatrix: BitMatrix = multiFormatW.encode(urlSent, BarcodeFormat.QR_CODE, 250, 250)
            var barcodeEncoder = BarcodeEncoder()
            var bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
            var imgBit = bitmap

            imgQr.setImageBitmap(imgBit)


        } catch (e: WriterException) {
            e.printStackTrace()
        } catch (e: StorageException) {
            e.printStackTrace()
        }
    }
}
