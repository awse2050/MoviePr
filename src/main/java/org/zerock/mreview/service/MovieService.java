package org.zerock.mreview.service;

import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    public Long register(MovieDTO movieDTO);

    default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
        // 객체를 담을 Map을 생성
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle()).build();

        entityMap.put("movie", movie);
        // DTO로 받아낸 MovieImage를 담을 List생성 -> DTO에서 List로 받고 있기 때문.
        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();
        if(imageDTOList != null || imageDTOList.size() > 0) {

            List<MovieImage> movieImageList = imageDTOList.stream().map(imageDto -> {

                MovieImage movieImage = MovieImage.builder()
                        .path(imageDto.getPath())
                        .uuid(imageDto.getUuid())
                        .imgName(imageDto.getImgName())
                        .movie(movie)
                        .build();
                return movieImage;

            }).collect(Collectors.toList());
            entityMap.put("imgList", movieImageList);
        }

        return entityMap;
    }
}
