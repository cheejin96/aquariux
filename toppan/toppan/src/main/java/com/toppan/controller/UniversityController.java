package com.toppan.controller;

import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.toppan.model.University;
import com.toppan.service.UniversityService;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UniversityController {

	@Autowired
	UniversityService uniService;
	
	/**
	 * Challenge 1
	 * - GET/university: Retrieve a list of universities.
	 * - POST/university: Create a new university.
	 * - GET/university/{id}: Retrieve details of a specific university by its ID.
	 * - PUT/university/{id}: Update details of a specific university.
	 * - DELETE/university/{id}: Delete a specific university.
	 * - POST/university/bookmark/{id}: Bookmark a specific university.
	 * 
	 * Challenge 2
	 * - Implement parameters to filter universities on Get/university API.
	 * - Implement pagination for the GET/university endpoint to limit the number of results returned.
	 * 
	 * Bonus
	 * - For GET/university API, return the bookmarked universities in the beginning of the list.
	 * - Authentication and Authorization: Implement a basic authentication mechanism to 
	 * secure the API endpoints. Only authenticated users should be able to create, update, or delete tasks. 
	 * You can use simple username/password authentication or explore\ token-based authentication (e.g., JWT
	 * Unit Testing: Unit test all non-UI classes, TDD if possible.
	 */
	
	@RequestMapping(value = "/university", method = RequestMethod.GET)
	@ResponseBody
	public String getUniversity(HttpServletRequest request
			, @RequestParam(required = false) String name
			, @RequestParam(required = false) String country
			, @RequestParam(required = false) Boolean isBookmark
			, @RequestParam(required = false) Boolean isActive
			, @RequestParam(required = false) String createdStart
			, @RequestParam(required = false) String createdEnd
			, @RequestParam(required = false, defaultValue = "0") Integer pageNo
			, @RequestParam(required = false, defaultValue = "10") Integer pageSize
		) {
		
		try {
			System.out.println("getUniversity | entry ...");
			
			//Check value format
			if(StringUtils.isNotBlank(createdStart) || StringUtils.isNotBlank(createdEnd)) {
				
				if(StringUtils.isBlank(createdStart) || StringUtils.isBlank(createdEnd)) {
					return "Must enter createdStart and createdEnd for date range filter";
				}
				
				if(!isValidDate(createdStart) || !isValidDate(createdEnd) ) {
					return "Date format is wrong, only accept format in dd/MM/yy.";
				}
			}
			//Return uni list 
			List<University> uniList = uniService.getUniList(name, country, 
					isBookmark, isActive, createdStart, createdEnd, pageNo, pageSize);
			
			if(uniList == null) {
				return "No University is found";
			}
			
			if(uniList.size() == 0) {
				return "No University is found";
			}
			System.out.println("getUniversity | uniList size = " + uniList.size());
			System.out.println("getUniversity | exit ...");
			return toJSON(uniList);
			
		}catch(Exception e) {
			System.out.println("getUniversity | Fail to get university list | Exception : " + e.getLocalizedMessage());
			e.printStackTrace();
			return "Error, Please contact administrator.";
		}
	}
	
	@RequestMapping(value = "/university", method = RequestMethod.POST )
	@ResponseBody
	public String postUniversity(HttpServletRequest request
			, @RequestParam(required = true) String name
			, @RequestParam(required = true) String country
			, @RequestParam(required = false) String webpages) {
		
		try {
			
			//Proceed to create a new university
			if(uniService.doValidation(name, country)) {				
				University newUni = uniService.createNewUni(name, country, webpages);
				if(newUni != null) {
					return "Create new university successful";
				}	
			}
			
			return "Fail to create new university, Please contact administrator.";
		}catch(Exception e) {
			
			return "Error, Please contact administrator.";
		}
	}
	
	@RequestMapping(value = "/university/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getUniversityById(HttpServletRequest request, @PathVariable Long id) {
		
		try {
			System.out.println("getUniversityById | entry ...");
			//Return uni list 
			University university = uniService.getUniById(id);
			if(university == null) {
				return "University with id " + id + " not found";
			}
			
			System.out.println("getUniversityById | exit ...");
			return toJSON(university);
			
		}catch(Exception e) {
			System.out.println("getUniversityById | Fail to get university by ID | Exception : " + e.getLocalizedMessage());
			e.printStackTrace();
			return "Error, Please contact administrator.";
		}
	}
	
	@RequestMapping(value = "/university/{id}", method = RequestMethod.PUT)	
	@ResponseBody
	public String updateUniversityById(HttpServletRequest request
			, @PathVariable Long id
			, @RequestParam(required = false) String name
			, @RequestParam(required = false) String country
			, @RequestParam(required = false) String webpages
			, @RequestParam(required = false) Boolean isBookmark
			, @RequestParam(required = false) Boolean isActive) {
		
		try {
			System.out.println("updateUniversityById | entry ...");
			//Check if any values to update 
			if(StringUtils.isBlank(name) && StringUtils.isBlank(country) && 
					StringUtils.isBlank(webpages) && isBookmark == null 
					&& isActive == null) {
				return "No info to updates, please check the parameters ";
			}
			University university = uniService.updateUniversity(id, name, country, webpages, isBookmark, isActive);
			if(university == null) {
				return "Fail to update University with id : " + id;
			}
			
			System.out.println("updateUniversityById | exit ...");
			return toJSON(university);
			
		}catch(Exception e) {
			System.out.println("updateUniversityById | Fail to update university by ID | Exception : " + e.getLocalizedMessage());
			e.printStackTrace();
			return "Error, Please contact administrator.";
		}
	}
	
	@RequestMapping(value = "/university/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteUniversityById(HttpServletRequest request, @PathVariable Long id) {
		
		try {
			System.out.println("deleteUniversityById | entry ...");
			
			if(!uniService.deleteUniById(id)) {
				return "Fail to delete university, Please contact administrator.";
			}
			
			System.out.println("deleteUniversityById | exit ...");
			return "Delete university successful.";
			
		}catch(Exception e) {
			System.out.println("deleteUniversityById | Fail to get university by ID | Exception : " + e.getLocalizedMessage());
			e.printStackTrace();
			return "Error, Please contact administrator.";
		}
	}
	
	@RequestMapping(value = "/university/bookmark/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String bookmarkUniversityById(HttpServletRequest request, @PathVariable Long id) {
		
		try {
			System.out.println("bookmarkUniversityById | entry ...");
			
			if(!uniService.bookmarkUniById(id)) {
				return "Fail to bookmark university, Please contact administrator.";
			}
			
			System.out.println("bookmarkUniversityById | exit ...");
			return "Bookmark university successful.";
			
		}catch(Exception e) {
			System.out.println("deleteUniversityById | Fail to get university by ID | Exception : " + e.getLocalizedMessage());
			e.printStackTrace();
			return "Error, Please contact administrator.";
		}
	}
	
	private String toJSON(Object obj) {
		Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
		return gson.toJson(obj);
	}
	
	public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
