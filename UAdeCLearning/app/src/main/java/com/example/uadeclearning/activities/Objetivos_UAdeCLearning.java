package com.example.uadeclearning.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.uadeclearning.R;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class Objetivos_UAdeCLearning extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      //setContentView(R.layout.activity_objetivos__uade_clearning);
        setPrevText("Atras");
        setNextText("Siguiente");
        setFinishText("Empezar");
        setCancelText("Cancelar");

        addFragment(new Step.Builder().setTitle("Perfil")
                .setContent("¡Construye y presenta tu perfil único de programador en la App!")
                .setBackgroundColor(Color.parseColor("#ffc107"))
                .setDrawable(R.drawable.screen_1).build());

        addFragment(new Step.Builder().setTitle("Aprende")
                .setContent("Todos los temas de la carrera de ing en sistemas computacionales reunidos en una sola aplicación")
                .setSummary("Continua y aprende más sobre App")
                .setBackgroundColor(Color.parseColor("#00bcd4"))
                .setDrawable(R.drawable.screen_3).build());

        addFragment(new Step.Builder().setTitle("Compite")
                .setContent("Compite para saber quien es el mejor programador dentro de la aplicacion")
                .setSummary("Continua y aprende más sobre App")
                .setBackgroundColor(Color.parseColor("#8bc34a"))
                .setDrawable(R.drawable.screen_4).build());
    }


    public void finishTutorial() {
        Intent moveToLogin = new Intent(Objetivos_UAdeCLearning.this,Login_UAdeCLearning.class);
        startActivity(moveToLogin);
    }

    @Override
    public void currentFragmentPosition(int position) {

    }
}
