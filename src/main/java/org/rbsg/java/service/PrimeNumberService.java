package org.rbsg.java.service;


import org.springframework.stereotype.Service;



import java.util.LinkedList;

import java.util.List;



@Service

public class PrimeNumberService implements PrimeService {



    @Override

    public List<Integer> getPrimeNumbers(int upperLimit) {

        List<Integer> primeNumbers = new LinkedList<Integer>();



        if(upperLimit >= 2) {

            primeNumbers.add(2);

            for(int i = 3; i <= upperLimit; i += 2) {

                if(isPrime(i)){

                    primeNumbers.add(i);

                }

            }

        }



        return primeNumbers;

    }



    private boolean isPrime (int n){

        for (int i = 3; i * i <= n; i += 2) {

            if (n % i == 0) return false;

        }

        return true;

    }

}
