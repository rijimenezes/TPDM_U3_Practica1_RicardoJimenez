package mx.edu.ittepic.ricardojimenez.tpdm_u3_practica1_ricardojimenez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button autobuses,automoviles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autobuses = findViewById(R.id.button5);
        automoviles = findViewById(R.id.button6);

        automoviles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent automov = new Intent(MainActivity.this,Main2Activity.class);
                automov.putExtra("tipo",2);
                startActivity(automov);
            }
        });
        autobuses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent autobus = new Intent(MainActivity.this,Main2Activity.class);
                autobus.putExtra("tipo",1);
                startActivity(autobus);
            }
        });
    }
}
