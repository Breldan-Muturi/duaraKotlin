package turi.duara.duarakotlin.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import kotlinx.android.synthetic.main.activity_ad.*
import turi.duara.duarakotlin.R

class AdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)
        MobileAds.initialize(this, "ca-app-pub-4086130289404832~8596378388")
        val adRequest =
            AdRequest.Builder().addTestDevice("00B3B2CBAEDB21C80B7EE14509C86F4A").build()
        adView.loadAd(adRequest)
        loadInterstitialButton.setOnClickListener {

        }
        // Create a deep link and display it in the UI
        val newDeepLink = buildDeepLink(Uri.parse(DEEP_LINK_URL))
        linkViewSend.text = newDeepLink.toString()

        buttonShare.setOnClickListener { shareDeepLink(newDeepLink.toString()) }

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
                //Handle the deepLink, For example; open form the linked content, or apply promotional credit to the user's account
                //Display deepLink in the UI
                if (deepLink != null) {
                    Toast.makeText(applicationContext, "Found Deep Link", Toast.LENGTH_LONG).show()
                    linkViewReceive.text = deepLink.toString()
                } else {
                    Log.d(TAG, "getDynamicLink: no link found")
                    Toast.makeText(applicationContext, "Found no Deep Link", Toast.LENGTH_LONG)
                        .show()
                }
            }
            .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLinkFailure", e) }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun buildDeepLink(deepLink: Uri): Uri {
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(deepLink.toString()))
            .setDomainUriPrefix("https://manzi.page.link")
            //OPens Link with this app ON Android
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            //OPens Link with this app ON Ios
            .setIosParameters(DynamicLink.IosParameters.Builder("com.example.ios").build())
            .buildDynamicLink()
        val dynamicLinkUri = dynamicLink.uri
        return dynamicLinkUri
    }

    private fun shareDeepLink(deepLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase Deep Link")
        intent.putExtra(Intent.EXTRA_TEXT, deepLink)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "AdActivity"
        private const val DEEP_LINK_URL = "https://kotlin.example.com/deeplinks"
    }
}
