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

import com.rollingstone.dao.jpa.RsMortgageCustomerEmploymentRepository;
import com.rollingstone.domain.Customer;
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

    public RsMortgageCustomerEmploymentService() {
    }

    public Employment createEmployment(Employment employment) {
        return customerEmploymentRepository.save(employment);
    }

    public Employment getEmployment(long id) {
        return customerEmploymentRepository.findOne(id);
    }

    public void updateEmployment(Employment employment) {
    	customerEmploymentRepository.save(employment);
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
