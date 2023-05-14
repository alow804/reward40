package my.edu.tarc.reward40.ui.account

import android.content.ContentValues
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import my.edu.tarc.reward40.R
import my.edu.tarc.reward40.databinding.FragmentEditinfoBinding
import my.edu.tarc.reward40.databinding.FragmentProfileInfoBinding


class EditinfoFragment : Fragment() {
    private var _binding: FragmentEditinfoBinding? = null
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var user: FirebaseUser? = firebaseAuth.currentUser
    private val db = Firebase.firestore
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentEditinfoBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var userid = firebaseAuth.uid.toString()
        val profileRef = db.collection("User").document(userid)

        profileRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d(ContentValues.TAG, "get failed with ", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                    binding.contactEditInput.setText(snapshot.getString("contact"))
                    binding.addressInput.setText(snapshot.getString("address"))
            }
        }

        binding.saveBtn.setOnClickListener {
            val contact = binding.contactEditInput.text.toString()
            val address = binding.addressInput.text.toString()

            if (contact.isEmpty()||address.isEmpty()){
                Toast.makeText(activity,"Please enter the required fields!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val documentReference = db.collection("User").document(userid.toString())
            documentReference.update("contact",contact,
                "address",address).addOnSuccessListener {
                Toast.makeText(activity,"Profile Updated!",Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.navigation_profileinfo)
            }.addOnFailureListener {
                Log.e(TAG, "onFailure: ",it)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}