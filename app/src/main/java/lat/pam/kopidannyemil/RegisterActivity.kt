package lat.pam.kopidannyemil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/** 4. Register */
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        findViewById<View>(R.id.root).applySystemBarsPadding()

        val tilName = findViewById<TextInputLayout>(R.id.tilName)
        val tilUsername = findViewById<TextInputLayout>(R.id.tilUsername)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        findViewById<MaterialButton>(R.id.btnRegister).setOnClickListener {
            val name = etName.text?.toString()?.trim().orEmpty()
            val username = etUsername.text?.toString()?.trim().orEmpty()
            val password = etPassword.text?.toString().orEmpty()

            tilName.error = null
            tilUsername.error = null
            tilPassword.error = null
            var valid = true
            if (name.isEmpty()) {
                tilName.error = "Nama lengkap wajib diisi"
                valid = false
            }
            if (username.length < 3 || username.contains(" ")) {
                tilUsername.error = "Minimal 3 karakter, tanpa spasi"
                valid = false
            }
            if (password.length < 6) {
                tilPassword.error = "Password minimal 6 karakter"
                valid = false
            }
            if (!valid) return@setOnClickListener

            val error = AppData.register(name, username, password)
            if (error != null) {
                tilUsername.error = error
            } else {
                Toast.makeText(this, "Registrasi berhasil. Selamat datang!", Toast.LENGTH_SHORT).show()
                openHomeClearTask()
            }
        }

        findViewById<TextView>(R.id.tvToLogin).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
