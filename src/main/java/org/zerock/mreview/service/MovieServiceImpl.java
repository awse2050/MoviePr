package org.zerock.mreview.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

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
}
