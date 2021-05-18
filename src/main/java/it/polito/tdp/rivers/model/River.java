package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class River {

	private int id;
	private String name;
	private double flowAvg;
	private List<Flow> flows;
	private LocalDate firstDate;
	private LocalDate lastDate;
	private int totMisurazioni;
	
	public River(int id) {
		this.id = id;
	}

	public River(int id, String name) {
		this.id = id;
		this.name = name;
		
		this.flows = new ArrayList<>();
	}
	
	

	public int getTotMisurazioni() {
		return totMisurazioni;
	}

	public void setTotMisurazioni(int totMisurazioni) {
		this.totMisurazioni = totMisurazioni;
	}

	public LocalDate getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(LocalDate firstDate) {
		this.firstDate = firstDate;
	}

	public LocalDate getLastDate() {
		return lastDate;
	}

	public void setLastDate(LocalDate lastDate) {
		this.lastDate = lastDate;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public double getFlowAvg() {
		return flowAvg;
	}

	public void setFlowAvg(double flowAvg) {
		this.flowAvg = flowAvg;
	}

	public void setFlows(List<Flow> flows) {
		this.flows = flows;
	}

	public List<Flow> getFlows() {
		if (flows == null)
			flows = new ArrayList<Flow>();
		return flows;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		River other = (River) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void addFlow(Flow f) {
		this.flows.add(f);
	}
	
	public double cercaFlow(LocalDate data) {
		
		for(Flow f : flows) {
			if(data.equals(f.getDay())) {
				return f.getFlow();
			}
		}
		
		return 0.0;
	}
}
