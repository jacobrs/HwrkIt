package app;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "time", path = "time")
public interface TimeRepository extends CrudRepository<Time, Long> {

    //List<Time> findByClass(@Param("class") Class course);
	//List<Time> findByOwner(@Param("owner") User owner);
	
}
