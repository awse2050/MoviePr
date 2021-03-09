package org.zerock.mreview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.mreview.entity.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    // 평점이 없는 데이터가 있을수 있으므로 coalesce를 사용해서 영화의 평점을 가져옴
//    @Query("select m, avg(coalesce(r.grade, 0)), count(distinct r) from Movie m left outer join Review r on r.movie = m group by m" )
    @Query("select m , mi , avg(coalesce(r.grade, 0)), count(distinct r) from Movie m left outer join MovieImage mi on mi.movie = m "+
            "left outer join Review r on r.movie = m group by m ")
    Page<Object[]> getListPage(Pageable pageable);

    // 영화 + 이미지
//    @Query("select m, mi from Movie m left join MovieImage mi on mi.movie = m where m.mno = :mno")
    //  + 평균점수 및 리뷰개수
    @Query("select m, mi, avg(coalesce(r.grade,0)), count(r) from Movie m "
    + "left join MovieImage mi on mi.movie = m left join Review r on r.movie = m where m.mno = :mno group by mi")
    List<Object[]> getMovieWithAll(Long mno);

}
