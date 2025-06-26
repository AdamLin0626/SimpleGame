package com.example.simplegame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class Lobby : AppCompatActivity() {
    //定義元件參數
    private lateinit var TicTacToe_GameBtn: Button
    private lateinit var GuessNumber_GmaeBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_lobby)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //定義按鈕
        TicTacToe_GameBtn = findViewById(R.id.TicTacToe_GameBtn)
        GuessNumber_GmaeBTN = findViewById(R.id.GuessNumber_GameBtn)
        //轉跳
        val BtnListener = View.OnClickListener { WhichBtn ->
            when(WhichBtn.id){
                R.id.TicTacToe_GameBtn -> startActivity(Intent(this, TicTacToe_GamePage ::class.java))
                R.id.GuessNumber_GameBtn -> startActivity(Intent(this, GuessNumber_GmaePage ::class.java))
            }
        }
        TicTacToe_GameBtn.setOnClickListener(BtnListener)
        GuessNumber_GmaeBTN.setOnClickListener(BtnListener)
    }
}