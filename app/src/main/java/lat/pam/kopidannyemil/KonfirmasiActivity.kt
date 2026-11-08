package lat.pam.kopidannyemil

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

/** 8. Konfirmasi pesanan */
class KonfirmasiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_konfirmasi)
        findViewById<View>(R.id.root).applySystemBarsPadding()

        val order = AppData.lastOrder
        if (order == null) {
            finish()
            return
        }

        findViewById<TextView>(R.id.tvKonfGreeting).text = "Halo ${AppData.firstName()},"
        findViewById<TextView>(R.id.tvOrderId).text = order.id
        findViewById<TextView>(R.id.tvItemCount).text =
            order.items.joinToString("\n") { (item, qty) -> "${qty}x ${item.name}" }

        val shipTo = buildString {
            append(order.address.fullName)
            append("\n")
            append(order.address.address)
            if (order.address.landmark.isNotEmpty()) {
                append("\nPatokan: ")
                append(order.address.landmark)
            }
        }
        findViewById<TextView>(R.id.tvShipTo).text = shipTo
        findViewById<TextView>(R.id.tvKonfTotal).text = AppData.rupiah(order.total)

        findViewById<MaterialButton>(R.id.btnOke).setOnClickListener { backToHome() }

        // Tombol back juga kembali ke Home
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = backToHome()
        })
    }

    private fun backToHome() {
        AppData.backToHomeTab = true
        finish() // MainActivity masih ada di bawah, jadi otomatis kembali ke Home
    }
}
