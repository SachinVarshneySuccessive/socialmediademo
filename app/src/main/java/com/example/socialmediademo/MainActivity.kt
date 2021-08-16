package com.example.socialmediademo

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediademo.databinding.ActivityMainBinding
import im.getsocial.sdk.Communities
import im.getsocial.sdk.GetSocial
import im.getsocial.sdk.GetSocialError
import im.getsocial.sdk.common.PagingQuery
import im.getsocial.sdk.common.PagingResult
import im.getsocial.sdk.communities.*
import im.getsocial.sdk.ui.communities.ActivityFeedViewBuilder
import im.getsocial.sdk.ui.invites.InvitesViewBuilder

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var currentUser: CurrentUser

    companion object {
        var count = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Handler().postDelayed({
            currentUser = GetSocial.getCurrentUser()
            binding.userName.setText("user name is ${currentUser.displayName}")
        }, 2000)



        binding.refer.setOnClickListener {
            InvitesViewBuilder.create().show()
        }

        binding.userFeed.setOnClickListener {
            ActivityFeedViewBuilder.create(ActivitiesQuery.timeline()).show()
        }

        binding.foundUser.setOnClickListener {
            val query = UsersQuery.find("user")
            val pagingQuery = PagingQuery(query)
            Communities.getUsers(pagingQuery, { result: PagingResult<User> ->
                val users = result.entries
                Log.d("Communities", "Users with name user: $users")
            }, { error: GetSocialError ->
                Log.d("Communities", "Failed to get users: $error")
            })        }

        binding.createGroup.setOnClickListener {
            val groupContent = GroupContent.create("sample_group")
                .withTitle("Sample Group")
                .withDescription("This the sample group")
                .withDiscoverable(true) // group will be visible for everyone
                .withPrivate(true) // joining requires approval
                .setPermission(CommunitiesAction.POST, Role.ADMIN) // only admins can create posts
                .setPermission(CommunitiesAction.POST, Role.MEMBER) // members can add reactions

            Communities.createGroup(groupContent, { result: Group ->
                Toast.makeText(this, "group created", Toast.LENGTH_LONG).show()
                print("Group created: $result")
            }, { error ->
                print("Failed to create group, error: $error")
            })
        }

        binding.postMsg.setOnClickListener{

        }
    }
}