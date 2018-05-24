package com.ubu.lmi.gii170j.interfaz;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ubu.lmi.gii170j.BuildConfig;
import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.persistencia.DataBaseManager;

import java.io.File;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Registro extends AppCompatActivity {

    //Tag for log.
    private static String TAG = Registro.class.getName();

    EditText nombreEt;
    EditText edadEt;
    EditText estaturaEt;
    EditText pesoEt;
    EditText maxEt;
    EditText minEt;
    EditText udsBasalEt;
    EditText udsRapidaEt;
    RadioButton rapidaCheck;
    RadioButton ultrarrapidaCheck;
    RadioButton decimal_BC_ceroCheck;
    RadioButton decimal_BC_dosCheck;
    RadioButton decimal_BC_tresCheck;

    RadioGroup radioGroup_insulinaBolo;
    RadioGroup radioGroup_decimalesBolo;
    ImageView imagePerfil;
    String path;
    String final_path = "";
    String final_bitmap="";

    Bitmap bitmap;
    //private final String CARPETA_RAIZ="ImagenesUBUDiabetes/";
    //private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";
    final int COD_SELECCIONA=10;
    //final int COD_FOTO=20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgistro);

        nombreEt = (EditText) findViewById(R.id.et_id_nombre);
        edadEt = (EditText) findViewById(R.id.et_id_edad);
        estaturaEt = (EditText) findViewById(R.id.et_id_estatura);
        pesoEt = (EditText) findViewById(R.id.et_id_peso);
        maxEt = (EditText) findViewById(R.id.et_id_ly2_1_registro_max);
        minEt = (EditText) findViewById(R.id.et_id_ly2_1_registro_min);
        udsBasalEt = (EditText) findViewById(R.id.et_udsBasal);
        udsRapidaEt = (EditText) findViewById(R.id.et_udsRapida);
        rapidaCheck = (RadioButton) findViewById(R.id.rb_id_rapida);
        ultrarrapidaCheck = (RadioButton) findViewById(R.id.rb_id_ultrarrapida);
        decimal_BC_ceroCheck = (RadioButton) findViewById(R.id.rb_id_cero);
        decimal_BC_dosCheck = (RadioButton) findViewById(R.id.rb_id_uno);
        decimal_BC_tresCheck = (RadioButton) findViewById(R.id.rb_id_dos);

        radioGroup_insulinaBolo = (RadioGroup) findViewById(R.id.rg_id_profile);
        radioGroup_decimalesBolo = (RadioGroup) findViewById(R.id.rg2_id_profile);



        cargarPreferencias();
        /**
         * Nuevo-Para la carga de la imagen-Perfil
         */
        imagePerfil = (ImageView)findViewById(R.id.imgV2_fragmentperfil);


        if(validaPermisos()){
            imagePerfil.setEnabled(true);
        }else{
            imagePerfil.setEnabled(false);
        }

    }

    /**
     * Función que carga en los editText los datos previamente registrados si los hubiera
     */
    public final void cargarPreferencias() {
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);


        EditText nombreEt2 = (EditText) findViewById(R.id.et_id_nombre);
        EditText edadEt2 = (EditText) findViewById(R.id.et_id_edad);
        EditText estaturaEt2 = (EditText) findViewById(R.id.et_id_estatura);
        EditText pesoEt2 = (EditText) findViewById(R.id.et_id_peso);
        EditText maxEt2 = (EditText) findViewById(R.id.et_id_ly2_1_registro_max);
        EditText minEt2 = (EditText) findViewById(R.id.et_id_ly2_1_registro_min);
        EditText uds1Et2 = (EditText) findViewById(R.id.et_udsBasal);
        EditText uds2Et2 = (EditText) findViewById(R.id.et_udsRapida);
        RadioButton rapidaCheck2 = (RadioButton) findViewById(R.id.rb_id_rapida);
        RadioButton ultrarrapidaCheck2 = (RadioButton) findViewById(R.id.rb_id_ultrarrapida);
        RadioButton decimal_BC_ceroCheck2 = (RadioButton) findViewById(R.id.rb_id_cero);
        RadioButton decimal_BC_dosCheck2 = (RadioButton) findViewById(R.id.rb_id_uno);
        RadioButton decimal_BC_tresCheck2 = (RadioButton) findViewById(R.id.rb_id_dos);
        //Nuevo-Para la imagen de perfil
        ImageView image_Profile2 = (ImageView) findViewById(R.id.imgV2_fragmentperfil);


        nombreEt2.setText(misPreferencias.getString(getString(R.string.nombre), ""));
        edadEt2.setText(misPreferencias.getString(getString(R.string.edad), ""));
        estaturaEt2.setText(misPreferencias.getString(getString(R.string.estatura), ""));
        pesoEt2.setText(misPreferencias.getString(getString(R.string.peso), ""));
        minEt2.setText(misPreferencias.getString(getString(R.string.min), ""));
        maxEt2.setText(misPreferencias.getString(getString(R.string.max), ""));
        uds1Et2.setText(misPreferencias.getString(getString(R.string.udsBasal), ""));
        uds2Et2.setText(misPreferencias.getString(getString(R.string.udsRapida), ""));
        rapidaCheck2.setChecked(misPreferencias.getBoolean(getString(R.string.rapida), false));
        ultrarrapidaCheck2.setChecked(misPreferencias.getBoolean(getString(R.string.ultrarrapida), false));
        decimal_BC_ceroCheck2.setChecked(misPreferencias.getBoolean(getString(R.string.decimal_bc_cero),false));
        decimal_BC_dosCheck2.setChecked(misPreferencias.getBoolean(getString(R.string.decimal_bc_dos),false));
        decimal_BC_tresCheck2.setChecked(misPreferencias.getBoolean(getString(R.string.decimal_bc_tres),false));
        //nuevo-Para la imagen de perfil
        Uri uri = Uri.parse(misPreferencias.getString(getString(R.string.image_perfil), ""));
        image_Profile2.setImageURI(uri);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Preferences loaded with previous values (if exist).");
        }
    }

    /**
     * validaPermisos. Metodo que comprueba si los permisos estan validados.
     * @return
     */
    private boolean validaPermisos() {
        //Comprobamos la version de nuestro dispositivo.
        // M --> Marshmallow
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        //Comprobamos los permisos de Camara y Escritura en SD estan Habilitados.
        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        //Comprobamos si se deben solicitar los permisos de Camara y Escritura en SD.
        if((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                imagePerfil.setEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }
    }

    /**
     * solicitarPermisosManual. Metodo que vuelve a solicitar una configuracion de permisos de forma manual.
     */
    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"Sí","No"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registro.this);
        alertDialogBuilder.setTitle("Configuración Manual");
        alertDialogBuilder.setMessage("¿Desea configurar los permisos de forma manual?");
        alertDialogBuilder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Sí")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos NO fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertDialogBuilder.show();
    }

    /**
     * cargarDialogoRecomendacion. Metodo que carga un dialogo para asiganar permisos a la app.
     */
    private void cargarDialogoRecomendacion() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registro.this);
        alertDialogBuilder.setTitle("Permisos Desactivados");
        alertDialogBuilder
                .setMessage("Aceptar los permisos para el correcto funcionamiento de la App")
                .setCancelable(false)
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            //@RequiresApi(api = Build.VERSION_CODES.M)
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
                            }
                        });

        //AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.show();
    }



    public void clickImage(View view) {
        loadImage();
    }

    public void loadImage(){
        final CharSequence[] opciones={"Cargar Imagen","Cancelar"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registro.this);
        alertDialogBuilder.setTitle("Seleccione una Opción");
        alertDialogBuilder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
        });
        alertDialogBuilder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri miPath=data.getData();
            imagePerfil.setImageURI(miPath);
            final_path = miPath.toString();
            imagePerfil.setBackgroundResource(R.color.transparent);
        }
    }

    /**
     * Función que registra los datos introducidos en el perfil de usuario.
     * Realiza pruebas de validación de los datos introducidos antes de guardarlos.
     */
    public void guardarperfilOnClick(View view) {

        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editorPreferencias = misPreferencias.edit();

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
        //Nuevo Spinner decimales Bolo Corrector
        Boolean bc_cero = decimal_BC_ceroCheck.isChecked();
        Boolean bc_dos = decimal_BC_dosCheck.isChecked();
        Boolean bc_tres = decimal_BC_tresCheck.isChecked();
        //Nuevo- Para la imagen de perfil
        String path_image = final_path;

        int minVal = Integer.parseInt(min);
        int maxVal = Integer.parseInt(max);

        //Comprobaciones de los valores introducidos
        if (minVal < 80 || maxVal > 250) {
            Toast.makeText(Registro.this, R.string.minmax_incorrecto, Toast.LENGTH_SHORT).show();
        } else if (minVal > maxVal) {
            Toast.makeText(Registro.this, R.string.minmax_orden, Toast.LENGTH_SHORT).show();
        } else if (nombre.length() == 0 || edad.length() == 0 || estatura.length() == 0 || peso.length() == 0 || max.length() == 0 || min.length() == 0 ||
                udsBasal.length() == 0 || udsRapida.length() == 0) {
            Toast.makeText(Registro.this, R.string.textfieldEmpty, Toast.LENGTH_SHORT).show();
        } else if (radioGroup_insulinaBolo.getCheckedRadioButtonId() == -1 || radioGroup_decimalesBolo.getCheckedRadioButtonId() == -1){
            Toast.makeText(Registro.this, R.string.noRadioButtonsChecked, Toast.LENGTH_SHORT).show();
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
            editorPreferencias.putBoolean(getString(R.string.decimal_bc_cero), bc_cero);
            editorPreferencias.putBoolean(getString(R.string.decimal_bc_dos), bc_dos);
            editorPreferencias.putBoolean(getString(R.string.decimal_bc_tres), bc_tres);
            //Nuevo-Para la imagen de perfil
            editorPreferencias.putString(getString(R.string.image_perfil),path_image);

            editorPreferencias.apply();

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Saved new user preferences in profile.");
                String outputValues = String.format("Nombre:%s Edad:%s Estatura:%s Peso:%s" +
                                " Min:%s Max:%s UdsBasal:%s UdsRapida:%s Rapida:%s Ultrarrapida:%s",
                        nombre, edad, estatura, peso, min, max, udsBasal, udsRapida, rapida, ultrarrapida);
                Log.d(TAG, outputValues);
            }
            //Comprobamos si la tabla alimento ya existe
            if (!misPreferencias.getBoolean("tablaAlimentos", false)) {
                //Llamar a la AsyncTask aqui:
                new RegistroAlimentos().execute(nombre);
            }
            finish();
        }

    }

    class RegistroAlimentos extends AsyncTask<String,Void,String> {
        private String[] tipoAlimento;
        private String[] numeroTipoAlimento;
        private String[] alimento;
        private String[] indicesGlucemico;
        private String[] raciones;

        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editorPreferencias = misPreferencias.edit(); // Nuevo cambio para ir a la Activity MenuPrincipal despues de registrarse


        DataBaseManager dbmanager = new DataBaseManager(getBaseContext());
        ContentValues values = new ContentValues();


        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            Toast.makeText(Registro.this, "Cargando datos...", Toast.LENGTH_LONG).show();

        }


        @Override
        protected String doInBackground(String... strings) {

            //Toast.makeText(Registro.this, "Cargando datos...:" , Toast.LENGTH_LONG).show();
            rellenarTablaAlimentos();
            editorPreferencias.putBoolean("tablaAlimentos", true);
            editorPreferencias.apply();

            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(Registro.this, "Carga finalizada", Toast.LENGTH_LONG).show();
        }

        private void rellenarTablaAlimentos() {
            //Categoria de loas Alimentos.
            tipoAlimento = getResources().getStringArray(R.array.arrayTipoAlimento);
            //Numero total de alimentos en cada categoria
            numeroTipoAlimento = getResources().getStringArray(R.array.numeroTipoAlimento);
            //Todos los alimentos (Todas las categorias)
            alimento = getResources().getStringArray(R.array.arrayAlimentos);
            //Indices Glucemicos de todos los alimentos
            indicesGlucemico = getResources().getStringArray(R.array.arrayIndicesGlucemicos);
            //RACIÓN DE HC(EN GRAMOS) de todos loas alimentos
            raciones = getResources().getStringArray(R.array.arrayRacionesHC);

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Total alimentos: " + alimento.length);
                Log.d(TAG, "Total I.G.: " + indicesGlucemico.length);
                Log.d(TAG, "Total Racios HC: " + raciones.length);

            }

            int contadorNumeroTipoAlimento = 0;
            int acumulado = Integer.parseInt(numeroTipoAlimento[contadorNumeroTipoAlimento]);

            for (int i = 0; i < alimento.length; ++i) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Procesando tipo de alimento: " + tipoAlimento[contadorNumeroTipoAlimento] +
                            " Alimento: " + alimento[i] + " 1 Ración en gramos: " + raciones[i] + "indice glucemico: " + indicesGlucemico[i]);
                }
                values.put("tipoAlimento", tipoAlimento[contadorNumeroTipoAlimento]);
                values.put("alimento", alimento[i]);
                values.put("racion", raciones[i]);
                values.put("indiceGlucemico", indicesGlucemico[i]);
                dbmanager.insertar("alimentos", values);
                values.clear();
                // si hemos procesado todos los alimentos de un cierto y no hay más tipos de alimentos a procesar
                if (i == acumulado - 1 && contadorNumeroTipoAlimento < tipoAlimento.length - 1) {
                    contadorNumeroTipoAlimento++; // avanzamos al siguiente tipo de alimento
                    acumulado += Integer.parseInt(numeroTipoAlimento[contadorNumeroTipoAlimento]);
                }
            }
        }

    }
}
