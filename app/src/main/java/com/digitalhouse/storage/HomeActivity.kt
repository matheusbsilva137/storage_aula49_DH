package com.digitalhouse.storage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"
    private lateinit var db: FirebaseFirestore
    private lateinit var cr: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        config()

        btnSave.setOnClickListener{
            var prod = getData()
            sendProd(prod)
        }

        btnUpdate.setOnClickListener{
            var prod = getData()
            updateProd(prod)
        }

        btnDelete.setOnClickListener{
            deleteProd()
        }
        readProds()
    }

    fun config(){
        db = FirebaseFirestore.getInstance()
        cr = db.collection("produtos")
    }

    fun getData(): MutableMap<String, Any>{
        val prod: MutableMap<String, Any> = HashMap()
        val nome = edNomeProd.text.toString()

        prod["nome"] = nome
        prod["qtd"] = edQtdProd.text.toString()
        prod["preco"] = edPrecoProd.text.toString()

        return prod
    }

    fun sendProd(prod: MutableMap<String, Any>){
        val nome = edNomeProd.text.toString()

        cr.document(nome).set(prod).addOnSuccessListener {
            Log.i(TAG, "Produto cadastrado com sucesso.")
        }.addOnFailureListener{
            Log.i(TAG, it.toString())
        }
    }

    private fun updateProd(prod: MutableMap<String, Any>){
        cr.document("Caneta Preta").update(prod)
    }

    private fun deleteProd(){
        cr.document("Guitarra").delete().addOnSuccessListener {
            Log.i(TAG, "Produto deletado com sucesso.")
        }.addOnFailureListener{
            Log.i(TAG, it.toString())
        }
    }

    fun readProds(){
        cr.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            }
    }
}