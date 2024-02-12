package org.example.springlearning.repo;
import org.example.springlearning.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

}
