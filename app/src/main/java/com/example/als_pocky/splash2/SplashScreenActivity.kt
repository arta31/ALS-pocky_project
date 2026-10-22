package com.example.als_pocky.splash2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.als_pocky.R
import com.example.als_pocky.auth2.AuthActivity
import com.example.als_pocky.base3.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Gradasi navy sampai ke belakang status bar, icon status bar putih
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContentView(R.layout.activity_splash_screen)

        // Akses penyimpanan "user_pref"
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        lifecycleScope.launch {
            delay(2000) // simulasi pengambilan data selama 2 detik

            // Cek status login
            val isLogin = sharedPref.getBoolean("isLogin", false)
            if (isLogin) {
                // isLogin true: ke BaseActivity
                var intent = Intent(this@SplashScreenActivity,
                    BaseActivity::class.java)
                startActivity(intent)
            } else {
                // isLogin false: ke AuthActivity
                var intent = Intent(this@SplashScreenActivity,
                    AuthActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }
}