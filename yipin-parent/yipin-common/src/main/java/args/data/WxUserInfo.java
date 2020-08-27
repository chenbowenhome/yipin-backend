package args.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WxUserInfo {
    @JsonProperty("nickName")
    private String nickname;
    private Integer gender;
    private String language;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
}