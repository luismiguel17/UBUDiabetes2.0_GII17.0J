package com.ubu.lmi.gii170j.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ubu.lmi.gii170j.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by LuisMiguel on 22/03/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegistroTest {

    /**
     * @Rule. Inicia la actividad Registro automaticamente durante la prueba.
     * Evita heredar de ActivityInstrumentationTestCase2.
     * Las reglas son interceptores que se ejecutan para cada método de prueba
     * y se ejecutarán antes que cualquiera de su código de configuración en el método.
     */
    @Rule
    public ActivityTestRule<Registro> perfilActivityRule = new ActivityTestRule<>(
            Registro.class);


    private SharedPreferences misPreferencias ;
    private Context context;
    /**
     * ***************************************
     * ***************************************
     *                TESTS                  *
     * ***************************************
     * ***************************************
     */

    /**
     * textFieldsEmpty. Test para los EditText cuando hay algun campo vacio.
     * @throws Exception
     */
    @Test
    public void textFieldsEmpty() throws Exception {
        final String expectedResult = "Rellene todos los campos";
        final int nameString = R.string.textfieldEmpty;
        context = getInstrumentation().getTargetContext();
        misPreferencias = context.getSharedPreferences("PreferenciasUsuario",Context.MODE_PRIVATE);
        Boolean arranque = misPreferencias.getBoolean("primeraEjecucion", false);

        if(!arranque){
            editTextFields_NewRegister(nameString,"Luis Miguel", "25", "","","90" ,
                    "120","12","15",expectedResult);
        }else{
            editTextFields_OldRegister(nameString,"70","130","15","",expectedResult);
        }

    }

    /**
     * valoresGlucemiaDeseadosShowError. Test para valores de glucemia deseados fuera de rango.
     * @throws Exception
     */

    @Test
    public void valoresGlucemiaFueraRango() throws Exception {
        final String expectedResult = "Introduce valores min y max de glucemia entre los valores indicados";
        final int nameString = R.string.minmax_incorrecto;

        context = getInstrumentation().getTargetContext();
        misPreferencias = context.getSharedPreferences("PreferenciasUsuario",Context.MODE_PRIVATE);
        Boolean arranque = misPreferencias.getBoolean("primeraEjecucion", false);

        if(!arranque){
            editTextFields_NewRegister(nameString,"Luis Miguel", "25", "160","60","80" ,
                    "300","12","15",expectedResult);
        }else{
            valoresGlucemiaDeseados_OldRegister(nameString,"90","260",expectedResult);
        }

    }


    /**
     * valoresGlucemiaMinMayor. Test para el valor de glucemia minimo mayor que el valor maximo.
     * @throws Exception
     */


    @Test
    public void valoresGlucemiaMinMayor() throws Exception {
        final String expectedResult = "El valor min de glucemia no puedes ser mayor que el valor máximo";
        final int nameString = R.string.minmax_orden;

        context = getInstrumentation().getTargetContext();
        misPreferencias = context.getSharedPreferences("PreferenciasUsuario",Context.MODE_PRIVATE);
        Boolean arranque = misPreferencias.getBoolean("primeraEjecucion", false);

        if(!arranque){
            editTextFields_NewRegister(nameString,"Luis Miguel", "25", "160","60","100" ,
                    "90","12","15",expectedResult);
        }else{
            valoresGlucemiaDeseados_OldRegister(nameString,"110","100",expectedResult);
        }

    }

    @Test
    public void radioButtonsNoChecked(){
        final int nameString = R.string.noRadioButtonsChecked;
        final String expectedResult = "Algún elemento sin seleccionar";

        context = getInstrumentation().getTargetContext();
        misPreferencias = context.getSharedPreferences("PreferenciasUsuario",Context.MODE_PRIVATE);
        Boolean arranque = misPreferencias.getBoolean("primeraEjecucion", false);

        if(!arranque){
            insulinaDelBolo_DecimalesdelBoloC(nameString,"Luis Miguel", "25", "160","60","100" ,
                    "120","20","20",expectedResult);
        }else{
            change_DecimalesdelBoloC();
        }

    }


    /**
     * ***************************************
     * ***************************************
     * MEtodos privados para lanzar los test:*
     * ***************************************
     * ***************************************
     */

    /**
     * editTextFields_NewRegister. Metodo que testea si hay algun campo vacio cuando se registra un usuario por primera vez.
     * @param name nombre usuario.
     * @param age edad usuario.
     * @param height altura usuario.
     * @param weight peso usuario.
     * @param min glucemia deseado minimo.
     * @param max glucemia deseado maximo.
     * @param udbasal unidades de insulina basal.
     * @param udrap unidades de insulina rapida.
     * @param expectedResult resultado esperado.
     */
    private void editTextFields_NewRegister(int nameString, String name , String age , String height, String weight, String min, String max ,
                                            String udbasal, String udrap, String expectedResult){
        //Type value in the EditText field
        onView(withId(R.id.et_id_nombre)).perform(typeText(name));
        //close Keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());

        //Type values in the EditText fields
        onView(withId(R.id.et_id_edad)).perform(typeText(age));
        onView(withId(R.id.et_id_estatura)).perform(typeText(height));
        onView(withId(R.id.et_id_peso)).perform(typeText(weight));
        //close keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());

        //Type values in the EditText fields
        onView(withId(R.id.et_id_ly2_1_registro_min)).perform(typeText(min));
        onView(withId(R.id.et_id_ly2_1_registro_max)).perform(typeText(max));

        //close keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());

        //Type value in the RadioGroup
        onView(withId(R.id.rb_id_rapida)).perform(click());

        //scrollTo -Funciona
        //onView(withId(R.id.ly_dm1_3_fragmentProfile)).perform(scrollTo());
        //swipeUp() root View.
        onView(isRoot()).perform(swipeUp());

        //Type values in the EditText fields
        onView(withId(R.id.et_udsBasal)).perform(typeText(udbasal));
        onView(withId(R.id.et_udsRapida)).perform(typeText(udrap));

        //close keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());
        //Type value in the RadioGroup
        onView(withId(R.id.rb_id_dos)).perform(click());

        // Click on a given button
        onView(withId(R.id.bt_guardar)).perform(click());


        // Check the expected text is displayed in the Ui
        onView(withText(nameString)).inRoot(withDecorView(
                not(is(perfilActivityRule.getActivity().getWindow().getDecorView())))).check(matches(withText(expectedResult)));
    }

    /**
     * editTextFields_OldRegister. Metodo que testea si hay algun campo vacio cuando un usuario modifica sus datos.
     * @param weight peso usuario.
     * @param max glucemia deseado maximo.
     * @param udbasal unidades de insulina basal.
     * @param udrap unidades de insulina rapida.
     * @param expectedResult resultado esperado.
     */
    private void editTextFields_OldRegister(int nameString, String weight, String max ,
                                            String udbasal, String udrap, String expectedResult){
        // first, clear old editText
        onView(withId(R.id.et_id_peso)).perform(clearText());
        //Type value in the EditText field
        onView(withId(R.id.et_id_peso)).perform(typeText(weight));
        //close keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());


        //first, clear old editText
        onView(withId(R.id.et_id_ly2_1_registro_max)).perform(clearText());
        //Type value in the EditText field
        onView(withId(R.id.et_id_ly2_1_registro_max)).perform(typeText(max));
        //close keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());

        //scrollTo -Funciona
        //onView(withId(R.id.ly_dm1_3_fragmentProfile)).perform(scrollTo());
        //swipeUp() root View.
        onView(isRoot()).perform(swipeUp());

        //first, clear old editText
        onView(withId(R.id.et_udsBasal)).perform(clearText());
        //Type value in the EditText field
        onView(withId(R.id.et_udsBasal)).perform(typeText(udbasal));
        //first, clear old editText
        onView(withId(R.id.et_udsRapida)).perform(clearText());
        //Type value in the EditText field
        onView(withId(R.id.et_udsRapida)).perform(typeText(udrap));

        //close keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());
        // Click on a given button
        onView(withId(R.id.bt_guardar)).perform(click());

        // Check the expected text is displayed in the Ui
        onView(withText(nameString)).inRoot(withDecorView(
                not(is(perfilActivityRule.getActivity().getWindow().getDecorView())))).check(matches(withText(expectedResult)));

    }

    /**
     * valoresGlucemiaDeseados_OldRegister. Metodo que testea si los valores de glucemia deseados estan dentro de los
     * valores indicados cuando un usuario modifica sus datos.
     * @param nameString
     * @param min
     * @param max
     * @param expectedResult
     */
    private void valoresGlucemiaDeseados_OldRegister(int nameString, String min, String max ,String expectedResult) {

        //first, clear old editText
        onView(withId(R.id.et_id_ly2_1_registro_max)).perform(clearText());
        //Type value in the EditText field
        onView(withId(R.id.et_id_ly2_1_registro_max)).perform(typeText(max));
        //first, clear old editText
        onView(withId(R.id.et_id_ly2_1_registro_min)).perform(clearText());
        //Type value in the EditText field
        onView(withId(R.id.et_id_ly2_1_registro_min)).perform(typeText(min));
        //close keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());

        //scrollTo -Funciona
        //onView(withId(R.id.ly_dm1_3_fragmentProfile)).perform(scrollTo());
        //swipeUp() root View.
        onView(isRoot()).perform(swipeUp());

        // Click on a given button
        onView(withId(R.id.bt_guardar)).perform(click());

        // Check the expected text is displayed in the Ui
        onView(withText(nameString)).inRoot(withDecorView(
                not(is(perfilActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(withText(expectedResult)));
    }

    private void insulinaDelBolo_DecimalesdelBoloC(int nameString, String name , String age , String height, String weight, String min, String max ,
                                                   String udbasal, String udrap, String expectedResult){

        //Type value in the EditText field
        onView(withId(R.id.et_id_nombre)).perform(typeText(name));
        //close Keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());

        //Type values in the EditText fields
        onView(withId(R.id.et_id_edad)).perform(typeText(age));
        onView(withId(R.id.et_id_estatura)).perform(typeText(height));
        onView(withId(R.id.et_id_peso)).perform(typeText(weight));
        //close keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());

        //Type values in the EditText fields
        onView(withId(R.id.et_id_ly2_1_registro_min)).perform(typeText(min));
        onView(withId(R.id.et_id_ly2_1_registro_max)).perform(typeText(max));

        //close keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());

        //scrollTo -Funciona
        //onView(withId(R.id.ly_dm1_3_fragmentProfile)).perform(scrollTo());
        //swipeUp() root View.
        onView(isRoot()).perform(swipeUp());

        //Type values in the EditText fields
        onView(withId(R.id.et_udsBasal)).perform(typeText(udbasal));
        onView(withId(R.id.et_udsRapida)).perform(typeText(udrap));

        //close keyboard
        onView(withId(R.id.ly_fragmentProfile)).perform(closeSoftKeyboard());


        // Click on a given button
        onView(withId(R.id.bt_guardar)).perform(click());


        // Check the expected text is displayed in the Ui
        onView(withText(nameString)).inRoot(withDecorView(
                not(is(perfilActivityRule.getActivity().getWindow().getDecorView())))).check(matches(withText(expectedResult)));
    }

    /**
     * change_DecimalesdelBoloC. Metodo que cambia el numero de decimales del bolo corrector a cero.
     */
    private void change_DecimalesdelBoloC() {
        //scrollTo -Funciona
        //onView(withId(R.id.ly_dm1_3_fragmentProfile)).perform(scrollTo());
        //swipeUp() root View.
        onView(isRoot()).perform(swipeUp());
        //Type value in the RadioGroup
        onView(withId(R.id.rb_id_cero)).perform(click());
        // Click on a given button
        onView(withId(R.id.bt_guardar)).perform(click());
    }

}