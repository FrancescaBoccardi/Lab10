package it.polito.tdp.rivers.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.db.RiversDAO;
import it.polito.tdp.rivers.model.Event.EventType;

public class Model {
	
	private Map<Integer,River> idMap;
	private RiversDAO dao;
	
	// Coda degli eventi
	
	private PriorityQueue<Event> queue;
	
	// Parametri di input
	
	private double Q;
	private double C;
	private double k;
	private double fOutMin;
	private double fOutSup;
	private double probFOutSup;
	private long totGiorni;
	
	private LocalDate startDate;
	private LocalDate endDate;
	
	// Parametri di output
	
	private int giorniOut;
	private double CMed;
	
	
	public Model() {
		dao = new RiversDAO();
		idMap = new HashMap<Integer,River>();
		dao.getAllRivers(idMap);
	}

	public Map<Integer, River> getIdMap() {
		return idMap;
	}
	
	public void setParameters(int riverId) {
		dao.setParameters(idMap, riverId);
	}
	
	public void setFlowsByRiver(River r) {
		dao.setAllFlowsByRiver(r);
	}
	
	public void lanciaSimulazione(River river, double k) {
		this.init(river.getId(), k);
		this.run();
	}
	
	private void init(int riverId, double k) {
		
		River r = idMap.get(riverId);
		this.setParameters(riverId);
		this.setFlowsByRiver(r);
		
		// inizializza coda eventi
		this.queue = new PriorityQueue<>();
		
		// inizializza parametri del mondo
		this.k = k;
		this.Q = k*r.getFlowAvg()*30*60*60*24; 
		this.C = Q/2;
		this.fOutMin = 0.8*r.getFlowAvg()*24*60*60;
		this.fOutSup = 10 * this.fOutMin;
		this.probFOutSup = 0.05;
		this.startDate = r.getFirstDate();
		this.endDate = r.getLastDate();
		totGiorni = ChronoUnit.DAYS.between(startDate, endDate);
		
		// inizializza output
		this.giorniOut=0;
		this.CMed=0;
		
		
		// inietta gli eventi di input
		LocalDate giorno = this.startDate;
		double flowIn;
		double flowOut;
		
		while(giorno.isBefore(endDate)) {
			
			flowIn = r.cercaFlow(giorno)*24*60*60;
			
			if(Math.random()*1<=this.probFOutSup) {
				flowOut = this.fOutSup;
			} else {
				flowOut = this.fOutMin;
			}
			Event e = new Event(giorno, EventType.GIORNO, r);
			e.setFlowIn(flowIn);
			e.setFlowOut(flowOut);
			this.queue.add(e); 
			giorno = giorno.plusDays(1);
		}
	}
	
	
	private void run() {
		while(!this.queue.isEmpty()) {
			Event e = queue.poll();
			this.processEvent(e);
			System.out.println(e+" "+C);
		}
		
		this.CMed = CMed/totGiorni;
		System.out.println(giorniOut);
	}
	
	private void processEvent(Event e) {
		
		River r = e.getRiver();
		
		switch(e.getType()){
		
		case GIORNO:
			
			double flowIn = e.getFlowIn();
			double flowOut = e.getFlowOut();
			
			if((C+flowIn-flowOut)<0) {
				this.giorniOut++;
				this.C=0;
				this.CMed += C;
				break;
			} else{
				this.C += (flowIn-flowOut);
			}
			
			if(C>Q){
				Event temp = new Event(e.getDate(),EventType.TRACIMAZIONE,r);
				temp.setFlowOut(C-Q);
				temp.setFlowIn(0);
				this.queue.add(temp);
			} else {
				this.CMed += C;
			}
			
			break;
		
		case TRACIMAZIONE:
			C -= e.getFlowOut();
			this.CMed += C;
			break;
		}
		
	}

	public int getGiorniOut() {
		return giorniOut;
	}

	public double getCMed() {
		return CMed;
	}
	
	
	
	
	

}
