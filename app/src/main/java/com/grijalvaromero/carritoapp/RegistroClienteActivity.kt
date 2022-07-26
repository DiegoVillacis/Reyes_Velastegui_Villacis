package com.grijalvaromero.carritoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.grijalvaromero.carritoapp.configs.Config
import com.grijalvaromero.carritoapp.databinding.ActivityRegistroClienteBinding
import org.json.JSONObject
import java.text.CharacterIterator


class RegistroClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_registro_cliente)

        val binding = ActivityRegistroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

    binding.buttonCliienteRegistrar.setOnClickListener {
        var cedula = binding.editTextClienteCedula.text.toString()
        var clave = binding.editTextClienteClave.text.toString()
        var banderaRVV:Boolean= false

        if(validarCamposRVV(binding)) {
            if(validarCedulaRVV(cedula)){
                if(validarContraseñaRVV(clave)){

                    banderaRVV= true
                }else{
                    Toast.makeText(this,"La clave de tener minimo 4 caracteres, " +
                            "mayuscula, minuscula,numero,y  caracter especial",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"Cedula incorrecta",Toast.LENGTH_LONG).show()
            }

        }else{

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Campos Incompletos")
            builder.setMessage("LLene todos los campos")
            builder.setPositiveButton("Aceptar") { dialog, which ->
            }
            builder.show()
        }

        if(banderaRVV){
            var config = Config()
            var url = config.ipServidor+ "Cliente"

            val params = HashMap<String,String>()
            params["cedulaCli"] =  binding.editTextClienteCedula.text.toString()
            params["nombreCli"] = binding.editTextClienteNombre.text.toString()
            params["apellidoCli"] = binding.editTextClienteApellido.text.toString()
            params["direccionCli"] = binding.editTextClienteDireccion.text.toString()
            params["contrasenia"] = binding.editTextClienteClave.text.toString()
            val jsonObject = JSONObject(params as Map<*, *>?)

            // Volley post request with parameters
            val request = JsonObjectRequest(
                Request.Method.POST,url, jsonObject,
                Response.Listener {
                    // Process the json
                    Toast.makeText(applicationContext, "Cliente Insertado con exito", Toast.LENGTH_LONG).show()
                    var inte = Intent(this,LoginActivity::class.java)
                    startActivity(inte)

                }, Response.ErrorListener{
                    // Error in request
                    Toast.makeText(applicationContext, "No se Inserto con exito", Toast.LENGTH_LONG).show()

                })

            request.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                // 0 means no retry
                0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            val queue = Volley.newRequestQueue(this)
            queue.add(request)

        }


    }


    }

    private fun validarCamposRVV(binding: ActivityRegistroClienteBinding): Boolean {

        if (binding.editTextClienteCedula.text.toString().equals("")) return  false
        if (binding.editTextClienteApellido.text.toString().equals("")) return  false
        if (binding.editTextClienteDireccion.text.toString().equals("")) return  false
        if (binding.editTextClienteNombre.text.toString().equals("")) return  false
        if (binding.editTextClienteClave.text.toString().equals("")) return  false

        return true

    }


    private fun validarCedulaRVV(cedulaRVV: String): Boolean {

        var cedulaCorrectaRVV = false

        try {
            if (cedulaRVV.length === 10)
            {
                val tercerDigitoRVV = cedulaRVV.substring(2, 3).toInt()

                if (tercerDigitoRVV < 6) {

                    val coefValCedulaRVV = intArrayOf(2, 1, 2, 1, 2, 1, 2, 1, 2)
                    val verificadorRVV = cedulaRVV.substring(9, 10).toInt()
                    var sumaRVV = 0
                    var digitoRVV = 0

                    for (i in 0 until cedulaRVV.length - 1) {

                        digitoRVV = cedulaRVV.substring(i, i + 1).toInt() * coefValCedulaRVV[i]
                        sumaRVV += digitoRVV % 10 + digitoRVV / 10

                    }

                    if (sumaRVV % 10 == 0 && sumaRVV % 10 == verificadorRVV) {

                        cedulaCorrectaRVV = true

                    } else if (10 - sumaRVV % 10 == verificadorRVV) {

                        cedulaCorrectaRVV = true

                    } else {

                        cedulaCorrectaRVV = false

                    }
                } else {
                    cedulaCorrectaRVV = false
                }
            } else {
                cedulaCorrectaRVV = false
            }
        } catch (nfe: NumberFormatException) {

            cedulaCorrectaRVV = false

        } catch (err: Exception) {

            cedulaCorrectaRVV = false
        }
        if (!cedulaCorrectaRVV) {
            println("La Cédula ingresada es Incorrecta")
        }
        return cedulaCorrectaRVV
    }





    private fun validarContraseñaRVV(clave: String ): Boolean{
        var mayusRVV = false;
        var minusRVV = false;
        var numRVV = false;
        var carRVV = false;
        var bRVV= false;
        var mayusCont = 0;
        var minusCont = 0;

        if(clave.length>=6 && clave.length<=10){
            for(item in clave)
            {
                Log.i("clave", item.toString())
                if(Character.isDigit(item)){
                    numRVV = true;
                }
                if(Character.isUpperCase(item)){
                    mayusCont++
                    if(mayusCont >= 2)
                        mayusRVV= true;
                }
                if(Character.isLowerCase(item)){
                    minusCont++
                    if(minusCont >= 2)
                        minusRVV=true;
                }
                if(Character.isLetterOrDigit(item)){
                    carRVV=true;
                }
            }
        }else{
            bRVV=false;
        }

        if(numRVV && minusRVV && mayusRVV && carRVV){
            bRVV=true;
        }
        return bRVV;
    }

}