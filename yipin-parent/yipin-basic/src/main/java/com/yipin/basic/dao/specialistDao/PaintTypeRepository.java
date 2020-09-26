package com.yipin.basic.dao.specialistDao;

import com.yipin.basic.entity.production.Production;
import com.yipin.basic.entity.specialist.PaintType;
import com.yipin.basic.entity.specialist.Specialist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaintTypeRepository extends JpaRepository<PaintType,Integer> {
    PaintType findPaintTypeById(Integer id);

}