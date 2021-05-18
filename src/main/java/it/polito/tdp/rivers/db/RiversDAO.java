package it.polito.tdp.rivers.db;

import java.util.Map;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.River;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RiversDAO {

	public void getAllRivers(Map<Integer,River> idMap) {
		
		final String sql = "SELECT id, name FROM river";


		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
					idMap.put(res.getInt("id"), new River(res.getInt("id"), res.getString("name")));
				}
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

	}
	
	public void setParameters(Map<Integer,River> idMap, int riverId){
		
		String sql = "SELECT river, AVG(flow) AS f_med, MIN(DAY) AS min, MAX(DAY) AS max, COUNT(*) AS tot "
				+ "FROM flow "
				+ "WHERE river=? "
				+ "GROUP BY river";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, riverId);
			ResultSet res = st.executeQuery();
			
			River r = idMap.get(riverId);

			if (res.next()) {
				r.setFlowAvg(res.getDouble("f_med"));
				r.setFirstDate(res.getDate("min").toLocalDate());
				r.setLastDate(res.getDate("max").toLocalDate());
				r.setTotMisurazioni(res.getInt("tot"));
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
	
	public void setAllFlowsByRiver(River r) {
		
		String sql ="SELECT * "
				+ "FROM flow "
				+ "WHERE river=?";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, r.getId());
			ResultSet res = st.executeQuery();
			

			while (res.next()) {
				Flow f = new Flow(res.getDate("day").toLocalDate(),res.getDouble("flow"),r);
				r.addFlow(f);
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		
	}
}
