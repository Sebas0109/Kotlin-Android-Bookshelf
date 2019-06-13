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

import com.example.bookreader.R
import com.example.bookreader.adapters.BookAdapter
import com.example.bookreader.listeners.RecyclerBooks
import com.example.bookreader.models.Book
import com.example.bookreader.models.User
import com.example.bookreader.toast
import com.example.bookreader.utils.ConnectionStrings
import com.example.bookreader.utils.UserData
import kotlinx.android.synthetic.main.fragment_favorites.view.*
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class FavoritesFragment : Fragment() {

    var elementos : ArrayList<Book> = ArrayList<Book>()

    private lateinit var recycler : RecyclerView
    private lateinit var adapter : BookAdapter
    private val layoutManager by lazy { LinearLayoutManager(context) }

    private var logged_user : User? = null

    private var list : ArrayList<Book>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.setTitle(R.string.favorite_book_fragment)
        var rootView =  inflater.inflate(R.layout.fragment_favorites, container, false)

        logged_user = UserData.User
        recycler = rootView.recyclerView as RecyclerView
        setRecyclerView()

        return rootView
    }

    private fun setRecyclerView()
    {
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = layoutManager
        list = getFavBooks(logged_user!!.Id,elementos).execute().get()
        adapter = (BookAdapter(list!!, object : RecyclerBooks {
            override fun onClick(book: Book, position: Int) {
                activity?.toast("Book ${book.Title}, ${book.Autor}")
                //LLAMADA A LA TRANSICION AL DETALLE BOOK
            }
        }))
        recycler.adapter = adapter
    }

    private class getFavBooks(val id_User : Int, var elem : ArrayList<Book>): AsyncTask<Void, Void, ArrayList<Book>>()
    {

        private var Books: MutableList<Book> = mutableListOf<Book>()
        private var book : Book? = null

        override fun doInBackground(vararg params: Void?): ArrayList<Book> {
            var client : OkHttpClient = OkHttpClient()

            val url : String = HttpUrl.parse("${ConnectionStrings.ApiUrl}/GetUserFavs")!!.newBuilder()
                .addEncodedQueryParameter("id_User",id_User.toString()).build().toString()

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
