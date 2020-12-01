package com.yipin.basic.dao.othersDao;

import com.yipin.basic.entity.others.ArtActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArtActivityRepository extends JpaRepository<ArtActivity,Integer> {
    ArtActivity findArtActivityById(Integer id);
    List<ArtActivity> findArtActivityBySlideStatusOrderByCreateTimeDesc(Integer slideStatus);
    Page<ArtActivity> findArtActivityByOrderByCreateTimeDesc(Pageable pageable);

    //通过是否结束查询活动
    Page<ArtActivity> findArtActivityByIsEndOrderByCreateTimeDesc(Integer isEnd,Pageable pageable);
    //通过是否为轮播活动查询
    Page<ArtActivity> findArtActivityBySlideStatusOrderByCreateTimeDesc(Integer slideStatus,Pageable pageable);
    //通过是否为产品类活动查询
    Page<ArtActivity> findArtActivityByProductTypeOrderByCreateTimeDesc(Integer productType,Pageable pageable);
    //通过是否为线上活动查询
    Page<ArtActivity> findArtActivityByActivityTypeOrderByCreateTimeDesc(Integer activityType,Pageable pageable);
    @Query(value = "SELECT * FROM art_activity a,art_activity_participants p WHERE p.user_id=?1 AND a.id=p.art_activity_id ORDER BY a.create_time DESC",nativeQuery = true)
    Page<ArtActivity> findUserJoinedActivity(Integer userId,Pageable pageable);

    @Query(value = "SELECT * FROM art_activity WHERE (content LIKE ?1 OR title LIKE ?1) ORDER BY create_time DESC",nativeQuery = true)
    Page<ArtActivity> findAllByKeyWord(String keyWord,Pageable pageable);
    //通过是否结束与keyword查询活动
    @Query(value = "SELECT * FROM art_activity WHERE (content LIKE ?2 OR title LIKE ?2) AND is_end=?1 ORDER BY create_time DESC",nativeQuery = true)
    Page<ArtActivity> findArtActivityByIsEndAndKeyWord(Integer isEnd,String keyWord,Pageable pageable);
    //通过是否为轮播活动与keyword查询
    @Query(value = "SELECT * FROM art_activity WHERE (content LIKE ?2 OR title LIKE ?2) AND slide_status=?1 ORDER BY create_time DESC",nativeQuery = true)
    Page<ArtActivity> findArtActivityBySlideStatusAndKeyWord(Integer slideStatus,String keyWord,Pageable pageable);
    //通过是否为产品类活动与keyword查询
    @Query(value = "SELECT * FROM art_activity WHERE (content LIKE ?2 OR title LIKE ?2) AND product_type=?1 ORDER BY create_time DESC",nativeQuery = true)
    Page<ArtActivity> findArtActivityByProductTypeAndKeyWord(Integer productType,String keyWord,Pageable pageable);
    //通过是否为线上活动与keyword查询
    @Query(value = "SELECT * FROM art_activity WHERE (content LIKE ?2 OR title LIKE ?2) AND activity_type=?1 ORDER BY create_time DESC",nativeQuery = true)
    Page<ArtActivity> findArtActivityByActivityTypeAndKeyWord(Integer activityType,String keyWord,Pageable pageable);
    @Query(value = "SELECT * FROM art_activity a,art_activity_participants p WHERE p.user_id=?2 AND a.id=p.art_activity_id AND (a.content LIKE ?1 OR a.title LIKE ?1) ORDER BY a.create_time DESC",nativeQuery = true)
    Page<ArtActivity> findUserJoinedActivityByKey(String keyWord,Integer userId,Pageable pageable);
}
