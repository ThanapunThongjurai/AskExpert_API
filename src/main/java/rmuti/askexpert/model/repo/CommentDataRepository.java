package rmuti.askexpert.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import rmuti.askexpert.model.table.CommentData;

public interface CommentDataRepository extends JpaRepository<CommentData, Integer> {
    //List<CommentData> findAll();
}