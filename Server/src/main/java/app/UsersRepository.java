package app;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "Users", path = "Users")
public interface UsersRepository extends CrudRepository<User, Long> {

    List<User> findByLastName(@Param("name") String name);

}