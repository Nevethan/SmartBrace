package com.example.nevethan.smartbrace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
    The Main class validates your login inputs and determines whether you have access or not.
    It also have the duty to register a new Member, which leads to another Activity.
 */
public class MainActivity extends AppCompatActivity {


    DatabaseManager dbManager = new DatabaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view){

        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        String userInput = username.getText().toString();
        String passInput = password.getText().toString();

        String pass = dbManager.searchDb(userInput);

        //Checks whether the EditTexts are blank or not. If they are, the uer will be warned
        if(!userInput.isEmpty() && !passInput.isEmpty()){
            if(passInput.equals(pass)){
                Intent intent = new Intent(this, Options.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "The Username or Password is wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Please Enter Username and Password", Toast.LENGTH_SHORT).show();
        }
    }

    //Action to start the Activity_sign_up
    public void register(View view){
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }
}
