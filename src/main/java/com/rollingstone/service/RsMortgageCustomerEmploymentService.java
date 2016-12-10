package com.rollingstone.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rollingstone.dao.jpa.RsMortgageCustomerEmploymentRepository;
import com.rollingstone.domain.Customer;
import com.rollingstone.domain.Education;
import com.rollingstone.domain.Employment;

/*
 * Service class to do CRUD for Customer Employment through JPS Repository
 */
@Service
public class RsMortgageCustomerEmploymentService {

    private static final Logger log = LoggerFactory.getLogger(RsMortgageCustomerEmploymentService.class);

    @Autowired
    private RsMortgageCustomerEmploymentRepository customerEmploymentRepository;

    @Autowired
    CounterService counterService;

    @Autowired
    GaugeService gaugeService;
    
    @Autowired
   	private CustomerClient customerClient;

    public RsMortgageCustomerEmploymentService() {
    }

    @HystrixCommand(fallbackMethod = "createEmploymentWithoutValidation")
    public Employment createEmployment(Employment employment) throws Exception {
    	Employment createdEmployment = null;
    	if (employment != null && employment.getCustomer() != null){
    		
    		log.info("In service employment create"+ employment.getCustomer().getId());
    		if (customerClient == null){
        		log.info("In customerClient null got customer");
    		}
    		else {
    			log.info("In customerClient not null got customer");
    		}
    		
    		Customer customer = customerClient.getCustomer((new Long(employment.getCustomer().getId())));
    		
    		if (customer != null){
    			log.info("Customer Validation Successful. Creating Employment with valid customer.");
    			createdEmployment  = customerEmploymentRepository.save(employment);
    		}else {
    			log.info("Invalid Customer");
    			throw new Exception("Invalid Customer");
    		}
    	}
    	else {
    			throw new Exception("Invalid Customer");
    	}
        return createdEmployment;
    }

    public Employment createEmploymentWithoutValidation(Employment employment) throws Exception {
    	Employment createdEmployment = null;
		log.info("Customer Validation Failed. Creating Employment without validation.");
    	createdEmployment  = customerEmploymentRepository.save(employment);
    	
        return createdEmployment;
    }
    
    public Employment getEmployment(long id) {
        return customerEmploymentRepository.findOne(id);
    }

    public void updateEmployment(Employment employment) throws Exception {
    	Employment createdEmployment = null;
    	if (employment != null && employment.getCustomer() != null){
    		
    		log.info("In service employment create"+ employment.getCustomer().getId());
    		if (customerClient == null){
        		log.info("In customerClient null got customer");
    		}
    		else {
    			log.info("In customerClient not null got customer");
    		}
    		
    		Customer customer = customerClient.getCustomer((new Long(employment.getCustomer().getId())));
    		
    		if (customer != null){
    			createdEmployment  = customerEmploymentRepository.save(employment);
    		}else {
    			log.info("Invalid Customer");
    			throw new Exception("Invalid Customer");
    		}
    	}
    	else {
    			throw new Exception("Invalid Customer");
    	}
    }

    public void deleteEmployment(Long id) {
    	customerEmploymentRepository.delete(id);
    }

    public Page<Employment> getAllEmploymentsByPage(Integer page, Integer size) {
        Page pageOfEmployments = customerEmploymentRepository.findAll(new PageRequest(page, size));
        
        // example of adding to the /metrics
        if (size > 50) {
            counterService.increment("com.rollingstone.getAll.largePayload");
        }
        return pageOfEmployments;
    }
    
    public List<Employment> getAllEmployments() {
        Iterable<Employment> pageOfEmployments = customerEmploymentRepository.findAll();
        
        List<Employment> customerEemployments = new ArrayList<Employment>();
        
        for (Employment employment : pageOfEmployments){
        	customerEemployments.add(employment);
        }
    	log.info("In Real Service getAllEmployments  size :"+customerEemployments.size());

    	
        return customerEemployments;
    }
    
    public List<Employment> getAllEmploymentsForCustomer(Customer customer) {
        Iterable<Employment> pageOfEmployments = customerEmploymentRepository.findCustomerEmploymentByCustomer(customer);
        
        List<Employment> customerEemployments = new ArrayList<Employment>();
        
        for (Employment employment : pageOfEmployments){
        	customerEemployments.add(employment);
        }
    	log.info("In Real Service getAllEmployments  size :"+customerEemployments.size());

    	
        return customerEemployments;
    }
}
