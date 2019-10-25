package turi.duara.duarakotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseRef = firebaseDatabase.getReference("messages")

        databaseRef.setValue("Hello I love android and kotlin!")
        //Read frm our DB
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.value
                Log.d("VALUE", value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ERROR", error!!.message)
            }
        })
    }
}
