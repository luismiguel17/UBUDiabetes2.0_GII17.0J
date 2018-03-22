package com.ubu.lmi.gii170j.interfaz;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by LuisMiguel on 21/03/2018.
 */
public class CarbohidratosTest {
    //Instanciamos un onjeto de tipo CalculaBolo
    Carbohidratos carboHidratos;

    double grHCLecheEntera;
    double grHCArrozCocido;
    double grHCMandarinas;
    @Test
    public void calcularGramosDeHidratosDeCarbono() throws Exception {
        carboHidratos = new Carbohidratos();
        //Cálculo de gramos de HC en 100 gramos de arroz cocido
        int grArrozCocido = 100;
        String grPerRacionAC = "38.0";
        grHCArrozCocido =  grArrozCocido * (1.0/38.0)*10.0;
        //System.out.println("Cálculo de gramos de HC en 100 gramos de arroz cocido:" +  grHCArrozCocido);
        assertEquals((float) grHCArrozCocido, (float) carboHidratos.calcularGramosDeHidratosDeCarbono(grArrozCocido,grPerRacionAC) ,3);

        //Cálculo de gramos de HC en 250 gramos de leche entera
        int grLecheEnt = 250;
        String grPerRacionLE = "200.0";
        grHCLecheEntera = grLecheEnt * (1.0/200.0)*10.0;
        //System.out.println("Cálculo de gramos de HC en 250 gramos de leche entera (asumimos de 1 litro de leche es un kilo de leche) " +
                       //grHCLecheEntera );
        assertEquals((float) grHCLecheEntera,(float) carboHidratos.calcularGramosDeHidratosDeCarbono(grLecheEnt,grPerRacionLE),3);

        //Cálculo de gramos de HC en 200 gramos de mandarina
        int grMandarinas = 200;
        String grPerRacionMan = "100.0";
        grHCMandarinas = grMandarinas * (1.0/100.0)*10.0;
        //System.out.println("Cálculo de gramos de HC en 200 gramos de mandarina " + grHCMandarinas );
        assertEquals((float) grHCMandarinas,(float) carboHidratos.calcularGramosDeHidratosDeCarbono(grMandarinas,grPerRacionMan),3);

    }
    @Test
    public void añadirOtroOnClick() throws Exception {

    }

    @Test
    public void finalizarOnClick() throws Exception {
    }

}