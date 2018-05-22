package com.ubu.lmi.gii170j.calculos;

import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.persistencia.ValoresPOJO;

import org.junit.Test;

import static org.junit.Assert.*;
public class CalculaBolo_NoPreciso_Test {

    //Instanciamos un objeto de tipo ValoresPojo
    ValoresPOJO valoresP;
    //Instanciamos un onjeto de tipo CalculaBolo
    CalculaBolo calculadorBolo;
    double udsInsulina;
    //Obtiene si se usa o no insulina rápida.
    boolean rapida;

    /**
     * calculoBoloCorrectorSinAlimentos1. Test que realiza las pruebas para el calculo de la recomendación del bolo de insulina final (uds).
     * Calculo debolo de insulina para 14 pacientes diferentes con distintas necesidades de insulina diaria
     * glucemia objetivo de 120 mg/dl y una glucemia de 250, 225, 200 y 175.
     * Este test se realizará teniendo en cuenta los resultados que aparecen en la hoja de calculo "Casos validación UBUdiabetes.xlsx" (Hoja: "Sin Aliemntos 1" )
     * que nos proporcionó Diego Serrano Gómez. (Con un margen de error = 1)
     * @throws Exception
     */
    @Test
    public void calculoBoloCorrectorSinAlimentos1_A() throws Exception {
        rapida = true;
        double grHC = 0.0;
        //double glucemiaObjetivo = 120.0;
        /*
         * Para obtener la glucemia minima y la maxima, (Valores necesarios para crear el objeto "ValoresPojo")
         * se tendra en cuenta la formula para el calculo de la Glucemia objetivo = (glucemiaMinima+glucemiaMaxima)/2
         * si glucemiaObjetivo = 120.0 --> glu_min = 120.0 & glu_max = 120.0
         */
        double glu_min = 120.0;
        double glu_max = 120.0;
        double glucemia = 250.0;
        double [] insulinaRapida ={10.0 ,12.0, 14.0, 16.0, 18.0, 20.0, 22.0, 24.0, 26.0, 28.0, 30.0, 32.0, 34.0, 36.0};
        double [] insulinaLenta = {8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0, 22.0, 24.0, 26.0, 28.0, 30.0, 32.0, 34.0,};
        double [] UdsInsEsperado = {1.0, 2.0, 2.0, 2.0, 2.0, 3.0, 3.0, 3.0, 4.0, 4.0, 4.0, 4.0, 4.0, 5.0};
        double media_Teorica_Esperada = 2.84 ;
        double media_Teorica_Obtenida = 0.0 ;

        for (int i= 0; i< insulinaLenta.length; i++){

            valoresP = new ValoresPOJO(rapida,insulinaLenta[i],insulinaRapida[i],glu_min,glu_max,glucemia);
            calculadorBolo = new CalculaBolo(valoresP,grHC);

            //System.out.println("Resultado Esperado " + UdsInsEsperado[i]);
            //System.out.println("Resultado Obtenido " + Math.floor(calculadorBolo.calculoBoloCorrector()));
            //System.out.println("----------------------------------------------------------------");
            assertEquals((float)UdsInsEsperado[i],(float)Math.floor(calculadorBolo.calculoBoloCorrector()),1);
            media_Teorica_Obtenida += Math.floor(calculadorBolo.calculoBoloCorrector());

        }
        media_Teorica_Obtenida = media_Teorica_Obtenida/UdsInsEsperado.length;
        //System.out.println("Resultado Esperado " + media_Teorica_Esperada);
        //System.out.println("Resultado Obtenido " + media_Teorica_Obtenida);
        assertEquals((float)media_Teorica_Esperada,(float)media_Teorica_Obtenida,1);

    }

    @Test
    public void calculoBoloCorrectorSinAlimentos1_B() throws Exception {
        rapida = true;
        double grHC = 0.0;
        //double glucemiaObjetivo = 120.0;
        /*
         * Para obtener la glucemia minima y la maxima, (Valores necesarios para crear el objeto "ValoresPojo")
         * se tendra en cuenta la formula para el calculo de la Glucemia objetivo = (glucemiaMinima+glucemiaMaxima)/2
         * si glucemiaObjetivo = 120.0 --> glu_min = 120.0 & glu_max = 120.0
         */
        double glu_min = 120.0;
        double glu_max = 120.0;
        double glucemia = 225.0;
        double [] insulinaRapida ={10.0, 12.0, 14.0, 16.0, 18.0, 20.0, 22.0, 24.0, 26.0, 28.0, 30.0, 32.0, 34.0, 36.0};
        double [] insulinaLenta = {8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0, 22.0, 24.0, 26.0, 28.0, 30.0, 32.0, 34.0};
        double [] UdsInsEsperado = {1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.0, 3.0, 3.0, 3.0, 4.0, 4.0, 4.0};
        double media_Teorica_Esperada = 2.36 ;
        double media_Teorica_Obtenida = 0.0 ;

        for (int i= 0; i< insulinaLenta.length; i++){

            valoresP = new ValoresPOJO(rapida,insulinaLenta[i],insulinaRapida[i],glu_min,glu_max,glucemia);
            calculadorBolo = new CalculaBolo(valoresP,grHC);

            //System.out.println("Resultado Esperado " + UdsInsEsperado[i]);
            //System.out.println("Resultado Obtenido " + Math.floor(calculadorBolo.calculoBoloCorrector()));
            //System.out.println("----------------------------------------------------------------");
            assertEquals((float) UdsInsEsperado[i],(float) Math.floor(calculadorBolo.calculoBoloCorrector()),1);
            media_Teorica_Obtenida += Math.floor(calculadorBolo.calculoBoloCorrector());

        }
        media_Teorica_Obtenida = media_Teorica_Obtenida/UdsInsEsperado.length;
        //System.out.println("Resultado Esperado " + media_Teorica_Esperada);
        //System.out.println("Resultado Obtenido " + media_Teorica_Obtenida);
        assertEquals((float)media_Teorica_Esperada,(float)media_Teorica_Obtenida,1);

    }

    @Test
    public void calculoBoloCorrectorSinAlimentos1_C() throws Exception {
        rapida = true;
        double grHC = 0.0;
        //double glucemiaObjetivo = 120.0;
        /*
         * Para obtener la glucemia minima y la maxima, (Valores necesarios para crear el objeto "ValoresPojo")
         * se tendra en cuenta la formula para el calculo de la Glucemia objetivo = (glucemiaMinima+glucemiaMaxima)/2
         * si glucemiaObjetivo = 120.0 --> glu_min = 120.0 & glu_max = 120.0
         */
        double glu_min = 120.0;
        double glu_max = 120.0;
        double glucemia = 200.0;
        double [] insulinaRapida ={10.0, 12.0, 14.0, 16.0, 18.0, 20.0, 22.0, 24.0, 26.0, 28.0, 30.0, 32.0, 34.0, 36.0};
        double [] insulinaLenta = {8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0, 22.0, 24.0, 26.0, 28.0, 30.0, 32.0, 34.0};
        double [] UdsInsEsperado = {1.0, 1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.0, 3.0, 3.0, 3.0};
        double media_Teorica_Esperada = 1.84 ;
        double media_Teorica_Obtenida = 0.0 ;

        for (int i= 0; i< insulinaLenta.length; i++){

            valoresP = new ValoresPOJO(rapida,insulinaLenta[i],insulinaRapida[i],glu_min,glu_max,glucemia);
            calculadorBolo = new CalculaBolo(valoresP,grHC);

            //System.out.println("Resultado Esperado " + UdsInsEsperado[i]);
            //System.out.println("Resultado Obtenido " + Math.floor(calculadorBolo.calculoBoloCorrector()));
            //System.out.println("----------------------------------------------------------------");
            assertEquals((float) UdsInsEsperado[i],(float) Math.floor(calculadorBolo.calculoBoloCorrector()),1);
            media_Teorica_Obtenida += Math.floor(calculadorBolo.calculoBoloCorrector());

        }
        media_Teorica_Obtenida = media_Teorica_Obtenida/UdsInsEsperado.length;
        //System.out.println("Resultado Esperado " + media_Teorica_Esperada);
        //System.out.println("Resultado Obtenido " + media_Teorica_Obtenida);
        assertEquals((float)media_Teorica_Esperada,(float)media_Teorica_Obtenida,1);

    }

    @Test
    public void calculoBoloCorrectorSinAlimentos1_D() throws Exception {
        rapida = true;
        double grHC = 0.0;
        //double glucemiaObjetivo = 120.0;
        /*
         * Para obtener la glucemia minima y la maxima, (Valores necesarios para crear el objeto "ValoresPojo")
         * se tendra en cuenta la formula para el calculo de la Glucemia objetivo = (glucemiaMinima+glucemiaMaxima)/2
         * si glucemiaObjetivo = 120.0 --> glu_min = 120.0 & glu_max = 120.0
         */
        double glu_min = 120.0;
        double glu_max = 120.0;
        double glucemia = 175.0;
        double [] insulinaRapida ={10.0, 12.0, 14.0, 16.0, 18.0, 20.0, 22.0, 24.0, 26.0, 28.0, 30.0, 32.0, 34.0, 36.0};
        double [] insulinaLenta = {8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0, 22.0, 24.0, 26.0, 28.0, 30.0, 32.0, 34.0};
        double [] UdsInsEsperado = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0};
        double media_Teorica_Esperada = 1.35 ;
        double media_Teorica_Obtenida = 0.0 ;

        for (int i= 0; i< insulinaLenta.length; i++){

            valoresP = new ValoresPOJO(rapida,insulinaLenta[i],insulinaRapida[i],glu_min,glu_max,glucemia);
            calculadorBolo = new CalculaBolo(valoresP,grHC);

            //System.out.println("Resultado Esperado " + UdsInsEsperado[i]);
            //System.out.println("Resultado Obtenido " + Math.floor(calculadorBolo.calculoBoloCorrector()));
            //System.out.println("----------------------------------------------------------------");
            assertEquals((float) UdsInsEsperado[i],(float) Math.floor(calculadorBolo.calculoBoloCorrector()),1);
            media_Teorica_Obtenida += Math.floor(calculadorBolo.calculoBoloCorrector());

        }
        media_Teorica_Obtenida = media_Teorica_Obtenida/UdsInsEsperado.length;
        //System.out.println("Media Resultado Esperado " + media_Teorica_Esperada);
        //System.out.println("Media Resultado Obtenido " + media_Teorica_Obtenida);
        assertEquals((float)media_Teorica_Esperada,(float)media_Teorica_Obtenida,1);

    }
}
