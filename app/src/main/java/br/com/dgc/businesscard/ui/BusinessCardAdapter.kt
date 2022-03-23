package br.com.dgc.businesscard.ui

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.dgc.businesscard.R
import br.com.dgc.businesscard.data.BusinessCard
import com.google.android.material.card.MaterialCardView

class BusinessCardAdapter (val c:Context) : ListAdapter<BusinessCard, BusinessCardAdapter.ViewHolder>(DiffCallback()) {
    var listenerShare : (View) -> Unit = {}
    var listenerDelete : (View) -> Unit = {}

    var imagemUri : String? = null

    inner class ViewHolder (
        private val v : View
    ) : RecyclerView.ViewHolder(v) {
        fun bind(item : BusinessCard){
            v.findViewById<TextView>(R.id.tv_nome).text = item.nome
            v.findViewById<TextView>(R.id.tv_telefone).text = item.telefone
            v.findViewById<TextView>(R.id.tv_email).text = item.email
            v.findViewById<TextView>(R.id.tv_nome_empresa).text = item.empresa
            imagemUri = item.imagem
            v.findViewById<ImageView>(R.id.iv_imagem).setImageURI(Uri.parse(imagemUri))
            v.findViewById<MaterialCardView>(R.id.mcv_content).setBackgroundColor(Color.parseColor(item.fundoPersonalizado))
            v.findViewById<MaterialCardView>(R.id.mcv_content).setOnClickListener{
                popupMenus(it)
            }
        }

        private fun popupMenus(v:View){
            val popupMenus = PopupMenu(c, v)
            popupMenus.inflate(R.menu.context_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.miApagar -> {
                        listenerDelete(v)
                        true
                    }
                    R.id.miCompartilhar -> {
                        listenerShare(v)
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_business_card, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class DiffCallback : DiffUtil.ItemCallback<BusinessCard>(){
    override fun areItemsTheSame(oldItem: BusinessCard, newItem: BusinessCard): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BusinessCard, newItem: BusinessCard): Boolean {
        return oldItem.id == newItem.id
    }

}