package it.polito.tdp.rivers.model;

import java.time.LocalDate;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		TRACIMAZIONE,
		GIORNO
	};
	
	private LocalDate date;
	private EventType type;
	private double flowIn;
	private double flowOut;
	private River r;
	
	
	public Event(LocalDate date, EventType type, River r) {
		this.type=type;
		this.date = date;
		this.r = r;
	}



	@Override
	public String toString() {
		return "Event [date=" + date + ", type=" + type + ", flowIn=" + flowIn + ", flowOut=" + flowOut + "]";
	}



	@Override
	public int compareTo(Event o) {
		return date.compareTo(o.date);
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public EventType getType() {
		return type;
	}


	public void setType(EventType type) {
		this.type = type;
	}


	public double getFlowIn() {
		return flowIn;
	}


	public void setFlowIn(double flowIn) {
		this.flowIn = flowIn;
	}


	public double getFlowOut() {
		return flowOut;
	}


	public void setFlowOut(double flowOut) {
		this.flowOut = flowOut;
	}



	public River getRiver() {
		return r;
	}



	public void setR(River r) {
		this.r = r;
	}

	


	
	
	
	
	
	

}
