package com.team7.tikkle

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.team7.tikkle.data.ResponseNamecheck
import com.team7.tikkle.retrofit.APIS
import com.team7.tikkle.retrofit.RetrofitClient
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    private lateinit var retService: APIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


        val text_view = findViewById<TextView>(R.id.nicknameCheck)
        retService = RetrofitClient
            .getRetrofitInstance()
            .create(APIS::class.java)

        getRequestWithPathParameters(text_view)




    }

    private fun getRequestWithPathParameters(text_view: TextView) {
        val editText = findViewById<EditText>(R.id.enterNick)
        val inputText = editText.text.toString()
        val pathResponse: LiveData<Response<ResponseNamecheck>> = liveData {
            val response = retService.getAlbum("티끌")
            emit(response)

        }

        pathResponse.observe(this, Observer {
            val check = it.body()?.result
            Toast.makeText(this@EditProfileActivity, "$check", Toast.LENGTH_LONG).show()
            if (check == true){
                val text_check = findViewById<TextView>(R.id.nicknameCheck)
                text_check.setTextColor(Color.parseColor("#67C451"))
                text_check.setText("사용할 수 있는 닉네임입니다.")
            }
        })
    }

}