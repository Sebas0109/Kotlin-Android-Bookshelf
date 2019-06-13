package com.example.bookreader.fragments


import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import com.example.bookreader.R
import com.example.bookreader.activities.DetailBook
import com.example.bookreader.adapters.BookAdapter
import com.example.bookreader.goToActivity
import com.example.bookreader.listeners.RecyclerBooks
import com.example.bookreader.models.Book
import com.example.bookreader.toast
import com.example.bookreader.utils.ConnectionStrings
import kotlinx.android.synthetic.main.fragment_books.view.*
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class BooksFragment : Fragment() {

    var elementos : ArrayList<Book> = ArrayList<Book>()
    lateinit var listview : ListView

    private lateinit var recycler : RecyclerView
    private lateinit var adapter : BookAdapter
    private val layoutManager by lazy { LinearLayoutManager(context) }

    private var list : ArrayList<Book>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(R.string.book_list_fragment)
        val rootView =inflater.inflate(R.layout.fragment_books, container, false)

        recycler = rootView.recyclerView as RecyclerView
        setRecyclerView()

        return rootView
    }

    private fun setRecyclerView()
    {
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = layoutManager
        list = getBooks(elementos).execute().get()
        adapter = (BookAdapter(list!!, object : RecyclerBooks {
            override fun onClick(book: Book, position: Int) {
                activity?.toast("Book ${book.Title}, ${book.Autor}")
                activity?.goToActivity<DetailBook> {
                    putExtra("book", book)
                }
            }
            }))
        recycler.adapter = adapter
    }

    private class getBooks(var elem : ArrayList<Book>): AsyncTask<Void,Void,ArrayList<Book>>()
    {

        private var Books: MutableList<Book> = mutableListOf<Book>()
        private var book : Book? = null

        override fun doInBackground(vararg params: Void?): ArrayList<Book> {
            var client : OkHttpClient = OkHttpClient()

            val url : String = HttpUrl.parse("${ConnectionStrings.ApiUrl}/GetBooks")!!.newBuilder().build().toString()

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
                    var titulo : String = json.getString("title")
                    var autor : String = json.getString("autor")
                    var bookPortrait : String = json.getString("bookPortrait")
                    var imageHolder : String = json.getString("imageHolder")
                    var description : String = json.getString("description")
                    var content : String = json.getString("content")

                    book = Book(id,titulo,autor,bookPortrait,imageHolder,description,content)
                    Books.add(book!!)
                }
            }
            catch (e: IOException)
            {
                e.printStackTrace()
            }catch (e: JSONException)
            {
                e.printStackTrace()
            }
            elem = Books as ArrayList<Book>
            return elem
        }
    }
}
