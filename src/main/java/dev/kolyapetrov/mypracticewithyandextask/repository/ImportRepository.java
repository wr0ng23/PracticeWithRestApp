package dev.kolyapetrov.mypracticewithyandextask.repository;

import dev.kolyapetrov.mypracticewithyandextask.entity.Import;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImportRepository extends MongoRepository<Import, String> {
}
