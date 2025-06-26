package com.example.simplegame

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TicTacToe_GamePage : AppCompatActivity() {

    private lateinit var PlayZone: GridLayout
    private lateinit var ResetButton: Button

    private var isPlayerOneTurn = true // true 為 O，false 為 X
    private var isSinglePlayer = true  // 是否為單人模式
    private var gameOver = false       // 勝負已分，禁用操作

    private val board = Array(3) { Array(3) { "" } }
    private val buttons = Array(3) { arrayOfNulls<Button>(3) }
    private val moveHistory = mutableListOf<Pair<Int, Int>>() // 記錄下棋順序

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.tictactoe_gamepage)

        PlayZone = findViewById(R.id.PlayZone)
        ResetButton = findViewById(R.id.ResetButton)

        showModeSelectDialog() // 選擇 1P / 2P
        createBoard()          // 動態建立按鈕

        ResetButton.setOnClickListener {
            resetGame()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // 選擇模式：1P or 2P
    private fun showModeSelectDialog() {
        val HowManyPlayer = getString(R.string.how_many_player_text)
        AlertDialog.Builder(this)
            .setTitle(HowManyPlayer)
            .setMessage("")
            .setPositiveButton("1P（對電腦）") { _, _ ->
                isSinglePlayer = true
                isPlayerOneTurn = true
            }
            .setNegativeButton("2P（雙人遊戲）") { _, _ ->
                isSinglePlayer = false
                isPlayerOneTurn = true
            }
            .setCancelable(false)
            .show()
    }

    // 建立 3x3 棋盤
    private fun createBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                val button = Button(this).apply {
                    textSize = 32f
                    setOnClickListener { handleMove(i, j) }
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 200
                        height = 200
                        setMargins(4, 4, 4, 4)
                    }
                }
                buttons[i][j] = button
                PlayZone.addView(button)
            }
        }
    }

    // 玩家或電腦下棋
    private fun handleMove(row: Int, col: Int, isComputer: Boolean = false) {
        if (board[row][col].isNotEmpty() || gameOver) return

        val symbol = if (isPlayerOneTurn) "O" else "X"
        board[row][col] = symbol
        buttons[row][col]?.text = symbol
        moveHistory.add(row to col)

        if (checkWin(symbol)) {
            Toast.makeText(this, "$symbol 獲勝！", Toast.LENGTH_SHORT).show()
            disableAllButtons()
            gameOver = true
            return
        }

        // 移除最早的棋子（滿格時）
        if (moveHistory.size > 9) {
            val (oldRow, oldCol) = moveHistory.removeAt(0)
            board[oldRow][oldCol] = ""
            buttons[oldRow][oldCol]?.text = ""
        }

        // 換回合
        if (isSinglePlayer) {
            // 單人模式下，O 為玩家，X 為電腦
            if (!isComputer) {
                isPlayerOneTurn = false
                Handler(Looper.getMainLooper()).postDelayed({
                    makeComputerMove()
                }, 500)
            } else {
                isPlayerOneTurn = true
            }
        } else {
            // 雙人模式，自動切換
            isPlayerOneTurn = !isPlayerOneTurn
        }
    }

    // 電腦出手邏輯
    private fun makeComputerMove() {
        if (gameOver) return

        // 優先找能贏的步
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].isEmpty()) {
                    board[i][j] = "X"
                    if (checkWin("X")) {
                        board[i][j] = ""
                        handleMove(i, j, isComputer = true)
                        return
                    }
                    board[i][j] = ""
                }
            }
        }

        // 否則亂下
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].isEmpty()) {
                    emptyCells.add(i to j)
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val (r, c) = emptyCells.random()
            handleMove(r, c, isComputer = true)
        }
    }

    // 勝利條件檢查
    private fun checkWin(symbol: String): Boolean {
        for (i in 0..2) {
            if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) ||
                (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)) return true
        }
        if ((board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
            (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)) return true
        return false
    }

    // 勝利後禁止操作
    private fun disableAllButtons() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]?.isEnabled = false
            }
        }
    }

    // 重置遊戲
    private fun resetGame() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = ""
                buttons[i][j]?.apply {
                    text = ""
                    isEnabled = true
                }
            }
        }
        isPlayerOneTurn = true
        gameOver = false
        moveHistory.clear()
        showModeSelectDialog()
    }
}
