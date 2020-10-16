package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.ArtMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtMsgRepository extends JpaRepository<ArtMsg,Integer> {
    Page<ArtMsg> findArtMsgByReceiveUserIdOrderByCreateTimeDesc(Integer receiveUserId,Pageable pageable);
    List<ArtMsg> findArtMsgByViewStatusAndReceiveUserId(Integer viewStatus,Integer receiveUserId);
    ArtMsg findArtMsgById(Integer id);
}
