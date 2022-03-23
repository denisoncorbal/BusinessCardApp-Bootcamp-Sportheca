package br.com.dgc.businesscard.ui


import android.content.Intent
import android.os.Bundle
import android.view.ViewManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.dgc.businesscard.App
import br.com.dgc.businesscard.R
import br.com.dgc.businesscard.data.BusinessCard
import br.com.dgc.businesscard.util.Image
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModels{
        MainViewModelFactory((application as App).repository)
    }

    private val adapter by lazy{BusinessCardAdapter(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<RecyclerView>(R.id.rv_cards).adapter = adapter
        getAllBusinessCard()
        insertListener()
    }

    private fun insertListener(){
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener{
            val intent = Intent(this@MainActivity, AddBusinessCardActivity::class.java)
            startActivity(intent)
        }
        adapter.listenerShare = {
            card -> Image.share(this@MainActivity, card)
        }
        adapter.listenerDelete = {
                card ->
            run {
                val viewManager = card.parent as ViewManager
                viewManager.removeView(card)
            }
        }
    }

    private fun getAllBusinessCard(){
        mainViewModel.getAll().observe(this) { businessCards ->
            adapter.submitList(businessCards)
        }
    }
}