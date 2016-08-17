package org.akalu.dataserver;


import java.util.List;

import org.akalu.model.Department;
import org.akalu.model.DobT;
import org.akalu.model.Employee;
import org.akalu.service.DepartmentDataService;
import org.akalu.service.EmployeeDataService;
import org.akalu.uri.DataURI;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is a web service class which will be exposed for rest service.
 * The annotation @RequestMapping is used at method level for Rest Web Service URL mapping.
 * 
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

@RestController
public class DataController {
	@Autowired
	private DepartmentDataService depDataService;

	@Autowired
	private EmployeeDataService emplDataService;

	private static final Logger logger = Logger.getLogger(DataController.class);
 	
	/**
	 * 
	 * Method returns 1 requested department record.
	 * <p>In the request parameters attribute {@code defaultValue} will assign a default value
	 * for parameter which value is not available in request.
	 * 
	 * @param id  
	 * @return object of Department.class
	 */

	@RequestMapping(value = DataURI.GET_DEP, method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public Department getDepartmentDetail(
		@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {

		logger.debug("GET for Department with id="+id);
		Department p = depDataService.getDepbyId(id);
		return p;
	}

	/**
	 * 
	 * Method returns requested list of departments.
	 * <p>In the request parameters attribute {@code defaultValue} will assign a default value
	 * for parameter which value is not available in request.
	 * 
	 * @param first - defines the value of MIN(id) of the first department in resulted list  
	 * @param n - defines the maximum number of records to return
	 * @return List<Department>
	 */
	@RequestMapping(value = DataURI.GET_LIST_DEP, method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public List<Department> getDepartmentsList(
			 @RequestParam(value = "f", required = false, defaultValue = "0") Integer first,
			 @RequestParam(value = "n", required = false, defaultValue = "10") Integer n) {

		logger.debug("GET for list of Departments with index in range "+first+"-"+n);

		List<Department> p = depDataService.list(first, n);
		return p;
	}
	
	/**
	 * 
	 * Method returns requested list of employees.
	 * 
	 * @param first - defines the value of MIN(id) of the first employee in resulted list  
	 * @param n - defines the id of department for list of employees to return from.
	 * @return List<Employee>
	 */

	@RequestMapping(value = DataURI.GET_LIST_EMPL, method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public List<Employee> getEmployeesList(
			 @RequestParam(value = "f", required = false, defaultValue = "0") Integer first,
			 @RequestParam(value = "n", required = false, defaultValue = "10") Integer n,
			 @RequestParam(value = "id", required = false, defaultValue = "1") Long id) {
		
		logger.debug("GET for list of Employees from department with id="+id);

		List<Employee> p = emplDataService.list(first,n, id);
		
		return p;
	}
	
	/**
	 * 
	 * Main search method. 
	 * 
	 * <p>d - contains pair of Date variables which define search criteria.
	 *  
	 * @return List<Employee> - list of employees
	 */
	@RequestMapping(value = DataURI.SEARCH_EMPL, method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public List<Employee> getSearchList(@RequestBody DobT d) {
		
		logger.debug("POST for list of Employees with dob ="+d.toString());

	
		if (d.getDob2() == null) return emplDataService.search1(d.getDob1());
			return emplDataService.search2(d.getDob1(),d.getDob2());
	}
	
	@RequestMapping(value = DataURI.DELETE_DEP, method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public Boolean deleteDepartment(@RequestParam(value = "id") Long id) {
		
		logger.debug("GET to delete department with id="+id);

		return depDataService.delete(id);
	}
	
	/**
	 * 
	 * Method used to add/update  a single department record.
	 * 
	 * @param isNew - boolean which defines branch of code to execute (add or update)
	 * @param dep - contains updated data
	 * @return id of newly added/updated department
	 */

	@RequestMapping(value = DataURI.UPDATE_DEP, method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public Long updateDepartment(
			 @RequestParam(value = "new", required = false, defaultValue = "false") Boolean isNew,
			 @RequestBody Department dep) {

		if (isNew){
			logger.debug("POST to add department");
			return depDataService.addnew(dep);
		}else{
			logger.debug("POST to update department with id="+dep.getId());
			depDataService.update(dep);
			return dep.getId();
		}
		
	}
	
	
	@RequestMapping(value = DataURI.GET_EMPL, method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public Employee getEmployeeDetail(
		@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
		
		logger.debug("GET for Employee with id="+id);

		Employee p = emplDataService.getEmplbyId(id);
		return p;
	}

	
	@RequestMapping(value = DataURI.DELETE_EMPL, method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public Boolean deleteEmployee(@RequestParam(value = "id") Long id) {
		
		logger.debug("GET to delete Employee with id="+id);

		return emplDataService.delete(id);
		
	}
	
	/**
	 * 
	 * Method used to add/update  a single employee record.
	 * 
	 * @param isNew - boolean which defines branch of code to execute (add or update)
	 * @param empl - contains updated data
	 * @return id of newly added/updated employee
	 */

	@RequestMapping(value = DataURI.UPDATE_EMPL, method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public Long updateEmployee(
			 @RequestParam(value = "new", required = false, defaultValue = "false") Boolean isNew,
			 @RequestBody Employee empl) {
		if (isNew){
			logger.debug("POST to add Employee");

			return emplDataService.addnew(empl);
		}else{

			logger.debug("POST to update Employee with id="+empl.getId());
			emplDataService.update(empl);
			return empl.getId();
		}
		
	}
	
	@RequestMapping(value = DataURI.DEP_DIR, method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public Long getDepartmentSize() {

		logger.debug("GET for Departments size");
		Long s = depDataService.getDepSize();
		return s;
	}

	@RequestMapping(value = DataURI.EMPL_DIR, method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	 public Long getEmployeeSize(
			 @RequestParam(value = "id", required = false, defaultValue = "0") Long id) {

		logger.debug("GET for Employees size from dep with id="+id);
		Long s = emplDataService.getEmplSize(id);
		return s;
	}

	
	

} 
