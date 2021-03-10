package org.zerock.mreview.service;

import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewService {
    // 해당 영화의 리뷰목록
    public List<ReviewDTO> getListOfMovie(Long mno);

    public Long register(ReviewDTO reviewDTO);

    public void modify(ReviewDTO reviewDTO);

    public void remove(Long reviewnum);

    // 보통 화면에서 데이터를 전송받을 때
    default Review dtoToEntity(ReviewDTO reviewDTO) {

        Review review = Review.builder()
                .reviewnum(reviewDTO.getReviewnum())
                .movie(Movie.builder().mno(reviewDTO.getMno()).build())
                .member(Member.builder().mid(reviewDTO.getMid()).build())
                .grade(reviewDTO.getGrade())
                .text(reviewDTO.getText())
                .build();

        return review;
    }
    // 화면으로 데이터를 전송할 떄
    default ReviewDTO entityToDto(Review review) {

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reviewnum(review.getReviewnum())
                .mno(review.getMovie().getMno())
                .mid(review.getMember().getMid())
                .nickname(review.getMember().getNickname())
                .email(review.getMember().getEmail())
                .grade(review.getGrade())
                .text(review.getText())
                .regDate(review.getRegDate())
                .modDate(review.getModDate())
                .build();

        return  reviewDTO;
    }

}
