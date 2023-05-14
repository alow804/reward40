package my.edu.tarc.reward40

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.reward40.databinding.ActivityRegisterBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStore: FirebaseFirestore
    private var db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStore = FirebaseFirestore.getInstance()

        binding.haveText.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val fullName = binding.fullNameInput.text.toString()
            val ic = binding.icInput.text.toString()
            val contactNo = binding.contactInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val confirmPass = binding.confirmedInput.text.toString()
            val address = binding.addressInput.text.toString()
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            var today: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kuala_Lumpur"))
            var registerDateMonth: LocalDate = LocalDateTime.ofInstant(today.toInstant(), today.getTimeZone().toZoneId()).toLocalDate()
            val registerDate = registerDateMonth.format(dateFormat)
            var userid =""




            if(email.isNotEmpty()&&fullName.isNotEmpty()&&ic.isNotEmpty()&&contactNo.isNotEmpty()
                &&password.isNotEmpty()&&confirmPass.isNotEmpty()) {
                if(password == confirmPass){
                        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                            if(it.isSuccessful){
                                    Toast.makeText(this,"Account Created!",Toast.LENGTH_SHORT).show()
                                    userid = firebaseAuth.uid.toString()
                                    var documentReference = firebaseStore.collection("User").document(userid)
                                    var user = User(fullName,ic,email,contactNo,800,userid,registerDate,address)
                                    documentReference.set(user).addOnSuccessListener {
                                        today.set(Calendar.DAY_OF_MONTH, today.getActualMaximum(Calendar.DATE))
                                        var ldLastDayOfMonth: LocalDate = LocalDateTime.ofInstant(today.toInstant(), today.getTimeZone().toZoneId()).toLocalDate()
                                        val strLastDayOfMonth = ldLastDayOfMonth.format(dateFormat)

                                        var id = db.collection("Reward").document().id
                                        var rewardMap = hashMapOf(
                                            "reward_ID" to id,
                                            "reward_product" to "Milo Nestle 2kg",
                                            "reward_point" to 180,
                                            "reward_status" to "Redeemable",
                                            "reward_store" to "99Speedmart",
                                            "reward_expired" to strLastDayOfMonth,
                                            "reward_redeem_date" to "",
                                            "reward_redeem_time" to "",
                                            "image" to "images/milo_nestle_2kg.png",
                                            "user_ID" to userid
                                        )

                                        db.collection("Reward").document(id)
                                            .set(rewardMap)
                                            .addOnSuccessListener { Log.d(TAG, "Success add reward") }
                                            .addOnFailureListener { e -> Log.w(TAG, "Error") }

                                        firebaseAuth.signOut()
                                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                    }.addOnFailureListener {
                                        Log.d("TAG", "On failure:"+it.message.toString())
                                    }
                            }else{
                                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                            }
                        }
                }else{
                    Toast.makeText(this,"Password is not matching, please enter again.",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Please enter the required fields.",Toast.LENGTH_SHORT).show()
            }




        }
    }
}