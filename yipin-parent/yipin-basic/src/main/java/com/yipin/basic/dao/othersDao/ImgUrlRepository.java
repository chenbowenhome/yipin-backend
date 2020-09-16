package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.ImgUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImgUrlRepository extends JpaRepository<ImgUrl,Integer> {
    List<ImgUrl> findAllByUrl(String url);
}
