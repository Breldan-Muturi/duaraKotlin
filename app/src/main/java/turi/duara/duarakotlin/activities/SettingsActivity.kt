package turi.duara.duarakotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_settings.*
import turi.duara.duarakotlin.R

class SettingsActivity : AppCompatActivity() {
    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null
    var mStorageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mCurrentUser = FirebaseAuth.getInstance().currentUser

        var userId = mCurrentUser!!.uid
        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)

        mDatabase!!.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var displayName = dataSnapshot!!.child("display_name").value
                var image = dataSnapshot!!.child("image").value
                var userStatus = dataSnapshot!!.child("status").value
                var thumbnail = dataSnapshot!!.child("thumbnail").value

                settingsDisplayName.text = displayName.toString()
                settingsStatusText.text = userStatus.toString()
            }

            override fun onCancelled(databaseError: DatabaseError   ) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
