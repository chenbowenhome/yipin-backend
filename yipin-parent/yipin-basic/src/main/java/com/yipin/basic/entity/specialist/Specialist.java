package com.yipin.basic.entity.specialist;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "specialist")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Specialist implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 名字
     */
    private String name;
    /**
     * 专业
     */
    private String major;
    /**
     * 毕业院校
     */
    private String university;
    /**
     * 教龄
     */
    private Integer teachingAge;
    /**
     * 擅长年龄端
     */
    private String adeptAge;
    /**
     * 擅长画种
     */
    @Type( type = "json" )
    @Column( columnDefinition = "json" )
    private List<Integer> adeptPaint;
    /**
     * 所属机构
     */
    private String organization;
    /**
     * 学生数量
     */
    private Integer studentNums;
    /**
     * 教师资格证书
     */
    private String teacherCertification;
    /**
     * 获奖证书
     */
    @Type( type = "json" )
    @Column( columnDefinition = "json" )
    private List<String> prizeCertification;
    /**
     * 审核状态
     */
    private Integer checkStatus;

    private Date createTime;

    private Date updateTime;
}
