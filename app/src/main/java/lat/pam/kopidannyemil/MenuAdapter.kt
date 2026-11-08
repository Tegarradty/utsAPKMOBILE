package lat.pam.kopidannyemil

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/** Adapter daftar menu di halaman Home. */
class MenuAdapter(
    private val onCartChanged: () -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val items = mutableListOf<MenuItem>()

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEmoji: TextView = itemView.findViewById(R.id.tvEmoji)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val btnAdd: TextView = itemView.findViewById(R.id.btnAdd)
        val layoutStepper: LinearLayout = itemView.findViewById(R.id.layoutStepper)
        val btnMinus: TextView = itemView.findViewById(R.id.btnMinus)
        val btnPlus: TextView = itemView.findViewById(R.id.btnPlus)
        val tvQty: TextView = itemView.findViewById(R.id.tvQty)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submit(list: List<MenuItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = items[position]
        val density = holder.itemView.resources.displayMetrics.density

        holder.tvEmoji.text = item.emoji
        holder.tvEmoji.background = GradientDrawable().apply {
            cornerRadius = 18f * density
            setColor(Color.parseColor(item.bgColor))
        }
        holder.tvName.text = item.name
        holder.tvDesc.text = item.description
        holder.tvPrice.text = AppData.rupiah(item.price)

        val qty = AppData.qty(item.id)
        holder.btnAdd.visibility = if (qty == 0) View.VISIBLE else View.GONE
        holder.layoutStepper.visibility = if (qty == 0) View.GONE else View.VISIBLE
        holder.tvQty.text = qty.toString()

        holder.btnAdd.setOnClickListener { changeQty(holder, item, add = true) }
        holder.btnPlus.setOnClickListener { changeQty(holder, item, add = true) }
        holder.btnMinus.setOnClickListener { changeQty(holder, item, add = false) }
    }

    private fun changeQty(holder: MenuViewHolder, item: MenuItem, add: Boolean) {
        if (add) AppData.addToCart(item.id) else AppData.removeFromCart(item.id)
        val pos = holder.bindingAdapterPosition
        if (pos != RecyclerView.NO_POSITION) notifyItemChanged(pos)
        onCartChanged()
    }
}
