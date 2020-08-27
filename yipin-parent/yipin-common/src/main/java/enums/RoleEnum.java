package enums;

import lombok.Getter;
import lombok.ToString;

@ToString
public enum RoleEnum {

    STUDENT(0,"student"),
    TEACHER(1,"teacher"),
    VISITOR(2,"visitor"),
    ADMIN(2,"admin"),
    ;

    @Getter
    private Integer identity;
    @Getter
    private String role;

    RoleEnum(Integer identity, String role) {
        this.identity = identity;
        this.role = role;
    }
}
