package turi.duara.duarakotlin.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import turi.duara.duarakotlin.R
import turi.duara.duarakotlin.activities.ChatActivity
import turi.duara.duarakotlin.activities.ProfileActivity
import turi.duara.duarakotlin.models.Users

class UsersAdapter(databaseQuery: DatabaseReference, var context: Context)
    :FirebaseRecyclerAdapter<Users, UsersAdapter.ViewHolder>(
    Users::class.java,
    R.layout.users_row,
    UsersAdapter.ViewHolder::class.java,
    databaseQuery

){
    override fun populateViewHolder(viewHolder: UsersAdapter.ViewHolder?, user: Users?, position: Int) {
        var userId = getRef(position).key
        viewHolder!!.bindView(user!!, context)
        viewHolder.itemView.setOnClickListener{
            //CREATE AN ALERT DIALOG
            var options = arrayOf("Open Profile", "Send Message")
            var builder = AlertDialog.Builder(context)
            builder.setTitle("Select Options")
            builder.setItems(options, DialogInterface.OnClickListener{ dialogInterface, i ->
                var userName = viewHolder.userNameTxt
                var userStat = viewHolder.userStatusTxt
                var profilePic = viewHolder.userProfilePicLink

                if(i == 0){
                        //  Open User Profile
                    var profileIntent = Intent(context, ProfileActivity::class.java)
                    profileIntent.putExtra("userId", userId)
                    context.startActivity(profileIntent)

                } else {
                    // Send Message
                    var chatIntent = Intent(context, ChatActivity::class.java)
                    chatIntent.putExtra("userId", userId)
                    chatIntent.putExtra("name", userName)
                    chatIntent.putExtra("status", userStat)
                    chatIntent.putExtra("profile", profilePic)
                    context.startActivity(chatIntent)

                }
            })

            builder.show()
//            Toast.makeText(context,"User row clicked $userId", Toast.LENGTH_LONG).show()
        }

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var userNameTxt: String? = null
        var userStatusTxt: String? = null
        var userProfilePicLink: String? = null
        fun bindView (user: Users, context: Context){
            var userName = itemView.findViewById<TextView>(R.id.userName)
            val userStatus = itemView.findViewById<TextView>(R.id.userStatus)
            var userProfilePic = itemView.findViewById<CircleImageView>(R.id.usersProfile)

//        Set the strings so we can pass in the intent
            userNameTxt = user.display_name
            userStatusTxt = user.status
            userProfilePicLink = user.thumb_image

            userName.text = user.display_name
            userStatus.text = user.status

            Picasso.get().load(userProfilePicLink)
                .placeholder(R.drawable.default_avata)
                .into(userProfilePic)
        }
    }

}

