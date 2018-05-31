package com.ubu.lmi.gii170j.interfaz;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Menu;
import android.widget.AdapterView;

import com.ubu.lmi.gii170j.R;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.AllOf.allOf;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
/**
 * Created by LuisMiguel on 22/05/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CarboHidratosTest {

    /**
     * @Rule. Inicia la actividad Registro automaticamente durante la prueba.
     * Evita heredar de ActivityInstrumentationTestCase2.
     * Las reglas son interceptores que se ejecutan para cada método de prueba
     * y se ejecutarán antes que cualquiera de su código de configuración en el método.
     */
    //@Rule
    //public ActivityTestRule<Carbohidratos> carbohidratosActivityTestRule = new ActivityTestRule<>(
            //Carbohidratos.class);

    @Rule
    public IntentsTestRule<Carbohidratos> carbohidratosActivityTestRule =
            new IntentsTestRule<>(Carbohidratos.class);

    private SharedPreferences misPreferencias ;
    private Context context;

    /**
     * ***************************************
     * ***************************************
     *                TESTS                  *
     * ***************************************
     * ***************************************
     */
    @Test
    public void pruebaTest() throws Exception {

        final int nameString = R.string.bolo;

        searchFoodRice(nameString, "Arroz", "100");
    }

    /**
     * ***************************************
     * ***************************************
     * MEtodos privados para lanzar los test:*
     * ***************************************
     * ***************************************
     */
    private void searchFoodRice(int dialogTextId, String food, String grs){


        try {
            Intent resultData = new Intent();
            /// Type food to trigger suggestions.
            onView(withId(R.id.actv_id_buscador))
                    .perform(typeText(food), closeSoftKeyboard());
            // Check the expected text is displayed in the Ui
            onView(withText("Arroz cocido")).inRoot(withDecorView(
                    not(is(carbohidratosActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            onView(withText("Arroz crudo")).inRoot(withDecorView(
                    not(is(carbohidratosActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            onView(withText("Arroz hinchado para desayuno")).inRoot(withDecorView(
                    not(is(carbohidratosActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            onView(withText("Arroz integral, crudo")).inRoot(withDecorView(
                    not(is(carbohidratosActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            onView(withText("Arroz intregral, cocido")).inRoot(withDecorView(
                    not(is(carbohidratosActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            onView(withText("Arroz salvaje, cocido")).inRoot(withDecorView(
                    not(is(carbohidratosActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            onView(withText("Arroz salvaje, crudo")).inRoot(withDecorView(
                    not(is(carbohidratosActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            onView(withText("Fideos de arroz, tipo Udon cocido")).inRoot(withDecorView(
                    not(is(carbohidratosActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));


            Thread.sleep(1500);

            // Tap on a suggestion: "Arroz cocido".
            onView(withText("Arroz cocido"))
                    .inRoot(withDecorView(not(is(carbohidratosActivityTestRule.getActivity().getWindow().getDecorView()))))
                    .perform(click());

            //click on the search term, the text should be filled in.
            onView(withId(R.id.actv_id_buscador))
                    .check(matches(withText("Arroz cocido")));

            //select food from spinner
            //onView(withId(R.id.spiner_alimentos)).perform(click());
            //onData(allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
            onView(withId(R.id.spiner_alimentos)).check(matches(withSpinnerText(containsString("Arroz cocido"))));

            //Type values in the EditText fields
            onView(withId(R.id.et_gramos)).perform(typeText(grs),closeSoftKeyboard());

            // Click on a given button
            onView(withId(R.id.bt_id_addAlimento)).perform(click());

            // Click on a given button
            onView(withId(R.id.bt_finalizar)).perform(click());

            onView(withText("Bolo corrector")).check(matches(allOf(withText(dialogTextId), isDisplayed())));

            Thread.sleep(1500);
            onView(withText(R.string.aceptar)).perform(click());

            //intended(hasComponent(MenuPrincipal.class.getName()));
            /**
            ViewInteraction appCompatButton2 = onView(
                    Matchers.allOf(withId(R.id.bt_bolo), withText("Calcular bolo"),
                            childAtPosition(
                                    Matchers.allOf(withId(R.id.ly1_id_MenuPrincipal),
                                            childAtPosition(
                                                    withId(R.id.fragment),
                                                    0)),
                                    1)));
             */
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}


