package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

public class DepartmentDaoJDBC implements DepartmentDao{

	private Connection conn = null;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Department ogj) {
		PreparedStatement st = null; 
		try {
			st = conn.prepareStatement("INSERT INTO seller "
					+ "(Name , Id) "
					+ "VALUES "
					+ "(?,?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, ogj.getName());
			st.setInt(2, ogj.getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					ogj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("No rows affected!");
			}
			
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Department ogj) {
		PreparedStatement st = null; 
		try {
			st = conn.prepareStatement("UPDATE seller "
					+ "SET Name = ? "
					+ "WHERE Id = ?");
			st.setString(1, ogj.getName());
			st.setInt(2, ogj.getId());
			
			st.executeUpdate();
			
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT department.* "
					+ "FROM department "
					+ "WHERE department.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				return dep;
			}
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sl =new Seller();
		sl.setId(rs.getInt("Id"));
		sl.setName(rs.getString("Name"));
		sl.setEmail(rs.getString("Email"));
		sl.setBaseSalary(rs.getDouble("BaseSalary"));
		sl.setBirthDate(rs.getDate("BirthDate"));
		sl.setDepartmant(dep);
		return sl;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT department.* "
					+ "FROM department "
					+ "ORDER BY Name");
			rs = st.executeQuery();
			
			List<Department> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while(rs.next()) {
				Department dep = map.get(rs.getInt("Id"));
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("Id"), dep);
				}
				
				Department sl = instantiateDepartment(rs);
				list.add(sl);
			}
			return list;
		}catch(SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
