package de.hidin.linktreebackendservice.entry;

import de.hidin.linktreebackendservice.entity.LinkCollection;
import de.hidin.linktreebackendservice.entity.LinkEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LinkEntryRepository extends JpaRepository<LinkEntry, Long> {

}
