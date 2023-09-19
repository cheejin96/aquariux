
package com.toppan.service;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.toppan.dao.UniversityDao;
import com.toppan.model.University;

import io.micrometer.common.util.StringUtils;

@Service
public class UniversityService {
	
	@Autowired
	UniversityDao universityDao;
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

	public List<University>getUniList(String name, String country, Boolean isBookmark, Boolean isActive, 
			String createdStart, String createdEnd, int pageNo, int pageSize) {
		
		try {
			System.out.println("getUniList | entry ...");
			
			Timestamp tsStart  = null;
			Timestamp tsEnd = null;
			if(StringUtils.isNotBlank(createdStart) && StringUtils.isNotBlank(createdEnd)) {
				tsStart = new Timestamp(dateFormat.parse(createdStart).getTime());
				tsEnd = new Timestamp(dateFormat.parse(createdEnd).getTime());
			}
			
			System.out.println("getUniList | tsStart = " + tsStart);
			System.out.println("getUniList | tsEnd = " + tsEnd);
			Pageable pageable = PageRequest.of(pageNo, pageSize);
			List<University> uniList = universityDao.searchByFilter(name, country, isBookmark, isActive, tsStart, tsEnd, pageable);
			
			if(uniList == null) {
				System.out.println("getUniList | University list not found ...");
			}
			
			System.out.println("getUniList | exit ...");
			return uniList;
		}catch(Exception e) {
			System.out.println("getUniList | Fail to get university list | Exception : " + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public University createNewUni(String name, String country, String webpages) {
		
		try {
			System.out.println("createNewUni | entry ...");
			University university = new University();
			university.setName(name);
			university.setCountry(country);
			university.setWebpages(webpages);
			
			System.out.println("createNewUni | proceed to save new university ...");
			university = universityDao.saveAndFlush(university);
			
			System.out.println("createNewUni | exit ...");
			return university;
		}catch(Exception e) {
			System.out.println("createNewUni | Fail to create university | Exception : " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	
	public University getUniById(Long id) {
		University university = null;
		if(universityDao.findById(id).isPresent()) {
			university = universityDao.findById(id).get();
		}
		return university;
	}
	
	public boolean deleteUniById(Long id) {
		
		boolean deleted = false;
		
		University university = getUniById(id);			
		if(university != null) {
			university.setActive(false);
			university.setDeletedAt(new Timestamp(System.currentTimeMillis()));
			university.setLastModified(new Timestamp(System.currentTimeMillis()));
			System.out.println("updateUniversity | proceed to delete university ...");
			university = universityDao.saveAndFlush(university);
			
			deleted = true;
		}
		
		return deleted;
	}
	
	public boolean bookmarkUniById(Long id) {
		
		boolean bookmark = false;
		
		University university = getUniById(id);			
		if(university != null) {
			university.setBookmark(true);
			university.setLastModified(new Timestamp(System.currentTimeMillis()));
			
			System.out.println("updateUniversity | proceed to bookmark university ...");
			university = universityDao.saveAndFlush(university);
			
			bookmark = true;
		}
		
		return bookmark;
	}
	
	
	public University updateUniversity(Long id, String name, String country, String webpages, Boolean isBookmark, Boolean isActive) {
		
		try {
			System.out.println("updateUniversity | entry ...");
			University university = getUniById(id);			
			if(university != null) {
				System.out.println("updateUniversity | unversity found ...");
				university = universityDao.findById(id).get();
				
				if(StringUtils.isNotBlank(name)) {
					university.setName(name);					
				}
				
				if(StringUtils.isNotBlank(country)) {
					university.setName(country);					
				}
				
				if(StringUtils.isNotBlank(webpages)) {
					university.setName(webpages);					
				}
				
				if(isBookmark != null) {					
					university.setBookmark(isBookmark);
				}
				
				if(isActive != null) {
					university.setActive(isActive);					
				}
				
				university.setLastModified(new Timestamp(System.currentTimeMillis()));
				System.out.println("updateUniversity | proceed to update university ...");
				university = universityDao.saveAndFlush(university);				
			}
			
			System.out.println("updateUniversity | exit ...");
			return university;
		}catch(Exception e) {
			System.out.println("updateUniversity | Fail to update university | Exception : " + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean doValidation(String name, String country) {
		
		if(StringUtils.isBlank(name) || StringUtils.isBlank(country)) {
			return false;
		}
		
		return true;
	}
}
