package model;


import io.github.biezhi.anima.Anima;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Dec 03, 2018 11:25
 */
public class Model{

    public static Anima getModel() {
        return Connect.getConnect();
    }

}
