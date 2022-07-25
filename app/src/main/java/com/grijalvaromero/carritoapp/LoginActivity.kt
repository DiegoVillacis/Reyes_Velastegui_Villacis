package com.grijalvaromero.carritoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.grijalvaromero.carritoapp.adapters.ProductoAdapter
import com.grijalvaromero.carritoapp.configs.Conexion
import com.grijalvaromero.carritoapp.configs.ConexionCliente
import com.grijalvaromero.carritoapp.configs.Config
import com.grijalvaromero.carritoapp.modelos.Cliente
import com.grijalvaromero.carritoapp.modelos.Producto
import org.json.JSONObject

var clientes = ArrayList<Cliente>()
 lateinit var editTextUsuario: EditText
lateinit var editTextClave: EditText
lateinit var  cedula:String
lateinit var clave:String
lateinit var idCliente:String


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUsuario= findViewById(R.id.editTextUsuario)
        editTextClave = findViewById(R.id.editTextTextClave)





    }

    fun registro( view:View){

        var inten = Intent(this,RegistroClienteActivity::class.java)
        startActivity(inten)
    }

    fun login(view:View){
        cedula = editTextUsuario.text.toString()
        clave = editTextClave.text.toString()

        var bandera:Boolean= false

        var config = Config()
        var url = config.ipServidor+ "Cliente"
        var jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { respuesta: JSONObject ->
                var datos = respuesta.getJSONArray("data")
                for (i in 0 until datos.length()){
                    val item = datos.getJSONObject(i)
                   Log.i("Cliente",item.getString("contrasenia"))
                    if(cedula == item.getString("cedulaCli").toString() && clave == item.getString("contrasenia").toString()){
                       bandera = true

                        idCliente= item.getString("idCliente").toString()

                    }
                }
                var conexion = ConexionCliente(this)
                var  db = conexion.writableDatabase

                if (bandera){

                    db.execSQL("Insert into usuario (id_usuario) values ("+ idCliente +")")
                    var inten = Intent(this,MainActivity::class.java)
                    inten.putExtra("idCliente", "1")
                    startActivity(inten)
                }else{
                    Toast.makeText(this,"Usuario o contraseña Incorrecto",Toast.LENGTH_LONG).show()
                }

            },
            Response.ErrorListener {  },)

        val queue = Volley.newRequestQueue(this)
        queue.add(jsonObjectRequest)


    }
}