package lat.pam.kopidannyemil

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * 5. Home (daftar menu) dan 6. Profile.
 * Keduanya berada di satu Activity dan berpindah lewat bottom navigation.
 * Tab "Order" membuka halaman 7. Alamat pengiriman.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var homeView: View
    private lateinit var profileView: View
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var cartBar: MaterialCardView
    private lateinit var tvCartQty: TextView
    private lateinit var tvCartTotal: TextView
    private lateinit var tvMenuCount: TextView
    private lateinit var adapter: MenuAdapter

    private lateinit var tvHomeGreeting: TextView
    private lateinit var tvHomeAvatar: TextView
    private lateinit var tvProfileGreeting: TextView
    private lateinit var tvProfileAvatar: TextView
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileUsername: TextView
    private lateinit var tilProfileName: TextInputLayout
    private lateinit var etProfileName: TextInputEditText
    private lateinit var etProfileUsername: TextInputEditText

    private var currentCategory = "Semua"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Jika belum login (mis. aplikasi dibuka ulang), kembali ke Welcome
        if (AppData.currentUser == null) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.root).applySystemBarsPadding()

        homeView = findViewById(R.id.homeView)
        profileView = findViewById(R.id.profileView)
        bottomNav = findViewById(R.id.bottomNav)
        cartBar = findViewById(R.id.cartBar)
        tvCartQty = findViewById(R.id.tvCartQty)
        tvCartTotal = findViewById(R.id.tvCartTotal)
        tvMenuCount = findViewById(R.id.tvMenuCount)
        tvHomeGreeting = findViewById(R.id.tvHomeGreeting)
        tvHomeAvatar = findViewById(R.id.tvHomeAvatar)
        tvProfileGreeting = findViewById(R.id.tvProfileGreeting)
        tvProfileAvatar = findViewById(R.id.tvProfileAvatar)
        tvProfileName = findViewById(R.id.tvProfileName)
        tvProfileUsername = findViewById(R.id.tvProfileUsername)
        tilProfileName = findViewById(R.id.tilProfileName)
        etProfileName = findViewById(R.id.etProfileName)
        etProfileUsername = findViewById(R.id.etProfileUsername)

        setupHome()
        setupProfile()
        setupBottomNav()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (profileView.visibility == View.VISIBLE) {
                    bottomNav.selectedItemId = R.id.nav_home
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (AppData.currentUser == null || !::adapter.isInitialized) return

        if (AppData.backToHomeTab) {
            AppData.backToHomeTab = false
            bottomNav.selectedItemId = R.id.nav_home
        }
        refreshUserInfo()
        refreshMenu()
        updateCartBar()
    }

    // ---------------- Home ----------------

    private fun setupHome() {
        val rv = findViewById<RecyclerView>(R.id.rvMenu)
        rv.layoutManager = LinearLayoutManager(this)
        adapter = MenuAdapter { updateCartBar() }
        rv.adapter = adapter

        findViewById<ChipGroup>(R.id.chipGroup).setOnCheckedStateChangeListener { _, checkedIds ->
            currentCategory = when (checkedIds.firstOrNull()) {
                R.id.chipKopi -> "Kopi"
                R.id.chipNonKopi -> "Non-Kopi"
                R.id.chipMakanan -> "Makanan"
                else -> "Semua"
            }
            applyFilter()
        }
        applyFilter()

        cartBar.setOnClickListener { openOrder() }
    }

    private fun applyFilter() {
        val list = if (currentCategory == "Semua") AppData.menu
        else AppData.menu.filter { it.category == currentCategory }
        adapter.submit(list)
        tvMenuCount.text = "${list.size} menu"
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshMenu() {
        adapter.notifyDataSetChanged()
    }

    private fun updateCartBar() {
        val qty = AppData.totalQty()
        cartBar.visibility = if (qty > 0 && homeView.visibility == View.VISIBLE) View.VISIBLE else View.GONE
        tvCartQty.text = "$qty item di keranjang"
        tvCartTotal.text = AppData.rupiah(AppData.subtotal())
    }

    private fun openOrder() {
        if (AppData.totalQty() == 0) {
            Toast.makeText(this, "Keranjang masih kosong. Pilih menu dulu, ya!", Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(Intent(this, AlamatActivity::class.java))
    }

    // ---------------- Profile ----------------

    private fun setupProfile() {
        findViewById<MaterialButton>(R.id.btnSave).setOnClickListener {
            val name = etProfileName.text?.toString()?.trim().orEmpty()
            tilProfileName.error = null
            if (name.isEmpty()) {
                tilProfileName.error = "Nama tidak boleh kosong"
                return@setOnClickListener
            }
            AppData.updateName(name)
            hideKeyboard(etProfileName)
            refreshUserInfo()
            Toast.makeText(this, "Profil berhasil disimpan", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialButton>(R.id.btnLogout).setOnClickListener {
            AppData.logout()
            val intent = Intent(this, SignUpActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun refreshUserInfo() {
        val user = AppData.currentUser ?: return
        val greeting = "Halo ${AppData.firstName()},"
        tvHomeGreeting.text = greeting
        tvProfileGreeting.text = greeting
        tvHomeAvatar.text = AppData.initial()
        tvProfileAvatar.text = AppData.initial()
        tvProfileName.text = user.fullName
        tvProfileUsername.text = "@${user.username}"
        etProfileName.setText(user.fullName)
        etProfileUsername.setText(user.username)
    }

    // ---------------- Bottom navigation ----------------

    private fun setupBottomNav() {
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    showHome(true)
                    true
                }
                R.id.nav_order -> {
                    openOrder()
                    false // tetap di tab yang sedang aktif
                }
                R.id.nav_profile -> {
                    showHome(false)
                    true
                }
                else -> false
            }
        }
    }

    private fun showHome(show: Boolean) {
        homeView.visibility = if (show) View.VISIBLE else View.GONE
        profileView.visibility = if (show) View.GONE else View.VISIBLE
        if (!show) refreshUserInfo()
        updateCartBar()
    }
}
