package exception;


import enums.ResultEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BasicException extends RuntimeException {
    private ResultEnum resultEnum;

    public BasicException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.resultEnum = resultEnum;
    }
}