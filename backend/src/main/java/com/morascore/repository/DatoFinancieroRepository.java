package com.morascore.repository;


import com.morascore.entity.DatoFinanciero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatoFinancieroRepository extends JpaRepository<DatoFinanciero, Long> {

    Optional<DatoFinanciero> findByCliente_Id(Long id);

    List<DatoFinanciero> findByNivelAhorro(Integer nivelAhorro);

}
