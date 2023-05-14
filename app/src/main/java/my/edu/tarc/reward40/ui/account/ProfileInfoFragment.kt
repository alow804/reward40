package my.edu.tarc.reward40.ui.account

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.reward40.R
import my.edu.tarc.reward40.databinding.FragmentAccountBinding
import my.edu.tarc.reward40.databinding.FragmentProfileInfoBinding


class ProfileInfoFragment : Fragment(){
    private var _binding: FragmentProfileInfoBinding? = null
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var user: FirebaseUser? = firebaseAuth.currentUser
    private val db = Firebase.firestore
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentProfileInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userid = firebaseAuth.uid.toString()
        val profileRef = db.collection("User").document(userid)

        profileRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d(TAG, "get failed with ", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                binding.fullNameText.setText(snapshot.getString("fullname"))
                binding.icText.setText(snapshot.getString("ic"))
                binding.emailTextView.setText(user?.email.toString())
                binding.contactNoText.setText(snapshot.getString("contact"))
                binding.registeredDateText.setText(snapshot.getString("registeredDate"))
                binding.addressText.setText(snapshot.getString("address"))
            }
        }

        binding.changeText.setOnClickListener {
            findNavController().navigate(R.id.navigation_chgpass)
        }

        binding.editImage.setOnClickListener {
            findNavController().navigate(R.id.navigation_edit)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}