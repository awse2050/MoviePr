package org.zerock.mreview.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {
    // entity객체로 만들어낸 목록을 dtoList로 변환시켜서 화면을 전송
    private List<DTO> dtoList;

    private int totalPage;
    private int page, size, start , end;

    private boolean prev, next;
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result,  Function<EN, DTO> fn) {

        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePaging(result.getPageable());
    }

    private void makePaging(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        //  임시 끝페이지 계산
        int tempEnd = (int)(Math.ceil( page * 0.1)) * 10;

        start = tempEnd - 9;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
