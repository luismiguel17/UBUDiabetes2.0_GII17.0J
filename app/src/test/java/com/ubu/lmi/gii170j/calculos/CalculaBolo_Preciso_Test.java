package com.ubu.lmi.gii170j.calculos;

import com.ubu.lmi.gii170j.R;
import com.ubu.lmi.gii170j.persistencia.ValoresPOJO;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by LuisMiguel on 21/03/2018.
 */
public class CalculaBolo_Preciso_Test {

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
        assertEquals((float)(500/(insulinaBasal+insulinaRapida)),(float)calculadorBolo.calculaRatio(),0);

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
        //System.out.println("FSI(Rapida):" + (float)calculadorBolo.calculaFactorSensibilidad());
        assertEquals((float)(1500/insTotal),(float)calculadorBolo.calculaFactorSensibilidad(),0);
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
        //System.out.println("FSI (Ultrarapida):" + (float)calculadorBolo.calculaFactorSensibilidad());
        assertEquals((float)(1800/insTotal),(float)calculadorBolo.calculaFactorSensibilidad(),0);
    }

    /**
     * calculaGlucemiaObjetivo. Test que realiza las pruebas del calculo del valor de glucemia objetivo.
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
        assertEquals((float)((glucemiaMinima+glucemiaMaxima)/2),(float)calculadorBolo.calculaGlucemiaObjetivo(),0);
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
     * @throws Exception exception.
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

    /**
     * calculoBoloCorrectorAlimentos. Test que realiza las pruebas para el calculo de la recomendación del bolo de insulina final (uds)
     * para todos los alimentos (de 1 en 1).
     * Este test se realizará teniendo en cuenta los resultados que aparecen en la hoja de calculo "Casos validación UBUdiabetes.xlsx" (Hoja: "Alimentos de 1 en 1" )
     * que nos proporcionó Diego Serrano Gómez.
     * @throws Exception
     */
    @Test
    public void calculoBoloCorrectorAlimentos1en1() throws Exception {
        double grHC = 0.0;

        rapida = true;
        insulinaBasal = 15.0;
        insulinaRapida= 15.0;
        glucemiaMinima = 100.0;
        glucemiaMaxima = 140.0;
        glucemia = 120.0;
        valoresP = new ValoresPOJO(rapida,insulinaBasal,insulinaRapida,glucemiaMinima,glucemiaMaxima,glucemia);

        String[] alimentos = {"Cuajada","Flan","Helado de crema","Helado de Hielo","Helado sin azucar añadido","Kéfir","Leche desnatada","Leche semidesnatada","Leche entera","Leche condensada",
                "Leche en polvo","Nata líquida","Natillas","Petit Suisse","Queso fresco","Yogur natural entero o desnatado","Yogur desnatado sabores o fruta","Yogur entero sabores o fruta","Yogur líquido","Yogur tipo actimel","Yogur tipo actimet 0%", "Arroz crudo","Arroz cocido","Arroz integral crudo","Arroz integral cocido",
        "Arroz hinchado para desayuno","Arroz salvaje crudo","Arroz salvaje cocido","Avena crudo",
        "Avena cocido","Avena copos","Boniato","Cebada crudo","Cebada cocido","Centeno crudo","Centeno cocido",
        "Cereales desayuno","Cerales desayuno ricos en fibra","Cuscús crudo","Cuscús cocido","Fideos de arroz, udon, cocido",
        "Fideos de soja","Galleteas tipo Digestiva","Galletas tipo María","Galletas tipo Principe","Galleta sin azucar",
        "Garbanzo crudo","Garbanzo cocido","Guisantes","Harina de trigo o maiz","Harina decenteno","Harina de soja",
        "Hojaldre crudo","Hojaldre horneado","Judias blancas crudo","Judias blancas cocido","Lentejas crudo","Lentejas cocido",
        "Maiz en lata","Maiz en lata sin azucar añadido","Mijo crudo","Mijo cocico","Muesli","Pan blanco","Pan de centeno",
        "Pan de molde","Pan de hamburguesa o frankfurt","Pan de trigo integral","Pan rallado","Pan tostado o biscote",
        "Pan en bastoncillos","Pasta alimenticia crudo","Pasta alimenticia coido","Pasta al huevo","Patata cocida hervida",
        "Patata horno o asada","Patatas fritas","Patatas chips","Puré de patatas, copos","Puré de patatas elaborado con leche",
        "Quinoa crudo","Quinoa cocido","Sémola de trigo crudo","Sémola de trigo cocido","Soja seca crudo","Soja seca cocido","Sushi",
        "Tapioca crudo","Tapioca cocido","Trigo sarraceno crudo","Trigo sarraceno cocido","Trigo tierno crudo","Trigo tierno cocido",
        "Yuca cocido","Albaricoque","Arandano","Castaña cruda","Castaña tostada","Cereza","Chirimoya","Ciruela","Coco fresco",
        "Coco seco","Dátil","Frambuesa","Fresones","Granada","Grosella","Grosella negra","Higos","Kiwi","Lichi","Mandariona","Mango",
        "Manzana","Manzana asada","Melocotón","Melocotón en conserva","Melón","Membrillo","Menbrillo dulce","Moras","Naranja","Nectarina", "Nispero",
        "Pera","Papaya","Paraguayo","Piña","Piña en conserva","Piña en su jugo","Plátano","Sandía","Uva","Acelga","Ajo","Alcachofa","Apio",
        "Apio-navo","Berenjena","Brócoli","Calabacín","Calabaza","Cardo","Cebolla","Cebolla frita en aros",
        "Col Bruselas, Coloflor","Endibia","Judía verde","Lechuga",
        "Nabo","Palmitos","Pepino","Pimiento verde/rojo","Puerro","Rábano","Remolacha","Repollo","Setas","Soja en brotes","Tomate",
        "Zanahoria","Zanahoria hervida","Zanahoria en conserva","Aceituna","Albaricoque seco","Almendra","Almendra tostada","Avellana","Cacahuete",
        "Ciruela pasa","Dátil seco","Higo","Nuez","Piñón","Pipas","Pistacho","Sésamo","Uva pasa","Bebida isotónica","Bebida refrescante tipo cola o sabores",
        "Bebida de cacao","Bebida de soja","Bebida energética","Bitter","Cava seco o semiseco",
        "Cerveza","cerveza light","Cerveza sin","Horchata","Horchata light","Licor de melocotón o manzana","Mosto",
                "Sangría", "Sidra","Tónica","Vermut","Vino dulce","Zumo de fruta comercial","Zumo de fruta natural o sin azucar añadido","Azucar blanco",
                "Azucar moreno","Barrita energética (cereales)","Bizcocho o melindo","Bollería en general","Cruasán","Cacao en polvo",
        "Cacao en polvo sin azucar","Calamares a la romana","Canelones con bechamel","Caramelo","Chocolate blanco o con leche","Chocolate negro",
        "Churros","Crema de cacao","Crema de cacahuete","Crema pastelera","Croquetas","Donuts","Empanadilla de carne","Ensaimada","Fructosa (edulcorante)",
        "Gazpacho comercial","Glucosa (líquida o en pastillas)","Golosinas","Ketchup","Lasaña","Levadura","Magdalena","Mazapán","Merengue","Mermelada",
        "Miel","Palomitas","Pastel de chocolate","Pastel de crema","Pizza","Regaliz","Salsa bechamel","Salsa boloñesa","Salsa carbonara",
                "Salsa de tomate comercial","Surimi (palitos de cangrejo)","Tarta de manzana","Tortilla de patata","Turrón de Alicante","Turrón de Jijona","Vinagre tipo Modena"};

        double[] racionHCenGr = {200.0,50.0,50.0,50.0,100.0,200.0,200.0,200.0,200.0,20.0,25.0,300.0,50.0,70.0,250.0,
                        200.0,125.0,70.0,70.0,100.0,100.0,13.0,38.0,13.0,40.0,12.0,13.0,34.0,17.0,34.0,15.0,50.0,14.0,42.0,
                15.0,38.0,15.0,20.0,15.0,65.0,50.0,40.0,16.0,15.0,14.0,18.0,20.0,50.0,100.0,15.0,17.0,70.0,30.0,24.0,20.0,
                50.0,20.0,50.0,50.0,90.0,15.0,53.0,15.0,20.0,20.0,20.0,18.0,23.0,15.0,15.0,15.0,15.0,50.0,16.0,50.0,35.0,
                30.0,20.0,15.0,80.0,19.0,48.0,14.0,90.0,30.0,100.0,45.0,12.0,33.0,12.0,42.0,16.0,39.0,33.0,150.0,100.0,
                30.0,25.0,100.0,50.0,100.0,200.0,150.0,15.0,150.0,200.0,70.0,200.0,140.0,100.0,100.0,70.0,100.0,100.0,100.0,
                50.0,100.0,50.0,200.0,150.0,20.0,150.0,100.0,100.0,100.0,100.0,125.0,100.0,100.0,85.0,60.0,50.0,200.0,50.0,300.0,
                40.0,300.0,300.0,500.0,300.0,300.0,300.0,200.0,300.0,150.0,100.0,300.0,300.0,
                250.0,300.0,300.0,200.0,300.0,300.0,300.0,300.0,150.0,300.0,300.0,300.0,300.0,150.0,200.0,225.0,250.0,15.0,
                150.0,140.0,150.0,100.0,15.0,15.0,15.0,300.0,300.0,80.0,80.0,100.0,15.0,130.0,100.0,100.0,250.0,80.0,100.0,
                250.0,250.0,300.0,250.0,75.0,300.0,30.0,70.0,100.0,200.0,100.0,75.0,75.0,100.0,200.0,10.0,10.0,20.0,20.0,
                20.0,20.0,12.0,22.0,120.0,100.0,12.0,17.0,25.0,25.0,25.0,100.0,40.0,50.0,23.0,50.0,23.0,10.0,150.0,10.0,18.0,50.0,100.0,
                130.0,25.0,25.0,11.0,20.0,13.0,20.0,25.0,35.0,40.0,15.0,100.0,100.0,150.0,100.0,100.0,25.0,120.0,25.0,25.0,15.0};

        double gramosConsumo = 250.0;
        double[] UdsInsEsperado = {0.75,3.0,3.0,3.0,1.5,0.75,0.75,0.75,0.75,7.5,6.0,0.5,3.0,2.14,0.6,0.75,1.2,2.14,2.14,1.5,
                1.5,11.54,3.95,11.54,3.75,12.51,11.54,4.41,8.83,4.41, 10.0,  3.0,  10.72,  3.57,  10.0,  3.95,  10.0,  7.5,  10.0,
                2.31,  3.0,  3.75, 9.38, 10.0, 10.72, 8.34, 7.5, 3.0, 1.5,  10.0, 8.83, 2.14, 5.0, 6.25, 7.5, 3.0, 7.5, 3.0, 3.0,
                1.67, 10.0, 2.83, 10.0, 7.5, 7.5, 7.5, 8.34, 6.52, 10.0, 10.0, 10.0, 10.0, 3.0, 9.38, 3.0, 4.29, 5.0, 7.5, 10.0, 1.88,
                7.9, 3.13, 10.72, 1.67, 5.0, 1.5, 3.33, 12.51, 4.55, 12.51, 3.57, 9.38, 3.85, 4.55, 1.0, 1.5, 5.0, 6.0, 1.5, 3.0, 1.5,
                0.75, 1.0, 10.0, 1.0, 0.75, 2.14, 0.75, 1.07, 1.5, 1.5, 2.14, 1.5, 1.5, 1.5, 3.0, 1.5, 3.0, 0.75, 1.0, 7.5, 1.0, 1.5,
                1.5, 1.5, 1.5, 1.2, 1.5, 1.5, 1.77, 2.5, 3.0, 0.75, 3.0, 0.5, 3.75, 0.5, 0.5, 0.3, 0.5, 0.5, 0.5, 0.75, 0.5, 1.0, 1.5,
                0.5, 0.5, 0.6, 0.5, 0.5, 0.75, 0.5, 0.5, 0.5, 0.5, 1.0, 0.5, 0.5, 0.5, 0.5, 1.0, 0.75, 0.67, 0.6, 10.0, 1.0, 1.07, 1.0,
                1.5, 10.0, 10.0, 10.0, 0.5, 0.5, 1.88, 1.88, 1.5, 10.0, 1.15, 1.5, 1.5, 0.6, 1.88, 1.5, 0.6, 0.6, 0.5, 0.6, 2.0, 0.5,
                5.0, 2.14, 1.5, 0.75, 1.5, 2.0, 2.0, 1.5, 0.75, 15.01, 15.01, 7.5, 7.5, 7.5, 7.5, 12.51, 6.82, 1.25, 1.5, 12.51, 8.83,
                6.0, 6.0, 6.0, 1.5, 3.75, 3.0, 6.52, 3.0, 6.52, 15.01, 1.0, 15.01, 8.34, 3.0, 1.5, 1.15, 6.0, 6.0, 13.64, 7.5, 11.54,
                7.5, 6.0, 4.29, 3.75, 10.0, 1.5, 1.5, 1.0, 1.5, 1.5, 6.0, 1.25, 6.0, 6.0, 10.0 };

       for (int i = 0; i<alimentos.length; i++){
           grHC = (gramosConsumo * 10.0 ) / racionHCenGr[i];
           calculadorBolo = new CalculaBolo(valoresP,grHC);
           //System.out.println("Resultado Esperado " +  UdsInsEsperado[i]);
           //redondeamos a dos decimales--> n * 100/100
           //System.out.println("Resultado Obtenido " + (Math.rint(calculadorBolo.calculoBoloCorrector()*100)/100));
           //System.out.println("----------------------------------------------------------------");
           assertEquals((float)UdsInsEsperado[i],(float)(Math.rint(calculadorBolo.calculoBoloCorrector()*100)/100),0.05);


       }

    }
}