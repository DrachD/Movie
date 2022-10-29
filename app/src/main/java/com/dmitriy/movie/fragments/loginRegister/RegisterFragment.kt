package com.dmitriy.movie.fragments.loginRegister

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dmitriy.movie.R
import com.dmitriy.movie.databinding.FragmentRegisterBinding
import com.dmitriy.movie.model.account.entities.Account
import com.dmitriy.movie.repository.RepositorySharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    val actionCodeSettings = actionCodeSettings {
        // URL you want to redirect back to. The domain (www.example.com) for this
        // URL must be whitelisted in the Firebase Console.
        url = "https://www.example.com/finishSignUp?cartId=1234"
        // This must be true
        handleCodeInApp = true
        setIOSBundleId("com.example.ios")
        setAndroidPackageName(
            "com.example.android",
            true, /* installIfNotAvailable */
            "12" /* minimumVersion */)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        binding.buttonRegisterRegister.setOnClickListener { onCreateAccountPressed() }
    }

    private fun onCreateAccountPressed() {
        val email = binding.edEmailRegister.text.toString()
        val firstname = binding.edFirstName.text.toString()
        val secondname = binding.edSecondName.text.toString()
        val password = binding.edPasswordRegister.text.toString()

        if (email.isBlank()) return
        if (firstname.isBlank()) return
        if (secondname.isBlank()) return
        if (password.isBlank()) return

//        RepositorySharedPreferences(requireContext()).saveAccountData(true, Account(
//            username = firstname,
//            email = email,
//            createdAt = System.currentTimeMillis()
//        ))

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onCreateAccountFirebase(firstname, email)
                } else {
                    Toast.makeText(requireContext(), "Произошла ошибка при создании пользователя", Toast.LENGTH_SHORT).show()
                }
            }
//        findNavController().navigate(R.id.tabsFragment, null, navOptions {
//            popUpTo(R.id.main_graph) {
//                inclusive = true
//            }
//        })
    }

    private fun onCreateAccountFirebase(firstname: String, email: String) {
        val account = Account(
            username = firstname,
            email = email,
            createdAt = System.currentTimeMillis()
        )

        database.child("users").child(auth.currentUser!!.uid)
            .setValue(account).addOnCompleteListener {
                if (it.isSuccessful) {
                    RepositorySharedPreferences(requireContext())
                        .saveAccountData(
                            account
                        )
                    Toast.makeText(requireContext(), "Пользователь успешно зарегестрирован!", Toast.LENGTH_SHORT).show()
                    onSendEmailVerification()
                } else {
                    Toast.makeText(requireContext(), "Возникли проблемы с базой данных!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun onSendEmailVerification() {
        auth.currentUser!!.sendEmailVerification()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Пожалуйста, подтвердите ваш аккаунт на вашей почте!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}