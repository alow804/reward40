package my.edu.tarc.reward40.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import my.edu.tarc.reward40.LoginActivity
import my.edu.tarc.reward40.R
import my.edu.tarc.reward40.databinding.FragmentProfileInfoBinding
import my.edu.tarc.reward40.databinding.FragmentResetBinding


class resetFragment : Fragment() {
    private var _binding: FragmentResetBinding? = null
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentResetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.sendOTPbtn.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email = binding.emailText.text.toString().trim()

        if(email.isEmpty()){
            binding.emailText.setError("Email is Required!")
            binding.emailText.requestFocus()
            return
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if(it.isSuccessful()){
                Toast.makeText(activity,"Check your email to Reset Password", Toast.LENGTH_LONG).show()
                firebaseAuth.signOut()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(activity,"Invalid Email, Please Enter Again!", Toast.LENGTH_LONG).show()
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}