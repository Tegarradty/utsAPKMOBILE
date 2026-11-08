package lat.pam.kopidannyemil

import java.util.Locale

/** Model user (disimpan di memori, tanpa database). */
data class User(var fullName: String, val username: String, val password: String)

/** Model menu makanan / minuman. */
data class MenuItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val category: String,
    val emoji: String,
    val bgColor: String
)

/** Model alamat pengiriman. */
data class Address(val fullName: String, val address: String, val landmark: String)

/** Model pesanan yang sudah dikirim. */
data class Order(
    val id: String,
    val items: List<Pair<MenuItem, Int>>,
    val subtotal: Int,
    val deliveryFee: Int,
    val total: Int,
    val address: Address
)

/**
 * Sumber data statis (dummy) untuk seluruh aplikasi.
 * Semua data hanya hidup selama aplikasi berjalan.
 */
object AppData {

    const val DELIVERY_FEE = 8000

    // Akun demo bawaan
    private val users = mutableListOf(
        User("Tegar Raditya", "tegar", "123456")
    )

    var currentUser: User? = null
        private set

    var lastOrder: Order? = null
        private set

    /** Penanda agar MainActivity kembali ke tab Home setelah pesanan selesai. */
    var backToHomeTab = false

    val categories = listOf("Semua", "Kopi", "Non-Kopi", "Makanan")

    val menu = listOf(
        MenuItem(1, "Kopi Susu Gula Aren", "Espresso, susu segar, dan gula aren asli", 22000, "Kopi", "🧋", "#F6D5B5"),
        MenuItem(2, "Cappuccino", "Espresso dengan foam susu yang lembut", 25000, "Kopi", "☕", "#EFD9C6"),
        MenuItem(3, "Americano", "Espresso dan air panas, rasa bold & clean", 20000, "Kopi", "☕", "#E8D3C0"),
        MenuItem(4, "Matcha Latte", "Matcha Jepang dengan susu, segar dan creamy", 27000, "Non-Kopi", "🍵", "#D9ECCB"),
        MenuItem(5, "Chocolate Signature", "Cokelat premium dengan susu creamy", 24000, "Non-Kopi", "🍫", "#E6CDBA"),
        MenuItem(6, "Donat Cokelat", "Donat empuk dengan glaze cokelat", 12000, "Makanan", "🍩", "#F8DACB"),
        MenuItem(7, "Sourdough Loaf", "Roti fermentasi alami, renyah di luar", 35000, "Makanan", "🍞", "#F3E3C3"),
        MenuItem(8, "Croissant Butter", "Berlapis-lapis, renyah, aroma butter", 18000, "Makanan", "🥐", "#F7E6CC"),
        MenuItem(9, "Pisang Goreng Keju", "Pisang kepok goreng dengan parutan keju", 15000, "Makanan", "🍌", "#FAEBB8")
    )

    // id menu -> jumlah
    private val cart = linkedMapOf<Int, Int>()

    // ---------- Akun ----------

    fun login(username: String, password: String): Boolean {
        val user = users.firstOrNull {
            it.username.equals(username, ignoreCase = true) && it.password == password
        }
        currentUser = user
        return user != null
    }

    /** @return pesan error, atau null jika berhasil. */
    fun register(fullName: String, username: String, password: String): String? {
        if (users.any { it.username.equals(username, ignoreCase = true) }) {
            return "Username sudah dipakai"
        }
        val user = User(fullName, username, password)
        users.add(user)
        currentUser = user
        return null
    }

    fun updateName(newName: String) {
        currentUser?.fullName = newName
    }

    fun logout() {
        currentUser = null
        cart.clear()
    }

    fun firstName(): String =
        currentUser?.fullName?.trim()?.split(" ")?.firstOrNull().orEmpty().ifEmpty { "Kamu" }

    fun initial(): String =
        currentUser?.fullName?.trim()?.firstOrNull()?.uppercase() ?: "K"

    // ---------- Keranjang ----------

    fun qty(id: Int): Int = cart[id] ?: 0

    fun addToCart(id: Int) {
        cart[id] = qty(id) + 1
    }

    fun removeFromCart(id: Int) {
        val q = qty(id) - 1
        if (q <= 0) cart.remove(id) else cart[id] = q
    }

    fun cartItems(): List<Pair<MenuItem, Int>> =
        cart.mapNotNull { (id, q) -> menu.firstOrNull { it.id == id }?.let { it to q } }

    fun totalQty(): Int = cart.values.sum()

    fun subtotal(): Int = cartItems().sumOf { (item, q) -> item.price * q }

    fun total(): Int = subtotal() + DELIVERY_FEE

    fun placeOrder(address: Address): Order {
        val order = Order(
            id = "KN-" + System.currentTimeMillis().toString().takeLast(6),
            items = cartItems(),
            subtotal = subtotal(),
            deliveryFee = DELIVERY_FEE,
            total = total(),
            address = address
        )
        lastOrder = order
        cart.clear()
        return order
    }

    // ---------- Util ----------

    fun rupiah(value: Int): String =
        "Rp " + String.format(Locale.US, "%,d", value).replace(',', '.')
}
