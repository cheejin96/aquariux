package com.toppan.dao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toppan.model.University;

public interface UniversityDao extends JpaRepository<University, Long>{

	@Query("SELECT u FROM University u "
			+ "WHERE 1=1 "
			+ " AND (:name IS NULL OR (u.name LIKE %:name%) ) "
			+ " AND (:country IS NULL OR (u.country LIKE %:country%) ) "
			+ " AND (:isBookmark IS NULL OR (u.isBookmark = :isBookmark) ) "
			+ " AND (:isActive IS NULL OR (u.isActive = :isActive) ) "
			+ " AND (:createdStart IS NULL OR (u.created BETWEEN :createdStart AND :createdEnd) ) "
			+ " ORDER BY u.isBookmark DESC")
	List<University> searchByFilter(@Param("name") String name
			, @Param("country") String country
			, @Param("isBookmark") Boolean isBookmark
			, @Param("isActive") Boolean isActive
			, @Param("createdStart") Timestamp createdStart
			, @Param("createdEnd") Timestamp createdEnd
			, Pageable pageable);

}
