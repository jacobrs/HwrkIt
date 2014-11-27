package app;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "classes", path = "classes")
public interface ClassRepository extends CrudRepository<Class, Long> {

    //List<Class> findByClassName(@Param("name") String name);

}