package com.example.codingsalad.mypage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.codingsalad.Auth.IntroActivity
import com.example.codingsalad.Auth.UserDB
import com.example.codingsalad.R
import com.example.codingsalad.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


private lateinit var binding : FragmentMypageBinding
private lateinit var auth: FirebaseAuth

class MyPageFragment: Fragment(R.layout.fragment_mypage) {
    companion object{
        private var imageUri : Uri? = null
        private val fireStorage = FirebaseStorage.getInstance().reference
        private val fireDatabase = FirebaseDatabase.getInstance().reference
        private val user = Firebase.auth.currentUser
        private val uid = user?.uid.toString()
        fun newInstance() : MyPageFragment {
            return MyPageFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.resultCode == AppCompatActivity.RESULT_OK) {
                imageUri = result.data?.data //????????? ?????? ??????
                binding.myPageImgUserProfile.setImageURI(imageUri) //????????? ?????? ??????

                //?????? ????????? ?????? ??? ????????? ????????? ??????
                fireStorage.child("userImages/$uid/photo").delete().addOnSuccessListener {
                    fireStorage.child("userImages/$uid/photo").putFile(imageUri!!).addOnSuccessListener {
                        fireStorage.child("userImages/$uid/photo").downloadUrl.addOnSuccessListener {
                            val photoUri : Uri = it
                            println("$photoUri")
                            fireDatabase.child("users/$uid/profileImageUrl").setValue(photoUri.toString())
                            Toast.makeText(requireContext(), "?????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Log.d("?????????", "??????")
            }
            else{
                Log.d("?????????", "??????")
            }
        }
    //?????? ??????????????? ???
    //?????????????????? ??????????????? ?????????????????? ??????
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //view ????????? ????????? return??? ?????? ??????????????? glide??? ????????? ??????
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)
        val photo = view?.findViewById<ImageView>(R.id.my_page_img_user_profile)
        val email = view?.findViewById<TextView>(R.id.my_page_email)
        val name = view?.findViewById<TextView>(R.id.my_page_user_name)
        val button = view?.findViewById<Button>(R.id.my_page_btn_user_profile)

        //????????? ??????
        fireDatabase.child("users").child(uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val userProfile = snapshot.getValue<UserDB>()
                println(userProfile)
                Glide.with(requireContext()).load(userProfile?.profileImageUrl)
                    .apply(RequestOptions().circleCrop())
                    .into(photo!!)
                email?.text = userProfile?.email
                name?.text = userProfile?.name
            }
        })

        button?.setOnClickListener{
            if(name?.text!!.isNotEmpty()) {
                fireDatabase.child("users/$uid/name").setValue(name.text.toString())
                name.clearFocus()
                Toast.makeText(requireContext(), "???????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        val fragmentMypageBinding = FragmentMypageBinding.bind(view)
        binding = fragmentMypageBinding

        binding.logoutBtn.setOnClickListener {
            auth.signOut()

            val intent = Intent(context, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(requireContext(), "???????????? ???????????????.", Toast.LENGTH_SHORT).show()
        }

        binding.registerOutBtn.setOnClickListener {
            val popupView: View = layoutInflater.inflate(com.example.codingsalad.R.layout.popup_report, null)
            val builder = AlertDialog.Builder(
                requireContext()
            )
            builder.setView(popupView)

            val alertDialog = builder.create()
            alertDialog.show()


            val btnInsert = popupView.findViewById<Button>(com.example.codingsalad.R.id.yesBtn)
            btnInsert.setOnClickListener {
                revokeAccess()

                val intent = Intent(context, IntroActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                Toast.makeText(requireContext(), "?????? ?????? ???????????????.", Toast.LENGTH_SHORT).show()
            }

            val btnCancel = popupView.findViewById<Button>(com.example.codingsalad.R.id.noBtn)
            btnCancel.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        binding.version.setOnClickListener {
            Toast.makeText(requireContext(), "?????? ?????? ????????? ?????????", Toast.LENGTH_SHORT).show()
        }
    }

    private fun revokeAccess() {

        val user = Firebase.auth.currentUser!!
        user.delete()
    }
}