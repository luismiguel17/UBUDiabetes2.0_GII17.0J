package com.ubu.lmi.gii170j.calculos;

import com.ubu.lmi.gii170j.persistencia.ValoresPOJO;

/**
 * Created by LuisMiguel on 15/03/2018.
 */

public class CalculaBolo {

    /**
     * Gramos de hidratos de carbono.
     */
    private double gramosHidratosCarbono;

    /**
     * Valores.
     */
    private ValoresPOJO valores;

    /**
     * Constructor.
     *
     * @param sumatorio
     */
    public CalculaBolo(ValoresPOJO valoresPOJO, double sumatorio) {

        // Almacenar valor de HC
        gramosHidratosCarbono = sumatorio;
        valores = valoresPOJO;
    }

    /**
     * Realiza el calculo del Ratio.
     *
     * @return ratio
     */
    public double calculaRatio() {
        return 500 / (valores.getInsulinaBasal() + valores.getInsulinaRapida());
    }

    /**
     * CAMBIO: DE PRIVATE A PUBLIC
     * Realiza el calculo del factor de sensibilidad.
     *
     * @return FSI factor de sensibilidad
     */
    public double calculaFactorSensibilidad() {
        double suma = valores.getInsulinaBasal() + valores.getInsulinaRapida();

        double constante = valores.isRapida() ? 1500 : 1800;
        return constante / suma;
    }

    /**
     * CAMBIO: DE PRIVATE A PUBLIC
     * Calcula la glucemia objetivo como la media de la glucemia máxima y la mínima.
     *
     * @return glucemia objetivo
     */
    public double calculaGlucemiaObjetivo() {
        return (valores.getGlucemiaMaxima() + valores.getGlucemiaMinima()) / 2;
    }

    /**
     * calculaUdsGlucemia. cálculo de Unidades de Insulina según glucemia
     * @return
     */

    public double calculaUdsGlucemia(){
        double operando=(valores.getGlucemia() - calculaGlucemiaObjetivo()) / calculaFactorSensibilidad();
        // Bug versión 1.1, "El resultado para el cálculo de insulina si la glucosa en sangre es menor de
        // la glucemia objetivo, es negativo. DEBERIA SER CERO.
        if (operando < 0) { // si es negativo el operando
            operando = 0; // se toma valor cero
        }
        return operando;
    }

    /**
     * Realiza el calculo del bolo corrector aplicando las formulas necesarias.
     */
    public double calculoBoloCorrector() {
        double operando1 = gramosHidratosCarbono / calculaRatio();
        double operando2 = calculaUdsGlucemia();

        return (operando1 + operando2);
    }
}
