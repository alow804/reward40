package my.edu.tarc.reward40

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.reward40.databinding.FragmentChangepassBinding
import my.edu.tarc.reward40.databinding.FragmentProfileInfoBinding
import my.edu.tarc.reward40.ui.account.AccountViewModel
import java.util.HashMap
import java.util.Objects

class ChangepassFragment : Fragment() {
    private var _binding: FragmentChangepassBinding? = null
    private val binding get() = _binding!!
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = firebaseAuth.currentUser
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentChangepassBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forgetText.setOnClickListener {
            findNavController().navigate(R.id.navigation_reset)
        }

        binding.chgPassBtn.setOnClickListener{
            val userid = firebaseAuth.currentUser?.uid
            val newPassword = binding.newInput.text.toString().trim()
            val currentPassword = binding.currentInput.text.toString().trim()
            val confirmedPassword = binding.confirmedInput.text.toString().trim()
            if (newPassword.isEmpty()||currentPassword.isEmpty()||confirmedPassword.isEmpty()){
                Toast.makeText(activity,"Please enter the required fields!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(currentPassword != confirmedPassword){
                Toast.makeText(activity,"Password mismatch!Please enter again.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val credential = EmailAuthProvider.getCredential(user?.email.toString(),currentPassword)
            user?.reauthenticate(credential)?.addOnCompleteListener {
                if(it.isSuccessful){
                    user?.updatePassword(newPassword)?.addOnSuccessListener {
                        Toast.makeText(activity,"Password Change Successful",Toast.LENGTH_LONG).show()
                        firebaseAuth.signOut()
                        val intent = Intent(activity, LoginActivity::class.java)
                        startActivity(intent)
                    }?.addOnFailureListener {
                        Toast.makeText(activity, it.toString(),Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(activity, "Invalid Current Password!Please enter again.",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}