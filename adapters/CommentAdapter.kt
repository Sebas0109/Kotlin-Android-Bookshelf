package com.example.bookreader.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.bookreader.R
import com.example.bookreader.loadByResource
import com.example.bookreader.models.Comment
import com.example.bookreader.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_comment.view.*

class CommentAdapter (private val getContext : Context, private val dataSource : ArrayList<Comment>): BaseAdapter()
{
    private val inflater : LayoutInflater = getContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): Any = dataSource[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val holder : ViewHolder

        if (convertView == null)
        {
            view = inflater.inflate(R.layout.list_comment, parent, false)

            holder = ViewHolder()
            holder.imgU = view.imgUser as ImageView
            holder.palabras = view.palabras as TextView
            holder.date = view.date as TextView

            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val imgU = holder.imgU
        val palabras = holder.palabras
        val date = holder.date

        val recipe = getItem(position) as Comment

        imgU.loadByResource(R.drawable.ic_person)
        palabras.text = recipe.Content
        date.text = recipe.DateCreated
        return view
    }

}

private class ViewHolder
{
    lateinit var imgU : ImageView
    lateinit var palabras : TextView
    lateinit var date : TextView
}