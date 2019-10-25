package turi.duara.duarakotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseRef = firebaseDatabase.getReference("messages").push()
        mAuth = FirebaseAuth.getInstance()
//        Sign existing users in
        mAuth!!.signInWithEmailAndPassword("breldanmuturi001@outlook.com","Sunrise@1997")
            .addOnCompleteListener{
                task: Task<AuthResult> ->
                if(task.isSuccessful){
//                    Sign in was successful
                    Toast.makeText(this,"Signed In Successful", Toast.LENGTH_LONG).show()
                } else {
//                    Not successful
                    Toast.makeText(this,"Sign In Unsuccessful", Toast.LENGTH_LONG).show()

                }
            }
    }

    override fun onStart() {
        super.onStart()
        currentUser = mAuth!!.currentUser
//        Call a function that will update the UI with current user
        if(currentUser != null){
            Toast.makeText( this, "User is logged in", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText( this, "User is logged out", Toast.LENGTH_LONG).show()
        }
    }

    data class Employee(
        val name: String,
        val position: String,
        val homeAddress: String,
        val age: Int
    )
}
