package org.zerock.mreview.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.service.ReviewService;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/reviews/*")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @GetMapping(value = "/{mno}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno")Long mno) {
        log.info("in Controller GetList mno : " + mno);

        return new ResponseEntity<>(service.getListOfMovie(mno), HttpStatus.OK);
    }

    @PostMapping(value = "/{mno}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Long> register(@RequestBody ReviewDTO reviewDTO) {
        log.info("in Controller register DTO : " + reviewDTO);

        Long reviewNum = service.register(reviewDTO);

        return new ResponseEntity<>(reviewNum, HttpStatus.OK);
    }

    @PutMapping(value = "/{mno}/{rno}")
    public ResponseEntity<Long> modify(@RequestBody ReviewDTO reviewDTO, @PathVariable("rno")Long reviewnum) {

        log.info("in Controller Modify Number : " + reviewnum);
        log.info("Modify DTO : " + reviewDTO);
        service.modify(reviewDTO);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{mno}/{rno}")
    public ResponseEntity<Long> delete(@PathVariable("rno")Long reviewnum) {
        log.info("in Controller Remove Number : " + reviewnum);
        service.remove(reviewnum);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }

}
