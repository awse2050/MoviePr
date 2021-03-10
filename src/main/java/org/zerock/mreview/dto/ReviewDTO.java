package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    // 리뷰 번호
    private Long reviewnum;
    // 영화 번호
    private Long mno;
    // 회원 아이디
    private Long mid;
    // 회원 이름
    private String nickname;
    // 회원 이메일
    private String email;

    private int grade;

    private String text;

    private LocalDateTime regDate, modDate;

}
