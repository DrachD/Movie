package com.dmitriy.movie.fragments.loginRegister

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.dmitriy.movie.R
import com.dmitriy.movie.databinding.FragmentLoginBinding
import com.dmitriy.movie.model.account.entities.Account
import com.dmitriy.movie.repository.RepositorySharedPreferences
import com.dmitriy.movie.view.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = (context as MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding.buttonLoginLogin.setOnClickListener { onSignInAccountPressed() }
    }

    private fun onSignInAccountPressed() {
        val edPassword = binding.edPasswordLogin.text.toString()
        val edEmail = binding.edEmailLogin.text.toString()
        try {
//            val password = "qwerty"
//            var email = ""
//
//            RepositorySharedPreferences(requireContext()).loadAccountData(object : RepositorySharedPreferences.Callback {
//                override fun onLoadAccountData(isSignIn: Boolean, account: Account) {
//                    email = (account.email) ?: ""
//                }
//            })

            if (edEmail.isBlank()) throw EmptyFieldException()
            if (edPassword.isBlank()) throw EmptyFieldException()

//            val record = if (edEmail == email) {
//                edEmail
//            } else {
//                null
//            }

//            if (record != null && edPassword == password) {
//
//            } else {
//                throw AuthException()
//            }
            auth.signInWithEmailAndPassword(edEmail, edPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.let {
                            if (it.isEmailVerified) {
                                Toast.makeText(requireContext(), "Вход завершен успешно!", Toast.LENGTH_SHORT).show()
                                RepositorySharedPreferences(requireContext()).saveAccountSignIn(true)
                                mainActivity.updateAccountUsername()
                                findNavController().navigate(R.id.action_loginFragment_to_tabsFragment, null, navOptions {
                                    popUpTo(R.id.main_graph) {
                                        inclusive = true
                                    }
                                })

                            } else {
                                it.sendEmailVerification()
                                Toast.makeText(requireContext(), "Для начала подтвердите ваш аккаунт на вашей почте!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Возникли проблемы со входом!", Toast.LENGTH_SHORT).show()
                        throw AuthException()
                    }
                }
        } catch (e: EmptyFieldException) {
            Toast.makeText(requireContext(), "Field is empty", Toast.LENGTH_SHORT).show()
        } catch (e: AuthException) {
            Toast.makeText(requireContext(), "Password or Email is incorrect", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeNavigateToTabsEvent() {
        findNavController().navigate(R.id.action_loginFragment_to_tabsFragment, null, navOptions {
            popUpTo(R.id.loginFragment) {
                inclusive = true
            }
        })
    }

    private fun onSignUpButtonPressed() {
        val email = binding.edEmailLogin.text.toString()
        val emailArg = email.ifBlank {
            null
        }

        val direction = LoginFragmentDirections
    }
}

class EmptyFieldException : RuntimeException()
class AuthException : RuntimeException()