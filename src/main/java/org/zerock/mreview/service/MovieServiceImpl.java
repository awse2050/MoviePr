package org.zerock.mreview.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
public class MovieServiceImpl implements MovieService{

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository imageRepository;

    @Transactional
    @Override
    public Long register(MovieDTO movieDTO) {

        Map<String, Object> entityMap = dtoToEntity(movieDTO);

        Movie movie = (Movie) entityMap.get("movie");
        log.info("movie : " + movie);
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");
        log.info("movieImageList : " + movieImageList);

        movieRepository.save(movie);

        for (MovieImage movieImage : movieImageList) {
            imageRepository.save(movieImage);
        }
        return movie.getMno();
    }

    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {

        //정렬기준
        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());

        // 엔티티객체 목록
        Page<Object[]> result = movieRepository.getListPage(pageable);

        // PageResultDTO에 던져줄 Function
        Function<Object[], MovieDTO> fn = entity -> entityToDto(
                (Movie) entity[0],
                (List<MovieImage>) Arrays.asList((MovieImage)entity[1]),
                (Double) entity[2],
                (Long) entity[3]
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public MovieDTO getMoive(Long mno) {
        // 해당 메서드를 호출하면 Object[] 의 형태로 4가지의 데이터가 추출된다.
        List<Object[]> result = movieRepository.getMovieWithAll(mno);
        // Movie 엔티티는 가장 앞에 존재
        Movie movie = (Movie) result.get(0)[0];
        // 영화이미지 개수만큼 MovieImage 객체가 필요하다.
        List<MovieImage> movieImageList = new ArrayList<>();
        // 목록 하나씩 뽑아서 해당 객체에 추가
        result.forEach( arr -> {
            MovieImage movieImage = (MovieImage) arr[1];
            movieImageList.add(movieImage);
        });

        Double avg = (Double) result.get(0)[2];
        Long reviewCnt = (Long) result.get(0)[3];

        return  entityToDto(movie, movieImageList, avg, reviewCnt);
    }

}
