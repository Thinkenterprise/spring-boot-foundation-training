package com.thinkenterprise.domain.route;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkenterprise.domain.core.AbstractEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Flight extends AbstractEntity {

	private double price;
	
	private LocalDate date;

	@JsonIgnore
	@ManyToOne
	private Route route;

	public Flight() {
		super();
	}
	
	public Flight(double price, LocalDate date) {
		super();
		this.price = price;
		this.date = date;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

}
