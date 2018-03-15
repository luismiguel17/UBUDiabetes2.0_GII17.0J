package com.ubu.lmi.gii170j.persistencia;

/**
 * Created by LuisMiguel on 15/03/2018.
 */

public class ValoresPOJO {

    /**
     * Constructor.
     *
     * @param rapida
     * @param insulinaBasal
     * @param insulinaRapida
     * @param glucemiaMinima
     * @param glucemiaMaxima
     * @param glucemia       glucemia inicial
     */
    public ValoresPOJO(boolean rapida, double insulinaBasal, double insulinaRapida,
                       double glucemiaMinima, double glucemiaMaxima, double glucemia) {
        this.rapida = rapida;
        this.insulinaBasal = insulinaBasal;
        this.insulinaRapida = insulinaRapida;
        this.glucemiaMinima = glucemiaMinima;
        this.glucemiaMaxima = glucemiaMaxima;
        this.glucemia = glucemia;
    }

    /**
     * Glucemia actual.
     */
    private double glucemia;

    /**
     * Obtien la lectura actual de glucemia.
     *
     * @return glucemia actual
     */
    public double getGlucemia() {
        return glucemia;
    }

    /**
     * Insulina rápida.
     */
    private boolean rapida;

    /**
     * Obtiene si se usa o no insulina rápida.
     *
     * @return true si usa insulina rapida o no
     */
    public boolean isRapida() {
        return rapida;
    }

    /**
     * Insulina basal.
     */
    private double insulinaBasal;

    /**
     * Obtiene la insulina basal.
     *
     * @return insulina basal
     */
    public double getInsulinaBasal() {
        return insulinaBasal;
    }

    /**
     * Insulina rápida.
     */
    private double insulinaRapida;

    /**
     * Obtiene la insulina rápida.
     *
     * @return insulina rápida
     */
    public double getInsulinaRapida() {
        return insulinaRapida;
    }

    /**
     * Glucemia mínima.
     */
    private double glucemiaMinima; // min

    /**
     * Obtiene la glucemia mínima.
     *
     * @return glucemia mínima
     */
    public double getGlucemiaMinima() {
        return glucemiaMinima;
    }

    /**
     * Glucemia máxima.
     */
    private double glucemiaMaxima;  // max

    /**
     * Obtiene la glucemia máxima.
     *
     * @return glucemia máxima
     */
    public double getGlucemiaMaxima() {
        return glucemiaMaxima;
    }
}
