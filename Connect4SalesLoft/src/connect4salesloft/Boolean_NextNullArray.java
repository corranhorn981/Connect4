/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4salesloft;

import java.util.ArrayList;

/**
 *
 * @author David
 */
public class Boolean_NextNullArray {

    private boolean bool;
    private ArrayList<Integer> nextNull;

    public Boolean_NextNullArray(boolean bool, ArrayList<Integer> nextNull) {
        this.bool = bool;
        this.nextNull = nextNull;
    }

    public boolean getBool() {
        return bool;
    }
    public ArrayList<Integer> getArrayList(){
        return nextNull;
    }

}
