package com.example.bookreader.activities

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bookreader.*
import com.example.bookreader.utils.ConnectionStrings
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonGoLoggin.setOnClickListener {
            goToActivity<LoginActivity> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        buttonSignUp.setOnClickListener {
            val username : String = editTextName.text.toString()
            val email : String = editTextEmail.text.toString()
            val password : String = editTextPassword.text.toString()
            val password2 : String = editTextConfirmPassword.text.toString()
            if (isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(password, password2))
            {
                //CREACION DE USUARIOS Y ENVIO A LA PAGINA PRINCIPAL
                if(Register(username,email,password).execute().get())
                {
                    toast("Go Back to Login to Open Session")
                }else
                {
                    toast("Error adding User")
                }

            }else {
                toast("Please make sure all data is correct")
            }
        }

        //USAMOS LA VALIDACION DE LAS EXTENSOINS FUNCTIONS PASANDO LA FUNCOIN QUE SE VA A EMPLEAR
        editTextEmail.validate {
            editTextEmail.error = if (isValidEmail(it)) null else "The email is not valid"
        }

        editTextPassword.validate {
            editTextPassword.error = if (isValidPassword(it)) null else "The password should contain 1 number, 1 uppercase , 1 lowercase, 1 special character and minimum 4 characters"
        }

        editTextConfirmPassword.validate {
            editTextConfirmPassword.error = if (isValidConfirmPassword(editTextPassword.text.toString(), it)) null else "Passwords are different check them again"
        }
    }

    //private class metodo asincrono
    private class Register(val username : String, val mail : String, val password: String): AsyncTask<Void,Void,Boolean>()
    {
        var res : String = ""
        override fun doInBackground(vararg params: Void?): Boolean {

            var client : OkHttpClient = OkHttpClient()

            val url: String = HttpUrl.parse("${ConnectionStrings.ApiUrl}/Register")!!.newBuilder().build().toString()

            var postBody : String = "{\n " +
                    "   \"username\": \"$username\",\n " +
                    "   \"password\": \"$password\",\n" +
                    "   \"mail\": \"$mail\",\n" +
                    "}"

            var res : String = ""
            lateinit var json : JSONObject

            var builder : Request.Builder = Request.Builder()
            builder = builder.url(url)
            builder = builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),postBody))
            var request : Request = builder.build()

            try {
                client.newCall(request).execute()
                return true

            }catch (e : IOException)
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
