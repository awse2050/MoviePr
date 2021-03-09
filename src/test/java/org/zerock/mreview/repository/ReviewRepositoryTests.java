package org.zerock.mreview.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository repository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertTest() {
        IntStream.rangeClosed(1,200).forEach(i -> {
            // 영화 번호
            Long mno = (long)(Math.random() * 100) + 1;

            // 리뷰어 번호
            Long mid = ((long)(Math.random() * 100) + 1);

            Member member = Member.builder().mid(mid).build();

            Review movieReview = Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int)(Math.random() * 5) +1)
                    .text("이 영화에 대한 느낌..."+i)
                    .build();

            repository.save(movieReview);
        });
    }

    @Test
    public void testGetMovieReview() {

        Movie movie = Movie.builder().mno(98L).build();

        List<Review> result = repository.findByMovie(movie);

        for (Review review : result) {
            System.out.println(review.getReviewnum());
            System.out.println("\t"+review.getGrade());
            System.out.println("\t"+review.getText());
            System.out.println("\t"+review.getMember().getEmail());
        }
    }

    @Test
    public void testdelete() {

        Long mid = 1L;

        Member member = Member.builder().mid(mid).build();

        repository.deleteByMember(member);
        memberRepository.deleteById(mid);

    }


}
