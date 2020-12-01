package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.ArtActivityParticipants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtActivityParticipantsRepository extends JpaRepository<ArtActivityParticipants,Integer> {
    ArtActivityParticipants findArtActivityParticipantsByUserIdAndArtActivityId(Integer userId,Integer artActivityId);
    Page<ArtActivityParticipants> findArtActivityParticipantsByUserIdOrderByCreateTimeDesc(Integer userId, Pageable pageable);
}
