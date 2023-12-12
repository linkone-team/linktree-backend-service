package de.hidin.linktreebackendservice.collection;

import de.hidin.linktreebackendservice.entity.LinkCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkCollectionRepository extends JpaRepository<LinkCollection, Long> {

    Optional<LinkCollection> findBySlug(String slug);

}
