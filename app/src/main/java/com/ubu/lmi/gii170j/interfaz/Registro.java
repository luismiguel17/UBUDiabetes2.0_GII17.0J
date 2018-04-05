package com.ubu.lmi.gii170j.interfaz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ubu.lmi.gii170j.BuildConfig;
import com.ubu.lmi.gii170j.R;

public class Registro extends AppCompatActivity {

    /**
     * Tag for log.
     */
    private static String TAG = Registro.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgistro);

        cargarPreferencias();
    }

    /**
     * Función que carga en los editText los datos previamente registrados si los hubiera
     */
    public final void cargarPreferencias() {
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);

        EditText nombreEt = (EditText) findViewById(R.id.et_id_nombre);
        EditText edadEt = (EditText) findViewById(R.id.et_id_edad);
        EditText estaturaEt = (EditText) findViewById(R.id.et_id_estatura);
        EditText pesoEt = (EditText) findViewById(R.id.et_id_peso);
        EditText maxEt = (EditText) findViewById(R.id.et_id_ly2_1_registro_max);
        EditText minEt = (EditText) findViewById(R.id.et_id_ly2_1_registro_min);
        EditText uds1Et = (EditText) findViewById(R.id.et_udsBasal);
        EditText uds2Et = (EditText) findViewById(R.id.et_udsRapida);
        RadioButton rapidaCheck = (RadioButton) findViewById(R.id.rb_id_rapida);
        RadioButton ultrarrapidaCheck = (RadioButton) findViewById(R.id.rb_id_ultrarrapida);

        nombreEt.setText(misPreferencias.getString(getString(R.string.nombre), ""));
        edadEt.setText(misPreferencias.getString(getString(R.string.edad), ""));
        estaturaEt.setText(misPreferencias.getString(getString(R.string.estatura), ""));
        pesoEt.setText(misPreferencias.getString(getString(R.string.peso), ""));
        minEt.setText(misPreferencias.getString(getString(R.string.min), ""));
        maxEt.setText(misPreferencias.getString(getString(R.string.max), ""));
        uds1Et.setText(misPreferencias.getString(getString(R.string.udsBasal), ""));
        uds2Et.setText(misPreferencias.getString(getString(R.string.udsRapida), ""));
        rapidaCheck.setChecked(misPreferencias.getBoolean(getString(R.string.rapida), false));
        ultrarrapidaCheck.setChecked(misPreferencias.getBoolean(getString(R.string.ultrarrapida), false));

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Preferences loaded with previous values (if exist).");
        }
    }

    /**
     * Función que registra los datos introducidos en el perfil de usuario.
     * Realiza pruebas de validación de los datos introducidos antes de guardarlos.
     */
    public void guardarperfilOnClick(View view) {

        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editorPreferencias = misPreferencias.edit();

        EditText nombreEt = (EditText) findViewById(R.id.et_id_nombre);
        EditText edadEt = (EditText) findViewById(R.id.et_id_edad);
        EditText estaturaEt = (EditText) findViewById(R.id.et_id_estatura);
        EditText pesoEt = (EditText) findViewById(R.id.et_id_peso);
        EditText maxEt = (EditText) findViewById(R.id.et_id_ly2_1_registro_max);
        EditText minEt = (EditText) findViewById(R.id.et_id_ly2_1_registro_min);
        EditText udsBasalEt = (EditText) findViewById(R.id.et_udsBasal);
        EditText udsRapidaEt = (EditText) findViewById(R.id.et_udsRapida);
        RadioButton rapidaCheck = (RadioButton) findViewById(R.id.rb_id_rapida);
        RadioButton ultrarrapidaCheck = (RadioButton) findViewById(R.id.rb_id_ultrarrapida);

        String nombre = nombreEt.getText().toString();
        String edad = edadEt.getText().toString();
        String estatura = estaturaEt.getText().toString();
        String peso = pesoEt.getText().toString();
        String max = maxEt.getText().toString();
        String min = minEt.getText().toString();
        String udsBasal = udsBasalEt.getText().toString();
        String udsRapida = udsRapidaEt.getText().toString();
        Boolean rapida = rapidaCheck.isChecked();
        Boolean ultrarrapida = ultrarrapidaCheck.isChecked();

        int minVal = Integer.parseInt(min);
        int maxVal = Integer.parseInt(max);

        if (minVal < 80 || maxVal > 250) {
            Toast.makeText(Registro.this, R.string.minmax_incorrecto, Toast.LENGTH_SHORT).show();
        } else if (minVal > maxVal) {
            Toast.makeText(Registro.this, R.string.minmax_orden, Toast.LENGTH_SHORT).show();
        } else if (nombre.length() == 0 || edad.length() == 0 || estatura.length() == 0 || peso.length() == 0 || max.length() == 0 || min.length() == 0 ||
                udsBasal.length() == 0 || udsRapida.length() == 0) {
            Toast.makeText(Registro.this, R.string.textfieldEmpty, Toast.LENGTH_SHORT).show();
        } else {
            editorPreferencias.putBoolean("primeraEjecucion", true);
            editorPreferencias.putString(getString(R.string.nombre), nombre);
            editorPreferencias.putString(getString(R.string.edad), edad);
            editorPreferencias.putString(getString(R.string.estatura), estatura);
            editorPreferencias.putString(getString(R.string.peso), peso);
            editorPreferencias.putString(getString(R.string.min), min);
            editorPreferencias.putString(getString(R.string.max), max);
            editorPreferencias.putString(getString(R.string.udsBasal), udsBasal);
            editorPreferencias.putString(getString(R.string.udsRapida), udsRapida);
            editorPreferencias.putBoolean(getString(R.string.rapida), rapida);
            editorPreferencias.putBoolean(getString(R.string.ultrarrapida), ultrarrapida);

            editorPreferencias.apply(); // changed a commy by apply by recommendation of IntelliJ

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Saved new user preferences in profile.");
                String outputValues = String.format("Nombre:%s Edad:%s Estatura:%s Peso:%s" +
                                " Min:%s Max:%s UdsBasal:%s UdsRapida:%s Rapida:%s Ultrarrapida:%s",
                        nombre, edad, estatura, peso, min, max, udsBasal, udsRapida, rapida, ultrarrapida);
                Log.d(TAG, outputValues);
            }

            finish();
        }
    }

}
