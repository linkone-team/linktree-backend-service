package de.hidin.linktreebackendservice.entry;

import de.hidin.linktreebackendservice.entity.LinkEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkEntryRepository extends JpaRepository<LinkEntry, Long> {
}
