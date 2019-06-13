package com.example.bookreader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.bookreader.activities.LoginActivity
import com.example.bookreader.activities.ReadQrActivity
import com.example.bookreader.fragments.BooksFragment
import com.example.bookreader.fragments.FavoritesFragment
import com.example.bookreader.fragments.ProfileFragment
import com.example.bookreader.fragments.ScanFragment
import com.example.bookreader.utils.UserData
import com.example.mylibrary2.ToolBarActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolBarActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBarToload(toolbar as Toolbar)

        setNavDrawer()
        setUserHeaderInfo()

        if (savedInstanceState == null)
        {
            fragmentTransaction(BooksFragment()) //para el default
            navView.menu.getItem(0).isChecked = true
        }
    }

    private fun setNavDrawer()
    {
        //EL VALOR D LOS STRING S ES DE PARTE DE QUE ES ES STRING QUE REPRESENTA EL ABRIR Y CUAL REPRESENTA EL CERRAR
        val toogle = ActionBarDrawerToggle(this, drawerLayout, _toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        toogle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    private fun setUserHeaderInfo()
    {
        val name = navView.getHeaderView(0).findViewById<TextView>(R.id.textViewName)
        name?.let {it.text = UserData.User!!.Username }
    }

    private fun loadFragmentById(id : Int)
    {
        when(id){
            R.id.nav_books -> fragmentTransaction(BooksFragment())
            R.id.nav_favorites -> fragmentTransaction(FavoritesFragment())
            R.id.nav_scan -> fragmentTransaction(ScanFragment())
            R.id.nav_profile -> fragmentTransaction(ProfileFragment())
        }
    }

    private fun fragmentTransaction(fragment : Fragment)
    {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment).commit()
    }

    private fun showMessageNavItemSelectedById(id: Int)
    {
        when(id){
            R.id.nav_books -> toast("Books")
            R.id.nav_favorites -> toast("Favorites")
            R.id.nav_scan -> toast("Scan")
            R.id.nav_profile -> toast("Profile")
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        showMessageNavItemSelectedById(p0.itemId)
        loadFragmentById(p0.itemId)
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.general_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_log_out -> {
                //ESTA LINEA COMENTADA ES DONDE SE PONE EL CODIGO DE CERRAR SESION
                goToActivity<LoginActivity>{
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }

            R.id.menu_scan -> {
                goToActivity<ReadQrActivity> {

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
