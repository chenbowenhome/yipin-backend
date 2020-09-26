package com.yipin.basic.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class TopicArticleForm implements Serializable {
    /**
     * 文章内容
     */
    @NotEmpty(message = "文章内容不能为空")
    private String content;
    /**
     * 文章图片
     */
    @NotEmpty(message = "文章图片不能为空")
    private String topicImage;
    /**
     * 文章标题
     */
    @NotEmpty(message = "文章标题不能为空")
    private String title;
}
