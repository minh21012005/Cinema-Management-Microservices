package com.example.service.impl;

import com.example.client.UserClient;
import com.example.domain.entity.Movie;
import com.example.domain.entity.Rating;
import com.example.domain.enums.RatingStatus;
import com.example.domain.request.RatingReqDTO;
import com.example.domain.response.RatingResDTO;
import com.example.mapper.RatingMapper;
import com.example.repository.MovieRepository;
import com.example.repository.RatingRepository;
import com.example.service.CommentModerationService;
import com.example.service.RatingService;
import com.example.util.error.IdInvalidException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl
        extends BaseServiceImpl<Rating, Long, RatingReqDTO, RatingResDTO>
        implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final MovieRepository movieRepository;
    private final UserClient userClient;
    private final CommentModerationService commentModerationService;

    protected RatingServiceImpl(RatingRepository ratingRepository,
                                RatingMapper ratingMapper,
                                MovieRepository movieRepository,
                                UserClient userClient,
                                CommentModerationService commentModerationService) {
        super(ratingRepository);
        this.ratingRepository = ratingRepository;
        this.ratingMapper = ratingMapper;
        this.movieRepository = movieRepository;
        this.userClient = userClient;
        this.commentModerationService = commentModerationService;
    }

    @Override
    public RatingResDTO create(RatingReqDTO dto) throws IdInvalidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        Movie movie = movieRepository.findById(dto.getMovieId()).orElseThrow(
                () -> new IdInvalidException("Phim không hợp lệ!")
        );

        boolean exists = ratingRepository.existsByMovieAndUserIdAndStatusNot(
                movie, userId, RatingStatus.REJECTED
        );
        if (exists) {
            throw new IdInvalidException("Bạn đã đánh giá phim này rồi");
        }

        if (dto.getComment() != null && !dto.getComment().isEmpty()) {
            boolean isCommentValid = commentModerationService.isCommentSafe(dto.getComment());
            if (!isCommentValid) {
                throw new IdInvalidException("Comment của bạn chứa từ ngữ không hợp lệ!");
            }
        }

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = jwt.getClaim("email");
        String userName = userClient.getNameByEmail(email);

        Rating rating = ratingMapper.toEntity(dto);
        rating.setMovie(movie);
        rating.setStatus(RatingStatus.APPROVED);
        rating.setUserId(userId);

        RatingResDTO ratingResDTO = ratingMapper.toDto(ratingRepository.save(rating));
        ratingResDTO.setUsername(userName);

        return ratingResDTO;
    }

    @Override
    public List<RatingResDTO> getRatingsByMovie(Long id) throws IdInvalidException {
        movieRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Phim không hợp lệ!")
        );

        return ratingRepository.findByMovie_IdAndStatus(id, RatingStatus.APPROVED)
                .stream().map(ratingMapper::toDto).toList();
    }
}
