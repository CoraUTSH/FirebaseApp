package com.example.firebaseapp.Screens
import android.content.ContentValues.TAG

import android.util.Log

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


val database=FirebaseDatabase.getInstance()
val dataref= database.reference.child("Contactos")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    var name by rememberSaveable {mutableStateOf("")}
    var email by rememberSaveable {mutableStateOf("")}
    var phone by rememberSaveable {mutableStateOf("")}
    var datos by rememberSaveable {mutableStateOf(listOf<Contacto>())}
    Text(text = "Agregar usuarios Firebase")
    Spacer(modifier =Modifier.height(15.dp) )
    TextField(
        value = name, onValueChange = { name = it },
        label = { Text(text = "Inserta tu nombre") },
        placeholder = { Text(text = "Juan Torres Torres") },
        singleLine = true,
    )
    Spacer(modifier =Modifier.height(15.dp) )
    TextField(value = email, onValueChange = {email=it},
        label={ Text(text = "Inserta tu email")},
        placeholder = {Text(text="Juan2T@gmail.com")},
        singleLine = true,
    )
    Spacer(modifier =Modifier.height(15.dp) )
    TextField(value = phone, onValueChange = {phone=it},
        label={ Text(text = "Número Celular")},
        singleLine = true,
    )
    Spacer(modifier =Modifier.height(15.dp) )

    Button(onClick = {
        agregarDatos(name,email,phone)



    }) {
        Text(text = "Enviar")
    }
    Spacer(modifier =Modifier.height(15.dp) )
    val context= LocalContext.current

    Button(onClick = {

//verDatos()
       val postListener = object : ValueEventListener {
           override fun onDataChange(dataSnapshot: DataSnapshot) {
               // Get Post object and use the values to update the UI
               //val post = dataSnapshot.child().getValue()
               //Log.d("","$dataSnapshot")
               // ...
               var dat= listOf<Contacto>()
               if(dataSnapshot.exists()){
                for (user in dataSnapshot.children){
                    val nombre=user.child("name").getValue(String::class.java)
                    val correo=user.child("email").getValue(String::class.java)
                    val tel=user.child("phone").getValue(String::class.java)

                    dat=dat+(Contacto("$nombre","$correo","$tel"))

                }
                   datos=dat
                   Log.d("","Prueba ${datos.size}")
            }
           }



           override fun onCancelled(databaseError: DatabaseError) {
               // Getting Post failed, log a message
               Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
           }
       }
       dataref.addValueEventListener(postListener)
        //Toast.makeText(context,"$postListener",Toast.LENGTH_SHORT).show()
        Log.d("","$postListener")
        //for (inf in datos){
        //}



    }) {
        Text(text = "Ver Contactos")
    }

    datos.forEach{contacto->
        DatosUsers(contacto.name+"prueba", contacto.email+"OSO", contacto.phone)
    }
}

@Composable
fun DatosUsers(name:String, tel:String, email:String){
    Text(text = "$name $tel $email")
}
fun agregarDatos(name:String,email:String,phone: String){
    val key= database.reference.push().key //Definir mi clave del registro
    val contact=Contacto(name,email,phone)
    dataref.child(key.toString()).setValue(contact)//Agregando a la BD
}
/*fun verDatos(){

    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            //val post = dataSnapshot.child().getValue()
            //Log.d("","$dataSnapshot")
            // ...


        }



        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }
    dataref.addValueEventListener(postListener)
    //Toast.makeText(context,"$postListener",Toast.LENGTH_SHORT).show()
    Log.d("","$postListener")

}*/

data class Contacto(val name:String,val email:String,val phone:String)

@Preview(showSystemUi = true)
@Composable
fun PreviewScreen(){
    HomeScreen()
}




