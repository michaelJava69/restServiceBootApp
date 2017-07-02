package org.rbsg.java.model;

 



import java.io.Serializable;
import java.util.List;


public class PrimesResponse implements Serializable {

    private static final long serialVersionUID = 1746127840105174833L;
    private final int initial;
    private final List<Integer> primes;



    public PrimesResponse(int initial, List<Integer> primes) {

        this.initial = initial;

        this.primes = primes;

    }





    public int getInitial() {
        return initial;
    }



    public List<Integer> getPrimes() {
        return primes;
    }

}
