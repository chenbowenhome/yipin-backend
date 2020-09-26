package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.ArtMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtMsgRepository extends JpaRepository<ArtMsg,Integer> {
    Page<ArtMsg> findArtMsgByUserIdOrderByCreateTimeDesc(Integer userId,Pageable pageable);
    List<ArtMsg> findArtMsgByViewStatusAndUserId(Integer viewStatus,Integer userId);
}
