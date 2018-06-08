package br.com.voobex.todolist.auth

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_reset_password.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.text.TextUtils
import br.com.voobex.todolist.R


class ResetPasswordActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        mAuth = FirebaseAuth.getInstance()

        btnResetPassword.setOnClickListener {
            val email = edtResetEmail.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, R.string.enterEmail, Toast.LENGTH_SHORT).show()
            } else {
                mAuth!!.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@ResetPasswordActivity, R.string.checkEmail, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@ResetPasswordActivity, R.string.sendFailEmailReset, Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
