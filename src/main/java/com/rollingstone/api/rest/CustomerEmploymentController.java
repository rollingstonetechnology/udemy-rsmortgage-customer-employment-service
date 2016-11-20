package com.rollingstone.api.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.rollingstone.domain.Customer;
import com.rollingstone.domain.Employment;
import com.rollingstone.exception.HTTP400Exception;
import com.rollingstone.service.RsMortgageCustomerEmploymentService;
/*
 * Demonstrates how to set up RESTful API endpoints using Spring MVC
 */

@RestController
@RequestMapping(value = "/rsmortgage-customer-employment-service/v1/customer-employment")
public class CustomerEmploymentController extends AbstractRestController {

    @Autowired
    private RsMortgageCustomerEmploymentService customerEmploymentService;
  
    @RequestMapping(value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomerEmployment(@RequestBody Employment education,
                                 HttpServletRequest request, HttpServletResponse response) {
    	Employment createdEmployment = this.customerEmploymentService.createEmployment(education);
        response.setHeader("Location", request.getRequestURL().append("/").append(createdEmployment.getId()).toString());
    }

    @RequestMapping(value = "",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Page<Employment> getAllCustomersEmploymentByPage(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      HttpServletRequest request, HttpServletResponse response) {
        return this.customerEmploymentService.getAllEmploymentsByPage(page, size);
    }
    
    @RequestMapping(value = "/all",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    List<Employment> getAllCustomerEmployments(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      HttpServletRequest request, HttpServletResponse response) {
        return this.customerEmploymentService.getAllEmployments();
    }
    
    @RequestMapping(value = "/all/{customerId}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    List<Employment> getAllCustomerEmploymentsForSingleCustomer(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                      @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                      @PathVariable("id") Long id,
                                      HttpServletRequest request, HttpServletResponse response) {
        return this.customerEmploymentService.getAllEmploymentsForCustomer(new Customer());
    }

    
    @RequestMapping("/simple/{id}")
	public Employment getSimpleCustomerEmployment(@PathVariable("id") Long id) {
    	Employment education = this.customerEmploymentService.getEmployment(id);
         checkResourceFound(education);
         return education;
	}

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Employment getEmployment(@PathVariable("id") Long id,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Employment education = this.customerEmploymentService.getEmployment(id);
        checkResourceFound(education);
        return education;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCustomerEmployment(@PathVariable("id") Long id, @RequestBody Employment education,
                                 HttpServletRequest request, HttpServletResponse response) {
        checkResourceFound(this.customerEmploymentService.getEmployment(id));
        if (id != education.getId()) throw new HTTP400Exception("ID doesn't match!");
        this.customerEmploymentService.updateEmployment(education);
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomerEmployment(@PathVariable("id") Long id, HttpServletRequest request,
                                 HttpServletResponse response) {
        checkResourceFound(this.customerEmploymentService.getEmployment(id));
        this.customerEmploymentService.deleteEmployment(id);
    }
}
