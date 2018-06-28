package com.ubu.lmi.gii170j.calculations;

import com.ubu.lmi.gii170j.persistence.ValoresPOJO;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by LuisMiguel on 21/03/2018.
 * Esta clase se realizará teniendo en cuenta los valores que aparecen en la hoja de calculo
 * "Casos validación UBUdiabetes.xlsx" (Hoja: "Casos clinicos") que nos proporcionó Diego Serrano Gómez.
 */
public class CalculaBolo_CasoClinico_FalloTest {

    //Instanciamos un objeto de tipo ValoresPojo
    private ValoresPOJO valoresP;
    //Instanciamos un onjeto de tipo CalculaBolo
    private CalculaBolo calculadorBolo;
    private double udsInsulina;
    //Obtiene si se usa o no insulina rápida.
    private boolean rapida = true;
    //uds Insulina Basal
    private double insulinaBasal = 20.0;
    //uds Insulina rapida
    private double insulinaRapida = 20.0;
    //uds min Glucemia en la normalidad
    private double glucemiaMinima = 100.0;
    ////uds max Glucemia en la normalidad
    private double glucemiaMaxima = 120.0;
    //lectura actual de glucemia mg/dl
    private double glucemia = 130.0;
    //uds insulina total en 24h
    private double insTotal;
    //Alimentos Antes DESAYUNO
    private double hcLecheEntera = 12.5;
    private double hcKiwi = 10.0;
    private double gramosHC = hcLecheEntera + hcKiwi;

    /**
     * calculaRatio. Test que realiza las pruebas de calculo del Ratio.
     * La variable Ratio representa los gramos de hidratos de carbono que
     * son metabolizados por una unidad de insulina rápida.
     * @throws Exception exception.
     */
    @Test
    public void calculaRatio() throws Exception {

        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,gramosHC);
        //System.out.println("Ratio:" + (float)calculadorBolo.calculaRatio());
        assertEquals((float)(500/insTotal),(float)calculadorBolo.calculaRatio(),4);
    }

    /**
     * calculaFactorSensibilidad. Test que realiza las pruebas de calculo del FSI en el caso de Ins. rápida.
     * El FSI es el valor de glucemia en mg/dl que se consigue reducir al administrar una unidad de análogo de insulina
     * de acción rápida.
     * @throws Exception exception.
     */
    @Test
    public void calculaFactorSensibilidad() throws Exception {

        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,gramosHC);
        //System.out.println("FSI:" + (float)calculadorBolo.calculaFactorSensibilidad());
        assertEquals((float)(1500/insTotal),(float)calculadorBolo.calculaFactorSensibilidad(),2);
    }


    /**
     * calculaGlucemiaObjetivo. Test que realiza las pruebas del calculo del valor de glucemia objetivo.
     * GO es el valor que el usuario quiere conseguir con la dosis de la insulina.
     * @throws Exception exception.
     */
    @Test
    public void calculaGlucemiaObjetivo() throws Exception {


        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,gramosHC);
        //System.out.println("GObjetivo:" + (float)calculadorBolo.calculaGlucemiaObjetivo());
        assertEquals((float)((glucemiaMinima+glucemiaMaxima)/2),(float)calculadorBolo.calculaGlucemiaObjetivo(),3);
    }

    /**
     * calculagrHcRatio. Test que realiza las pruebas del calculo del valor de gramos de HC per ratio.
     * @throws Exception exception.
     */
    @Test
    public void calculaGr_HC_Ratio() throws Exception{

        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,gramosHC);

        double ratio = 500/insTotal;
        //System.out.println("GR_HC per Ratio:" + (gramosHC/ratio));
        assertEquals((float)(gramosHC/ratio),(float)calculadorBolo.calculagrHcRatio(),3);
    }

    /**
     * calculaUdsGlucemia. Test que realiza las pruebas para el calculo de las unidades
     * de insulina (UI) para una glucemia determinada.
     * UI a inyectar según glucemia.
     * @throws Exception exception.
     */
    @Test
    public void calculaUdsGlucemia() throws Exception {

        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,gramosHC);
        double gluObjetivo = (glucemiaMinima+glucemiaMaxima)/2;
        double fsi = 1500/insTotal;
        //System.out.println("UI Glucemia(manual):" + (int)(glucemia-gluObjetivo)/fsi);
        assertEquals((int)((glucemia-gluObjetivo)/fsi),(int)calculadorBolo.calculaUdsGlucemia());
    }

    /**
     * calculoBoloCorrector. Test que realiza las pruebas para el calculo de la recomendación del bolo de insulina final (uds).
     * @throws Exception exception.
     */
    @Test
    public void calculoBoloCorrector() throws Exception {
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

        //System.out.println("Uds Bolo Corrector(manual): " + (double)((uiGlucemia + uiAlimentos)));
        //System.out.println("Uds Bolo Corrector(Programa): " + (double)(calculadorBolo.calculoBoloCorrector()));
        assertEquals((float)((uiGlucemia + uiAlimentos)),(float)(calculadorBolo.calculoBoloCorrector()),5);
    }

}