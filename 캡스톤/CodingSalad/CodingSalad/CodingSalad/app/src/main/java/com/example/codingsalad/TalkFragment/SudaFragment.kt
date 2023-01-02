package com.example.codingsalad.TalkFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bokchi.mysolelife.board.BoardListLVAdapter
import com.example.codingsalad.Model.BoardModel
import com.example.codingsalad.Board.BoardWriteActivity
import com.example.codingsalad.R
import com.example.codingsalad.Utile.FBRef
import com.example.codingsalad.board.BoardInsideActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.example.codingsalad.databinding.FragmentSudaBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SudaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SudaFragment : Fragment() {
    private lateinit var binding: FragmentSudaBinding

    private val boardDataList = mutableListOf<BoardModel>()
    private val boardKeyList = mutableListOf<String>()

    private val TAG = SudaFragment::class.java.simpleName

    private lateinit var boardRVAdapter: BoardListLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_suda, container, false)




        boardRVAdapter = BoardListLVAdapter(boardDataList)
        binding.boardListView.adapter = boardRVAdapter

        binding.boardListView.setOnItemClickListener { parent, view, position, id ->
            //첫 번째 방법으로는 listview 에 있는 데이터 title content time  다 다른 액티비티로 전달해줘서 만들기

//            val intent = Intent(context,BoardInsideActivity::class.java)
//            intent.putExtra("title",boardDataList[position].title)
//            intent.putExtra("content",boardDataList[position].content)
//            intent.putExtra("time",boardDataList[position].time)
//            startActivity(intent)

            //두번째 방법으로는 firebase 에 있는 board 에 대한 데이터의 id를 기반으로 다시 데이터를 받아오는 방법
            val intent = Intent(context, BoardInsideActivity::class.java)
            intent.putExtra("key", boardKeyList[position])
            startActivity(intent)


        }


        //토크에서 연필모양 클릭시 엑티비티 바꿔줌


        binding.writeBtn.setOnClickListener {
            val intent = Intent(context, BoardWriteActivity::class.java)
            startActivity(intent)
        }

        /*binding.homeTap.setOnClickListener {   //홈텝 클릭시 어떻게 하겠다 ~
            it.findNavController().navigate(R.id.action_talkFragment_to_homeFragment)
        }

        binding.tipTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_tipFragment)
        }

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_bookmarkFragment)
        }

        binding.storeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_talkFragment_to_storeFragment)
        }*/

        getFBBoardData()

        return binding.root
    }

    private fun getFBBoardData() {

        val postListener = object : ValueEventListener {    //컨텐츠 아이디 값을 받아오고 있음 firebase에 컨텐츠들을 저장
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear()

                for (DataModel in dataSnapshot.children) {

                    Log.d(TAG, DataModel.toString())
                    //DataModel.key

                    val item = DataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(DataModel.key.toString())

                }
                boardKeyList.reverse()
                boardDataList.reverse()
                boardRVAdapter.notifyDataSetChanged()
                Log.d(TAG, boardDataList.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)

    }


}