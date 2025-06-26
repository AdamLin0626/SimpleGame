package com.example.simplegame

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class GuessNumber_GmaePage : AppCompatActivity() {
    //定義元件
    private lateinit var inputGuessNumber: EditText
    private lateinit var reCheck_Button: Button
    private lateinit var reStart_Button: Button
    private lateinit var HistoryList: TextView
    private lateinit var HistoryList_Scroll: ScrollView

    private lateinit var secret: String
    private val historyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.guessnumber_gamepage)

        inputGuessNumber = findViewById(R.id.inputGuessNumber)
        reCheck_Button = findViewById(R.id.reCheck_Button)
        reStart_Button = findViewById(R.id.reStart_Button)
        HistoryList = findViewById(R.id.HistoryList)
        HistoryList_Scroll = findViewById(R.id.HistoryList_Scroll)

        startNewGame()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        reCheck_Button.setOnClickListener {
            val Input4Number = getString(R.string.input_four_number)
            val guess = inputGuessNumber.text.toString()
            if (!isValidGuess(guess)) {
                Toast.makeText(this, Input4Number, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val (a, b) = calculateAB(secret, guess)
            val result = "$guess → ${a}A${b}B"
            val Congratulations = getString(R.string.congratulations, secret)
            historyList.add(result)
            updateHistory()

            if (a == 4) {
                Toast.makeText(this, Congratulations, Toast.LENGTH_LONG).show()
                reCheck_Button.isEnabled = false
            }
            inputGuessNumber.text.clear()
        }

        reStart_Button.setOnClickListener {
            startNewGame()
        }
    }

    private fun startNewGame() {
        secret = generateSecretNumber()
        historyList.clear()
        updateHistory()
        reCheck_Button.isEnabled = true
        inputGuessNumber.text.clear()
        Toast.makeText(this, "遊戲重新開始！請輸入新猜測", Toast.LENGTH_SHORT).show()
    }

    private fun updateHistory() {
        HistoryList.text = historyList.joinToString("\n")
        // 滾動到底部
        HistoryList_Scroll.post { HistoryList_Scroll.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    private fun generateSecretNumber(): String {
        val digits = mutableListOf<Char>()
        while (digits.size < 4) {
            val c = Random.nextInt(0, 10).digitToChar()
            if (c !in digits) digits.add(c)
        }
        return digits.joinToString("")
    }

    private fun isValidGuess(guess: String): Boolean {
        if (guess.length != 4) return false
        if (!guess.all { it.isDigit() }) return false
        if (guess.toSet().size != 4) return false
        return true
    }

    private fun calculateAB(secret: String, guess: String): Pair<Int, Int> {
        var a = 0
        var b = 0
        for (i in 0..3) {
            if (guess[i] == secret[i]) {
                a++
            } else if (guess[i] in secret) {
                b++
            }
        }
        return Pair(a, b)
    }
}
