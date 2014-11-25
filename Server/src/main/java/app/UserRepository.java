package app;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "userse")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    List<User> findByLastName(@Param("name") String name);

}