package app;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "assignations", path = "assignations")
public interface AssignationRepository extends CrudRepository<Assignation, Long> {
	//Assignation findByUser(@Param("uid") int uid);
	//List<Time> findByClass(@Param("id") Class course);	
}