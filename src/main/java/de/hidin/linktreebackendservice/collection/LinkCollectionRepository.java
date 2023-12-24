package de.hidin.linktreebackendservice.collection;

import de.hidin.linktreebackendservice.entity.LinkCollection;
import de.hidin.linktreebackendservice.entity.LinkEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LinkCollectionRepository extends JpaRepository<LinkCollection, Long> {

    Optional<LinkCollection> findBySlug(String slug);

    Page<LinkCollection> findBySlugStartsWith(String slug, Pageable pageable);

    @Query("SELECT le FROM LinkCollection lc JOIN lc.entries le WHERE lc.slug = :slug AND (LOWER(le.displayText) LIKE LOWER(concat('%', :searchText, '%')) OR LOWER(le.targetUrl) LIKE LOWER(concat('%', :searchText, '%')))")
    Page<LinkEntry> findByDisplayTextOrTargetUrl(@Param("slug") String slug, @Param("searchText") String searchText, Pageable pageable);


}
