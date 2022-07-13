package com.kls.easyenergy.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.kls.easyenergy.R
import com.kls.easyenergy.model.User
import com.kls.easyenergy.databinding.ActivityLoginBinding
import com.kls.easyenergy.model.LoginResponse
import com.kls.easyenergy.utils.UserPreference
import com.kls.easyenergy.viewmodel.LoginViewModel
import com.kls.easyenergy.viewmodel.ViewModelFactory
import com.kls.jnecourierapps.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var activityLoginBinding: ActivityLoginBinding

    companion object {
        private const val TAG = "Login Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        setupViewModel()

        activityLoginBinding.btnLogin.setOnClickListener {

            val inputUsername = activityLoginBinding.edtUsername.text.toString()
            val inputPassword = activityLoginBinding.edtPassword.text.toString()


            accessAccount(inputUsername, inputPassword)
        }

        activityLoginBinding.txtSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            if(user.isLogin) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    private fun accessAccount(inputUsername: String, inputPassword: String) {
        showLoading(true)

        val client = ApiConfig.getApiService().login(inputUsername, inputPassword)
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                showLoading(false)
                val responseBody = response.body()
                Log.d(TAG, "onResponse: $responseBody")
                if(response.isSuccessful && responseBody?.status == "success") {
                    loginViewModel.saveUser(User(responseBody.dataLogin.accessToken.toString(), true))
                    Toast.makeText(this@LoginActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                    Toast.makeText(this@LoginActivity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure2: ${t.message}")
                Toast.makeText(this@LoginActivity, getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            activityLoginBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityLoginBinding.progressBar.visibility = View.GONE
        }
    }


}