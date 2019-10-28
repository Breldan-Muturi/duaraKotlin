package turi.duara.duarakotlin.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_settings.*
import turi.duara.duarakotlin.R
import java.io.ByteArrayOutputStream
import java.io.File


class SettingsActivity : AppCompatActivity() {
    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null
    var mStorageRef: StorageReference? = null
    var GALLERY_ID: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        mStorageRef = FirebaseStorage.getInstance().reference

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var userId = mCurrentUser!!.uid
        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)

        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var displayName = dataSnapshot.child("display_name").value.toString()
                var image = dataSnapshot.child("image").value.toString()
                var userStatus = dataSnapshot.child("status").value.toString()
                var thumbnail = dataSnapshot.child("thumb_image").value

                settingsDisplayName.text = displayName
                settingsStatusText.text = userStatus
                if (!image.equals("default")) {
                    Picasso.get().load(image)
                        .placeholder(R.drawable.default_avata)
                        .into(settingsProfileId)
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        settingsChangeStatusBtn.setOnClickListener {
            var intent = Intent(this, StatusActivity::class.java)
            intent.putExtra("status", settingsStatusText.text.toString().trim())
            startActivity(intent)
        }

        settingsChangeImgBtn.setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {
            var image: Uri = data!!.data!!
            CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)
        }
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri
                var userId = mCurrentUser!!.uid
                var thumbFile = File(resultUri.path)
                var thumbBitMap = Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(75)
                    .compressToBitmap(thumbFile)

//              Now we upload to Firebase
                var byteArray = ByteArrayOutputStream()
                thumbBitMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
                var thumbByteArray: ByteArray
                thumbByteArray = byteArray.toByteArray()
                var filePath = mStorageRef!!.child("chat_profile_images")
                    .child(userId + ".jpg")

//                Create a Directory for thumb images(smaller images)
                var thumbFilePath = mStorageRef!!.child("chat_profile_images")
                    .child("thumbs")
                    .child(userId + ".jpg")

//                Saving the image files
                filePath.putFile(resultUri)
                    .addOnSuccessListener { taskSnapshot ->
                        filePath.downloadUrl.addOnCompleteListener { taskSnapshot ->
                            var downloadUrl = taskSnapshot.result.toString()
                            var updateObj = HashMap<String, Any>()
                            updateObj.put("image", downloadUrl)

                            // We save the profile image
                            mDatabase!!.updateChildren(updateObj)
                                .addOnCompleteListener { task: Task<Void> ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            this,
                                            "Profile Image Saved!" + " " + downloadUrl,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Profile Image Failed!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                        }
                    }
                thumbFilePath.putBytes(thumbByteArray).addOnSuccessListener { taskSnapshot ->
                    thumbFilePath.downloadUrl.addOnCompleteListener { taskSnapshot ->
                        var thumbUrl = taskSnapshot.result.toString()
                        var updateObj = HashMap<String, Any>()
                        updateObj.put("thumb_image", thumbUrl)

                        // We save the profile image
                        mDatabase!!.updateChildren(updateObj)
                            .addOnCompleteListener { task: Task<Void> ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Thumb Image Saved!" + " " + thumbUrl,
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(this, "Thumb Image Failed!", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                    }
                }
            }


        }
    }
}


//        super.onActivityResult(requestCode, resultCode, da
