package com.ubu.lmi.gii170j.interfaz;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ubu.lmi.gii170j.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
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
        editTextFields(nameString,"Luis Miguel", "25", "","","90" ,
                "120","12","15",expectedResult);
    }

    /**
     * valoresGlucemiaDeseadosShowError. Test para valores de glucemia deseados fuera de rango.
     * @throws Exception
     */

    @Test
    public void valoresGlucemiaFueraRango() throws Exception {
        final String expectedResult = "Introduce valores min y max de glucemia entre los valores indicados";
        final int nameString = R.string.minmax_incorrecto;

        valoresGlucemiaDeseados(nameString,"Luis Miguel", "25", "160","60","70" ,
                "300","12","15",expectedResult);
    }


    /**
     * valoresGlucemiaMinMayor. Test para el valor de glucemia minimo mayor que el valor maximo.
     * @throws Exception
     */


    @Test
    public void valoresGlucemiaMinMayor() throws Exception {
        final String expectedResult = "El valor min de glucemia no puedes ser mayor que el valor máximo";
        final int nameString = R.string.minmax_orden;

        valoresGlucemiaDeseados(nameString,"Luis Miguel", "25", "160","60","100" ,
                "90","12","15",expectedResult);
    }

    @Test
    public void radioButtonRapidaChecked(){
        final int id_radioButton = R.id.rb_id_rapida;
        insulinaDelBolo(id_radioButton);

    }

    @Test
    public void radioButtonUltraRapidaChecked(){
        final int id_radioButton = R.id.rb_id_ultrarrapida;
        insulinaDelBolo(id_radioButton);

    }
    /**
     * ***************************************
     * ***************************************
     * MEtodos privados para lanzarlos test: *
     * ***************************************
     * ***************************************
     */

    /**
     *
     * @param name
     * @param age
     * @param height
     * @param weight
     * @param min
     * @param max
     * @param udbasal
     * @param udrap
     * @param expectedResult
     */
    private void  editTextFields(int nameString,String name , String age , String height, String weight, String min, String max ,
                             String udbasal, String udrap, String expectedResult){
        //Type values in the EditText fields
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

        //Type values in the EditText fields
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
        // Click on a given button
        onView(withId(R.id.bt_guardar)).perform(click());

        // Check the expected text is displayed in the Ui
        onView(withText(R.string.textfieldEmpty)).inRoot(withDecorView(
                not(is(perfilActivityRule.getActivity().getWindow().getDecorView())))).check(matches(withText(expectedResult)));
    }

    private void valoresGlucemiaDeseados(int nameString,String name , String age , String height, String weight, String min, String max ,
                                         String udbasal, String udrap, String expectedResult) {
        //Type values in the EditText fields
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

        //Type values in the EditText fields
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
        // Click on a given button
        onView(withId(R.id.bt_guardar)).perform(click());

        // Check the expected text is displayed in the Ui
        onView(withText(nameString)).inRoot(withDecorView(
                not(is(perfilActivityRule.getActivity().getWindow().getDecorView())))).
                check(matches(withText(expectedResult)));
    }

    private void insulinaDelBolo(int insulina){

        //swipeUp() root View.
        onView(isRoot()).perform(swipeUp());
        //Click on a given Radiobutton
        onView(withId(insulina)).perform(click());
        // Check the expected test is displayed in the Ui
        //onView(withId(insulina)).check(matches((withText(expectedResult))));
        onView(withId(insulina)).check(matches(isChecked()));

    }

}