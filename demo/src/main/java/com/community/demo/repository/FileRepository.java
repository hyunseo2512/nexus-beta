package com.community.demo.repository;

import com.community.demo.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String> {

    Optional<List<File>> findBySaveDir(String today);
}
