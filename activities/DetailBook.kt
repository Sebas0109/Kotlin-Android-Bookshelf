package com.example.bookreader.activities

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.example.bookreader.R
import com.example.bookreader.adapters.CommentAdapter
import com.example.bookreader.goToActivity
import com.example.bookreader.loadByUrl
import com.example.bookreader.models.Book
import com.example.bookreader.models.Comment
import com.example.bookreader.models.User
import com.example.bookreader.toast
import com.example.bookreader.utils.ConnectionStrings
import com.example.bookreader.utils.UserData
import com.example.mylibrary2.ToolBarActivity
import kotlinx.android.synthetic.main.activity_detail_book.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class DetailBook : AppCompatActivity() {

    var elementos : ArrayList<Comment> = ArrayList<Comment>()
    private var listview : ListView? = null

    private var book : Book? = null
    private var loggedUser : User? = null
    private var docName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_book)

        listview = bookCommentListview
        loggedUser = UserData.User
        book = intent.getSerializableExtra("book") as Book
        if (book != null)
        {
            bookImageHolder.loadByUrl("${ConnectionStrings.BlobsPng}${book!!.ImageHolder}.png")
            bookImagePortrait.loadByUrl("${ConnectionStrings.BlobsPng}${book!!.BookPortrait}.png")
            bookTitle.text = book!!.Title
            bookAutor.text = book!!.Autor
            bookDescription.text = book!!.Description
        }

        //CARGA DE LOS COMENTARIOS
        elementos = GetComments(elementos,book!!.Id).execute().get()
        val adapter = CommentAdapter(this, elementos)
        listview!!.adapter = adapter

        bookRead.setOnClickListener {
            docName = book!!.Content
            goToActivity<ReadPdfActivity> {
                putExtra("docName", docName)
            }
        }

        bookFavorite.setOnClickListener {
            if(SaveToFavs(loggedUser!!.Id, book!!.Id).execute().get())
            {
                toast("Book added to Favs")
            }else
            {
                toast("Error adding to favorites")
            }
        }

        btnShareScan.setOnClickListener {
            var shareURl : String = "${ConnectionStrings.BlobsPdf}${book!!.Content}.pdf"
            goToActivity<ShareBookQR> {
                putExtra("urlPdf", shareURl)
            }
        }

        bookSendComment.setOnClickListener {
            var comment : String = bookInputText.text.toString()

            //LLAMADA PARA SUBIR COMENTARIO
            postComment(loggedUser!!.Id, book!!.Id, comment).execute().get()
            elementos = emptyList<Comment>() as ArrayList<Comment>
            elementos = GetComments(elementos,book!!.Id).execute().get()
            val adapter = CommentAdapter(this, elementos)
            listview!!.adapter = adapter

        }

    }

    private class GetComments(var elem : ArrayList<Comment>, val id_Book : Int): AsyncTask<Void,Void,ArrayList<Comment>>()
    {
        private var Comments: MutableList<Comment> = mutableListOf<Comment>()
        private var comment : Comment? = null

        override fun doInBackground(vararg params: Void?): ArrayList<Comment> {
            var client : OkHttpClient = OkHttpClient()

            var url : String = HttpUrl.parse("${ConnectionStrings.ApiUrl}/GetComments")!!.newBuilder().addEncodedQueryParameter("id",id_Book.toString())
                .build().toString()
            var res: String = ""
            lateinit var json: JSONObject

            var request = Request.Builder().url(url).build()
            var response: Response? = null

            try {
                response = client.newCall(request).execute()
                res = response.body()!!.string()
                var Jarray: JSONArray = JSONArray(res)

                for (i in 0 until Jarray.length()){
                    val `object` : JSONObject = Jarray.getJSONObject(i)
                    json = `object`
                    Log.w("**********",res)
                    Log.w("**********", json.toString())

                    var id : Int = json.getInt("id")
                    var id_Book : Int = json.getInt("id_Book")
                    var id_User : Int = json.getInt("id_User")
                    var content : String = json.getString("content")
                    var dateCreated : String = json.getString("dateCreated")


                    comment = Comment(id,id_Book,id_User,content,dateCreated)
                    Comments.add(comment!!)
                }
            }
            catch (e: IOException)
            {
                e.printStackTrace()
            }catch (e: JSONException)
            {
                e.printStackTrace()
            }
            elem = Comments as ArrayList<Comment>
            return elem
        }
    }

    private class SaveToFavs(val id_User : Int, val id_Book : Int):AsyncTask<Void,Void,Boolean>()
    {
        override fun doInBackground(vararg params: Void?): Boolean {
            var client : OkHttpClient = OkHttpClient()

            var formBodybuilder : FormBody.Builder = FormBody.Builder()
            lateinit var formBody : FormBody

            formBodybuilder.add("id_User", id_User.toString())
            formBodybuilder.add("id_Book", id_Book.toString())

            formBody = formBodybuilder.build()

            var url : String = HttpUrl.parse("${ConnectionStrings.ApiUrl}AddFavourite")!!.newBuilder()
                .build().toString()

            /*var postBody : String = "{\n " +
                    "   \"id_User\": \"$id_User\",\n " +
                    "   \"id_Book\": \"$id_Book\",\n" +
                    "}"*/

            var res : String = ""
            lateinit var json : JSONObject

            var builder : Request.Builder = Request.Builder()
            builder = builder.url(url)
            builder = builder.post(formBody)
            var request : Request = builder.build()

            try {

                client.newCall(request).execute()
                return true
            }
            catch (e: IOException)
            {
                e.printStackTrace()
            }catch (e: JSONException)
            {
                e.printStackTrace()
            }
            return false
        }
    }

    private class  postComment(val id_user : Int, val id_book :Int, val comment : String): AsyncTask<Void,Void,Boolean>()
    {
        override fun doInBackground(vararg params: Void?): Boolean? {
            var client : OkHttpClient = OkHttpClient()

            var formBodybuilder : FormBody.Builder = FormBody.Builder()
            lateinit var formBody : FormBody

            formBodybuilder.add("id_user",id_user.toString())
            formBodybuilder.add("id_book", id_book.toString())
            formBodybuilder.add("content",comment)

            formBody = formBodybuilder.build()

            var url : String = HttpUrl.parse("${ConnectionStrings.ApiUrl}Comment")!!.newBuilder()
                .build().toString()

            /*var postBody : String = "{\n " +
                    "   \"id_user\": \"$id_user\",\n " +
                    "   \"id_book\": \"$id_book\",\n" +
                    "   \"content\": \"$comment\",\n" +
                    "}"*/

            var res : String = ""
            lateinit var json : JSONObject

            var builder : Request.Builder = Request.Builder()
            builder = builder.url(url)
            builder = builder.post(formBody)
            var request : Request = builder.build()

            try {

                client.newCall(request).execute()
                return true
            }
            catch (e: IOException)
            {
                e.printStackTrace()
            }catch (e: JSONException)
            {
                e.printStackTrace()
            }
            return false
        }
    }
}
