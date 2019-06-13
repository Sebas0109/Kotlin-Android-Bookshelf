package com.example.bookreader.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.bookreader.R
import com.example.bookreader.utils.ConnectionStrings
import java.io.File
import android.content.Intent


class ReadPdfActivity : AppCompatActivity() {

    private var document : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_pdf)

        var docName : String = intent.getStringExtra("docName")

        //document = GetDoc(docName).execute().get()

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("${ConnectionStrings.BlobsPdf}$docName.pdf"))
        startActivity(browserIntent)

        //pdf.fromFile(document!!).load()
       //pdf.fromUri(Uri.parse("${ConnectionStrings.BlobsPdf}$docName.pdf"))
        //https://imageneslibros.blob.core.windows.net/imgsandbooks/ElPsicoanalista.pdf
    }
}
