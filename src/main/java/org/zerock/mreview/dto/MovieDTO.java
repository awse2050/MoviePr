package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long mno;

    private String title;

    private double avg;

    private int reviewCnt;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    // 기본값을 지정하는 어노테이션.
    @Builder.Default
    private List<MovieImageDTO>imageDTOList = new ArrayList<>();
}
