package com.example.bookreader.activities

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bookreader.*
import com.example.bookreader.models.User
import com.example.bookreader.utils.ConnectionStrings
import com.example.bookreader.utils.UserData
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonCall.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (isValidEmail(email) && isValidPassword(password)) {

                val user : User = Login(email, password).execute().get()
                if (user.Username != "")
                {
                    UserData.User = user
                    goToActivity<MainActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                }
            }else {
                toast("Please make sure all data is correct")
            }
        }

        buttonCreateAccount.setOnClickListener{goToActivity<RegisterActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)}

        editTextEmail.validate {
            editTextEmail.error = if (isValidEmail(it)) null else "The email is not valid"
        }

        editTextPassword.validate {
            editTextPassword.error = if (isValidPassword(it)) null else "The password should contain 1 number, 1 uppercase , 1 lowercase, 1 special character and minimum 4 characters"
        }
    }

    private class Login(val mail :String, val password :String) :AsyncTask<Void,Void,User>()
    {

        private var _user : User? = null
        override fun doInBackground(vararg params: Void?): User {

            var client : OkHttpClient = OkHttpClient()

            val url: String = HttpUrl.parse("${ConnectionStrings.ApiUrl}/Login")!!.newBuilder().addEncodedQueryParameter("mail",mail)
                .addEncodedQueryParameter("password",password).build().toString()

            var res : String = ""
            lateinit var json : JSONObject

            var request : Request = Request.Builder().url(url).build()

            var response : Response? = null

            try {
                response = client.newCall(request).execute()
                res = response.body()!!.string()
                var JsonOb = JSONObject(res)

                val `object`: JSONObject = JsonOb
                json = `object`
                Log.w("******************", res)
                Log.w("------------------", json.toString())
                var id : Int = json.getInt("id")
                var username : String = json.getString("username")
                var password : String = json.getString("password")
                var mail : String = json.getString("mail")
                var status : Int = json.getInt("status")
                _user = User(id,username,password,mail,status)
            }catch (e :IOException)
            {
                e.printStackTrace()
            }catch (e: JSONException)
            {
                e.printStackTrace()
            }
            return _user!!
        }
    }
}
