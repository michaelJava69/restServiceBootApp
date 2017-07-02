package org.rbsg.java.controller;


import org.apache.log4j.Logger; 
import org.rbsg.java.model.PrimesResponse;
import org.rbsg.java.service.PrimeService;
import org.rbsg.java.service.PrimeNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;


@Api(value = "primenumbers")
@RestController
@RequestMapping("/primes")
public class PrimesController {



	final static Logger logger = Logger.getLogger(PrimesController.class);



  

    /*
     * Method for the PrimesController class
     *
	     * @param upperLimit - Prime Number upper limit  
	     *                     UpperLimit be (final) too indicate that it can not be changed)
     *  
     * 
     * 
     */
	
	@ApiOperation(value = "getPrimeNumbers", nickname = "Return Prime Numbers", response = PrimesResponse.class)
	@JsonProperty(required = true)
    @ApiModelProperty(notes = "Prime number upper limit", required = true)
	
    @RequestMapping(value = "/{upperLimit}", method = RequestMethod.GET,headers="Accept=application/json")
    @ResponseBody
    @Cacheable(value = "primes", key = "#upperLimit")

    public PrimesResponse getPrimeNumbers(@PathVariable final int upperLimit) {

    	logger.info(" **** Test : Inside PrimeController  ***"); 
	   	  
	   	 /*
	   	  * Custom cache
	   	  * 
	   	  * This is where the caching logic resides. I check cache and if not populated i carry out the 
	   	  * PrimesNumber task. If it is I get it from the Cache.
	   	  * 
	   	  * To emphasis the wait time when not getting from cache I have deliberately delayed by 2 secs 
	   	  * the call outside of the cache
	   	  * 
	   	  */
	   	 
	       PrimesResponse primesResponse; 
	       //Cache xyz = CacheManager.getInstance().getCache("primes");
	       PrimeNumberService primeService = new PrimeNumberService();
	       logger.info( "Getting data outside of the cachce.********....." );
	   	  
      	       
	       primesResponse = new PrimesResponse(upperLimit, primeService.getPrimeNumbers(upperLimit));           
	       return primesResponse   ;
    	 

    }





}
