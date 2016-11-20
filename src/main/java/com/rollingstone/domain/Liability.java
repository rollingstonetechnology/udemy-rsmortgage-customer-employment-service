package com.rollingstone.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;

/*
 * A User POJO serving as an Entity as well as a Data Transfer Object i.e DTO
 */
@Entity
@Table(name = "rsmortgage_liability")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Liability {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_from", unique = true, nullable = false, length = 10)
	private Date fromDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "maturity_date", unique = true, nullable = false, length = 10)
	private Date dateMaturing;
	
	@OneToOne
	@JoinColumn(name="id")
	private LiabilityType investmentType;

	@Column(nullable = false)
	private double originalTotalLiability;

	@Column(nullable = false)
	private double currentTotalLiability;

	@Column(nullable = false)
	private String 	paymentFrequency;
	
	@Column(nullable = false)
	private float 	periodEMI;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	@JsonBackReference
	Customer customer;
	
	public Liability(){
		
	}

	public long getId() {
		return id;
	}

	
	
}
