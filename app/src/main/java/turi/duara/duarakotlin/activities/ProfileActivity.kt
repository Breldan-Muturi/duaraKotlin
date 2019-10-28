package turi.duara.duarakotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.users_row.*
import turi.duara.duarakotlin.R

class ProfileActivity : AppCompatActivity() {

    var mCurrentUser: FirebaseUser? = null
    var mUsersDatabase: DatabaseReference? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar!!.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(intent.extras != null){
            userId = intent!!.extras!!.get("userId").toString()
            mCurrentUser = FirebaseAuth.getInstance().currentUser
            mUsersDatabase = FirebaseDatabase.getInstance().reference.child("Users")
                .child(userId!!)
            setUpProfile()
        }

        profileSendMessage.setOnClickListener {
            var chatIntent = Intent(this, ChatActivity::class.java)
            chatIntent.putExtra("userId", userId)
            startActivity(chatIntent)
        }

    }

    private fun setUpProfile() {
        mUsersDatabase!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var displayName = dataSnapshot.child("display_name").value.toString()
                var status = dataSnapshot.child("status").value.toString()
                var image = dataSnapshot.child("image").value.toString()

                profileName.text = displayName
                profileStatus.text = status
                Picasso.get().load(image)
                    .placeholder(R.drawable.default_avata)
                    .into(profilePicture)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
