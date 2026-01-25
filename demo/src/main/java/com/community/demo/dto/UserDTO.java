package com.community.demo.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String email;
    private String pwd;

    // Name 필드 통합 (nickName 사용)
    // private String firstName;
    // private String lastName;
    private String pwdConfirm;

    private String nickName;
    private LocalDateTime lastLogin;
    private LocalDateTime regDate, modDate;
    private List<AuthUserDTO> authList;
}
