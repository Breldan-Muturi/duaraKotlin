package turi.duara.duarakotlin.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_iam.*
import kotlinx.android.synthetic.main.fragment_chats.*
import turi.duara.duarakotlin.R
import turi.duara.duarakotlin.adapters.SectionPagerAdapter
import turi.duara.duarakotlin.models.MyClickListener

class DashboardActivity : AppCompatActivity() {
    private lateinit var firebaseIam: FirebaseInAppMessaging
    override fun onCreate(savedInstanceState: Bundle?) {

        var sectionAdapter: SectionPagerAdapter? = null

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        firebaseIam = FirebaseInAppMessaging.getInstance()
        firebaseIam.isAutomaticDataCollectionEnabled = true
        firebaseIam.setMessagesSuppressed(false)
        addClickListener()
        //Get and log the instance Id
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener(object: OnSuccessListener<InstanceIdResult> {
                override fun onSuccess(instanceResultId: InstanceIdResult) {
                    val instanceId = instanceResultId.id
                    instanceIdText.text = getString(R.string.instance_id_fmt, instanceId)
                    Log.d(TAG,"instanceId: $instanceId")
                }
            })

        supportActionBar!!.title = "Home"
        sectionAdapter = SectionPagerAdapter(supportFragmentManager)
        dashViewPagerId.adapter = sectionAdapter
        mainTabs.setupWithViewPager(dashViewPagerId)
        mainTabs.setTabTextColors(Color.WHITE, Color.GREEN)

        if (intent.extras != null) {
            var username = intent.extras!!.get("name")
            Toast.makeText(this, username.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun addClickListener(){
        val listener = MyClickListener()
        firebaseIam.addClickListener(listener)
    }

    private fun suppressMessages(){
        firebaseIam.setMessagesSuppressed(true)

    }

    private fun enableDataCollection(){
        //Only needed when inapp_messaging_auto_data_collection_enabled is set to false in the android manifest
        firebaseIam.isAutomaticDataCollectionEnabled = true

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item != null) {
            if (item.itemId == R.id.logoutid) {
                //Log out the user
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            if (item.itemId == R.id.settingsId) {
                //Take user to settings activity
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            if (item.itemId == R.id.AdsId) {
                //Take user to settings activity
                startActivity(Intent(this, AdActivity::class.java))
            }
            if (item.itemId == R.id.eventTriggerButton) {
                //Take user to In App Messaging activity
                firebaseIam.triggerEvent("engagement_party")
            }

        }
        return true
    }
    companion object{
        private const val TAG = "DashBoardActivity"
    }


}
