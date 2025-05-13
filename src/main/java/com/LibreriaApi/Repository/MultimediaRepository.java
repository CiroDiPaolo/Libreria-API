package com.LibreriaApi.Repository;

import com.LibreriaApi.Model.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultimediaRepository extends JpaRepository<Multimedia, Long> {
}