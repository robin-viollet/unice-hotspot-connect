package fr.paragoumba.unice_login

import android.accounts.AccountManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.PrintWriter
import java.net.URL
import java.util.Scanner
import javax.net.ssl.HttpsURLConnection

class App : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

//        if (intent.action == Intent.ACTION_MAIN){
//
//            sendRequest(password = "")
//            finish()
//
//        } else if (intent.action == Intent.ACTION_VIEW){

            setContentView(R.layout.activity_main)

            val button = findViewById<Button>(R.id.update_button)
            val login = findViewById<EditText>(R.id.loginText)
            val password = findViewById<EditText>(R.id.passwordText)

            button.setOnClickListener {
                sendRequest(login.text.toString(), password.text.toString())
            }

//        }
    }

    private fun sendRequest(user: String, password: String){
        lifecycleScope.launch {

            withContext(Dispatchers.IO) {
                val url = URL("https://t212.wifi.unice.fr:8003/index.php?zone=cp_hotspot")

                val urlConnection: HttpsURLConnection =
                    url.openConnection() as HttpsURLConnection

                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true

                val query = "auth_user=$user&auth_pass=$password&redirurl=&accept=Entrer"

                urlConnection.setRequestProperty("Content-Length", "${query.toByteArray().size}");

                urlConnection.outputStream.use {
                    val out = PrintWriter(it)

                    //AccountManager.get(baseContext).addAccount("unice", "password", "")

                    out.print(query)
                    out.flush()

                    val ins = urlConnection.inputStream
                    val scanner = Scanner(ins)

                    while (scanner.hasNext()) {
                        Log.d("webpage", scanner.nextLine())
                    }
                }
            }
        }
    }
}