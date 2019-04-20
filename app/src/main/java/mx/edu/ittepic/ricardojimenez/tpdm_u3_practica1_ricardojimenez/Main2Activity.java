package mx.edu.ittepic.ricardojimenez.tpdm_u3_practica1_ricardojimenez;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    int tipo;
    TextView etiqueta;
    EditText marca,modelo,serie,fecha,id;
    Button insertar,consultar,eliminar,actualizar;
    ListView lista;
    List<Vehiculo> datosConsulta;
    DatabaseReference servicioConcencionaria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        id= findViewById(R.id.id);
        etiqueta = findViewById(R.id.textView);
        marca = findViewById(R.id.editText);
        modelo = findViewById(R.id.editText2);
        serie = findViewById(R.id.editText3);
        fecha = findViewById(R.id.editText4);

        insertar = findViewById(R.id.button);
        consultar = findViewById(R.id.button2);
        eliminar = findViewById(R.id.button3);
        actualizar = findViewById(R.id.button4);
        lista = findViewById(R.id.lista);

        Bundle dato = getIntent().getExtras();
        tipo = dato.getInt("tipo");
        String tipoEtiqueta = tipo == 1 ? "AUTOBUSES":"AUTOMOVILES";
        etiqueta.setText("CONCENSIONARIO DE "+ tipoEtiqueta);
        servicioConcencionaria = FirebaseDatabase.getInstance().getReference();

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accion("insertar",tipo==1?"autobuses":"automoviles");
            }
        });

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accion("consultar",tipo==1?"autobuses":"automoviles");
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accion("eliminar",tipo==1?"autobuses":"automoviles");
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accion("actualizar",tipo==1?"autobuses":"automoviles");
            }
        });
    }

    private void accion(String tarea, final String rama) {
        switch (tarea){
            case "insertar":
                if(id.getText().toString().equals("")||modelo.getText().toString().equals("")||
                    serie.getText().toString().equals("")||
                    marca.getText().toString().equals("")||
                    fecha.getText().toString().equals("")){
                    mensaje("Error","DEBE LLENAR TODOS LOS CAMPOS");
                    return;
                }
              inserta(rama,
                       new Vehiculo(id.getText().toString(),modelo.getText().toString(),
                               serie.getText().toString(),
                               marca.getText().toString(),
                               fecha.getText().toString()),id.getText().toString());
                break;
            case "consultar":
                consultarVehiculo(rama);
                break;
            case "eliminar":
                final EditText idVehiculo = new EditText(this);
                idVehiculo.setHint("ID DEL VEHICULO A ELIMINAR");
                AlertDialog.Builder alerta = new AlertDialog.Builder(this);
                alerta.setTitle("ATENCION!").setMessage("BUSCAR ID:").setView(idVehiculo).setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarVehiculo(rama,idVehiculo.getText().toString());
                    }
                }).setNegativeButton("Cancelar", null).show();
                break;
            case "actualizar":
                final EditText idV = new EditText(this);
                idV.setHint("ID DEL VEHICULO A ELIMINAR");
                AlertDialog.Builder al = new AlertDialog.Builder(this);
                al.setTitle("ATENCION!").setMessage("BUSCAR ID:").setView(idV).setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actualizarVehiculo(rama,idV.getText().toString());
                    }
                }).setNegativeButton("Cancelar", null).show();
                break;
        }
    }
    private void actualizarVehiculo(final String rama,final String id) {
        servicioConcencionaria.child(rama).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Vehiculo v = dataSnapshot.getValue(Vehiculo.class);

                if(v!=null){
                    final View datosActualizar = getLayoutInflater().inflate(R.layout.interfaz_actualizar,null);
                    final EditText marc = datosActualizar.findViewById(R.id.editText5);
                    final EditText mod = datosActualizar.findViewById(R.id.editText6);
                    final EditText ser = datosActualizar.findViewById(R.id.editText7);
                    final EditText fec = datosActualizar.findViewById(R.id.editText8);
                    mod.setText(v.modelo);
                    marc.setText(v.marca);
                    ser.setText(v.serie);
                    fec.setText(v.fechaLanzamiento);
                    AlertDialog.Builder al = new AlertDialog.Builder(Main2Activity.this);
                    al.setTitle("ATENCION ACTUALIZANDO "+v.marca+" "+v.modelo).setView(datosActualizar)
                            .setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    inserta(rama,new Vehiculo(id,mod.getText().toString(),ser.getText().toString(),marc.getText().toString(),fec.getText().toString()),id);
                                }
                            }).setNegativeButton("Cancelar",null).show();
                }else{
                    mensaje("Atencion","No se encontro el vehiculo!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void eliminarVehiculo(final String rama, final String id) {
        servicioConcencionaria.child(rama).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Vehiculo v = dataSnapshot.getValue(Vehiculo.class);
                if(v!=null){

                    AlertDialog.Builder al = new AlertDialog.Builder(Main2Activity.this);
                    al.setTitle("ATENCION").setMessage("Esta seguro de eliminar el vehiculo\n"+ v.marca+" "+v.modelo)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    servicioConcencionaria.child(rama).child(id).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mensaje("EXITO","Se elimino el vehiculo \n"+v.marca+" "+v.modelo);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mensaje("ERROR!","No se logro eliminar "+e.getMessage());
                                        }
                                    });
                                }
                            }).setNegativeButton("Cancelar",null).show();
                }else{
                    mensaje("Atencion","No se encontro el vehiculo!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void consultarVehiculo(final String rama) {
        servicioConcencionaria.child(rama).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datosConsulta = new ArrayList<>();
                for(final DataSnapshot snap : dataSnapshot.getChildren()) {
                    servicioConcencionaria.child(rama).child(snap.getKey())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Vehiculo auto = dataSnapshot.getValue(Vehiculo.class);
                                        if (auto != null) {
                                            datosConsulta.add(auto);
                                        }
                                    crearListView();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void crearListView() {
        String[] nombre ={""};
        ArrayAdapter<String> adaptador;
        if(datosConsulta.size()<=0) {
            lista = new ListView(this);
            return;
        }
        nombre = new String[datosConsulta.size()];
        for(int i=0;i<nombre.length;i++){
            Vehiculo j = datosConsulta.get(i);
            nombre[i] = j.id+" - "+j.marca+"\n"+j.modelo;
        }
        adaptador = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,nombre);
        lista.setAdapter(adaptador);
    }

    private void inserta(final String rama,Vehiculo datos,String ide) {
        servicioConcencionaria.child(rama).child(ide)
                .setValue(datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mensaje("Exito ","Se completo la operacion ");
                        id.setText("");
                        marca.setText("");modelo.setText("");serie.setText("");
                        fecha.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mensaje("ERROR","No se logro completar la operacion ");
                    }
                });
    }
    private void mensaje(String title, String message) {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle(title).setMessage(message).setPositiveButton("OK",null).show();
    }
}
