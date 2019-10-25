package turi.duara.duarakotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import turi.duara.duarakotlin.R

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar!!.title = "Home"

        if(intent.extras != null) {
            var username = intent.extras!!.get("name")
            Toast.makeText(this, username.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
