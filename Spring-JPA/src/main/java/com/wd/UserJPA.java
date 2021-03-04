package com.wd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.List;

public interface UserJPA extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity>, Serializable {

    List<UserEntity> findByAndAgeBetween(Integer start,Integer end);
    List<UserEntity> findByNameLike(String name);

}
