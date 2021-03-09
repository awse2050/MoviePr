package org.zerock.mreview.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Member;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 영화 조회시 Review를 조회할수 있도록 한다
    // Member의 객체와 그 이메일을 가져올 때 마다 해당 객체를 로딩해야하므로
    // 어노테션을 사용해서 이 객체만 Review가 로딩될때 가져오도록 FETCH 타입으로 설정
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    // update 나 delete를 이용할때는 Modifying 어노테이션을 적용해야함.
    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(Member member);
}
