package com.yipin.basic.dao.specialistDao;

import com.yipin.basic.entity.specialist.Specialist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpecialistRepository extends JpaRepository<Specialist,Integer> {
    @Query(value = "SELECT * FROM specialist WHERE (check_status=0 OR check_status=1) AND user_id=?",nativeQuery = true)
    List<Specialist> findSpecialistByCheckStatus(Integer userId);
    Page<Specialist> findSpecialistByCheckStatusOrderByCreateTimeDesc(Integer checkStatus,Pageable pageable);
    Specialist findSpecialistById(Integer id);
    Specialist findSpecialistByUserId(Integer userId);
}
