package com.example.bookreader.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.bookreader.R
import com.example.bookreader.inflate
import com.example.bookreader.listeners.RecyclerBooks
import com.example.bookreader.loadByUrl
import com.example.bookreader.models.Book
import com.example.bookreader.utils.ConnectionStrings
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.CloudBlobClient
import com.microsoft.azure.storage.blob.CloudBlobContainer
import kotlinx.android.synthetic.main.recycler_book.view.*

class BookAdapter (private val books : List<Book>, private val listener : RecyclerBooks) : RecyclerView.Adapter<BookAdapter.ViewHolder>(){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(p0.inflate(R.layout.recycler_book))
    }

    override fun getItemCount() = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(books[position], listener)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(book : Book, listener : RecyclerBooks) = with(itemView) {

            textViewTitle.text = book.Title

            /*val storageAcc: CloudStorageAccount = CloudStorageAccount.parse(ConnectionStrings.connectionAzureBlobsContainer)     //AQUI CRASHEA
            //Create blob client
            val blobClient: CloudBlobClient = storageAcc.createCloudBlobClient()
            //Get reference to a container
            val container: CloudBlobContainer = blobClient.getContainerReference("imagenes")*/

            imageViewBg.loadByUrl("${ConnectionStrings.BlobsPng}${book.ImageHolder}.png") //llamar a la url de la imagen del container blob

            setOnClickListener { listener.onClick(book, adapterPosition) }
        }
    }
}