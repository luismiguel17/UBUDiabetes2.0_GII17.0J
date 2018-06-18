package com.ubu.lmi.gii170j.view;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.ubu.lmi.gii170j.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RunAllAplication {

    @Rule
    public ActivityTestRule<SplashScreen> mActivityTestRule = new ActivityTestRule<>(SplashScreen.class);

    @Test
    public void runAllAplication() {
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(2000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatEditText = onView(
allOf(withId(R.id.et_id_nombre),
childAtPosition(
allOf(withId(R.id.ly_id_DatPers_fragementProfile),
childAtPosition(
withId(R.id.ly_fragmentProfile),
1)),
2)));
        appCompatEditText.perform(scrollTo(), replaceText("luis"), closeSoftKeyboard());

        //pressBack();

        ViewInteraction appCompatEditText2 = onView(
allOf(withId(R.id.et_id_edad),
childAtPosition(
allOf(withId(R.id.ly_dp1_2_fragmentProfile),
childAtPosition(
withId(R.id.ly_id_DatPers_fragementProfile),
4)),
0)));
        appCompatEditText2.perform(scrollTo(), replaceText("25"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
allOf(withId(R.id.et_id_estatura),
childAtPosition(
allOf(withId(R.id.ly_dp1_2_fragmentProfile),
childAtPosition(
withId(R.id.ly_id_DatPers_fragementProfile),
4)),
1)));
        appCompatEditText3.perform(scrollTo(), replaceText("160"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
allOf(withId(R.id.et_id_peso),
childAtPosition(
allOf(withId(R.id.ly_dp1_2_fragmentProfile),
childAtPosition(
withId(R.id.ly_id_DatPers_fragementProfile),
4)),
2)));
        appCompatEditText4.perform(scrollTo(), replaceText("60"), closeSoftKeyboard());

        //pressBack();

        ViewInteraction appCompatEditText5 = onView(
allOf(withId(R.id.et_id_ly2_1_registro_min),
childAtPosition(
allOf(withId(R.id.lydm1_2_fragmentProfile),
childAtPosition(
withId(R.id.ly_dm_fragmentProfile),
3)),
0)));
        appCompatEditText5.perform(scrollTo(), replaceText("100"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
allOf(withId(R.id.et_id_ly2_1_registro_max),
childAtPosition(
allOf(withId(R.id.lydm1_2_fragmentProfile),
childAtPosition(
withId(R.id.ly_dm_fragmentProfile),
3)),
1)));
        appCompatEditText6.perform(scrollTo(), replaceText("120"), closeSoftKeyboard());

        //pressBack();

        ViewInteraction appCompatRadioButton = onView(
allOf(withId(R.id.rb_id_rapida), withText("Rápida"),
childAtPosition(
allOf(withId(R.id.rg_id_profile),
childAtPosition(
withId(R.id.ly_dm1_2_fragmentProfile),
1)),
0),
isDisplayed()));
        appCompatRadioButton.perform(click());

        ViewInteraction appCompatEditText7 = onView(
allOf(withId(R.id.et_udsBasal),
childAtPosition(
allOf(withId(R.id.ly_dm1_3_2_fragmentProfile),
childAtPosition(
withId(R.id.ly_dm1_3_fragmentProfile),
2)),
0),
isDisplayed()));
        appCompatEditText7.perform(replaceText("20"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
allOf(withId(R.id.et_udsRapida),
childAtPosition(
allOf(withId(R.id.ly_dm1_3_2_fragmentProfile),
childAtPosition(
withId(R.id.ly_dm1_3_fragmentProfile),
2)),
1),
isDisplayed()));
        appCompatEditText8.perform(replaceText("20"), closeSoftKeyboard());

        //pressBack();

        ViewInteraction appCompatRadioButton2 = onView(
allOf(withId(R.id.rb_id_dos), withText("Dos"),
childAtPosition(
allOf(withId(R.id.rg2_id_profile),
childAtPosition(
withId(R.id.ly_dm1_3_3_fragmentProfile),
1)),
2),
isDisplayed()));
        appCompatRadioButton2.perform(click());

        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.bt_guardar), withText("Guardar"),
childAtPosition(
allOf(withId(R.id.ly_fragmentProfile),
childAtPosition(
withId(R.id.fragmentRegistro),
0)),
3)));
        appCompatButton.perform(scrollTo(), click());

        /**
        ViewInteraction appCompatButton2 = onView(
allOf(withId(R.id.bt_b), withText("Calcular bolo"),
childAtPosition(
allOf(withId(R.id.ly1_id_MenuPrincipal),
childAtPosition(
withId(R.id.fragment),
0)),
1)));
        appCompatButton2.perform(scrollTo(), click());
*/
        ViewInteraction appCompatEditText9 = onView(
allOf(withId(R.id.et_cantidadglucemia),
childAtPosition(
allOf(withId(R.id.ly1_id_RegistroGlucemias),
childAtPosition(
withId(R.id.fragment),
0)),
1)));
        appCompatEditText9.perform(scrollTo(), click());

        ViewInteraction appCompatEditText10 = onView(
allOf(withId(R.id.et_cantidadglucemia),
childAtPosition(
allOf(withId(R.id.ly1_id_RegistroGlucemias),
childAtPosition(
withId(R.id.fragment),
0)),
1)));
        appCompatEditText10.perform(scrollTo(), replaceText("130"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
allOf(withId(R.id.button), withText("Guardar"),
childAtPosition(
allOf(withId(R.id.ly1_id_RegistroGlucemias),
childAtPosition(
withId(R.id.fragment),
0)),
2)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatButton4 = onView(
allOf(withId(R.id.bt_incidencias), withText("Guardar"),
childAtPosition(
allOf(withId(R.id.ly1_id_Incidencias),
childAtPosition(
withId(R.id.fragment),
0)),
4)));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction appCompatAutoCompleteTextView = onView(
allOf(withId(R.id.actv_id_buscador),
childAtPosition(
allOf(withId(R.id.fragmentCarboHidratos),
childAtPosition(
withClassName(is("android.support.design.widget.CoordinatorLayout")),
1)),
0),
isDisplayed()));
        appCompatAutoCompleteTextView.perform(click());

        ViewInteraction appCompatAutoCompleteTextView2 = onView(
allOf(withId(R.id.actv_id_buscador),
childAtPosition(
allOf(withId(R.id.fragmentCarboHidratos),
childAtPosition(
withClassName(is("android.support.design.widget.CoordinatorLayout")),
1)),
0),
isDisplayed()));
        appCompatAutoCompleteTextView2.perform(replaceText("entera"), closeSoftKeyboard());

        DataInteraction appCompatTextView = onData(anything())
.inAdapterView(childAtPosition(
withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
0))
.atPosition(0);
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText11 = onView(
allOf(withId(R.id.et_gramos),
childAtPosition(
allOf(withId(R.id.ly_id_addalimento_fc),
childAtPosition(
withId(R.id.fragmentCarboHidratos),
4)),
0),
isDisplayed()));
        appCompatEditText11.perform(replaceText("250"), closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
allOf(withId(R.id.bt_id_addAlimento),
childAtPosition(
allOf(withId(R.id.ly_id_addalimento_fc),
childAtPosition(
withId(R.id.fragmentCarboHidratos),
4)),
1),
isDisplayed()));
        appCompatButton5.perform(click());

        //pressBack();

        ViewInteraction appCompatButton6 = onView(
allOf(withId(R.id.bt_finalizar), withText("Finalizar"),
childAtPosition(
allOf(withId(R.id.fragmentCarboHidratos),
childAtPosition(
withClassName(is("android.support.design.widget.CoordinatorLayout")),
1)),
9),
isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatButton7 = onView(
allOf(withId(android.R.id.button3), withText("Aceptar"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.ScrollView")),
0),
0)));
        appCompatButton7.perform(scrollTo(), click());

        }

        private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
