package pe.com.inventarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.inventarios.model.SeqRegistro;

public interface SeqRegistroRepository extends JpaRepository<SeqRegistro, Long>{

}
