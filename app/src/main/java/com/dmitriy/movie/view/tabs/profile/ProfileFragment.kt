package com.dmitriy.movie.view.tabs.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.dmitriy.movie.R
import com.dmitriy.movie.databinding.FragmentProfileBinding
import com.dmitriy.movie.model.account.entities.Account
import com.dmitriy.movie.repository.RepositorySharedPreferences
import com.dmitriy.movie.util.findNavController
import com.dmitriy.movie.view.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = (context as MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database.reference
        auth = Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding.logoutButton.setOnClickListener { onSignOutButtonPressed() }
        updateUI()
    }

    private fun updateUI() {
//        database.child("users").child(auth.currentUser!!.uid).addValueEventListener(
//            object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val account = snapshot.getValue(Account::class.java)
//
//                    account?.let {
//                        binding.apply {
//                            mailTextView.text = account.email.toString()
//                            nicknameTextView.text = account.username.toString()
//
//                            dateCreatedTextView.text = convertLongToTime(account.createdAt)
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.d("logs", error.toString())
//                    Log.d("logs", error.message)
//                }
//            }
//        )

        RepositorySharedPreferences(requireContext())
            .loadAccountData(object : RepositorySharedPreferences.Callback {
            override fun onLoadAccountData(account: Account) {
                binding.apply {
                    mailTextView.text = account.email.toString()
                    nicknameTextView.text = account.username.toString()

                    dateCreatedTextView.text = convertLongToTime(account.createdAt)
                }
            }
        })
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }

    private fun onSignOutButtonPressed() {
        Log.d("logs", "logout")
        mainActivity.onLogoutPressed()
        RepositorySharedPreferences(requireContext()).saveAccountSignIn(false)
        mainActivity.updateAccountUsername()
//        val navHost = requireActivity().supportFragmentManager
//            .findFragmentById(R.id.fragmentContainer) as NavHostFragment
//        navHost.navController.navigate(R.id.introductionFragment, null, navOptions {
//            popUpTo(R.id.tabsFragment) {
//                inclusive = true
//            }
//        })

        findNavController().navigate(R.id.introductionFragment, null, navOptions {
            popUpTo(R.id.tabsFragment) {
                inclusive = true
            }
        })
    }
}