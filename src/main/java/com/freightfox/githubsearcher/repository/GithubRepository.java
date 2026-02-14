package com.freightfox.githubsearcher.repository;

import com.freightfox.githubsearcher.entity.GithubRepoEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;   
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GithubRepository extends JpaRepository<GithubRepoEntity, Long> {

    Optional<GithubRepoEntity> findByGithubRepoId(Long githubRepoId);

    @Query(
        "SELECT g FROM GithubRepoEntity g " +
        "WHERE (:language IS NULL OR LOWER(g.language) = LOWER(:language)) " +
        "AND (:minStars IS NULL OR g.stars >= :minStars)"
    )
    Page<GithubRepoEntity> searchRepositories(
            @Param("language") String language,
            @Param("minStars") Integer minStars,
            Pageable pageable
    );
}
