package args;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@ApiModel("分页参数")
@Data
public class PageArg {
    /**
     * 当前页码
     */
    @Min(value = 1, message = "最小pageNo为 1")
    private Integer pageNo;

    /**
     * 每页数量
     */
    @Range(min = 1,max = 100)
    private Integer pageSize;

    /**
     * 检查分页参数
     */
    public void validate() {
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 5;
        }
    }
}
