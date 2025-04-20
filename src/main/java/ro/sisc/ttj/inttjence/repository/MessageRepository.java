package ro.sisc.ttj.inttjence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sisc.ttj.inttjence.models.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByOrderByInsertTimestampAsc();
}
