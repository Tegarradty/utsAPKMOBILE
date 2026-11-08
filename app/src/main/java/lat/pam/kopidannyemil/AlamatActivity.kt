package lat.pam.kopidannyemil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/** 7. Alamat pengiriman (Order) */
class AlamatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alamat)
        findViewById<View>(R.id.root).applySystemBarsPadding()

        if (AppData.totalQty() == 0) {
            finish()
            return
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<TextView>(R.id.tvAlamatGreeting).text = "Halo ${AppData.firstName()},"

        // Ringkasan pesanan
        val container = findViewById<LinearLayout>(R.id.layoutOrderItems)
        val inflater = LayoutInflater.from(this)
        AppData.cartItems().forEach { (item, qty) ->
            val row = inflater.inflate(R.layout.item_order_row, container, false)
            row.findViewById<TextView>(R.id.tvRowName).text = "${qty}x  ${item.name}"
            row.findViewById<TextView>(R.id.tvRowPrice).text = AppData.rupiah(item.price * qty)
            container.addView(row)
        }
        findViewById<TextView>(R.id.tvSubtotal).text = AppData.rupiah(AppData.subtotal())
        findViewById<TextView>(R.id.tvOngkir).text = AppData.rupiah(AppData.DELIVERY_FEE)
        findViewById<TextView>(R.id.tvTotal).text = AppData.rupiah(AppData.total())

        // Form alamat
        val tilNama = findViewById<TextInputLayout>(R.id.tilNama)
        val tilAlamat = findViewById<TextInputLayout>(R.id.tilAlamat)
        val etNama = findViewById<TextInputEditText>(R.id.etNama)
        val etAlamat = findViewById<TextInputEditText>(R.id.etAlamat)
        val etPatokan = findViewById<TextInputEditText>(R.id.etPatokan)
        etNama.setText(AppData.currentUser?.fullName.orEmpty())

        val btnOrder = findViewById<MaterialButton>(R.id.btnOrder)
        btnOrder.text = "Order dan Kirim · ${AppData.rupiah(AppData.total())}"
        btnOrder.setOnClickListener {
            val nama = etNama.text?.toString()?.trim().orEmpty()
            val alamat = etAlamat.text?.toString()?.trim().orEmpty()
            val patokan = etPatokan.text?.toString()?.trim().orEmpty()

            tilNama.error = null
            tilAlamat.error = null
            var valid = true
            if (nama.isEmpty()) {
                tilNama.error = "Nama lengkap wajib diisi"
                valid = false
            }
            if (alamat.length < 10) {
                tilAlamat.error = "Tulis alamat lengkap (minimal 10 karakter)"
                valid = false
            }
            if (!valid) return@setOnClickListener

            AppData.placeOrder(Address(nama, alamat, patokan))
            startActivity(Intent(this, KonfirmasiActivity::class.java))
            finish()
        }
    }
}
