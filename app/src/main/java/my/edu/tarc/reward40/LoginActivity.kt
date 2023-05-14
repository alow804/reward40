package my.edu.tarc.reward40

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import my.edu.tarc.reward40.databinding.ActivityLoginBinding
import kotlin.math.sign


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@LoginActivity,gso)

        with(binding){
            btnLogin.setOnClickListener {
                var email = binding.emailText.text.toString()
                var password = binding.passwordText.text.toString()

                if(email.isNotEmpty()&&password.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                        if(it.isSuccessful){
                            email=""
                            password = ""
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            Toast.makeText(this@LoginActivity,"Welcome Back!",Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }else{
                            Toast.makeText(this@LoginActivity,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this@LoginActivity,"Please enter the required fields.",Toast.LENGTH_SHORT).show()
                }

            }

            forgotText.setOnClickListener{
                val intent = Intent(this@LoginActivity, ResetPass::class.java)
                startActivity(intent)
            }

            registerText.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            signGooglebtn.setOnClickListener {
                signInGoogle()
            }

        }


    }

    private fun signInGoogle() {
        val signIntent = googleSignInClient.signInIntent
        launcher.launch(signIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode==Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account : GoogleSignInAccount?=task.result
            if(account!=null){
                updateUI(account)
            }
        }else{
            Log.d(TAG,"Failure:",task.exception)
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }else{
                Log.d(TAG,"Failure:",it.exception)
            }
        }
    }

    override fun onStart(){
        super.onStart()
        if(firebaseAuth.currentUser !=null){
            val intent: Intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }
}