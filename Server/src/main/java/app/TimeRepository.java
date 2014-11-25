package app;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "times", path = "times")
public interface TimeRepository extends PagingAndSortingRepository<Time, Long> {

    List<Time> findByClassID(@Param("id") long id);
	List<Time> findByOwnerID(@Param("id") long id);
	
}