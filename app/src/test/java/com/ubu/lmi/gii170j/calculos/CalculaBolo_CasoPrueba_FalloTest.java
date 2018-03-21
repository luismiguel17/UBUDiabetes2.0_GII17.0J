package com.ubu.lmi.gii170j.calculos;

import com.ubu.lmi.gii170j.persistencia.ValoresPOJO;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by LuisMiguel on 21/03/2018.
 */
public class CalculaBolo_CasoPrueba_FalloTest {

    //Instanciamos un objeto de tipo ValoresPojo
    ValoresPOJO valoresP;
    //Instanciamos un onjeto de tipo CalculaBolo
    CalculaBolo calculadorBolo;
    double udsInsulina;
    //Obtiene si se usa o no insulina rápida.
    boolean rapida;
    //uds Insulina Basal
    double insulinaBasal;
    //uds Insulina rapida
    double insulinaRapida;
    //uds min Glucemia en la normalidad
    double glucemiaMinima;
    ////uds max Glucemia en la normalidad
    double glucemiaMaxima;
    //lectura actual de glucemia mg/dl
    double glucemia;
    //uds insulina total en 24h
    double insTotal;

    //Alimentos Antes DESAYUNO
    double hcLecheEntera = 12.5;
    double hcKiwi = 10.0;
    double gramosHC = hcLecheEntera + hcKiwi;


    @Test
    public void calculaRatio() throws Exception {
        rapida = true;
        insulinaBasal = 20.0;
        insulinaRapida=20.0;
        glucemiaMinima = 100.0;
        glucemiaMaxima = 120.0;
        glucemia = 130.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,gramosHC);
        System.out.println("Ratio:" + (float)calculadorBolo.calculaRatio());
        assertEquals((float)(500/insTotal),(float)calculadorBolo.calculaRatio(),4);
    }

    @Test
    public void calculaFactorSensibilidad() throws Exception {
        rapida = true;
        insulinaBasal = 20.0;
        insulinaRapida=20.0;
        glucemiaMinima = 100.0;
        glucemiaMaxima = 120.0;
        glucemia = 130.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,gramosHC);
        System.out.println("FSI:" + (float)calculadorBolo.calculaFactorSensibilidad());
        assertEquals((float)(1500/insTotal),(float)calculadorBolo.calculaFactorSensibilidad(),2);
    }


    @Test
    public void calculaGlucemiaObjetivo() throws Exception {

        rapida = true;
        insulinaBasal = 20.0;
        insulinaRapida=20.0;
        glucemiaMinima = 100.0;
        glucemiaMaxima = 120.0;
        glucemia = 130.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,gramosHC);
        System.out.println("GObjetivo:" + (float)calculadorBolo.calculaGlucemiaObjetivo());
        assertEquals((float)((glucemiaMinima+glucemiaMaxima)/2),(float)calculadorBolo.calculaGlucemiaObjetivo(),3);
    }

    @Test
    public void calculaGr_HC_Ratio() throws Exception{
        rapida = true;
        insulinaBasal = 20.0;
        insulinaRapida=20.0;
        glucemiaMinima = 100.0;
        glucemiaMaxima = 120.0;
        glucemia = 130.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,gramosHC);

        double ratio = 500/insTotal;
        System.out.println("GR_HC per Ratio:" + (gramosHC/ratio));
        assertEquals((float)(gramosHC/ratio),(float)calculadorBolo.calculaGr_HC_Ratio(),3);
    }
    @Test
    public void calculaUdsGlucemia() throws Exception {
        rapida = true;
        insulinaBasal = 20.0;
        insulinaRapida=20.0;
        glucemiaMinima = 100.0;
        glucemiaMaxima = 120.0;
        glucemia = 130.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,gramosHC);
        double gluObjetivo = (glucemiaMinima+glucemiaMaxima)/2;
        double fsi = 1500/insTotal;
        System.out.println("UI Glucemia(manual):" + (int)(glucemia-gluObjetivo)/fsi);
        assertEquals((int)((glucemia-gluObjetivo)/fsi),(int)calculadorBolo.calculaUdsGlucemia());
    }

    @Test
    public void calculoBoloCorrector() throws Exception {
        rapida = true;
        insulinaBasal = 20.0;
        insulinaRapida=20.0;
        glucemiaMinima = 100.0;
        glucemiaMaxima = 120.0;
        glucemia = 130.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,gramosHC);
        //Glucemia Objetivo
        double gluObjetivo = (glucemiaMinima+glucemiaMaxima)/2;
        //Factor de sensibilidad
        double fsi = 1500/insTotal;
        //Ratio de insulina
        double ratio = 500/insTotal;

        //Unidades Insulina Glucemia
        double uiGlucemia = (glucemia-gluObjetivo)/fsi;
        //UI necesarias para cubrir los hidratos de carbono de los alimentos que vayan a tomar en esa comida.
        double uiAlimentos = gramosHC/ratio;

        System.out.println("Uds Bolo Corrector(manual):" + (uiGlucemia + uiAlimentos));
        assertEquals((float)(uiGlucemia + uiAlimentos),(float)calculadorBolo.calculoBoloCorrector(),3);
    }

}