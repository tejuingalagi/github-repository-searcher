package com.freightfox.githubsearcher.repository;

import com.freightfox.githubsearcher.entity.GithubRepoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface GithubRepository extends JpaRepository<GithubRepoEntity, Long> {

    Optional<GithubRepoEntity> findByGithubRepoId(Long githubRepoId);

    List<GithubRepoEntity> findByLanguageIgnoreCase(String language);

    List<GithubRepoEntity> findByStarsGreaterThanEqual(Integer stars);
}
