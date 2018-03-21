package com.ubu.lmi.gii170j.calculos;

import com.ubu.lmi.gii170j.persistencia.ValoresPOJO;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by LuisMiguel on 21/03/2018.
 */
public class CalculaBoloTest {

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

    /**
     * calculaRatio. Test que realiza las pruebas de calculo del Ratio.
     * La variable Ratio representa los gramos de hidratos de carbono que
     * son metabolizados por una unidad de insulina rápida.
     * @throws Exception exception.
     */
    @Test
    public void calculaRatio() throws Exception {
        rapida = false;
        insulinaBasal = 15.0;
        insulinaRapida=15.0;
        glucemiaMinima = 90.0;
        glucemiaMaxima = 150.0;
        glucemia = 80.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,200);
        //System.out.println("Ratio:" + (float)calculadorBolo.calculaRatio());
        assertEquals((float)(500/(insulinaBasal+insulinaRapida)),(float)calculadorBolo.calculaRatio(),4);

    }

    /**
     * calculaFactorSensibilidadRapida. Test que realiza las pruebas de calculo del FSI en el caso de Ins. rápida.
     * El FSI es el valor de glucemia en mg/dl que se consigue reducir al administrar una unidad de análogo de insulina
     * de acción rápida.
     * @throws Exception exception.
     */
    @Test
    public void calculaFactorSensibilidadRapida() throws Exception {

        rapida = true;
        insulinaBasal = 15.0;
        insulinaRapida= 15.0;
        glucemiaMinima = 90.0;
        glucemiaMaxima = 150.0;
        glucemia = 80.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,200);
        //System.out.println("FSI:" + (float)calculadorBolo.calculaFactorSensibilidad());
        assertEquals((float)(1500/insTotal),(float)calculadorBolo.calculaFactorSensibilidad(),2);
    }

    /**
     * calculaFactorSensibilidadUltraRapida. Test que realiza las pruebas de calculo del FSI en el caso de Ins. Ultrarápida.
     * @throws Exception exception.
     */
    @Test
    public void calculaFactorSensibilidadUltraRapida() throws Exception {

        rapida = false;
        insulinaBasal = 15.0;
        insulinaRapida= 15.0;
        glucemiaMinima = 90.0;
        glucemiaMaxima = 150.0;
        glucemia = 80.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,200);
        //System.out.println("FSI:" + (float)calculadorBolo.calculaFactorSensibilidad());
        assertEquals((float)(1800/insTotal),(float)calculadorBolo.calculaFactorSensibilidad(),2);
    }

    /**
     * calculaGlucemiaObjetivo. Test uqe realiza las pruebas del calculo de el valor de glucemia objetivo.
     * GO es el valor que el usuario quiere conseguir con la dosis de la insulina.
     * @throws Exception exception.
     */

    @Test
    public void calculaGlucemiaObjetivo() throws Exception {
        rapida = true;
        insulinaBasal = 15.0;
        insulinaRapida= 15.0;
        glucemiaMinima = 90.0;
        glucemiaMaxima = 150.0;
        glucemia = 80.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,200);
        //System.out.println("GObjetivo:" + (float)calculadorBolo.calculaGlucemiaObjetivo());
        assertEquals((float)((glucemiaMinima+glucemiaMaxima)/2),(float)calculadorBolo.calculaGlucemiaObjetivo(),3);
    }

    /**
     * calculaUdsGlucemia. Test que realiza las pruebas para el calculo de las unidades
     * de insulina (UI) para una glucemia determinada.
     * UI a inyectar según glucemia.
     * @throws Exception exception.
     */
    @Test
    public void calculaUdsGlucemia() throws Exception {

        rapida = true;
        insulinaBasal = 15.0;
        insulinaRapida= 15.0;
        glucemiaMinima = 90.0;
        glucemiaMaxima = 150.0;
        glucemia = 80.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal+ insulinaRapida;
        double gluObjetivo = (glucemiaMinima+glucemiaMaxima)/2;
        double fsi = 1500/insTotal;
        calculadorBolo = new CalculaBolo(valoresP,200);
        //System.out.println("UI Glucemia(class):" + calculadorBolo.calculaUdsGlucemia());
        //System.out.println("UI Glucemia(manual):" + (int)(glucemia-gluObjetivo)/fsi);
        assertEquals((int)((glucemia-gluObjetivo)/fsi),(int)calculadorBolo.calculaUdsGlucemia());
    }

    /**
     * calculoBoloCorrector. Test que realiza las pruebas para el calculo de la recomendación del bolo de insulina final (uds).
     * @throws Exception
     */
    @Test
    public void calculoBoloCorrector() throws Exception {
        double grHC = 200.0;
        rapida = true;
        insulinaBasal = 15.0;
        insulinaRapida= 15.0;
        glucemiaMinima = 90.0;
        glucemiaMaxima = 150.0;
        glucemia = 80.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);
        insTotal = insulinaBasal + insulinaRapida;
        calculadorBolo = new CalculaBolo(valoresP,grHC);
        //Calculos segun formulas acuales
        double fsi = 1500/insTotal;
        double ratio = 500/insTotal;
        double gluObjetivo = (glucemiaMinima+glucemiaMaxima)/2;
        double uiGlucemia = (int)((glucemia-gluObjetivo)/fsi);
        //UI necesarias para cubrir los hidratos de carbono de los alimentos que vayan a tomar en esa comida.
        double uiAlimentos = (int) (grHC/ratio);
        //System.out.println("Uds Bolo Corrector(class):" + (int)calculadorBolo.calculoBoloCorrector());
        //System.out.println("Uds Bolo Corrector(manual):" + (int)(uiGlucemia + uiAlimentos));
        assertEquals((int)(uiGlucemia + uiAlimentos),(int)calculadorBolo.calculoBoloCorrector());
    }
}