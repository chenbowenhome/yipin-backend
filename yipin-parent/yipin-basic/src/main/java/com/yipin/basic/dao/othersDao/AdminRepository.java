package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Admin findAdminByUsername(String username);
    List<Admin> findAdminByOrderByCreateTime();
    Admin findAdminById(Integer id);
}
