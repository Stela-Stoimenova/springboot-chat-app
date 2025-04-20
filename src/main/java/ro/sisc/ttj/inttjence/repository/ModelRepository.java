package ro.sisc.ttj.inttjence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sisc.ttj.inttjence.models.Model;

public interface ModelRepository extends JpaRepository<Model, Long> {
    Model findFirstByName(String name);
}
