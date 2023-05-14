package my.edu.tarc.reward40

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import my.edu.tarc.reward40.databinding.ActivityResetPassBinding

class ResetPass : AppCompatActivity() {
    private lateinit var binding: ActivityResetPassBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fireUser: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

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
                Toast.makeText(this,"Check your email to Reset Password",Toast.LENGTH_LONG).show()
                val intent = Intent(this@ResetPass, LoginActivity::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(this,"Invalid Email, Please Enter Again!",Toast.LENGTH_LONG).show()
            }
        }

    }


}