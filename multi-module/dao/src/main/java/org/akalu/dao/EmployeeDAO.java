package org.akalu.dao;

import java.util.Date;
import java.util.List;

import org.akalu.model.Employee;

public interface EmployeeDAO {
	public List<Employee> list(int first, int amount, Long depid);

	public Employee getEmplbyId(Long id);

	public Long addnew(Employee dep);

	public Boolean update(Employee dep);

	public Boolean delete(Long id);

	public List<Employee> search1(Date dob1);

	public List<Employee> search2(Date dob1, Date dob2);

	public Long size(Long id);
}
