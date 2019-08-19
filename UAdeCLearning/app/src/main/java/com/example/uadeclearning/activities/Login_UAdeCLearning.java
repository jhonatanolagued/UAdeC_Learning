package com.example.uadeclearning.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uadeclearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login_UAdeCLearning extends AppCompatActivity {

    private EditText userMail,userPassword;
    private Button btnLogin;
    private Button btnRegister;
    private ProgressBar loginProgress;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    private ImageView loginPhoto;
    private TextView recContrasena;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__uadec_learning);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userMail = findViewById(R.id.login_mail);
        userPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.loginBtn);
        btnRegister = findViewById(R.id.loginBtn2);
        loginProgress = findViewById(R.id.login_progress);
        mAuth = FirebaseAuth.getInstance();
        recContrasena = findViewById(R.id.login_resContraseña);
        recContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showRecoverPasswordDialog();
            }
        });
        HomeActivity = new Intent(this, com.example.uadeclearning.activities.Menu_UAdeC_Learning.class);
        loginPhoto = findViewById(R.id.loginUserPhoto);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent registerActivity = new Intent (getApplicationContext(),Registro_UAdeCLearning.class);
                startActivity(registerActivity);
                finish();
            }
        });

        loginProgress.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if(mail.isEmpty() || password.isEmpty())
                {
                    showMessage("Por favor verifica los campos");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }

                else
                {
                    singIn(mail,password);
                }

            }
        });
    }

    private void showRecoverPasswordDialog()
    {
        //Alerta dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar Contraseña");

        //Asignar el layout
        LinearLayout linearLayout = new LinearLayout(this);

        //Establecer en el diálogo im EditText
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Correo Electrónico");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //Boton recuperar
        builder.setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Entrada de email
                String email = emailEt.getText().toString().trim();
                bevingRecovery(email);
            }
        });

        //Boton cancelar
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Cancelar dialogo
                dialogInterface.dismiss();
            }
        });
        //Mostrar el dialogo
        builder.create().show();
    }

    private void bevingRecovery(String email)
    {
        pd = new ProgressDialog(this);
        pd.setMessage("Enviando correo...");
        pd.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    pd.dismiss();
                    Toast.makeText(Login_UAdeCLearning.this,"Correo enviado", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    pd.dismiss();
                    Toast.makeText(Login_UAdeCLearning.this,"Error...", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Obtiene y muestra el mensaje de error
                pd.dismiss();
                Toast.makeText(Login_UAdeCLearning.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void singIn(String mail, String password)
    {
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    updateUI();
                }

                else
                {
                    showMessage(task.getException().getMessage());
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUI()
    {
        startActivity(HomeActivity);
        finish();

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String userID = mAuth.getCurrentUser().getUid();
        mDatabase.child("Usuarios").child(userID).child("Score").setValue(0);
        mDatabase.child("Usuarios").child(userID).child("Monedas").setValue(0);

    }

    private void showMessage(String text)
    {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null)
        {
            //el usuario ya está conectado, así que tenemos que redirigirlo a la página de inicio
            updateUI();
        }

    }
}
