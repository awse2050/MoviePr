package org.zerock.mreview.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.ReviewDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;
import org.zerock.mreview.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository repository;

    @Override
    public List<ReviewDTO> getListOfMovie(Long mno) {
        log.info("get Review List : " + mno);
        Movie movie = Movie.builder().mno(mno).build();

        List<Review> result =repository.findByMovie(movie);

        List<ReviewDTO> reviewDTOList = result.stream().map(entity -> entityToDto(entity)).collect(Collectors.toList());

        return reviewDTOList;
    }

    @Override
    public Long register(ReviewDTO reviewDTO) {
        log.info("regist dto : " + reviewDTO);

        Review review = dtoToEntity(reviewDTO);
        log.info(review);

        repository.save(review);

        return review.getReviewnum();
    }

    @Override
    public void modify(ReviewDTO reviewDTO) {

          Optional<Review> result = repository.findById(reviewDTO.getReviewnum());

          Review review = result.get();
          review.changeGrade(reviewDTO.getGrade());
          review.changeText(reviewDTO.getText());

          repository.save(review);
    }

    @Override
    public void remove(Long reviewnum) {

        repository.deleteById(reviewnum);

    }
}
